package com.example.photoquest.services

import android.util.Log
import com.example.photoquest.services.Toaster.makeShortToast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await


suspend fun signUserUp(email: String, password: String) {
    Firebase.auth.createUserWithEmailAndPassword(email, password).addOnFailureListener() { fail ->

        //TODO: test this!

        fail.message?.let { Log.d("MIKI", it) }
        fail.cause?.localizedMessage?.let { Log.d("MIKI", it) }
        fail.localizedMessage?.let { Log.d("MIKI", it) }
    }.await()
}

suspend fun signUserIn(email: String, password: String) {
    Firebase.auth.signInWithEmailAndPassword(email, password).addOnFailureListener() { fail ->
        fail.message?.let { Log.d("MIKI", it) }
        fail.cause?.localizedMessage?.let { Log.d("MIKI", it) }
        fail.localizedMessage?.let { Log.d("MIKI", it) }
        makeShortToast(message = "Wrong email or password!")
    }.await()
}

fun signUserOut() {
    Firebase.auth.signOut()
}

fun currentUserUid(): String? {
    return Firebase.auth.currentUser?.uid
}

fun isUserSignedIn(): Boolean {
    return Firebase.auth.currentUser != null
}