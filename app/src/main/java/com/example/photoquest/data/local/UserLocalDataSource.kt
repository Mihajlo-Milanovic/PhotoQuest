package com.example.photoquest.data.local

import android.content.Context
import com.example.photoquest.data.local.database.LocalDatabase
import com.example.photoquest.data.local.entities.LocalUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class UserLocalDataSource(
    applicationContext: Context,
    private val ioDispatcher: CoroutineContext,
) {

    private val userDao = LocalDatabase.getInstance(applicationContext).userDao()

    fun readUserWithUid(uid: String): Flow<LocalUser?> = userDao.readUserDistinctUtilChanged(uid)

    suspend fun createUser(user: LocalUser) = withContext(ioDispatcher) {
        userDao.createUser(user)
    }

    suspend fun updateUser(user: LocalUser) = withContext(ioDispatcher) {
        userDao.updateUser(user)
    }
}