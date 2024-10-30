package com.example.photoquest.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.photoquest.R

@Composable
fun DrawLogo(){

    Text(
        text = stringResource(id = R.string.photoQuest),
        fontSize = 64.sp,
        fontWeight = FontWeight.SemiBold,
        fontStyle = FontStyle.Italic,
        fontFamily = FontFamily.Cursive,
        color = MaterialTheme.colorScheme.primary
    )
}