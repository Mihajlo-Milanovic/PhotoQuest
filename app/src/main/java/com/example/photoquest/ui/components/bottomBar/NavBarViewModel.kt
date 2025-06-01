package com.example.photoquest.ui.components.bottomBar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.R
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

    override var navController by mutableStateOf<NavController?>(null)

    val buttons = setOf(
        NavButton(name = Screens.LEADERBOARD, icon = R.drawable.baseline_leaderboard),
        NavButton(name = Screens.SETTINGS, icon = R.drawable.baseline_settings),
        NavButton(name = Screens.MAP, icon = R.drawable.baseline_photo_quest_logo),
        NavButton(name = Screens.MAKE_QUEST, icon = R.drawable.baseline_add_circle),
        NavButton(name = Screens.PROFILE, icon = R.drawable.baseline_person),
    )

    private val currentScreen = mutableStateOf(Screens.PROFILE)

    fun setCurrentScreen(screen: Screens) {
        currentScreen.value = screen
    }

    fun getCurrentScreen(): Screens {
        return currentScreen.value
    }

    fun navigateToScreen(screen: Screens) {
        navController!!.navigate(screen.name) {
            popUpTo(screen.name) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun selected(screen: Screens): Boolean {
        return currentScreen.value == screen
    }
}