package com.example.photoquest.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.models.data.Quest
import com.example.photoquest.models.data.User
import com.example.photoquest.services.currentUserUid
import com.example.photoquest.services.getUserWithUid
import com.example.photoquest.services.getUsersQuests
import com.example.photoquest.services.signUserOut
import com.example.photoquest.ui.pictureFullSize.PictureFullSizeViewModel
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
            synchronized(this) {
                INSTANCE = null
            }
        }
    }

    override var navController by mutableStateOf<NavController?>(null)

    val displayedUser = mutableStateOf(User())
    var usersQuests = mutableListOf<Quest>()
        private set

    val showOptions = mutableStateOf(false)
    private val userLoaded = mutableStateOf(false)
    var usersQuestsLoaded = mutableStateOf(false)

    //TODO: var profilePictureUri = mutableStateOf()


    suspend fun getUsersInfo() = coroutineScope {

        launch(Dispatchers.Default) {
            currentUserUid()?.let {

                val user = getUserWithUid(it)

                if (user != null) {
                    displayedUser.value = user
                    userLoaded.value = true
                } else
                    userLoaded.value = false
            }

        }
    }

    suspend fun getUsersQuests() = coroutineScope {

        launch(Dispatchers.Default) {
            currentUserUid()?.let {
                usersQuests = getUsersQuests(it)
            }

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

    fun zoomProfilePicture() {
//        PictureFullSizeViewModel.getInstance().imageUri.value = TODO finish this
        navController?.navigate(Screens.PICTURE_FULL_SIZE.name)
    }

    fun questImageOnLongClick(quest: Quest) {
        PictureFullSizeViewModel
            .getInstance()
            .let {
                it.imageUri = quest.pictureDownloadURL
                it.contentDescription = quest.description
            }

        navController?.navigate(Screens.PICTURE_FULL_SIZE.name)
    }
}