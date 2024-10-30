package com.example.photoquest.ui.permissions

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.photoquest.R
import com.example.photoquest.ui.screens.makeQuest.MakeQuestScreenViewModel

@Composable
fun RequestPermission(
    permission: String
) {

    val vm = PermissionsViewModel.getInstance()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        vm.updatePermission(permission, isGranted)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (vm.isPermissionGranted(permission, context)) {
            Text("${permission.substringAfter("_")} permission granted")
        } else {
            Text("${permission.substringAfter("_")} permission not granted")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {

                permissionLauncher.launch(permission)
            }) {
                Text("Request ${permission.substringAfter("_")} Permission")
            }
        }
    }
}

@Composable
fun PleaseEnableLocationAndCameraDialog() {

    val makeQuestVM = MakeQuestScreenViewModel.getInstance()

    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true,
        ),
        onDismissRequest = { /*TODO*/ }) {

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 5.dp,
                    color = MaterialTheme.colorScheme.secondary,
                    shape = AbsoluteRoundedCornerShape(31.dp)
                )
                .clip(AbsoluteRoundedCornerShape(32.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    text = stringResource(R.string.toMakeQuest),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = { makeQuestVM.showEnableLocationTrackingDialog.value = false }) {
                    Text(text = stringResource(R.string.iUnderstand))
                }
            }
        }
    }
}

@Composable
fun RequestLocationAndCameraPermissions(
    context: Context
) {

    val permissionsViewModel = PermissionsViewModel.getInstance()

    /* TODO: Reconsider request for camera permission*/

    when (false) {

        permissionsViewModel.isPermissionGranted(
            permission = Manifest.permission_group.LOCATION,
            context = context
        ) -> RequestPermission(permission = Manifest.permission_group.LOCATION)

        permissionsViewModel.isPermissionGranted(
            permission = Manifest.permission.ACCESS_COARSE_LOCATION,
            context = context
        ) -> RequestPermission(permission = Manifest.permission.ACCESS_COARSE_LOCATION)

        permissionsViewModel.isPermissionGranted(
            permission = Manifest.permission.ACCESS_FINE_LOCATION,
            context = context
        ) -> RequestPermission(permission = Manifest.permission.ACCESS_FINE_LOCATION)

        else -> Log.i("PERMISSIONS", "All location tracking permissions granted.")
    }
}

