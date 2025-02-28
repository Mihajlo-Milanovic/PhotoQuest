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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.theme.PhotoQuestTheme

@Composable
fun ProfileScreen(
    navController: NavController
) {

    //TODO: Implement profile screen loading and quests

    val vm = ProfileScreenViewModel.getInstance()
    if (vm.navController == null)
        vm.setNavCtrl(navController)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSecondary),
                horizontalArrangement = Arrangement.End
            ) {
                ProfileScreenOptionsToggle(
                    vm = vm,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .windowInsetsPadding(insets = WindowInsets(top = 16.dp))
                )
            }
        },
        bottomBar = {
            NavBar(navController = navController)
        }
    ) { padding ->

        if (!vm.userLoaded.value) {
            LaunchedEffect(Unit) {
                Log.d("MIKI", "Loading the user")
                vm.getUsersInfo()
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            userScrollEnabled = true,

            ) {

            item {//Settings

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {

                    if (vm.showOptions.value) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onSecondary)
                                .padding(8.dp)
                                .align(Alignment.Top),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                        ) {
                            Button(onClick = { vm.onSignOut() }) {
                                Text(text = stringResource(id = R.string.signOut))
                            }
                        }
                    }
                }
            }

            item {//Picture, username, score ...
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

            item { //Users quests

                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.6f),
                    contentAlignment = Alignment.Center
                ) {

                    if (!vm.usersQuestsLoaded.value)
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    else
                        Column(
                            modifier = Modifier.fillMaxHeight(0.8f)
                        ) {

                            for (q in 0..vm.usersQuests.size - 3 step 3) {

                                Row {
                                    for (i in 0..3) {
                                        Box(
                                            modifier = Modifier
                                                .border(width = 1.dp, color = Color.Black)
                                        ) {
                                            Text(text = (i + q).toString())
                                        }
                                    }
                                }

                            }
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
        verticalArrangement = Arrangement.Bottom,
    ) {

        Text(
            text = stringResource(id = R.string.score),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = vm.displayedUser.value.score.toString(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.questsMade),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = vm.displayedUser.value.questsMade.toString(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { vm.onMakeNewQuest() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(id = R.string.makeNewQuest))
        }
    }
}

@Composable
fun ProfileScreenOptionsToggle(
    vm: ProfileScreenViewModel,
    modifier: Modifier = Modifier
) {

    IconButton(
        modifier = modifier,
        onClick = { vm.showOptions.value = !vm.showOptions.value }
    ) {
        Icon(painter = painterResource(id = R.drawable.grey_settings), contentDescription = null)
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
fun ProfilePictureFullSize(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.0f))
            .clickable { navController.popBackStack() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            modifier = Modifier
                .fillMaxWidth(),
            contentDescription = null,
        )
    }
}

@Preview(name = "LightTheme", showBackground = true)
@Preview(
    name = "DarkTheme",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun ProfileScreenPreview() {
    PhotoQuestTheme {
        ProfileScreen(navController = NavController(context = LocalContext.current))
    }
}