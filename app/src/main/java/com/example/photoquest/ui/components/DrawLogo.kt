package com.example.photoquest.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.photoquest.R

@Composable
fun DrawLogo() {

//    Text(
//        text = stringResource(id = R.string.photoQuest),
//        fontSize = 64.sp,
//        fontWeight = FontWeight.SemiBold,
//        fontStyle = FontStyle.Italic,
//        fontFamily = FontFamily.Cursive,
//        color = MaterialTheme.colorScheme.primary
//    )

    Image(painter = painterResource(R.mipmap.ic_photo_quest_foreground), "Photo Quest logo")
}