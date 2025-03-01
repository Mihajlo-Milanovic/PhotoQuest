package com.example.photoquest.ui.screens.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.photoquest.ui.components.bottomBar.NavBar

@Composable
fun MapScreen(navController: NavController) {

    val vm = MapScreenViewModel.getInstance()
    if (vm.navController == null)
        vm.setNavCtrl(navController)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { padding ->
        Text("Map screen", modifier = Modifier.padding(padding))
    }
}