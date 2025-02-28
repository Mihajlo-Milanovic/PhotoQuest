package com.example.photoquest.ui.components.bottomBar

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.ui.screens.auxiliary.NavExtender

class NavBarViewModel private constructor() : NavExtender {

    companion object {
        private var INSTANCE: NavBarViewModel? = null

        fun getInstance(): NavBarViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = NavBarViewModel()
                INSTANCE!!
            }
        }
    }

    override var navController: NavController? = null

    private val currentScreen = mutableStateOf(Screens.PROFILE)

    fun navigateToScreen(screen: Screens) {
        if (!navController?.popBackStack(screen.name, false)!!)
            navController?.navigate(screen.name)
        currentScreen.value = screen
    }

    fun selected(screen: Screens): Boolean {
        return currentScreen.value == screen
    }
}