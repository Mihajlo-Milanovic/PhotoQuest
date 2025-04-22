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
import com.example.photoquest.ui.pictureFullSize.PictureFullSizeViewModel
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.example.photoquest.ui.screens.viewQuest.ViewQuestScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ProfileScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {

        private var INSTANCE: ProfileScreenViewModel? = null

        fun getInstance(): ProfileScreenViewModel {

            return INSTANCE ?: synchronized(this) {

                INSTANCE = ProfileScreenViewModel()
                INSTANCE!!
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

    //TODO: Maybe not needed???
    fun onMakeNewQuest() {
        navController!!.navigate(Screens.MAKE_QUEST.name) {
            popUpTo(Screens.MAKE_QUEST.name) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun zoomProfilePicture() {
//        PictureFullSizeViewModel.getInstance().imageUri.value = TODO finish this
        navController?.navigate(Screens.PICTURE_FULL_SIZE.name)
    }

    fun questImageOnClick(quest: Quest) {
        PictureFullSizeViewModel
            .getInstance()
            .let {
                it.imageUri = quest.pictureDownloadURL
                it.contentDescription = quest.description
            }

        navController!!.navigate(Screens.PICTURE_FULL_SIZE.name) {
            popUpTo(Screens.PICTURE_FULL_SIZE.name) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun questOnClick(quest: Quest) {
        ViewQuestScreenViewModel
            .getInstance()
            .let {
                it.quest = quest
            }

        navController!!.navigate(Screens.VIEW_QUEST.name) {
            popUpTo(Screens.VIEW_QUEST.name) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun goToSettings() {
        navController!!.navigate(Screens.SETTINGS.name) {
            popUpTo(Screens.SETTINGS.name) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }
}