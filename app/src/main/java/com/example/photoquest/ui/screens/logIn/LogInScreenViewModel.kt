package com.example.photoquest.ui.screens.logIn

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel


class LogInScreenViewModel private constructor():ViewModel(){

    var email = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    var showPassword = mutableStateOf(false)
        private set

    companion object{

        private var INSTANCE:LogInScreenViewModel? = null

        fun getInstance(): LogInScreenViewModel{

            return INSTANCE ?: synchronized(this) {

                       INSTANCE = LogInScreenViewModel()
                        INSTANCE!!
                    }

        }
    }

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onShowPasswordClick(){
        showPassword.value = !showPassword.value
    }

    fun onLoginClick(context: Context) {

//        AuthenticationServices.getInstance().logIn(email = email.value,
//                                                    password = password.value
//                                                    )
    }

    fun onGoToSignInScreen(context: Context) {

    }


}