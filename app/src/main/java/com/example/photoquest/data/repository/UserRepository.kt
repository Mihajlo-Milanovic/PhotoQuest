package com.example.photoquest.data.repository

import android.content.Context
import com.example.photoquest.data.local.UserLocalDataSource
import com.example.photoquest.data.local.entities.LocalUser
import com.example.photoquest.data.network.UserRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserRepository(
    applicationContext: Context,
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val externalScope: CoroutineScope,
) {

    private val userMutex = Mutex()

    suspend fun getUserWithUid(uid: String, refresh: Boolean = false): Flow<LocalUser?> {

        var userFlow: Flow<LocalUser?>

        userMutex.withLock {
            userFlow = userLocalDataSource.readUserWithUid(uid)
        }

        externalScope.async {
            if (refresh || userFlow.first() == null) {
                userMutex.withLock {
                    userRemoteDataSource.readUserWithUid(uid).first()?.also { user ->
                        userLocalDataSource.createUser(user)
                    }
                }
            }
        }.await()

        return userFlow
    }

    suspend fun createUser(user: LocalUser) {
        externalScope.async {
            userMutex.withLock {
                userLocalDataSource.createUser(user)
                userRemoteDataSource.createUser(user)
            }
        }.await()
    }

    suspend fun updateUser(user: LocalUser) {
        externalScope.async {
            userMutex.withLock {
                userLocalDataSource.updateUser(user)
                userRemoteDataSource.updateUser(user)
            }
        }.await()
    }
}