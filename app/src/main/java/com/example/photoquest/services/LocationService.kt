package com.example.photoquest.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.HandlerThread
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import com.example.photoquest.ui.screens.map.EARTH_RADIUS_KM
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private const val earthRadius = 6371.0 // Radius of Earth in km

fun getUserCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    location: MutableState<Location?>
) {
    val permission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    if (permission == PackageManager.PERMISSION_GRANTED) {
        try {
            fusedLocationClient
                .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener {
                    location.value = it
                    Log.d(
                        "MIKI",
                        "User location is: \nlat=${it.latitude}\nlng=${it.longitude}"
                    )
                }
                .addOnFailureListener {
                    Log.e("MIKI", "Failed to get location.\n\n ${it.message}")
                }
        } catch (ex: Exception) {
            Log.e(
                "MIKI",
                "Failed to get location permission.\n\n ${ex.message}"
            )
        }
    } else {
        Toast.makeText(context, "Location permission required!", Toast.LENGTH_SHORT).show()
    }
}

fun getUserLocationUpdates(
    context: Context,
    locationAccuracyPriority: Int,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Location) -> Unit
): () -> Unit {
    val permission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    if (permission == PackageManager.PERMISSION_GRANTED) {

        val locationRequest = LocationRequest.Builder(3_000)
            .setMinUpdateIntervalMillis(2_500)
            .setPriority(locationAccuracyPriority)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let {
                    onLocationReceived(it)
                }
            }
        }

        val handlerThread = HandlerThread("LocationUpdatesThread")
        handlerThread.start()

        val looper = handlerThread.looper

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                looper
            )

        } catch (ex: SecurityException) {
            Log.e("MIKI", "Failed to get location permission.\n\n ${ex.message}")
        }

        return {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            handlerThread.quitSafely()
        }

    } else {
        Toast.makeText(context, "Location permission required!", Toast.LENGTH_SHORT).show()
    }

    return {}
}

fun reverseGeocode(latitude: Double, longitude: Double, context: Context): Address? {

    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
    val address = addresses?.firstOrNull()
    return address
}

fun getBoundsForRadius(center: LatLng, radiusKm: Double): LatLngBounds {

    val latDelta = Math.toDegrees(radiusKm / EARTH_RADIUS_KM)
    val lngDelta =
        Math.toDegrees(radiusKm / EARTH_RADIUS_KM / cos(Math.toRadians(center.latitude)))

    val southwest = LatLng(center.latitude - latDelta, center.longitude - lngDelta)
    val northeast = LatLng(center.latitude + latDelta, center.longitude + lngDelta)

    return LatLngBounds(southwest, northeast)
}

fun getDistanceFromLatLng(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {

    //haversine

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lng2 - lng1)

    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) *
            cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
}