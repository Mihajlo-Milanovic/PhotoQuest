package com.example.photoquest.ui.screens.signUp

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.photoquest.R

class SignUpScreenViewModel private constructor(): ViewModel() {

    companion object{

        private var INSTANCE: SignUpScreenViewModel? = null

        fun getInstance(): SignUpScreenViewModel {

            return INSTANCE ?: synchronized(this) {

                INSTANCE = SignUpScreenViewModel()
                INSTANCE!!
            }

        }

        fun clearData(){
            INSTANCE = null
        }
    }


    var username = mutableStateOf("")
        private set

    var email = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    var repPassword = mutableStateOf("")
        private set

    var showPassword = mutableStateOf(false)
        private set



    fun onUsernameChange(newUsername: String) {
        username.value = newUsername
    }

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onRepPasswordChange(newRepPassword: String) {
        repPassword.value = newRepPassword
    }

    fun onShowPasswordClick(){
        showPassword.value = !showPassword.value
    }

    fun onSignInClick(context: Context) {

        validatePassword(context = context)
        TODO("Not yet implemented")
    }

    private fun validatePassword(context : Context) {
        //TODO("Not yet implemented")
        if(password.value.length < 8) {
            Toast.makeText(context, R.string.passwordTooShort, Toast.LENGTH_SHORT).show()
            return
        }

        if (!password.value.contentEquals(repPassword.value)){
            Toast.makeText(context, R.string.passwordsDoNotMatch, Toast.LENGTH_SHORT).show()
            return
        }
        



    }


}