package com.example.photoquest.ui.screens.leaderboard

import androidx.navigation.NavController
import com.example.photoquest.ui.screens.auxiliary.NavExtender

class LeaderboardScreenViewModel : NavExtender {

    companion object {

        private var INSTANCE: LeaderboardScreenViewModel? = null

        fun getInstance(): LeaderboardScreenViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = LeaderboardScreenViewModel()
                INSTANCE!!
            }
        }
    }

    override var navController: NavController? = null
}