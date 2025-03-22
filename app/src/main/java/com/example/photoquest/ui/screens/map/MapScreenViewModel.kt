package com.example.photoquest.ui.screens.map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.ui.permissions.PermissionsViewModel
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

class MapScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {
        private var INSTANCE: MapScreenViewModel? = null

        fun getInstance(): MapScreenViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = MapScreenViewModel()
                INSTANCE!!
            }
        }
    }

    val pvm = PermissionsViewModel.getInstance()
    override val navController: MutableState<NavController?> = mutableStateOf(null)

    var fusedLocationClient: FusedLocationProviderClient? = null
        private set

    var location = mutableStateOf<LatLng?>(null)
        private set

    val cameraPositionState = CameraPositionState(
        position = CameraPosition(
            location.value ?: LatLng(44.8125, 20.4612),
            12f,
            0f,
            0f
        )
    )


    fun setFusedLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun setLocation(lat: Double, lng: Double) {
        location.value = LatLng(lat, lng)
    }

    fun setCameraPositionState(
        latLng: LatLng,
        zoom: Float = 16f,// TODO: determine dynamically
        tilt: Float = 0f,
        bearing: Float = 0f
    ) {
        cameraPositionState.position = CameraPosition(latLng, zoom, tilt, bearing)
    }


    @SuppressLint("MissingPermission")
    fun getUserLocation(
        context: Context,
        onLocationReceived: (Location) -> Unit
    ) {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).build()

        if (!pvm.arePermissionsGranted(
                context = context,
                array = arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
        ) {
            return
        }

        fusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    locationResult.lastLocation?.let {
                        onLocationReceived(it)
                        setCameraPositionState(LatLng(it.latitude, it.longitude))
                    }

                    fusedLocationClient!!.removeLocationUpdates(this) // Stop updates after getting location
                }
            },
            Looper.getMainLooper()
        )

    }
}