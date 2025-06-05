package com.example.photoquest.data.network

import com.example.photoquest.data.local.entities.LocalUser
import com.example.photoquest.data.services.UserApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRemoteDataSource(
    private val userApi: UserApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    fun readUserWithUid(uid: String): Flow<LocalUser?> = userApi.readUserWithUid(uid)

    suspend fun createUser(user: LocalUser) = withContext(ioDispatcher) {
        userApi.createUser(user)
    }

    suspend fun updateUser(user: LocalUser) = withContext(ioDispatcher) {
        userApi.updateUser(user)
    }

}
