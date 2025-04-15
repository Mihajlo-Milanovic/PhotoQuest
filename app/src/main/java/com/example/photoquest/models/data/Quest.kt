package com.example.photoquest.models.data

import android.net.Uri
import com.google.firebase.Timestamp

data class Quest(
    val publisherId: String = "",
    val title: String = "",
    val pictureUri: Uri? = null,
    var pictureDownloadURL: Uri = Uri.EMPTY,
    val description: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val timestamp: Timestamp = Timestamp.now(),
    var numberOfLikes: Int = 0
)