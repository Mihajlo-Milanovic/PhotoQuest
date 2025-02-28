package com.example.photoquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.photoquest.ui.screens.leaderboard.LeaderboardScreen
import com.example.photoquest.ui.screens.logIn.LogInScreen
import com.example.photoquest.ui.screens.makeQuest.MakeQuestScreen
import com.example.photoquest.ui.screens.profile.ProfilePictureFullSize
import com.example.photoquest.ui.screens.profile.ProfileScreen
import com.example.photoquest.ui.screens.signUp.SignUpScreen
import com.example.photoquest.ui.theme.PhotoQuestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            PhotoQuestTheme {

                PhotoQuestApp()
            }
        }
    }
}

@Composable
fun PhotoQuestApp() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = Screens.LOG_IN.name) {
        composable(Screens.LOG_IN.name) { LogInScreen(navController = navController) }
        composable(Screens.SIGN_UP.name) { SignUpScreen(navController = navController) }
        composable(Screens.PROFILE.name) { ProfileScreen(navController = navController) }
        dialog(Screens.PROFILE_PICTURE.name) { ProfilePictureFullSize(navController = navController) }
        composable(Screens.MAKE_QUEST.name) { MakeQuestScreen(navController = navController) }
        composable(Screens.LEADERBOARD.name) { LeaderboardScreen(navController = navController) }

    }
}

enum class Screens {
    LOG_IN,
    SIGN_UP,
    PROFILE,
    PROFILE_PICTURE,
    MAKE_QUEST,
    MAP,
    LEADERBOARD
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
    PhotoQuestTheme {
        PhotoQuestApp()
    }
}