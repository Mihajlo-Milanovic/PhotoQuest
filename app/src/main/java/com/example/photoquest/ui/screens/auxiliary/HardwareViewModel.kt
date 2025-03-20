package com.example.photoquest.ui.screens.auxiliary

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class HardwareViewModel private constructor(context: Context) : ViewModel() {

    companion object {
        private var INSTANCE: HardwareViewModel? = null

        fun getInstance(context: Context): HardwareViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = HardwareViewModel(context = context)
                INSTANCE!!
            }
        }
    }

    var isConnected by mutableStateOf(isInternetAvailable(context))

    var isLocationEnabled by mutableStateOf(isLocationEnabled(context))

    fun checkAgain(context: Context) {
        isConnected = isInternetAvailable(context)
        isLocationEnabled = isLocationEnabled(context)
    }
}