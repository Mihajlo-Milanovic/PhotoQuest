package com.example.photoquest.models.data

import androidx.annotation.DrawableRes
import com.example.photoquest.Screens

data class NavButton(
    val name: Screens,
    @DrawableRes val icon: Int
) {
    override fun equals(other: Any?): Boolean {

        return if (other is NavButton)
            this.name == other.name && this.icon == other.icon
        else
            false
    }
}
