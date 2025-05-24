package com.example.photoquest.ui.components

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.photoquest.R
import com.example.photoquest.ui.theme.PhotoQuestTheme

@Composable
fun PictureFullSizeDialog(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    contentDescription: String? = null,
    onDismissRequest: () -> Unit,
    content: @Composable BoxScope.() -> Unit = {}
) {
    val context = LocalContext.current

    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableFloatStateOf(1f) }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, gestureZoom, _ ->

                        val oldScale = zoom
                        val newScale = (zoom * gestureZoom).coerceIn(1f, 5f)

                        offset = if (oldScale == 1f) Offset.Zero else
                            ((offset + centroid / oldScale) - (centroid / newScale + pan / oldScale))

                        zoom = newScale
                    }
                }
                .clickable { onDismissRequest() }
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable(onClick = onDismissRequest)
            ) {

                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(imageUri)
                        .placeholder(
                            if (imageUri == null) R.drawable.baseline_image_not_supported
                            else R.drawable.baseline_image
                        )
                        .crossfade(true)
                        .build(),
                    contentDescription = contentDescription,
                    modifier = Modifier
                        .fillMaxSize(if (imageUri == null) 0.3f else 1f)
                        .graphicsLayer(
                            translationX = -offset.x * zoom,
                            translationY = -offset.y * zoom,
                            scaleX = zoom,
                            scaleY = zoom,
                            transformOrigin = TransformOrigin(0f, 0f)
                        )
                )

                if (imageUri == null) {

                    Text(
                        text = stringResource(R.string.imageUnavailable),
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            content()
        }
    }
}


@Composable
fun ProfilePictureFullSizeDialog(
    modifier: Modifier = Modifier,
    imageUri: Uri?,
    contentDescription: String? = null,
    onDismissRequest: () -> Unit,
    buttonOnClick: () -> Unit,
) {

    PictureFullSizeDialog(
        modifier = modifier,
        imageUri = imageUri,
        contentDescription = contentDescription,
        onDismissRequest = onDismissRequest
    ) {
        Column {
            Button(
                onClick = buttonOnClick,
                modifier = Modifier
                    .fillMaxWidth(0.95f)
            ) {
                Text(stringResource(R.string.update_profile_pic))
            }

            Spacer(Modifier.height(8.dp))
        }
    }
}


@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
fun ProfilePictureFullSizeDialogPreview(
) {
    PhotoQuestTheme {

        ProfilePictureFullSizeDialog(
            imageUri = null,
            contentDescription = null,
            onDismissRequest = {},
            buttonOnClick = {},
        )

    }
}