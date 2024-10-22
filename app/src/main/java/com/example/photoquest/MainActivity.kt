package com.example.photoquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.photoquest.ui.screens.logIn.LogInScreen
import com.example.photoquest.ui.screens.profile.ProfileScreen
import com.example.photoquest.ui.screens.signUp.SignUpScreen
import com.example.photoquest.ui.screens.start.StartScreen
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
fun PhotoQuestApp(){

    val navController = rememberNavController()

    NavHost(navController, startDestination = Screens.LogIn.name) {
        composable(Screens.Start.name) { StartScreen(navController = navController) }
        composable(Screens.LogIn.name) { LogInScreen(navController = navController) }
        composable(Screens.SignUp.name) { SignUpScreen(navController = navController) }
        composable(Screens.Profile.name) { ProfileScreen(navController = navController) }
    }
}

enum class Screens {
    Start,
    LogIn,
    SignUp,
    Profile,
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    PhotoQuestTheme {
        PhotoQuestApp()
    }
}