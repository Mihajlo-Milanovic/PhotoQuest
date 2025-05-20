package com.example.photoquest.utilities

import android.content.ContentValues
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.MutableState
import coil.ImageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

fun createImageUri(context: Context, imageName: String): Uri? {
    val appName = context.applicationInfo.loadLabel(context.packageManager).toString()
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "${imageName}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$appName")
        put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis() / 1000)
    }
    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )
}

fun rotationNeeded(
    context: Context,
    imageUri: Uri?,
    result: MutableState<Boolean>
) {
    CoroutineScope(EmptyCoroutineContext).launch(Dispatchers.IO) {

        try {
            val loader = ImageLoader(context)

            val request = ImageRequest.Builder(context)
                .data(imageUri)
                .allowHardware(false)
                .build()

            val image = (loader.execute(request).drawable as? BitmapDrawable)?.bitmap

            result.value = if (image != null && image.width > image.height)
                true
            else
                false

        } catch (e: Exception) {
            Log.e(
                "MIKI [modifier extension]",
                "Error occurred while rotating the image:\n\n" + e.message
            )
        }

    }
}