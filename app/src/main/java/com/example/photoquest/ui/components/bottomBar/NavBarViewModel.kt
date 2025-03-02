package com.example.photoquest.ui.components.bottomBar

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.ui.screens.auxiliary.NavExtender

class NavBarViewModel private constructor() : ViewModel(), NavExtender {

    companion object {
        private var INSTANCE: NavBarViewModel? = null

        fun getInstance(): NavBarViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = NavBarViewModel()
                INSTANCE!!
            }
        }
    }

    override val navController: MutableState<NavController?> = mutableStateOf(null)

    private val currentScreen = mutableStateOf(Screens.PROFILE)

    fun setCurrentScreen(screen: Screens) {
        currentScreen.value = screen
    }

    fun navigateToScreen(screen: Screens) {
        if (!navController.value?.popBackStack(screen.name, false)!!)
            navController.value?.navigate(screen.name)
//        currentScreen.value = screen
    }

    fun selected(screen: Screens): Boolean {
        return currentScreen.value == screen
    }
}