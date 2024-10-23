package com.example.photoquest.ui.screens.logIn

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.services.AuthenticationServices
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LogInScreenViewModel private constructor():ViewModel(){

    companion object{

        private var INSTANCE:LogInScreenViewModel? = null

        fun getInstance(): LogInScreenViewModel{

            return INSTANCE ?: synchronized(this) {

                       INSTANCE = LogInScreenViewModel()
                        INSTANCE!!
                    }

        }

        fun clearData(){
            INSTANCE = null
        }
    }

    var email = mutableStateOf("")
        private set

    var password = mutableStateOf("")
        private set

    var showPassword = mutableStateOf(false)
        private set

    var validationDone = mutableStateOf(false)
        private set

    var userLoggedIn = mutableStateOf(false)
        private set

    var validationStarted = mutableStateOf(false)
        private set


    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onShowPasswordClick(){
        showPassword.value = !showPassword.value
    }

    suspend fun validateUser() {
        coroutineScope{

            validationStarted.value = true

            userLoggedIn.value = async (Dispatchers.Default){
                Firebase.auth.currentUser != null
                //true
            }.await()

            validationDone.value = true
            validationStarted.value = false

        }
    }

    fun goToSignUpScreen(navController: NavController){
        if(!navController.popBackStack(Screens.SignUp.name, false))
            navController.navigate(Screens.SignUp.name)
    }

    fun onLogInClick(context: Context, navController: NavController) {

        runBlocking {

            launch{
                AuthenticationServices.getInstance().logIn(email = email.value,
                    password = password.value
                )
            }


        }
    }

}