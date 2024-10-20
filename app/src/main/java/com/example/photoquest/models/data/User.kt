package com.example.photoquest.models.data

import android.graphics.Picture

data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val picture: Picture,// TODO reconsider type Picture --> String
)
