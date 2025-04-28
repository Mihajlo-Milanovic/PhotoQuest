package com.example.photoquest.ui.screens.viewQuest

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.models.data.Quest
import com.example.photoquest.services.reverseGeocode
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.example.photoquest.ui.screens.map.MapScreenViewModel
import com.example.photoquest.ui.screens.pictureFullSize.PictureFullSizeViewModel

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
    var showFullDescription by mutableStateOf(false)

    var fullAddress by mutableStateOf("")
    private var searchedForAddress by mutableStateOf(true)

    var closeUpView by mutableStateOf(false)


    fun searchForAddress(context: Context) {
        if (!searchedForAddress) {
            reverseGeocode(quest.lat, quest.lng, context = context)?.getAddressLine(0)?.let {
                fullAddress = it
            }
            searchedForAddress = true
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


}