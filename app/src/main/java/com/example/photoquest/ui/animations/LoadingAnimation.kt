package com.example.photoquest.ui.animations

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.photoquest.ui.theme.PhotoQuestTheme

        //TODO revisit this


@Composable
fun SimpleLoadingIndicator() {

    CircularProgressIndicator( color = MaterialTheme.colorScheme.primary, )
}

@Preview(showBackground = true)
@Composable
fun SimpleLoadingIndicatorPreview() {
    PhotoQuestTheme {
        SimpleLoadingIndicator()
    }
}