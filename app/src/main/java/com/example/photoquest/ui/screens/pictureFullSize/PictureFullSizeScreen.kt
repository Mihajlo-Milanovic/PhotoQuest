package com.example.photoquest.ui.screens.pictureFullSize

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.photoquest.R

@Composable
fun PictureFullSize(
    navController: NavController,
) {
    val vm = PictureFullSizeViewModel.getInstance()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
        onClick = { navController.popBackStack() },

        ) {

        if (vm.imageUri != null)
            AsyncImage(
                model = vm.imageUri,
                contentDescription = vm.contentDescription,
                modifier = Modifier.fillMaxSize()
            )
        else
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.imageUnavailable),
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
    }
}