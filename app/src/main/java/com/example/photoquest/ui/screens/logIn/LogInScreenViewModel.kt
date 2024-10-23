package com.example.photoquest.ui.screens.logIn

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await


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

    var validationDone = mutableStateOf(false)
        private set

    var passwordTransformation: MutableState<VisualTransformation> = mutableStateOf(
        PasswordVisualTransformation()
    )
        private set

    var passwordIcon = mutableStateOf(R.drawable.black_eye)
        private set



    fun onEmailChange(newEmail: String) {
        email.value = newEmail
    }

    fun onPasswordChange(newPassword: String) {
        password.value = newPassword
    }

    fun onShowPasswordClick(){

        passwordTransformation.value = when(passwordTransformation.value){
            VisualTransformation.None -> {
                passwordIcon.value = R.drawable.black_eye
                PasswordVisualTransformation()
            }
            else -> {
                passwordIcon.value = R.drawable.red_warning
                VisualTransformation.None
            }
        }
    }

    suspend fun validateUser(navController: NavController) = coroutineScope{

            if ( async(Dispatchers.Default) {
                    Firebase.auth.currentUser != null
//                    true
                }.await()
            ) {

                Log.d("MIKI", "signInWithEmail:success")

                if (!navController.popBackStack(Screens.Profile.name, false))
                    navController.navigate(Screens.Profile.name)

                Log.d("MIKI", "tako sam i mislio")
            }

            validationDone.value = true
    }

    suspend fun onLogInClick(context: Context, navController: NavController) {

        validationDone.value = false

        try{
            Firebase.auth.signInWithEmailAndPassword(email.value, password.value).await()
        }catch (ex:Exception){
            MakeShortToast(context = context, message = ex.localizedMessage ?: "Hmm...Something suspicious happened!")
        }

        validateUser(navController = navController)
    }


    fun goToSignUpScreen(navController: NavController){
        if(!navController.popBackStack(Screens.SignUp.name, false))
            navController.navigate(Screens.SignUp.name)
    }
}