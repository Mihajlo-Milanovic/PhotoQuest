package com.example.photoquest.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.ui.screens.auxiliary.NavExtender

class MapScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {
        private var INSTANCE: MapScreenViewModel? = null

        fun getInstance(): MapScreenViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = MapScreenViewModel()
                INSTANCE!!
            }
        }
    }

    override var navController: NavController? = null
        
}