package com.example.photoquest.data.network

import com.example.photoquest.data.model.User
import com.example.photoquest.data.services.UserApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class UserRemoteDataSource(
    private val userApi: UserApi,
    private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getUserWithUid(uid: String): User? = withContext(ioDispatcher) {
        userApi.getUserWithUid(uid)
    }

    suspend fun createNewUser(user: User): Boolean = withContext(ioDispatcher) {
        userApi.createNewUser(user)
    }

}
