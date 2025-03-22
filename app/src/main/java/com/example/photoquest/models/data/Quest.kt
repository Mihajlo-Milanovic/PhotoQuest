package com.example.photoquest.models.data

import com.google.firebase.Timestamp

data class Quest(
    val publisherId: String = "",
    val title: String = "",
    val description: String = "",
    val pictureURL: String = "",
    val lat: Float = 0f,
    val lng: Float = 0f,
    val timestamp: Timestamp = Timestamp.now()
)