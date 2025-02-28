package com.example.photoquest.ui.screens.auxiliary

import androidx.navigation.NavController

interface NavExtender {

    var navController: NavController?

    fun setNavCtrl(navController: NavController) {
        this.navController = navController
    }
}