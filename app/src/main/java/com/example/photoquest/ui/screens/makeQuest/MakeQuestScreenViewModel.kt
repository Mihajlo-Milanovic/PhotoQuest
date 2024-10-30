package com.example.photoquest.ui.screens.makeQuest

import androidx.compose.runtime.mutableStateOf

class MakeQuestScreenViewModel private constructor() {

    companion object{

        private var INSTANCE: MakeQuestScreenViewModel? = null

        fun getInstance(): MakeQuestScreenViewModel{

            return INSTANCE?: synchronized(this){
                INSTANCE = MakeQuestScreenViewModel()
                INSTANCE!!
            }
        }
    }

    val showEnableLocationTrackingDialog = mutableStateOf(true)

}