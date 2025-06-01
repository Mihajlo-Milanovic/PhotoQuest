package com.example.photoquest.data.model

import android.net.Uri

data class User(
    val id: String = "",
//    val email: String= "", //TODO Reconsider
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val score: Int = 0,
    val questsMade: Int = 0,
    val pictureUri: Uri? = null, //TODO: Reconsider
)