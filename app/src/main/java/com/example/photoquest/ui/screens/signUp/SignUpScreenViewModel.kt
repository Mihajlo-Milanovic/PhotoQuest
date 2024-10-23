package com.example.photoquest.ui.screens.signUp

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
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

//    var showPassword = mutableStateOf(false)
//        private set

    var passwordTransformation: MutableState<VisualTransformation> = mutableStateOf(PasswordVisualTransformation())
        private set

    var passwordIcon = mutableStateOf(R.drawable.black_eye)
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
                passwordIcon.value = R.drawable.black_eye
                PasswordVisualTransformation()
            }
            else -> {
                passwordIcon.value = R.drawable.red_warning
                VisualTransformation.None
            }
        }
    }

    fun onSignInClick(context: Context, navController: NavController) {

        if(validatePassword(context = context)){

        }
        TODO("Not yet implemented")
    }

    private fun validatePassword(context : Context): Boolean {

        if(password.value.length < 8) {
            Toast.makeText(context, R.string.passwordTooShort, Toast.LENGTH_SHORT).show()
            return false
        }

        if (!password.value.contentEquals(repPassword.value)){
            Toast.makeText(context, R.string.passwordsDoNotMatch, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }


}