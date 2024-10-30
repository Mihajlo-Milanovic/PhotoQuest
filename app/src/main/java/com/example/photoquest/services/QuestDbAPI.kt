package com.example.photoquest.services

import android.util.Log
import com.example.photoquest.models.data.Quest
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class QuestDbAPI {

    private val quests = "quests"
    private val publisherId = "publisherId"

    suspend fun createNewQuest(quest: Quest) {

        try {
            Firebase.firestore.collection(quests).add(quest).await().id
//          Firebase.firestore.collection(quests).document(quest.id)

        } catch (ex: Exception) {
            Log.e("MIKI", ex.message ?: "Error occurred while writing the user in the DB.")
        }
    }

    suspend fun getUsersQuests(uid: String): List<Quest> {

        val list = arrayListOf<Quest>()
         Firebase.firestore.collection(quests).whereEqualTo(publisherId, uid).get().await()?.let { docs ->

            for (doc in docs)
                list += doc.toObject<Quest>()
        }

        return list
    }

}