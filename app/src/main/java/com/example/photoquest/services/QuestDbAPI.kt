package com.example.photoquest.services

import android.util.Log
import com.example.photoquest.models.data.Quest
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

private const val quests = "quests"
private const val publisherId = "publisherId"

suspend fun createNewQuest(quest: Quest) {

    try {
        var newQuestId: String = "[NEMA NISTA]"
        newQuestId = Firebase.firestore.collection(quests).add(quest).await().id

        Log.d("MIKI", "New quests ID is: $newQuestId")

    } catch (ex: Exception) {
        Log.e("MIKI ERROR", "An error occurred while creating a new quest.\n\n" + ex.message)
    }
}

suspend fun getUsersQuests(uid: String): MutableList<Quest> {

    val list = mutableListOf<Quest>()
    Firebase.firestore.collection(quests)
        .whereEqualTo(publisherId, uid).get().await()?.let { docs ->

            for (doc in docs)
                list += doc.toObject<Quest>()
        }

    return list
}
