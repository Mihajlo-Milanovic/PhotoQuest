package com.example.photoquest.ui.screens.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.models.data.Quest
import com.example.photoquest.models.data.User
import com.example.photoquest.services.QuestDbAPI
import com.example.photoquest.services.UserDbAPI
import com.example.photoquest.services.currentUserUid
import com.example.photoquest.services.signUserOut
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {

        private var INSTANCE: ProfileScreenViewModel? = null

        fun getInstance(): ProfileScreenViewModel {

            return INSTANCE ?: synchronized(this) {

                INSTANCE = ProfileScreenViewModel()
                INSTANCE!!
            }
        }

        private fun clearData() {
            INSTANCE = INSTANCE?.let { ProfileScreenViewModel() }
        }
    }

    override var navController: NavController? = null

    val displayedUser = mutableStateOf(User(pictureURL = "?"))
    var usersQuests = emptyList<Quest>()
        private set

    val showOptions = mutableStateOf(false)
    val userLoaded = mutableStateOf(false)
    val usersQuestsLoaded = mutableStateOf(true)


    suspend fun getUsersInfo() = coroutineScope {

        launch(Dispatchers.Default) {
            displayedUser.value = currentUserUid()?.let { UserDbAPI().getUserWithUid(it) }!!
            userLoaded.value = true
        }
    }

    suspend fun getUsersQuests() = coroutineScope {

        launch(Dispatchers.Default) {
            usersQuests = currentUserUid()?.let { QuestDbAPI().getUsersQuests(it) }!!
            usersQuestsLoaded.value = true
        }
    }


    fun onSignOut() {
        runBlocking {

            launch(Dispatchers.Default) {
                signUserOut()
            }

            navController?.popBackStack(Screens.PROFILE.name, true)
            navController?.navigate(Screens.LOG_IN.name)

            clearData()
        }
    }

    fun onMakeNewQuest() {
        navController?.navigate(Screens.MAKE_QUEST.name)
    }

    val isFullScreen = mutableStateOf(false)

    fun zoomProfilePicture() {
        navController?.navigate(Screens.PROFILE_PICTURE.name)
    }

}