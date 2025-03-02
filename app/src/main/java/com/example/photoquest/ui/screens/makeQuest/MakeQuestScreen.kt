package com.example.photoquest.ui.screens.makeQuest

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.permissions.PermitImageAndCameraAccessDialog
import com.example.photoquest.ui.permissions.PermitLocationTrackingDialog
import com.example.photoquest.ui.theme.PhotoQuestTheme

@Composable
fun MakeQuestScreen(
    navController: NavController
) {
    val vm = MakeQuestScreenViewModel.getInstance()
    if (vm.navController.value == null)
        vm.setNavCtrl(navController)
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { padding ->

        PermitLocationTrackingDialog(
            modifier = Modifier.padding(padding),
            requestReason = R.string.toMakeQuestLocationReason
        )

        PermitImageAndCameraAccessDialog(
            modifier = Modifier.padding(padding),
            requestReason = R.string.toMakeQuestCameraAndStorageReason
        )

        DrawMakeQuestScreen(
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun DrawMakeQuestScreen(
    modifier: Modifier
) {
    Text("Make quest screen", modifier = Modifier)
}

//@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MakeQuestScreenPreview() {
    PhotoQuestTheme {
        MakeQuestScreen(navController = NavController(context = LocalContext.current))
    }
}