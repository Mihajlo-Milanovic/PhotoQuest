package com.example.photoquest.services

import android.net.Uri
import android.util.Log
import com.example.photoquest.models.data.Quest
import com.example.photoquest.models.data.User
import com.example.photoquest.ui.screens.makeQuest.MakeQuestScreenViewModel
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.component1
import com.google.firebase.storage.component2
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await


private const val users = "users"
private const val quests = "quests"
private const val publisherId = "publisherId"
private const val questPhotos = "questPhotos"
private const val profilePhotos = "profilePhotos"
private const val pictureDownloadURL = "pictureDownloadURL"

suspend fun createNewUser(id: String, user: User): Boolean {

    try {
        Firebase.firestore.collection(users).document(id).set(user).await()
        return true
    } catch (ex: Exception) {
        Log.e("MIKI", ex.message ?: "Error occurred while writing the user to the DB.")
        return false
    }
}

suspend fun getUserWithUid(uid: String): User? {

    try {
        return Firebase.firestore.collection(users).document(uid).get().await()?.toObject<User>()!!
    } catch (ex: Exception) {
        Log.e("MIKI", "Error occurred wile fetching the user data.\n\n ${ex.message}")
        return null
    }
}


suspend fun createNewQuest(quest: Quest): Boolean {

    try {

        val newQuestId = Firebase.firestore.collection(users).document(quest.publisherId)
            .collection(quests).add(quest).await().id

        quest.pictureUri?.let { imgUri ->

            val questPicRef = Firebase.storage.reference
                .child("$questPhotos/$newQuestId/${quest.title}.jpg")

            val vm = MakeQuestScreenViewModel.getInstance()

            questPicRef.putFile(imgUri)
                .addOnProgressListener { (bytesTransferred, totalByteCount) ->
                    vm.uploadState = (bytesTransferred.toDouble() / totalByteCount.toDouble())
                }.addOnFailureListener { ex ->
                    Log.e(
                        "MIKI",
                        "An error occurred while uploading a quest picture.\n\n ${ex.message}"
                    )
                }.await()

            try {
                val downloadURL = questPicRef.downloadUrl.await()

                Firebase.firestore.collection(users).document(quest.publisherId)
                    .collection(quests).document(newQuestId)
                    .update(pictureDownloadURL, downloadURL)

                quest.pictureDownloadURL = downloadURL

                vm.showUploadScreen = false

            } catch (ex: Exception) {
                Log.e(
                    "MIKI",
                    "An error occurred while updating a quest pictures URL.\n\n ${ex.message}"
                )
            }


        }

        return true
    } catch (ex: Exception) {
        Log.e("MIKI", "An error occurred while creating a new quest.\n\n ${ex.message}")
    }
    return false
}

suspend fun getUsersQuests(uid: String): MutableList<Quest> {

    val list = mutableListOf<Quest>()
    try {
        Firebase.firestore.collection(users).document(uid).collection(quests)
            .get().await()?.let { docs ->
                for (doc in docs) {
                    val quest = Quest(
                        publisherId = doc.getString("publisherId") ?: "",
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        lat = doc.getDouble("lat") ?: 0.0,
                        lng = doc.getDouble("lng") ?: 0.0,
                        timestamp = doc.getTimestamp("timestamp") ?: Timestamp.now(),
                        pictureUri = doc.getString("pictureUri")?.let { Uri.parse(it) },
                        pictureDownloadURL = doc.getString("pictureDownloadURL")
                            ?.let { Uri.parse(it) } ?: Uri.EMPTY
                    )
                    list += quest
                }
            }
        Log.d("MIKI", "Loaded quests: \n${list.map { " -> ${it.title}\n" }}")
    } catch (ex: Exception) {
        Log.e("MIKI", "An error occurred while fetching users quests.\n\n ${ex.message}")
    }
    return list
}
