package com.example.photoquest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.photoquest.data.local.entities.LocalUser
import com.example.photoquest.data.services.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface UserDao : UserApi {

    @Query(
        """
        select *
        from user 
        where uid == :uid
    """
    )
    abstract override fun readUserWithUid(uid: String): Flow<LocalUser?>

    fun readUserDistinctUtilChanged(uid: String) =
        readUserWithUid(uid).distinctUntilChanged()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun createUser(user: LocalUser)

    @Update
    override suspend fun updateUser(user: LocalUser)
}