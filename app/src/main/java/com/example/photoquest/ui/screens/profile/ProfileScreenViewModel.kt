package com.example.photoquest.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.data.model.Quest
import com.example.photoquest.data.model.User
import com.example.photoquest.data.services.getUserWithUid
import com.example.photoquest.data.services.getUsersQuests
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

    var userUID by mutableStateOf<String?>(null)

    var displayedUser by mutableStateOf(User())
    var usersQuests = mutableStateListOf<Quest>()

    private var userLoaded by mutableStateOf(false)
    var usersQuestsLoaded by mutableStateOf(false)

    var showDeleteDialog by mutableStateOf(false)
    var questForDeletion: Quest? = null

    //TODO: var profilePictureUri = mutableStateOf()

    var showProfilePicture by mutableStateOf(false)

    suspend fun getUsersInfo() = coroutineScope {

        launch(Dispatchers.Default) {

            userUID?.let {
                val user = getUserWithUid(it)

                if (user != null) {
                    displayedUser = user
                    userLoaded = true
                } else
                    userLoaded = false
            }
        }
    }

    suspend fun getUsersQuests() = coroutineScope {

        launch(Dispatchers.Default) {
            userUID?.let {
                usersQuests = getUsersQuests(it).toMutableStateList()
            }

            usersQuestsLoaded = true
        }
    }

    fun questOnClick(quest: Quest) {

        ViewQuestScreenViewModel.getInstance().setDisplayedQuest(quest)

        navController!!.navigate(Screens.VIEW_QUEST.name) {
            popUpTo(Screens.VIEW_QUEST.name) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun deleteQuest() {

        usersQuests.remove(questForDeletion)

        //TODO: Add deletion from database
    }

    fun updateProfilePicture() {

    }

    fun reset() {
        displayedUser = User()
        usersQuests = mutableStateListOf<Quest>()

        userLoaded = false
        usersQuestsLoaded = false

        showDeleteDialog = false
        questForDeletion = null
    }
}