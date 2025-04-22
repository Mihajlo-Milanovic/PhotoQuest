package com.example.photoquest.ui.screens.map

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.models.data.Quest
import com.example.photoquest.services.getQuestsInRadius
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.example.photoquest.ui.screens.settings.SettingsScreenViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import kotlinx.coroutines.delay
import kotlin.math.cos

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

    val settingsViewModel = SettingsScreenViewModel.getInstance()

    var fusedLocationClient: FusedLocationProviderClient? = null
        private set

    var freeResources: () -> Unit = {}

    var inFocus by mutableStateOf(false)
    var location by mutableStateOf<LatLng?>(null)
        private set
    var camZoom by mutableFloatStateOf(17f)
    var closeUpView by mutableStateOf(false)

    var nearbyQuests = mutableListOf<Quest>()
    var searchInProgress by mutableStateOf(false)


    fun setFusedLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun setLocation(lat: Double, lng: Double) {
        location = LatLng(lat, lng)
    }

    fun getBoundsForRadius(center: LatLng, radiusKm: Double): LatLngBounds {
        val earthRadius = 6371.0

        val latDelta = Math.toDegrees(radiusKm / earthRadius)
        val lngDelta =
            Math.toDegrees(radiusKm / earthRadius / cos(Math.toRadians(center.latitude)))

        val southwest = LatLng(center.latitude - latDelta, center.longitude - lngDelta)
        val northeast = LatLng(center.latitude + latDelta, center.longitude + lngDelta)

        return LatLngBounds(southwest, northeast)
    }

    suspend fun checkForNearbyQuests(delayMillis: Long) {
        while (inFocus) {
            location?.let {
                nearbyQuests = getQuestsInRadius(
                    center = it,
                    radiusInKm = settingsViewModel.questSearchRadius
                )
            }
            delay(delayMillis)
        }
    }

    suspend fun checkForNearbyQuestsOnClick(context: Context) {

        val currentLocation = location
        if (currentLocation != null) {
            nearbyQuests = getQuestsInRadius(
                center = currentLocation,
                radiusInKm = settingsViewModel.questSearchRadius
            )
        } else {
            Toast.makeText(context, "Your location is unknown", Toast.LENGTH_SHORT).show()
        }
        searchInProgress = false
    }

}