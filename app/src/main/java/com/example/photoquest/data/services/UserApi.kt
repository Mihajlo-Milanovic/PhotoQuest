package com.example.photoquest.data.services

import com.example.photoquest.data.local.entities.LocalUser
import kotlinx.coroutines.flow.Flow

interface UserApi {

    fun readUserWithUid(uid: String): Flow<LocalUser?>

    suspend fun createUser(user: LocalUser)

    suspend fun updateUser(user: LocalUser)
}