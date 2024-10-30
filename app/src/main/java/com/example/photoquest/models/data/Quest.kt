package com.example.photoquest.models.data

import com.google.type.DateTime
import java.net.URL

data class Quest(
    val publisherId: String,
    val pictureURL: URL,
    val lat: Float,
    val lng: Float,
    val dateTime: DateTime
)