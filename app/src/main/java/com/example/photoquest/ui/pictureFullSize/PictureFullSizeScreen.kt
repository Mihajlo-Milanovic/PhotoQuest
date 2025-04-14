package com.example.photoquest.ui.pictureFullSize

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
            .clickable { navController.popBackStack() },
        contentAlignment = Alignment.Center
    ) {

        if (vm.imageUri != null)
            AsyncImage(
                model = vm.imageUri,
                contentDescription = vm.contentDescription,
                modifier = Modifier.fillMaxSize()
            )
        else
            Text(
                text = stringResource(R.string.imageUnavailable),
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
    }
}