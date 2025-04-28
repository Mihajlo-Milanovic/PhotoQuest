package com.example.photoquest.ui.screens.profile

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.photoquest.R
import com.example.photoquest.extensions.roundTo
import com.example.photoquest.models.data.Quest
import com.example.photoquest.services.reverseGeocode
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.theme.PhotoQuestTheme

@Composable
fun ProfileScreen(
    navController: NavController
) {

    //TODO: Implement profile picture upload and view

    val vm = ProfileScreenViewModel.getInstance()
    if (vm.navController == null)
        vm.setNavCtrl(navController)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        },
    ) { paddingValues ->

        LaunchedEffect(Unit) {
            Log.d("MIKI", "Loading the user")
            vm.getUsersInfo()
            Log.d("MIKI", "Loading the users quests")
            vm.getUsersQuests()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {


                item { //Picture, username, score ...
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {

                        ProfilePictureUsernameAndFullName(vm = vm)

                        Spacer(modifier = Modifier.width(32.dp))

                        ScoreAndQuestNumber(vm = vm)
                    }
                }


                if (!vm.usersQuestsLoaded.value) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize(0.5f),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {

                    //Users quests
                    items(vm.usersQuests) { q ->

                        QuestPreviewCard(
                            quest = q,
                            vm = vm,
                        )

                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun ProfilePictureUsernameAndFullName(vm: ProfileScreenViewModel) {

    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {

        ProfilePicture(
            vm = vm,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = vm.displayedUser.value.username,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Italic
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = vm.displayedUser.value.firstName + " " + vm.displayedUser.value.lastName,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }


}

@Composable
fun ScoreAndQuestNumber(vm: ProfileScreenViewModel) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {

        Text(
            text = "${stringResource(id = R.string.score)}\n${vm.displayedUser.value.score}",
            modifier = Modifier,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "${stringResource(id = R.string.questsMade)}\n${vm.displayedUser.value.questsMade}",
            modifier = Modifier,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfilePicture(vm: ProfileScreenViewModel, modifier: Modifier) {

    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_orange_camera_foreground),
            contentDescription = "Profile picture",
            modifier = Modifier
                .clickable { vm.zoomProfilePicture() },
        )
    }
}

@Composable
fun QuestPreviewCard(
    quest: Quest,
    vm: ProfileScreenViewModel,
    modifier: Modifier = Modifier,
) {

    var searchedForAddress by remember { mutableStateOf(false) }
    var fullAddress by remember { mutableStateOf<String?>(null) }

    if (!searchedForAddress) {
        val address = reverseGeocode(quest.lat, quest.lng, context = LocalContext.current)

        fullAddress = address?.getAddressLine(0)
        searchedForAddress = true
    }
    Card(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable { vm.questOnClick(quest) },
        //elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .clickable { vm.questOnClick(quest) },
        ) {

            AsyncImage(
                model = quest.pictureDownloadURL,
                contentDescription = quest.description,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { vm.questImageOnClick(quest) }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .clip(RoundedCornerShape(size = 8.dp))
                    .size(
                        height = 128.dp,
                        width = 128.dp
                    )
                    .background(MaterialTheme.colorScheme.secondaryContainer),
            )

            Spacer(Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .clickable { vm.questOnClick(quest) },
            ) {
                Text(text = (quest.title))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Address:\n" +
                            if (fullAddress != null) ("$fullAddress")
                            else "Lat: ${quest.lat.roundTo(6)},\nLong: ${quest.lng.roundTo(6)}"

                )
            }

        }
    }
}

//@Preview(name = "LightTheme", showBackground = true)
@Preview(
    name = "DarkTheme",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun ProfileScreenPreview() {
    PhotoQuestTheme {
        Scaffold { pv ->
            Column {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(pv)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .sizeIn(
                            maxHeight = 256.dp,
                            maxWidth = 256.dp
                        ),

                    ) {
                    Text("Hello World!")
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(pv)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .sizeIn(
                            maxHeight = 256.dp,
                            maxWidth = 256.dp
                        ),

                    ) {
                    Text("Hello World!")
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(pv)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .sizeIn(
                            maxHeight = 256.dp,
                            maxWidth = 256.dp
                        ),

                    ) {
                    Text("Hello World!")
                }
            }
        }
    }
}