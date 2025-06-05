package com.example.photoquest.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4

@Fts4
@Entity(tableName = "quest")
data class LocalQuest(

    //TODO: see if this needs ID and of which type

    @ColumnInfo(name = "publisher_id") val publisherId: String,

    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "picture_uri") val pictureUri: String,
    @ColumnInfo(name = "picture_download_uri") var pictureDownloadURL: String,

    @ColumnInfo(name = "lat") val lat: Double?,
    @ColumnInfo(name = "lng") val lng: Double?,
    @ColumnInfo(name = "timestamp") val timestamp: String,


    @ColumnInfo(name = "number_of_likes") var numberOfLikes: Int?

)