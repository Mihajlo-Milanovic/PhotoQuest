package com.example.photoquest.ui.screens.leaderboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.ui.screens.auxiliary.NavExtender

class LeaderboardScreenViewModel : ViewModel(), NavExtender {

    companion object {

        private var INSTANCE: LeaderboardScreenViewModel? = null

        fun getInstance(): LeaderboardScreenViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = LeaderboardScreenViewModel()
                INSTANCE!!
            }
        }
    }

    override var navController by mutableStateOf<NavController?>(null)
}