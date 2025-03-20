package com.example.photoquest.models.data

data class User(
    val id: String = "",
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val score: Int = 0,
    val questsMade: Int = 0,
    val pictureURL: String = "", //TODO: Razmotriti
)