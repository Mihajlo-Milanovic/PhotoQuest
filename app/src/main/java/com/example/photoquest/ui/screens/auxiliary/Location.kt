package com.example.photoquest.ui.screens.auxiliary

import android.content.Context
import android.location.LocationManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.photoquest.R
import kotlinx.coroutines.delay

@Composable
fun LocationNotEnabledSplashScreen(onRetry: () -> Unit) {

    val context = LocalContext.current
    var isLocationEnabled by remember { mutableStateOf(isLocationEnabled(context)) }

    LaunchedEffect(isLocationEnabled) {
        while (!isLocationEnabled) {
            delay(10000) // Check every 10 seconds
            isLocationEnabled = isLocationEnabled(context)
        }
        onRetry()
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onRetry()
            },
    )
    { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_no_gps_foreground),
                contentDescription = "Location not enabled",
                modifier = Modifier
                    .size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Location Not Enabled")
        }

        Box(
            Modifier
                .fillMaxSize()
                .clickable {
                    onRetry()
                }
        )
    }
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}