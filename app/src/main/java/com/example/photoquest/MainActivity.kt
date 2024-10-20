package com.example.photoquest

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.photoquest.ui.screens.logIn.LogInScreen
import com.example.photoquest.ui.screens.logIn.LoginScreenPreview
import com.example.photoquest.ui.theme.PhotoQuestTheme

class MainActivity() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PhotoQuestTheme {
                LogInScreen()
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    PhotoQuestTheme {
        LogInScreen()
    }
}