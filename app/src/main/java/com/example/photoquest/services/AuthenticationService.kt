package com.example.photoquest.services

import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

class AuthenticationServices private constructor(){

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

        fun clearData(){
            AuthenticationServices.INSTANCE = null
        }
    }

    fun signUp(email: String, password: String, username: String, navController: NavController) {

        //TODO: To check the username or Not ???

        runBlocking {
            launch {

                Firebase.auth.createUserWithEmailAndPassword(email, password).await()
                //logIn(email = email, password = password, navController = navController)
            }
        }

        //TODO: Add username to the user
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