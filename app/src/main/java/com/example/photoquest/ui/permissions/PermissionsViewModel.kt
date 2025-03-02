package com.example.photoquest.ui.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionsViewModel private constructor() {

    companion object {

        private var INSTANCE: PermissionsViewModel? = null

        fun getInstance(): PermissionsViewModel {

            return INSTANCE ?: synchronized(this) {
                INSTANCE = PermissionsViewModel()
                INSTANCE!!
            }
        }

        private fun clearData() {
            synchronized(this) {
                INSTANCE = null
            }
        }
    }

    fun isPermissionGranted(permission: String, context: Context): Boolean {
        return ContextCompat
            .checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun arePermissionsGranted(array: Array<String>, context: Context): Boolean {
        val arr = array.map {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

        return !arr.contains(false)
    }
}