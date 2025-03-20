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
import com.example.photoquest.models.data.User
import com.example.photoquest.services.Toaster.makeShortToast
import com.example.photoquest.services.UserDbAPI
import com.example.photoquest.services.currentUserUid
import com.example.photoquest.services.signUserIn
import com.example.photoquest.services.signUserUp
import com.example.photoquest.services.isUserSignedIn
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class SignUpScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {

        private var INSTANCE: SignUpScreenViewModel? = null

        fun getInstance(): SignUpScreenViewModel {

            return INSTANCE ?: synchronized(this) {

                INSTANCE = SignUpScreenViewModel()
                INSTANCE!!
            }

        }

        fun clearData() {
            INSTANCE = null
        }
    }

    override val navController: MutableState<NavController?> = mutableStateOf(null)

    val username = mutableStateOf("")
    val firstName = mutableStateOf("")
    val lastName = mutableStateOf("")

    val email = mutableStateOf("")

    val password = mutableStateOf("")
    val repPassword = mutableStateOf("")


    val passwordTransformation: MutableState<VisualTransformation> =
        mutableStateOf(PasswordVisualTransformation())
    val passwordIcon = mutableIntStateOf(R.drawable.black_eye)

    val signUpInProgress = mutableStateOf(false)

    fun onUsernameChange(newUsername: String) {
        username.value = newUsername
    }

    fun onFirstNameChange(newFirstName: String) {
        firstName.value = newFirstName
    }

    fun onLastNameChange(newLastName: String) {
        lastName.value = newLastName
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

    fun onShowPasswordClick() {

        passwordTransformation.value = when (passwordTransformation.value) {
            VisualTransformation.None -> {
                passwordIcon.intValue = R.drawable.black_eye
                PasswordVisualTransformation()
            }

            else -> {
                passwordIcon.intValue = R.drawable.red_eye
                VisualTransformation.None
            }
        }
    }

    suspend fun onSignInClick(context: Context) = coroutineScope {

        if (validatePassword(context = context)) {

            try {
                signUserUp(email = email.value, password = password.value)
                val udb = UserDbAPI()

                currentUserUid()?.let {
                    udb.createNewUser(
                        id = it,
                        user = User(
                            username = username.value,
                            lastName = lastName.value,
                            firstName = firstName.value,
                            pictureURL = ""
                        )
                    )
                }

                signUserIn(email = email.value, password = password.value)
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    makeShortToast(
                        message = ex.message ?: "Hmm...Something suspicious happened!"
                    )
                }
            }

            if (isUserSignedIn()) {
                withContext(Dispatchers.Main) {
                    navController.value?.popBackStack(Screens.LOG_IN.name, true)
                    navController.value?.navigate(Screens.PROFILE.name)
                }
            }

        }

        signUpInProgress.value = false
    }

    private suspend fun validatePassword(context: Context): Boolean = coroutineScope {

        if (password.value.length < 8) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.passwordTooShort, Toast.LENGTH_SHORT).show()
            }
            false
        } else if (!password.value.contentEquals(repPassword.value)) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, R.string.passwordsDoNotMatch, Toast.LENGTH_SHORT).show()
            }
            false
        } else
            true

    }


}