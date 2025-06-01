package com.example.photoquest.ui.utilities

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

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