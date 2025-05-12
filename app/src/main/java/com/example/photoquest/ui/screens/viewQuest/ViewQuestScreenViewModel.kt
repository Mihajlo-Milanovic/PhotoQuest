package com.example.photoquest.ui.screens.viewQuest

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.models.data.Quest
import com.example.photoquest.models.data.User
import com.example.photoquest.services.getUserWithUid
import com.example.photoquest.services.reverseGeocode
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.example.photoquest.ui.screens.map.MapScreenViewModel
import com.example.photoquest.ui.screens.pictureFullSize.PictureFullSizeViewModel
import com.example.photoquest.ui.screens.profile.ProfileScreenViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    var fullAddress by mutableStateOf<String?>(null)
    private var searchedForAddress by mutableStateOf(true)

    var closeUpView by mutableStateOf(false)

    var publisher by mutableStateOf<User?>(null)


    fun searchForAddress(context: Context) {
        reverseGeocode(quest.lat, quest.lng, context = context)?.let { address ->
            fullAddress = address.getAddressLine(0)
        }
    }

    fun onDescriptionClick() {
        showFullDescription = !showFullDescription
    }

    fun questImageOnClick() {
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

    suspend fun getPublishersInfo() {

        publisher = getUserWithUid(quest.publisherId)
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
        reset()
        this.quest = quest

        runBlocking {

            launch(Dispatchers.Default) {
                publisher = getUserWithUid(quest.publisherId)
            }
        }


    }

    private fun reset() {

        showFullDescription = false

        fullAddress = null
        searchedForAddress = true

        closeUpView = false
    }
}