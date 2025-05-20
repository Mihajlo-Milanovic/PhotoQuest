package com.example.photoquest.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.photoquest.R
import com.example.photoquest.utilities.rotationNeeded

@Composable
fun PictureFullSizeDialog(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    contentDescription: String? = null,
    onDismissRequest: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit) = {}
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(bottom = 16.dp)
                .clickable(onClick = onDismissRequest)
        ) {

            if (imageUri != null) {

                val rotationNeeded = remember { mutableStateOf(false) }
                rotationNeeded(context = context, imageUri = imageUri, result = rotationNeeded)

                AsyncImage(
                    model = imageUri,
                    contentDescription = contentDescription,
                    modifier = if (rotationNeeded.value)
                        modifier
                            .fillMaxHeight()
                    else
                        modifier.fillMaxWidth(),
                )
            } else {

                Text(
                    text = stringResource(R.string.imageUnavailable),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.fillMaxHeight(0.4f))
            }

            content()
        }
    }
}