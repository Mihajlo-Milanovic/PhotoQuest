package com.example.photoquest.ui.screens.pictureFullSize

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PictureFullSizeViewModel : ViewModel() {

    companion object {

        private var INSTANCE: PictureFullSizeViewModel? = null

        fun getInstance(): PictureFullSizeViewModel {

            return INSTANCE ?: synchronized(this) {
                INSTANCE = PictureFullSizeViewModel()
                INSTANCE!!
            }
        }
    }

    var imageUri by mutableStateOf<Uri?>(null)
    var contentDescription by mutableStateOf("")

}