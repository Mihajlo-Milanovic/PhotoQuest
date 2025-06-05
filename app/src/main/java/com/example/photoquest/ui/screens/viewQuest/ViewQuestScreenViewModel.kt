package com.example.photoquest.ui.screens.viewQuest

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.data.model.Quest
import com.example.photoquest.data.model.UserView
import com.example.photoquest.data.services.reverseGeocode
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.example.photoquest.ui.screens.map.MapScreenViewModel
import com.example.photoquest.ui.screens.profile.ProfileScreenViewModel

class ViewQuestScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {
        private var INSTANCE: ViewQuestScreenViewModel? = null

        fun getInstance(): ViewQuestScreenViewModel {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = ViewQuestScreenViewModel()
                INSTANCE!!
            }
        }
    }

    override var navController by mutableStateOf<NavController?>(null)

    val mapScreenViewModel = MapScreenViewModel.getInstance()

    var quest by mutableStateOf(Quest())
        private set
    var showFullDescription by mutableStateOf(false)
    var zoomPicture by mutableStateOf(false)

    var fullAddress by mutableStateOf<String?>(null)
    private var searchedForAddress by mutableStateOf(true)

    var closeUpView by mutableStateOf(false)

    var publisher by mutableStateOf<UserView?>(null)


    fun searchForAddress(context: Context) {
        reverseGeocode(quest.lat, quest.lng, context = context)?.let { address ->
            fullAddress = address.getAddressLine(0)
        }
    }

    fun onDescriptionClick() {
        showFullDescription = !showFullDescription
    }

    suspend fun getPublishersInfo() {

        //TODO:
//        publisher = getUserWithUid(quest.publisherId)
    }

    fun viewPublishersProfile() {

        ProfileScreenViewModel.getInstance().userUID = quest.publisherId

        navController!!.navigate(Screens.PROFILE.name) {
            popUpTo(Screens.PROFILE.name) {
                inclusive = false
            }
            launchSingleTop = true
        }
    }

    fun setDisplayedQuest(quest: Quest) {
        //TODO:
//        reset()
//        this.quest = quest
//
//        runBlocking {
//
//            launch(Dispatchers.Default) {
//                publisher = getUserWithUid(quest.publisherId)
//            }
//        }
    }

    private fun reset() {

        showFullDescription = false

        fullAddress = null
        searchedForAddress = true

        closeUpView = false
    }
}