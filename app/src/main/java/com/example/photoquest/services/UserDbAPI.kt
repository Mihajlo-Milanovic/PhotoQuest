package com.example.photoquest.services

import android.util.Log
import com.example.photoquest.models.data.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

class UserDbAPI {

    private val users = "users"

    suspend fun createNewUser(id: String, user: User) {

        try {
            Firebase.firestore.collection(users).document(id).set(user).await()
        } catch (ex: Exception) {
            Log.e("MIKI", ex.message ?: "Error occurred while writing the user in the DB.")
        }
    }

    suspend fun getUserWithUid(uid: String): User {

//        val user: User? = Firebase.firestore.collection(collection).document(uid).get().await()?.toObject<User>()
        return Firebase.firestore.collection(users).document(uid).get().await()?.toObject<User>()!!
    }

}