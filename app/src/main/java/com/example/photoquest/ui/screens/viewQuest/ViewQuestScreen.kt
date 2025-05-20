package com.example.photoquest.ui.screens.viewQuest

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.photoquest.R
import com.example.photoquest.extensions.fillMaxWidthSquare
import com.example.photoquest.models.data.Quest
import com.example.photoquest.services.getBoundsForRadius
import com.example.photoquest.services.getDistanceFromLatLng
import com.example.photoquest.ui.components.PictureFullSizeDialog
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.theme.PhotoQuestTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ViewQuestScreen(
    navController: NavController
) {
    val vm = remember { ViewQuestScreenViewModel.getInstance() }

    LaunchedEffect(vm) {
        if (vm.navController == null)
            vm.setNavCtrl(navController)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        },
    ) { pv ->

        //Full size picture dialog
        if (vm.zoomPicture) {

            PictureFullSizeDialog(
                imageUri = vm.quest.pictureDownloadURL,
                contentDescription = vm.quest.description,
                onDismissRequest = { vm.zoomPicture = false },
                modifier = Modifier.fillMaxSize(),
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(pv)
        ) {
            item {
                AsyncImage(
                    model = vm.quest.pictureDownloadURL,
                    contentDescription = vm.quest.description,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidthSquare()
                        .clickable { vm.zoomPicture = true }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                        )
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                )
            }

            item {
                TitleAndDescription(vm = vm)
            }

            item {
                LocationPreview(vm = vm)
            }


            item {
                UserInfo(vm = vm)
            }
        }
    }
}

@Composable
fun TitleAndDescription(
    vm: ViewQuestScreenViewModel,
    modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = vm.quest.title,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontSize = TextUnit(7f, TextUnitType.Em),
                fontFamily = FontFamily.Cursive
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable { vm.onDescriptionClick() },
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = Color.Red,
                disabledContainerColor = Color.Red
            )

        ) {
            Column {
                Text(
                    text = vm.quest.description,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize()
                        .sizeIn(
                            maxHeight = if (vm.showFullDescription) Dp.Unspecified
                            else 200.dp
                        )
                )

                Text(
                    text = if (!vm.showFullDescription) "Show more" else "Show less",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = TextUnit(10f, TextUnitType.Sp),
                        fontFamily = FontFamily.Monospace
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.onSecondary,
                        )
                        .padding(4.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun LocationPreview(
    vm: ViewQuestScreenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val camPosition = rememberCameraPositionState()
    val markerState = rememberUpdatedMarkerState(LatLng(vm.quest.lat, vm.quest.lng))

    LaunchedEffect(vm.quest) {
        vm.searchForAddress(context)
    }

    LaunchedEffect(vm.closeUpView, vm.mapScreenViewModel.location) {

        if (vm.closeUpView) {
            camPosition.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        LatLng(vm.quest.lat, vm.quest.lng),
                        18.5f,
                        0f,
                        0f
                    )
                )
            )
        } else vm.mapScreenViewModel.location?.let {
            camPosition.animate(
                update = CameraUpdateFactory.newLatLngBounds(
                    getBoundsForRadius(
                        LatLng(vm.quest.lat, vm.quest.lng),
                        getDistanceFromLatLng(
                            vm.quest.lat,
                            vm.quest.lng,
                            it.latitude,
                            it.longitude
                        ).let { distance ->
                            Log.d("MIKI", "Distance is -> $distance km")
                            if (distance > 0.1) distance else 0.1
                        }
                    ), 16
                )
            )
        }
    }

    Surface(
        shape = CardDefaults.shape,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidthSquare(),
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        GoogleMap(
            modifier = modifier
                .fillMaxSize(),
            cameraPositionState = camPosition,
            properties = MapProperties(
                isMyLocationEnabled = !vm.closeUpView,
                mapType = MapType.HYBRID,
            ),
            uiSettings = MapUiSettings(
                compassEnabled = true,
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = true,
                myLocationButtonEnabled = false,
                rotationGesturesEnabled = false,
                scrollGesturesEnabled = false,
                scrollGesturesEnabledDuringRotateOrZoom = false,
                tiltGesturesEnabled = false,
                zoomControlsEnabled = false,
                zoomGesturesEnabled = false,
            )

        ) {

            Marker(
                state = markerState,
                title = vm.fullAddress
            )

        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp)
                .padding(top = 16.dp, bottom = 16.dp)
                .alpha(0.8f)
        ) {
            FloatingActionButton(
                onClick = {
                    vm.closeUpView = !vm.closeUpView
                },
                shape = RoundedCornerShape(8.dp),
                contentColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .size(32.dp)
            ) {
                if (vm.closeUpView)
                    Icon(
                        painter = painterResource(R.drawable.baseline_zoom_out_map_24),
                        contentDescription = "Zoom out map",
                        modifier = Modifier
                    )
                else
                    Icon(
                        painter = painterResource(R.drawable.baseline_zoom_in_map_24),
                        contentDescription = "Zoom in map",
                        modifier = Modifier
                    )
            }
        }
    }
}

@Composable
fun UserInfo(
    vm: ViewQuestScreenViewModel,
    modifier: Modifier = Modifier
) {

    val publisherCoroutineScope = rememberCoroutineScope()

    LaunchedEffect(vm.quest) {
        publisherCoroutineScope.launch(Dispatchers.Default) {
            vm.getPublishersInfo()
        }
    }


    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { vm.viewPublishersProfile() },
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = Color.Red,
            disabledContainerColor = Color.Red
        )

    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable { vm.viewPublishersProfile() },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Text(
                text = "Publisher:\n\t ${vm.publisher?.username ?: "Unknown"}",
                modifier = Modifier
                    .clickable { vm.viewPublishersProfile() }
            )
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun ViewQuestPreView() {

    PhotoQuestTheme {

        ViewQuestScreenViewModel.getInstance().setDisplayedQuest(
            Quest(
                publisherId = "nNF46slnIlc4HeFE3bKbjDCOcxo1",
                title = "Here is the title",
                description = "Here is the description.\n\n\n\n\n\n\n\n\n\nAnd here is some looooooooooooooong dessssssccccrrriiipppttttiiiiooonnn!",
                lat = 43.959861,
                lng = 21.1741831,
                numberOfLikes = 0,
                pictureDownloadURL = Uri.parse("https://firebasestorage.googleapis.com/v0/b/photoquest-aa732.firebasestorage.app/o/questPhotos%2Fw7EIPH7Y2N8rl3PWCfqU%2FSelo.jpg?alt=media&token=bfcef57c-dd5d-4293-8cb4-d3f2c48e4580"),
                pictureUri = Uri.parse("content://media/external/images/media/1000005009"),
                timestamp = Timestamp.now(),
            )
        )
        ViewQuestScreen(navController = NavController(LocalContext.current))
    }
}