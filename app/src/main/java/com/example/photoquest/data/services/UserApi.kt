package com.example.photoquest.data.services

import android.util.Log
import com.example.photoquest.data.model.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

interface UserApi {
    suspend fun getUserWithUid(uid: String): User?
    suspend fun createNewUser(user: User): Boolean
}

class UserDbApi : UserApi {

    override suspend fun getUserWithUid(uid: String): User? {

        try {
            return Firebase.firestore.collection("users").document(uid).get().await()
                ?.toObject<User>()!!
        } catch (ex: Exception) {
            Log.e("MIKI", "Error occurred wile fetching the user data.\n\n ${ex.message}")
            return null
        }
    }

    override suspend fun createNewUser(user: User): Boolean {

        try {
            Firebase.firestore.collection("users").document(user.id).set(user).await()
            return true
        } catch (ex: Exception) {
            Log.e("MIKI", ex.message ?: "Error occurred while writing the user to the DB.")
            return false
        }
    }
}