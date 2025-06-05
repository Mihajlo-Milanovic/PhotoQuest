package com.example.photoquest.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.photoquest.data.local.dao.QuestDao
import com.example.photoquest.data.local.dao.UserDao
import com.example.photoquest.data.local.entities.LocalQuest
import com.example.photoquest.data.local.entities.LocalUser

@Database(entities = [LocalUser::class, LocalQuest::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun questDao(): QuestDao

    companion object {

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(applicationContext: Context): LocalDatabase {

            return INSTANCE ?: synchronized(this) {

                INSTANCE = Room.databaseBuilder(
                    context = applicationContext,
                    klass = LocalDatabase::class.java,
                    name = "local-database"
                )
                    .enableMultiInstanceInvalidation()
                    .build()

                INSTANCE!!
            }
        }
    }
}