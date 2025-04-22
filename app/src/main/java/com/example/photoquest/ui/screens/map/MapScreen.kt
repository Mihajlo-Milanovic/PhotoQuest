package com.example.photoquest.ui.screens.map

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.services.getUserLocationUpdates
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.permissions.PermitLocationTrackingDialog
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


@Composable
fun MapScreen(navController: NavController) {

    val context = LocalContext.current
    val vm = remember { MapScreenViewModel.getInstance() }
    val questCoroutineScope = rememberCoroutineScope()
    val locationCoroutineScope = rememberCoroutineScope()
    val camPosition = rememberCameraPositionState(
//        "userLocation",
//        init = {
//            CameraPosition(
//                vm.location ?: LatLng(44.8125, 20.4612),
//                12f,
//                0f,
//                0f
//            )
//        }
    )

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
        if (vm.settingsViewModel.automaticallySearchForQuest) {
            questCoroutineScope.launch(Dispatchers.Default) {
                vm.checkForNearbyQuests(5_000)
            }
        }
        onDispose {
            vm.freeResources
            vm.inFocus = false
        }
    }

    LaunchedEffect(vm.location, vm.closeUpView /*vm.camZoom*/) {
        vm.location?.let {
            if (vm.closeUpView)
                camPosition.animate(
                    update = CameraUpdateFactory.newLatLngZoom(
                        it,
                        vm.camZoom
                    )
                )
            else
                camPosition.animate(
                    update = CameraUpdateFactory.newLatLngBounds(
                        vm.getBoundsForRadius(it, vm.settingsViewModel.questSearchRadius),
                        8
                    )
                )
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        },
    ) { pv ->

        PermitLocationTrackingDialog(
            requestReason = R.string.locationPermissionReason,
            modifier = Modifier.padding(pv)
        )

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv),
            cameraPositionState = camPosition,
            properties = MapProperties(
                isMyLocationEnabled = vm.location != null,
                mapType = MapType.HYBRID
            ),

            ) {
            vm.nearbyQuests.forEach {
                Marker(
                    state = MarkerState(position = LatLng(it.lat, it.lng)),
                    title = it.title,
                )
            }
        }



        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
                .padding(start = 8.dp)
                .padding(top = 12.dp, bottom = 32.dp)
                .alpha(0.8f)
        ) {

            FloatingActionButton(
                onClick = {
                    if (vm.closeUpView)
                        vm.camZoom = camPosition.position.zoom

                    vm.closeUpView = !vm.closeUpView
                },
                shape = RectangleShape,
                contentColor = Color.DarkGray,
                containerColor = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(37.dp)
            ) {
                if (vm.closeUpView)
                    Icon(
                        painter = painterResource(R.drawable.baseline_zoom_out_map_24),
                        contentDescription = "Zoom out map",
                        modifier = Modifier
                            .size(if (!vm.searchInProgress) Dp.Unspecified else 0.dp)
                            .animateContentSize()
                    )
                else
                    Icon(
                        painter = painterResource(R.drawable.baseline_zoom_in_map_24),
                        contentDescription = "Zoom in map",
                        modifier = Modifier
                            .size(if (!vm.searchInProgress) Dp.Unspecified else 0.dp)
                            .animateContentSize()
                    )
            }

            if (!vm.settingsViewModel.automaticallySearchForQuest) {
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 0.5f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(600, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse"
                )

                FloatingActionButton(
                    onClick = {
                        if (!vm.searchInProgress) {
                            vm.searchInProgress = true
                            questCoroutineScope.launch(Dispatchers.Default) {
                                vm.checkForNearbyQuestsOnClick(context)
                            }
                        } else {
                            questCoroutineScope.cancel()
                            vm.searchInProgress = false
                        }
                    },
                    contentColor = Color.DarkGray,
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .scale(if (vm.searchInProgress) pulseScale else 1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = if (vm.searchInProgress) "Turn off quest search" else "Search for quests",
                        modifier = Modifier
                            .size(if (!vm.searchInProgress) Dp.Unspecified else 0.dp)
                            .animateContentSize()
                    )
                }
            }
        }
    }
}


//@Preview(
//    name = "DarkTheme",
//    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
//    showBackground = true
//)
//@Composable
//fun NavBarPreview() {
//    PhotoQuestTheme {
//        Scaffold(
//            bottomBar = {
//                NavBar(navController = NavController(LocalContext.current))
//            }) { pv ->
//            FloatingActionButton(
//                onClick = {
//                },
//                contentColor = MaterialTheme.colorScheme.surfaceContainerHighest,
//                containerColor = MaterialTheme.colorScheme.onSurface,
//                modifier = Modifier
//                    .size(32.dp)
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.baseline_zoom_in_map_24),
//                    contentDescription = "Zoom in map",
//                    modifier = Modifier
//                        //.size(if (!vm.searchInProgress) Dp.Unspecified else 0.dp)
//                        .animateContentSize()
//                )
//            }
//        }
//    }
//}