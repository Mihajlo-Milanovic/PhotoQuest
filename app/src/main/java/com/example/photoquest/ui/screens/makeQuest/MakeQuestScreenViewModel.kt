package com.example.photoquest.ui.screens.makeQuest

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.ui.screens.auxiliary.NavExtender

class MakeQuestScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {

        private var INSTANCE: MakeQuestScreenViewModel? = null

        fun getInstance(): MakeQuestScreenViewModel {

            return INSTANCE ?: synchronized(this) {
                INSTANCE = MakeQuestScreenViewModel()
                INSTANCE!!
            }
        }
    }

    override var navController: NavController? = null

    val showEnableLocationTrackingDialog = mutableStateOf(true)

}