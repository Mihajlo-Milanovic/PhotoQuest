package com.example.photoquest.ui.permissions

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.photoquest.R

@Composable
fun PermitLocationTrackingDialog(
    modifier: Modifier,
    requestReason: Int,
) {
    val context = LocalContext.current

    var dialogNeeded by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )
    }

    if (dialogNeeded) {

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) {}

        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true,
            ),
            onDismissRequest = {},
        ) {

            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .border(
                        width = 5.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = AbsoluteRoundedCornerShape(31.dp)
                    )
                    .clip(AbsoluteRoundedCornerShape(32.dp))
            ) {

                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(requestReason),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        permissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        )

                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                            dialogNeeded = false
                    }) {
                        Text(text = stringResource(R.string.iUnderstand))
                    }
                }
            }
        }
    }
}

@Composable
fun PermitCameraUsageDialog(
    modifier: Modifier,
    requestReason: Int,
) {

    val context = LocalContext.current

    var dialogNeeded by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        )
    }

    if (dialogNeeded) {

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) {}

        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true,
            ),
            onDismissRequest = {},
        ) {

            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .border(
                        width = 5.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = AbsoluteRoundedCornerShape(31.dp)
                    )
                    .clip(AbsoluteRoundedCornerShape(32.dp))
            ) {

                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(requestReason),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        permissionLauncher.launch(Manifest.permission.CAMERA)


                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                            dialogNeeded = false
                    }) {
                        Text(text = stringResource(R.string.iUnderstand))
                    }
                }
            }
        }
    }
}


@Composable
fun PermitImageAccessDialog(
    modifier: Modifier,
    requestReason: Int,
) {

    val context = LocalContext.current

    val permissionArray = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED)
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(READ_MEDIA_IMAGES)
    } else {
        arrayOf(READ_EXTERNAL_STORAGE)
    }

    var dialogNeeded by remember {
        mutableStateOf(
            permissionArray.map {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }.reduce { acc: Boolean, it ->
                acc && it
            }
        )
    }

    if (dialogNeeded) {

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) {}

        Dialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = true,
            ),
            onDismissRequest = {},
        ) {

            Surface(
                modifier = modifier
                    .fillMaxWidth()
                    .border(
                        width = 5.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = AbsoluteRoundedCornerShape(31.dp)
                    )
                    .clip(AbsoluteRoundedCornerShape(32.dp))
            ) {

                Column(
                    modifier = modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(requestReason),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        permissionLauncher.launch(permissionArray)

                        if (permissionArray.map {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    it
                                ) == PackageManager.PERMISSION_GRANTED
                            }.reduce { acc: Boolean, it ->
                                acc && it
                            })
                            dialogNeeded = false
                    }) {
                        Text(text = stringResource(R.string.iUnderstand))
                    }
                }
            }
        }
    }
}