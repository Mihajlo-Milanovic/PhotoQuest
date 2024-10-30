package com.example.photoquest.ui.screens.makeQuest

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.photoquest.ui.permissions.PleaseEnableLocationAndCameraDialog
import com.example.photoquest.ui.permissions.RequestLocationAndCameraPermissions
import com.example.photoquest.ui.theme.PhotoQuestTheme

@Composable
fun MakeQuestScreen(

    navController: NavController
) {
    val vm = MakeQuestScreenViewModel.getInstance()
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        if (!vm.showEnableLocationTrackingDialog.value) {
            PleaseEnableLocationAndCameraDialog()
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            RequestLocationAndCameraPermissions(
                context = context
            )
        }

    }

}

@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MakeQuestScreenPreview() {
    PhotoQuestTheme {
        MakeQuestScreen(navController = NavController(context = LocalContext.current))
    }
}