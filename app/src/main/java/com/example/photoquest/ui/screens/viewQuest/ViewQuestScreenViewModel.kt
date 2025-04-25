package com.example.photoquest.ui.screens.viewQuest

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.models.data.Quest
import com.example.photoquest.ui.pictureFullSize.PictureFullSizeViewModel
import com.example.photoquest.ui.screens.auxiliary.NavExtender

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

    var quest by mutableStateOf(Quest())
    var showFullDescription by mutableStateOf(false)

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