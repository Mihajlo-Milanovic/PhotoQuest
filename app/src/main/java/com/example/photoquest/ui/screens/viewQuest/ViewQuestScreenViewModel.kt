package com.example.photoquest.ui.screens.viewQuest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.ui.screens.auxiliary.NavExtender

class ViewQuestScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {
        private var INSTANCE: ViewQuestScreenViewModel? = null

        fun getInstance(): ViewQuestScreenViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = ViewQuestScreenViewModel()
                INSTANCE!!
            }
        }
    }

    override var navController by mutableStateOf<NavController?>(null)


}