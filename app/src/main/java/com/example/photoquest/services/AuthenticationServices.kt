package com.example.photoquest.services

interface AuthenticationServices {

    fun logIn(email:String, password:String)
    fun signUp(email: String, password: String)

    //TODO: Pogledati implementaciju...
}