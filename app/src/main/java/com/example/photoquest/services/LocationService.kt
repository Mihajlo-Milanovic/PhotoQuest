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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import java.util.Locale

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
