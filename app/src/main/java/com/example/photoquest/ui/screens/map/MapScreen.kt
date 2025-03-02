package com.example.photoquest.ui.screens.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.permissions.PermitLocationTrackingDialog


@Composable
fun MapScreen(navController: NavController) {

    val vm = MapScreenViewModel.getInstance()
    if (vm.navController.value == null)
        vm.setNavCtrl(navController)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { padding ->

        PermitLocationTrackingDialog(
            requestReason = R.string.locationPermissionReason,
            modifier = Modifier.padding(padding)
        )
        DrawMapScreen(modifier = Modifier.padding(padding))
    }
}

@Composable
fun DrawMapScreen(
    modifier: Modifier
) {
    Text("Map screen", modifier = modifier)
}