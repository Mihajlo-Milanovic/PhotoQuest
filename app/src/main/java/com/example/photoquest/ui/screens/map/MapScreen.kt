package com.example.photoquest.ui.screens.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.permissions.PermitLocationTrackingDialog
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType


@Composable
fun MapScreen(navController: NavController) {

    val context = LocalContext.current

    val vm = MapScreenViewModel.getInstance()

    if (vm.fusedLocationClient == null)
        vm.setFusedLocationClient(context)

    if (vm.navController.value == null)
        vm.setNavCtrl(navController)

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

        vm.fusedLocationClient?.let {
            vm.getUserLocation(
                context = context,
            ) { location ->
                vm.setLocation(location.latitude, location.longitude)
            }
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            cameraPositionState = vm.cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = vm.location.value != null,
                mapType = MapType.HYBRID
            )
        ) {
//            vm.location.value?.let {
//                Marker(
//                    state = MarkerState(position = it),
//                    title = "You are here"
//                )
//            }
        }

    }
}
