package com.example.photoquest.data.model

import android.net.Uri
import com.google.firebase.Timestamp

data class Quest(
    val publisherId: String = "",

    val title: String = "",
    val description: String = "",
    var pictureDownloadURL: Uri = Uri.EMPTY,

    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val timestamp: Timestamp = Timestamp.now(),

    var numberOfLikes: Int = 0
)