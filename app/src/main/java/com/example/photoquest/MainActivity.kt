package com.example.photoquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.photoquest.services.Toaster
import com.example.photoquest.services.isUserSignedIn
import com.example.photoquest.ui.components.bottomBar.NavBarViewModel
import com.example.photoquest.ui.pictureFullSize.PictureFullSize
import com.example.photoquest.ui.screens.auxiliary.HardwareViewModel
import com.example.photoquest.ui.screens.auxiliary.LocationNotEnabledSplashScreen
import com.example.photoquest.ui.screens.auxiliary.NoInternetSplashScreen
import com.example.photoquest.ui.screens.auxiliary.isInternetAvailable
import com.example.photoquest.ui.screens.auxiliary.isLocationEnabled
import com.example.photoquest.ui.screens.leaderboard.LeaderboardScreen
import com.example.photoquest.ui.screens.logIn.LogInScreen
import com.example.photoquest.ui.screens.makeQuest.MakeQuestScreen
import com.example.photoquest.ui.screens.map.MapScreen
import com.example.photoquest.ui.screens.profile.ProfileScreen
import com.example.photoquest.ui.screens.signUp.SignUpScreen
import com.example.photoquest.ui.theme.PhotoQuestTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            PhotoQuestTheme {

                Toaster.setAppContext(LocalContext.current)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        setContent {
            PhotoQuestTheme {

                val hvm = HardwareViewModel.getInstance(LocalContext.current)

                val context = LocalContext.current

                if (!hvm.isConnected) {
                    NoInternetSplashScreen {
                        hvm.isConnected = isInternetAvailable(context = context)
                    }
                }

                if (!hvm.isLocationEnabled && isUserSignedIn()) {
                    LocationNotEnabledSplashScreen {
                        hvm.isLocationEnabled = isLocationEnabled(context = context)
                    }
                }

                PhotoQuestApp()
            }
        }

    }
}

@Composable
fun PhotoQuestApp() {

    val navController = rememberNavController()
    val navBarViewModel = NavBarViewModel.getInstance()

    val startScreen = if (isUserSignedIn()) Screens.MAP else Screens.LOG_IN

    NavHost(navController, startDestination = startScreen.name) {
        composable(Screens.LOG_IN.name) {
            navBarViewModel.setCurrentScreen(Screens.LOG_IN)
            LogInScreen(navController = navController)
        }
        composable(Screens.SIGN_UP.name) {
            navBarViewModel.setCurrentScreen(Screens.SIGN_UP)
            SignUpScreen(navController = navController)
        }
        composable(Screens.PROFILE.name) {
            navBarViewModel.setCurrentScreen(Screens.PROFILE)
            ProfileScreen(navController = navController)
        }
        dialog(Screens.PICTURE_FULL_SIZE.name) {
            PictureFullSize(navController = navController)
        }
        composable(Screens.MAKE_QUEST.name) {
            navBarViewModel.setCurrentScreen(Screens.MAKE_QUEST)
            MakeQuestScreen(navController = navController)
        }
        composable(Screens.LEADERBOARD.name) {
            navBarViewModel.setCurrentScreen(Screens.LEADERBOARD)
            LeaderboardScreen(navController = navController)
        }
        composable(Screens.MAP.name) {
            navBarViewModel.setCurrentScreen(Screens.MAP)
            MapScreen(navController = navController)

        }
    }
}

enum class Screens {
    LOG_IN,
    SIGN_UP,
    PROFILE,
    PICTURE_FULL_SIZE,
    MAKE_QUEST,
    MAP,
    LEADERBOARD,
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
    PhotoQuestTheme {
        PhotoQuestApp()
    }
}