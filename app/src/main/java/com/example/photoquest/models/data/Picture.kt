package com.example.photoquest.models.data

import com.google.type.DateTime
import java.net.URL

data class Picture(
    val pictureURL: URL, // TODO reconsider type Picture --> String
    val lat: Int,
    val lng: Int,
    val publisherId: String,
    val dateTime: DateTime
)
