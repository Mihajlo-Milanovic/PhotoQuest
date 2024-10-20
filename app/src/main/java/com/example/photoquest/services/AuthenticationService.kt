package com.example.photoquest.services

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class AuthenticationServices(){

    val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    companion object{

        private var INSTANCE: AuthenticationServices? = null

        fun getInstance(): AuthenticationServices {

            return INSTANCE ?: synchronized(this) {

                INSTANCE = AuthenticationServices()
                INSTANCE!!
            }
        }
    }

    fun signUp(email: String, password: String, username: String) {

        //TODO: To check the username or Not ???

        runBlocking {
            Firebase.auth.createUserWithEmailAndPassword(email, password).await()
            logIn(email, password)
        }

        //TODO: Add username to the user
    }

    suspend fun logIn(email: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun logOut() {
        Firebase.auth.signOut()
    }

    suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }

    fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }
}