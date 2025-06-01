package com.example.photoquest.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.data.services.signUserOut
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SettingsScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {
        private var INSTANCE: SettingsScreenViewModel? = null

        fun getInstance(): SettingsScreenViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = SettingsScreenViewModel()
                INSTANCE!!
            }
        }
    }

    override var navController by mutableStateOf<NavController?>(null)

    var questSearchRadius by mutableDoubleStateOf(10.0)
    var showSearchArea by mutableStateOf(true)

    var automaticallySearchForQuest by mutableStateOf(false)

    fun onSignOut() {
        runBlocking {

            launch(Dispatchers.Default) {
                signUserOut()
            }

            navController?.popBackStack(Screens.PROFILE.name, true)
            navController?.navigate(Screens.LOG_IN.name)


            //TODO: Design a function to reset everything after sign out
        }
    }

}