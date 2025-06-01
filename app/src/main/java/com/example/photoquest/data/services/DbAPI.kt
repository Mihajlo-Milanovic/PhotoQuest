package com.example.photoquest.data.services

import android.net.Uri
import android.util.Log
import com.example.photoquest.data.model.Quest
import com.example.photoquest.ui.screens.makeQuest.MakeQuestScreenViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.component1
import com.google.firebase.storage.component2
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import kotlin.math.cos

private const val earthRadius = 6371.0 // Radius of Earth in km
private const val users = "users"
private const val quests = "quests"
private const val publisherId = "publisherId"
private const val questPhotos = "questPhotos"
private const val profilePhotos = "profilePhotos"
private const val pictureDownloadURL = "pictureDownloadURL"


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

        //TODO: Update 'questsMade' filed for the user

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
            .get()
            .await()?.let { docs ->
                for (doc in docs) {
                    list += getQuestFromDocument(doc)
                }
            }
        Log.d("MIKI", "Loaded quests: \n${list.map { " -> ${it.title}\n" }}")
    } catch (ex: Exception) {
        Log.e("MIKI", "An error occurred while fetching users quests.\n\n ${ex.message}")
    }
    return list
}

suspend fun getQuestsInRadius(center: LatLng, radiusInKm: Double): MutableList<Quest> {

    val lat = center.latitude
    val lng = center.longitude


    val deltaLat = Math.toDegrees(radiusInKm / earthRadius)
    val deltaLng = Math.toDegrees(radiusInKm / (earthRadius * cos(Math.toRadians(lat))))

    val latMin = lat - deltaLat
    val latMax = lat + deltaLat
    val lngMin = lng - deltaLng
    val lngMax = lng + deltaLng

    val nearbyQuests = mutableListOf<Quest>()

    try {
        val snapshot = Firebase.firestore.collectionGroup(quests)
            .get()
            .await()

        for (doc in snapshot.documents) {
            val quest = getQuestFromDocument(doc)

            if (quest.lat in latMin..latMax && quest.lng in lngMin..lngMax) {
                if (getDistanceFromLatLng(lat, lng, quest.lat, quest.lng) <= radiusInKm) {

                    Log.d("MIKI", "Found [ ${quest.title} ] in a circle nearby!")
                    nearbyQuests.add(quest)
                }
            }
        }

    } catch (e: Exception) {
        Log.e("MIKI", "Error fetching quests:\n\n ${e.message}")
    }

    return nearbyQuests
}

private fun getQuestFromDocument(doc: DocumentSnapshot): Quest {

    return Quest(
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
}