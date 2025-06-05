package com.example.photoquest.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.photoquest.data.model.UserView

//@Fts4
@Entity(tableName = "user")
data class LocalUser(

    @PrimaryKey val uid: String,

    @ColumnInfo(name = "username") val username: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,

    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "quests_made") val questsMade: Int,

    @ColumnInfo(name = "picture_uri") val pictureUri: String,
) {
    constructor(user: UserView) : this(
        uid = user.id,
        username = user.username,
        firstName = user.firstName,
        lastName = user.lastName,
        score = user.score,
        questsMade = user.questsMade,
        pictureUri = user.pictureUri.toString()
    )
}