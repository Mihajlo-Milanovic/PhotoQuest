package com.example.photoquest.ui.screens.makeQuest

import android.Manifest
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.photoquest.R
import com.example.photoquest.data.services.getUserCurrentLocation
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.screens.auxiliary.isLocationEnabled
import com.example.photoquest.ui.theme.PhotoQuestTheme
import com.example.photoquest.ui.utilities.createImageUri
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MakeQuestScreen(
    navController: NavController
) {
    val vm = remember { MakeQuestScreenViewModel.getInstance() }
    if (vm.navController == null)
        vm.setNavCtrl(navController)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { padding ->

        if (vm.showUploadScreen) {

            if (isLocationEnabled(LocalContext.current)) {

                UploadScreen(
                    modifier = Modifier.padding(padding),
                    vm = vm
                )
            } else {
                vm.goToNoLocationSplashScreen()
            }

        } else {
            DrawMakeQuestScreen(
                modifier = Modifier.padding(padding),
                vm = vm
            )
        }
    }
}

@Composable
fun DrawMakeQuestScreen(
    vm: MakeQuestScreenViewModel,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 32.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            OutlinedTextField(
                modifier = Modifier,
                value = vm.title,
                onValueChange = {
                    vm.title = it
                },
                label = { Text(stringResource(R.string.title)) },
                singleLine = true,
            )
        }

        if (vm.title.trimEnd().isNotEmpty()) {

            item {
                Spacer(modifier = Modifier.height(4.dp))

                QuestPicture(
                    vm = vm,
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(4.dp))

                TakePictureAndSaveInGalleryButton(
                    vm = vm,
                    modifier = Modifier,
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            item {
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
            }

            item {
                MakeQuestButton(
                    vm = vm,
                    modifier = Modifier
                )
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

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                vm.timestamp = Timestamp.now()
                vm.pictureTaken = true
                Toast.makeText(context, "Picture saved to gallery!", Toast.LENGTH_SHORT)
                    .show()
                Log.d("MIKI", "Picture taken and saved")


                try {
                    vm.imageUri?.let {
                        context.contentResolver.openInputStream(it)?.use {
                            vm.pictureReadyForDisplay = true
                            Log.d("MIKI", "Image ready")
                        }
                    }
                } catch (e: Exception) {
                    vm.pictureReadyForDisplay = false
                    Log.e("MIKI", "Image not ready yet: ${e.message}")
                }
            }


        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            vm.imageUri = createImageUri(
                context = context,
                imageName = vm.title,
            )
            Log.w("MIKI", "New URI -> ${vm.imageUri}")
            vm.imageUri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Camera permission required", Toast.LENGTH_SHORT).show()
        }
    }

    Button(
        modifier = modifier,
        onClick = {
            if (vm.pictureTaken) {
                vm.imageUri = null
                vm.pictureReadyForDisplay = false
                //vm.pictureTaken = false
            }
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }) {
        Text(if (vm.pictureTaken) stringResource(R.string.tryAgain) else stringResource(R.string.takePicture))
    }
}

@Composable
fun QuestPicture(
    vm: MakeQuestScreenViewModel,
    modifier: Modifier = Modifier
) {
    if (vm.pictureReadyForDisplay && vm.imageUri != null) {
        AsyncImage(
            model = vm.imageUri,
            contentDescription = "Quest Picture",
            contentScale = ContentScale.FillWidth,//TODO: Reconsider ContentCale
            modifier = modifier
                .fillMaxWidth()
                .sizeIn(
                    maxHeight = 256.dp,
                    maxWidth = 256.dp
                )
                .clickable {
                    vm.zoomQuestPicture()
                }
        )
    }
}

@Composable
fun UploadScreen(
    vm: MakeQuestScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (vm.location.value == null) {
        Log.d("MIKI", "Reading users location.")
        getUserCurrentLocation(
            context = context,
            fusedLocationClient = fusedLocationClient,
            location = vm.location
        )
    }
    if (vm.location.value != null)
        LaunchedEffect(null) {
            vm.coroutineScope.launch(Dispatchers.Default) {
                Log.d("MIKI", "Making a quest...")
                vm.makeQuest(context = context)
            }
        }

    Column(
        modifier = modifier
            .padding(horizontal = 32.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.makingAQuest),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { vm.uploadState.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            "${(vm.uploadState * 99).toInt()}%",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun MakeQuestButton(
    vm: MakeQuestScreenViewModel,
    modifier: Modifier = Modifier
) {
    if (vm.pictureTaken && vm.imageUri != null) {
        Button(
            onClick = {
                vm.showUploadScreen = true
            },
            modifier = modifier,
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}

//@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MakeQuestScreenPreview() {
    PhotoQuestTheme {
        MakeQuestScreen(navController = NavController(context = LocalContext.current))
    }
}