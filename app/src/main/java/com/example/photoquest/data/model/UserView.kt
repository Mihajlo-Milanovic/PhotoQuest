package com.example.photoquest.data.model

import android.net.Uri
import androidx.core.net.toUri
import com.example.photoquest.data.local.entities.LocalUser

data class UserView(
    val id: String = "",
//    val email: String= "", //TODO Reconsider
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val score: Int = 0,
    val questsMade: Int = 0,
    val pictureUri: Uri? = null, //TODO: Reconsider
) {

    constructor(localUser: LocalUser) : this(
        id = localUser.uid,
        username = localUser.username,
        firstName = localUser.firstName,
        lastName = localUser.lastName,
        score = localUser.score,
        questsMade = localUser.questsMade,
        pictureUri = localUser.pictureUri.toUri(),
    )
}