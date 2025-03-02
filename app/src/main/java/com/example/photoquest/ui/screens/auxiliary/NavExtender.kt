package com.example.photoquest.ui.screens.auxiliary

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController

interface NavExtender {

    val navController: MutableState<NavController?>

    fun setNavCtrl(navController: NavController) {
        this.navController.value = navController
    }
}