package com.example.photoquest.ui.screens.map

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.models.data.Quest
import com.example.photoquest.services.getQuestsInRadius
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.delay

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

    override var navController by mutableStateOf<NavController?>(null)

    var fusedLocationClient: FusedLocationProviderClient? = null
        private set

    var freeResources: () -> Unit = {}

    var inFocus by mutableStateOf(false)
    var location by mutableStateOf<LatLng?>(null)
        private set

    var nearbyQuests = mutableListOf<Quest>()

    val cameraPositionState = CameraPositionState(
        position = CameraPosition(
            location ?: LatLng(44.8125, 20.4612),
            12f,
            0f,
            0f
        )
    )

    fun setFusedLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun setLocation(lat: Double, lng: Double) {
        location = LatLng(lat, lng)
    }

    fun setCameraPositionState(
        latLng: LatLng,
        zoom: Float = 16f,// TODO: determine dynamically
        tilt: Float = 0f,
        bearing: Float = 0f
    ) {
        cameraPositionState.position = CameraPosition(latLng, zoom, tilt, bearing)
    }

    suspend fun checkForNearbyQuests() {
        while (inFocus) {
            location?.let {
                nearbyQuests = getQuestsInRadius(center = it, radiusInKm = 0.001)
            }
            delay(15_000)
        }
    }

}