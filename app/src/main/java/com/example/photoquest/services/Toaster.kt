package com.example.photoquest.services

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

object Toaster {

    private var appContext: Context? = null

    fun setAppContext(context: Context) {
        appContext = context
    }

    @Composable
    fun MakeShortToast(stringResourceId: Int) {
        Toast.makeText(
            LocalContext.current,
            stringResource(id = stringResourceId),
            Toast.LENGTH_SHORT
        )
            .show()
    }

    @Composable
    fun MakeLongToast(stringResourceId: Int) {
        Toast.makeText(
            LocalContext.current,
            stringResource(id = stringResourceId),
            Toast.LENGTH_LONG
        )
            .show()
    }

    fun makeShortToast(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
    }

    fun makeLongToast(message: String) {
        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
    }
}