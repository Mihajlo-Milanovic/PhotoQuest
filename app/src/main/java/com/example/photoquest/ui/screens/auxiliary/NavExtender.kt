package com.example.photoquest.ui.screens.auxiliary

import androidx.navigation.NavController

interface NavExtender {

    //    val navController: MutableState<NavController?>
    var navController: NavController?

    fun setNavCtrl(navController: NavController) {
        this.navController = navController
    }
}