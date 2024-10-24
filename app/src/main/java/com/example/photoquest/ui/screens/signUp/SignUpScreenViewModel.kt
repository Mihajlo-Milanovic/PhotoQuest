package com.example.photoquest.ui.screens.signUp

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.Screens
import com.example.photoquest.services.MakeShortToast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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

    var passwordTransformation: MutableState<VisualTransformation> = mutableStateOf(PasswordVisualTransformation())
        private set

    var passwordIcon = mutableIntStateOf(R.drawable.black_eye)
        private set

    var signUpInProgress = mutableStateOf(false)
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

        passwordTransformation.value = when(passwordTransformation.value){
            VisualTransformation.None -> {
                passwordIcon.intValue = R.drawable.black_eye
                PasswordVisualTransformation()
            }
            else -> {
                passwordIcon.intValue = R.drawable.red_warning
                VisualTransformation.None
            }
        }
    }

    suspend fun onSignInClick(context: Context, navController: NavController) = coroutineScope{

        if(validatePassword(context = context)){

            try {
                Firebase.auth.createUserWithEmailAndPassword(email.value, password.value).await()
                Firebase.auth.signInWithEmailAndPassword(email.value, password.value).await()
            }
            catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    MakeShortToast(
                        context = context,
                        message = ex.message ?: "Hmm...Something suspicious happened!"
                    )
                }
            }

            if ( Firebase.auth.currentUser != null) {
                withContext(Dispatchers.Main){
                    if (!navController.popBackStack(Screens.LogIn.name, true))
                        navController.navigate(Screens.Profile.name)
                }
            }

        }

        signUpInProgress.value = false
    }

    private suspend fun validatePassword(context : Context): Boolean  =  coroutineScope {

        if(password.value.length < 8) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.passwordTooShort, Toast.LENGTH_SHORT).show()
            }
            false
        }
        else if (!password.value.contentEquals(repPassword.value)){
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.passwordsDoNotMatch, Toast.LENGTH_SHORT).show()
            }
            false
        }
        else
            true

    }


}