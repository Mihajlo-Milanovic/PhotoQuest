package com.example.photoquest.ui.screens.auth.logIn

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.Screens
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LogInScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {

        private var INSTANCE: LogInScreenViewModel? = null

        fun getInstance(): LogInScreenViewModel {

            return INSTANCE ?: synchronized(this) {

                INSTANCE = LogInScreenViewModel()
                INSTANCE!!
            }

        }

        fun clearData() {
            INSTANCE = null
        }
    }

    override var navController by mutableStateOf<NavController?>(null)

    val email = mutableStateOf("")
    val password = mutableStateOf("")

    val validationDone = mutableStateOf(false)

    val passwordTransformation: MutableState<VisualTransformation> =
        mutableStateOf(PasswordVisualTransformation())
    val passwordIcon = mutableIntStateOf(R.drawable.baseline_open_eye)

    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onShowPasswordClick() {

        passwordTransformation.value = when (passwordTransformation.value) {
            VisualTransformation.None -> {
                passwordIcon.intValue = R.drawable.baseline_open_eye
                PasswordVisualTransformation()
            }

            else -> {
                passwordIcon.intValue = R.drawable.red_eye
                VisualTransformation.None
            }
        }
    }

    suspend fun validateUser() = coroutineScope {

        if (Firebase.auth.currentUser != null) {
            withContext(Dispatchers.Main) {
                navController!!.navigate(Screens.PROFILE.name) {
                    popUpTo(Screens.PROFILE.name) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }
        } else validationDone.value = true
    }

    suspend fun onLogInClick(context: Context) {

        validationDone.value = false

        try {
            Firebase.auth.signInWithEmailAndPassword(email.value, password.value).await()
        } catch (ex: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    context,
                    ex.message ?: "Hmm...Something suspicious happened!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        validateUser()
    }


    fun goToSignUpScreen() {
        navController!!.navigate(Screens.SIGN_UP.name) {
            popUpTo(Screens.SIGN_UP.name) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
}