package com.example.photoquest.ui.components.bottomBar

import androidx.compose.runtime.mutableStateOf
import com.example.photoquest.Screens

class NavBarViewModel private constructor() {

    companion object {
        private var INSTANCE: NavBarViewModel? = null

        fun getInstance(): NavBarViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = NavBarViewModel()
                INSTANCE!!
            }
        }
    }

    private val currentScreen = mutableStateOf(Screens.PROFILE)

    fun navigateToScreen(screen: Screens) {

    }

    fun selected(screen: Screens): Boolean {
        return currentScreen.value == screen
    }
}