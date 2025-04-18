package com.example.photoquest.ui.screens.map

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.services.getUserLocationUpdates
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.permissions.PermitLocationTrackingDialog
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun MapScreen(navController: NavController) {

    val context = LocalContext.current
    val vm = remember { MapScreenViewModel.getInstance() }
    val coroutineScope = rememberCoroutineScope()

    DisposableEffect(vm) {
        if (vm.fusedLocationClient == null) {
            vm.setFusedLocationClient(context)
        }
        if (vm.navController == null)
            vm.setNavCtrl(navController)


        Log.w("MIKI", "Tracking user location now...")
        vm.freeResources = getUserLocationUpdates(
            context = context,
            fusedLocationClient = vm.fusedLocationClient!!,
            locationAccuracyPriority = Priority.PRIORITY_HIGH_ACCURACY
        ) {
            vm.setLocation(it.latitude, it.longitude)
        }


        vm.inFocus = true
        coroutineScope.launch(Dispatchers.Default) {
            vm.checkForNearbyQuests()
        }

        onDispose {
            vm.freeResources
            vm.inFocus = false
        }
    }

    LaunchedEffect(vm.location) {
        vm.location?.let {
            vm.setCameraPositionState(LatLng(it.latitude, it.longitude))
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        },
    ) { padding ->

        PermitLocationTrackingDialog(
            requestReason = R.string.locationPermissionReason,
            modifier = Modifier.padding(padding)
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = vm.cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = vm.location != null,
                mapType = MapType.HYBRID
            )
        ) {
            vm.nearbyQuests.forEach {
                Marker(
                    state = MarkerState(position = LatLng(it.lat, it.lng)),
                    title = it.title,
                )
            }
        }

    }
}
