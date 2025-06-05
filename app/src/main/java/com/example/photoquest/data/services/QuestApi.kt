package com.example.photoquest.data.services

import com.example.photoquest.data.local.entities.LocalQuest
import kotlinx.coroutines.flow.Flow

interface QuestApi {

    suspend fun insertQuest(quest: LocalQuest)

    fun readUsersQuests(uid: String): Flow<List<LocalQuest>>

    fun readQuestsInRadius(
        minLat: Double,
        maxLat: Double,
        minLng: Double,
        maxLng: Double
    ): Flow<List<LocalQuest>>
}