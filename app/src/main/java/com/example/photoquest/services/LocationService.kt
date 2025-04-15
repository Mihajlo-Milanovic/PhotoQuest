package com.example.photoquest.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import java.util.Locale

fun getUserLocation(
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

fun reverseGeocode(latitude: Double, longitude: Double, context: Context): Address? {

    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(latitude, longitude, 1)
    val address = addresses?.firstOrNull()
    return address
}
