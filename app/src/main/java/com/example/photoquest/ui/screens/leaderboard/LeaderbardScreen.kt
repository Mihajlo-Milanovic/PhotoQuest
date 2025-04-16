package com.example.photoquest.ui.screens.leaderboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.photoquest.ui.components.bottomBar.NavBar

@Composable
fun LeaderboardScreen(navController: NavController) {

    val vm = LeaderboardScreenViewModel.getInstance()
    if (vm.navController == null)
        vm.setNavCtrl(navController)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { padding ->
        Text("Leaderboard screen", modifier = Modifier.padding(padding))
    }
}