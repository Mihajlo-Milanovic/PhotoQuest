package com.example.photoquest.ui.screens.start

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun StartScreen(
    navController: NavController
){

    //SplashScreen()
    Text(text = "This is start screen.")

/*
    if(false) // TODO: From VM find if the user is logged in or not and navigate to the needed screen accordingly
        ProfileScreen(navController)
    else
        LogInScreen(nav)
        */
}