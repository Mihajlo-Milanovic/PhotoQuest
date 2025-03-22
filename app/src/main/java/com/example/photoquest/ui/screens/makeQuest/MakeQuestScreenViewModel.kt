package com.example.photoquest.ui.screens.makeQuest

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.models.data.Quest
import com.example.photoquest.services.createNewQuest
import com.example.photoquest.services.currentUserUid
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.google.firebase.Timestamp

class MakeQuestScreenViewModel private constructor() : ViewModel(), NavExtender {

    companion object {

        private var INSTANCE: MakeQuestScreenViewModel? = null

        fun getInstance(): MakeQuestScreenViewModel {

            return INSTANCE ?: synchronized(this) {
                INSTANCE = MakeQuestScreenViewModel()
                INSTANCE!!
            }
        }
    }

    override val navController: MutableState<NavController?> = mutableStateOf(null)
    val maxTitleLength = 22

    var title by mutableStateOf<String>("")
    var imageUri by mutableStateOf<Uri?>(null)
    var description by mutableStateOf<String>("")
    var timestamp by mutableStateOf<Timestamp?>(null)

    var pictureTaken by mutableStateOf(false)

    suspend fun makeQuest() {

        createNewQuest(
            Quest(
                publisherId = currentUserUid()!!,
                title = title,
                description = description,
                pictureURL = imageUri.toString(),
                lat = 0f,
                lng = 0f,
                timestamp = Timestamp.now()
            )
        )
    }

}