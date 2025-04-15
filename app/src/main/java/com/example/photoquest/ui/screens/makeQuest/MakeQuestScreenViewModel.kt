package com.example.photoquest.ui.screens.makeQuest

import android.content.Context
import android.location.Location
import android.net.Uri
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.photoquest.Screens
import com.example.photoquest.models.data.Quest
import com.example.photoquest.services.createNewQuest
import com.example.photoquest.services.currentUserUid
import com.example.photoquest.ui.pictureFullSize.PictureFullSizeViewModel
import com.example.photoquest.ui.screens.auxiliary.NavExtender
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext

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

    var title by mutableStateOf("")
    var imageUri by mutableStateOf<Uri?>(Uri.EMPTY)
    var description by mutableStateOf("")
    var timestamp by mutableStateOf<Timestamp?>(null)
    var location = mutableStateOf<Location?>(null)

    var pictureTaken by mutableStateOf(false)
    var pictureReadyForDisplay by mutableStateOf(false)

    var showUploadScreen by mutableStateOf(false)
    var uploadState by mutableDoubleStateOf(0.0)
    val coroutineScope = CoroutineScope(EmptyCoroutineContext)

    suspend fun makeQuest(
        context: Context
    ) {
        try {

            Log.d("MIKI", "Is location NULL???")
            if (location.value == null)
                Log.d("MIKI", "Yes, it is...")
            else
                Log.d("MIKI", "No, it is not...")

            createNewQuest(
                Quest(
                    publisherId = currentUserUid()!!,
                    title = title,
                    pictureUri = imageUri,
                    pictureDownloadURL = Uri.EMPTY,
                    description = description,
                    lat = location.value!!.latitude,
                    lng = location.value!!.longitude,
                    timestamp = Timestamp.now()
                )
            )

            reset()
        } catch (ex: Exception) {
            showUploadScreen = false
            Looper.prepare()
            Toast.makeText(
                context,
                "Problem encountered while reading location!",
                Toast.LENGTH_SHORT
            ).show()
            Log.e("MIKI", "Problem encountered while reading location.\n\n ${ex.message}")
        }
    }

    fun zoomQuestPicture() {
        PictureFullSizeViewModel.getInstance().let {
            it.imageUri = imageUri
            it.contentDescription = "Quest picture"
        }

        navController.value?.navigate(Screens.PICTURE_FULL_SIZE.name)
    }

    fun goToNoLocationSplashScreen() {
        navController.value!!.navigate(Screens.NO_LOCATION_SPLASH.name)
    }

    fun reset() {
        title = ""
        imageUri = Uri.EMPTY
        description = ""
        timestamp = null
        location.value = null

        pictureTaken = false
        pictureReadyForDisplay = false

        showUploadScreen = false
        uploadState = 0.0
    }
}