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
    }

    private val permissions: MutableMap<String, Boolean> = mutableMapOf()

    fun updatePermission(permission: String, granted: Boolean) {

        permissions[permission] = granted
    }

    fun isPermissionGranted(permission: String, context: Context): Boolean {

        if (!permissions.containsKey(permission)) {
            permissions[permission] = checkPermission(context = context, permission = permission)
        }
        return permissions[permission]!!
    }

    private fun checkPermission(context: Context, permission: String): Boolean {
        return ContextCompat
            .checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }
}