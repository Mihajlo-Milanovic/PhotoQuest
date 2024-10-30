package com.example.photoquest.services

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await


suspend fun signUserUp(email: String, password: String) {
    Firebase.auth.createUserWithEmailAndPassword(email, password).await()
}

suspend fun signUserIn(email: String, password: String){
    Firebase.auth.signInWithEmailAndPassword(email, password).await()
}

suspend fun signUserOut(){
    Firebase.auth.signOut()
}

suspend fun currentUserUid(): String? {
    return Firebase.auth.currentUser?.uid
}

suspend fun userSignedIn(): Boolean{
    return Firebase.auth.currentUser != null
}