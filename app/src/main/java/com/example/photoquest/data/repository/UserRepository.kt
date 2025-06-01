package com.example.photoquest.data.repository

import com.example.photoquest.data.model.User
import com.example.photoquest.data.network.UserRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserRepository(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val externalScope: CoroutineScope
) {
    private val userMutex = Mutex()

    private var userMap: MutableMap<String, User> = mutableMapOf()

    suspend fun getUserWithUid(uid: String, refresh: Boolean = false): User? {

        if (refresh || userMap[uid] == null/*there is no local copy*/) {
            externalScope.async {
                userRemoteDataSource.getUserWithUid(uid)?.also { user ->
                    // Thread-safe write
                    userMutex.withLock {
                        userMap[uid] = user
                    }
                }
            }.await()
        }
        return userMutex.withLock { userMap[uid] }
    }

    suspend fun createNewUser(user: User): Boolean {
        return externalScope.async {
            userRemoteDataSource.createNewUser(user)
        }.await()
    }
}