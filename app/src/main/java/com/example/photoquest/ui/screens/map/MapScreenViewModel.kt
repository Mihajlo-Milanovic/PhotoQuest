package com.example.photoquest.ui.screens.map

import android.content.Context
import android.os.Looper
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.models.data.Quest
import com.example.photoquest.services.getBoundsForRadius
import com.example.photoquest.services.getQuestsInRadius
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.example.photoquest.ui.screens.settings.SettingsScreenViewModel
import com.example.photoquest.ui.screens.viewQuest.ViewQuestScreenViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.delay

const val EARTH_RADIUS_KM = 6378.137

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

    fun viewQuest(quest: Quest) {
        ViewQuestScreenViewModel.getInstance().quest = quest

        navController!!.navigate(Screens.VIEW_QUEST.name) {
            popUpTo(Screens.VIEW_QUEST.name) {
                inclusive = false
            }
            launchSingleTop = true
        }
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

        location.let {
            if (it != null) {
                nearbyQuests = getQuestsInRadius(
                    center = it,
                    radiusInKm = settingsViewModel.questSearchRadius
                )
            } else {
                Looper.prepare()
                Toast.makeText(context, "Your location is unknown", Toast.LENGTH_SHORT).show()
            }
        }
        searchInProgress = false
    }

    suspend fun setCameraPositionToUserLocation(cameraPosition: CameraPositionState) {

        location?.let {
            if (closeUpView)
                cameraPosition.animate(
                    update = CameraUpdateFactory.newLatLngZoom(
                        it,
                        camZoom
                    )
                )
            else
                cameraPosition.animate(
                    update = CameraUpdateFactory.newLatLngBounds(
                        getBoundsForRadius(it, settingsViewModel.questSearchRadius),
                        8
                    )
                )
        }
    }

}
