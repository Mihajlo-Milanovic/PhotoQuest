package com.example.photoquest.data.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await


suspend fun signUserUp(email: String, password: String, context: Context) {
    Firebase.auth.createUserWithEmailAndPassword(email, password).addOnFailureListener { fail ->

        //TODO: test this!

        fail.message?.let { Log.d("MIKI", it) }
        fail.cause?.localizedMessage?.let { Log.d("MIKI", it) }
        fail.localizedMessage?.let { Log.d("MIKI", it) }
        Toast.makeText(context, "Something went wrong...", Toast.LENGTH_SHORT).show()
    }.await()
}

suspend fun signUserIn(email: String, password: String, context: Context) {
    Firebase.auth.signInWithEmailAndPassword(email, password).addOnFailureListener { fail ->
        fail.message?.let { Log.d("MIKI", it) }
        fail.cause?.localizedMessage?.let { Log.d("MIKI", it) }
        fail.localizedMessage?.let { Log.d("MIKI", it) }
        Toast.makeText(context, "Wrong email or password!", Toast.LENGTH_SHORT).show()
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