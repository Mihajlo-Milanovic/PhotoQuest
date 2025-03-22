package com.example.photoquest.ui.screens.makeQuest

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.res.Configuration
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.photoquest.R
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.permissions.PermitLocationTrackingDialog
import com.example.photoquest.ui.theme.PhotoQuestTheme
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MakeQuestScreen(
    navController: NavController
) {
    val vm = MakeQuestScreenViewModel.getInstance()
    if (vm.navController.value == null)
        vm.setNavCtrl(navController)

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

//        PermitImageAndCameraAccessDialog(
//            modifier = Modifier.padding(padding),
//            requestReason = R.string.toMakeQuestCameraAndStorageReason
//        )

        DrawMakeQuestScreen(
            modifier = Modifier.padding(padding),
            //navController = navController,
            vm = vm
        )
    }
}

@Composable
fun DrawMakeQuestScreen(
    vm: MakeQuestScreenViewModel,
    modifier: Modifier,
    //navController: NavController
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier,
            value = vm.title,
            onValueChange = {
                vm.title = it
            },
            label = { Text(stringResource(R.string.title)) },
            singleLine = true,
        )

        if (vm.title.trimEnd().isNotEmpty()) {

            Spacer(modifier = Modifier.height(4.dp))

            vm.imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "Quest Picture",
                    modifier = Modifier.size(300.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            TakePictureAndSaveInGalleryButton(
                vm = vm,
                modifier = Modifier,
            )

            Spacer(modifier = Modifier.height(4.dp))

            OutlinedTextField(
                modifier = Modifier,
                value = vm.description,
                onValueChange = {
                    vm.description = it
                },
                label = { Text(stringResource(R.string.description)) },
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(4.dp))

            if (vm.pictureTaken) {

                Button(
                    onClick = {
                        coroutineScope.launch(Dispatchers.Default) { vm.makeQuest() }
                    }
                ) {
                    Text(stringResource(R.string.submit))
                }
            }
        }
    }
}

@Composable
fun TakePictureAndSaveInGalleryButton(
    vm: MakeQuestScreenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    vm.imageUri = createImageUri(
        context = context,
        imageName = vm.title,
    )

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                vm.timestamp = Timestamp.now()
                vm.pictureTaken = true
                Toast.makeText(context, "Picture saved to gallery!", Toast.LENGTH_SHORT).show()
            }
        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            vm.imageUri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    Button(
        modifier = modifier,
        onClick = {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }) {
        Text(if (vm.pictureTaken) stringResource(R.string.tryAgain) else stringResource(R.string.takePicture))
    }
}


fun createImageUri(context: Context, imageName: String): Uri? {
    val appName = context.applicationInfo.loadLabel(context.packageManager).toString()
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "${imageName}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$appName")
    }
    return context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )
}

//@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MakeQuestScreenPreview() {
    PhotoQuestTheme {
        MakeQuestScreen(navController = NavController(context = LocalContext.current))
    }
}