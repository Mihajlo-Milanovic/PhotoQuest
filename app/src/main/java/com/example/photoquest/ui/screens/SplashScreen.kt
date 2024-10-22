package com.example.photoquest.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.photoquest.R
import com.example.photoquest.ui.theme.PhotoQuestTheme

@Composable
fun SplashScreen(){

    Surface{
        Column (
            modifier = Modifier.padding(10.dp)
        ){
            Text(
                text = stringResource(id = R.string.photoQuest),
                fontSize = 64.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic,
                fontFamily = FontFamily.Cursive,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun SplashScreenPreview() {
    PhotoQuestTheme {
        SplashScreen()
    }
}