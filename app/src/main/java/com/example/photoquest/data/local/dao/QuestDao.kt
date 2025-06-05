package com.example.photoquest.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photoquest.data.local.entities.LocalQuest
import com.example.photoquest.data.services.QuestApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface QuestDao : QuestApi {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insertQuest(quest: LocalQuest)

    @Query(
        """
        select *
        from quest
        where publisher_id == :uid
    """
    )
    abstract override fun readUsersQuests(uid: String): Flow<List<LocalQuest>>

    fun readUsersQuestsDistinctUtilChanged(uid: String) =
        readUsersQuests(uid).distinctUntilChanged()

    @Query(
        """
        select *
        from quest
        where quest.lat in (:minLat, :maxLat) 
            and quest.lng in (:minLng, :maxLng)
    """
    )
    abstract override fun readQuestsInRadius(
        minLat: Double,
        maxLat: Double,
        minLng: Double,
        maxLng: Double
    ): Flow<List<LocalQuest>>

    suspend fun readQuestsInRadiusDistinctUntilChanged(
        minLat: Double,
        maxLat: Double,
        minLng: Double,
        maxLng: Double
    ) = readQuestsInRadius(
        minLat = minLat,
        maxLat = maxLat,
        minLng = minLng,
        maxLng = maxLng
    ).distinctUntilChanged()

}