package com.example.photoquest.ui.components.bottomBar

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.Screens
import com.example.photoquest.extensions.fillMaxHeightSquare
import com.example.photoquest.services.currentUserUid
import com.example.photoquest.ui.screens.profile.ProfileScreenViewModel
import com.example.photoquest.ui.theme.PhotoQuestTheme

@Composable
fun NavBar(navController: NavController) {

    val vm = NavBarViewModel.getInstance()
    if (vm.navController == null)
        vm.setNavCtrl(navController)

    NavigationBar(
        Modifier
            .navigationBarsPadding()
            .height(64.dp)
            .background(Color.Transparent),
        containerColor = Color.Transparent,
        contentColor = Color.Transparent

    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.BottomCenter
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(bottom = 8.dp, end = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    //Leaderboard Button
                    NavButton(
                        vm = vm,
                        iconId = R.drawable.baseline_leaderboard,
                        navigateTo = Screens.LEADERBOARD,
                    )

                    //Settings Button
                    NavButton(
                        vm = vm,
                        iconId = R.drawable.grey_settings,
                        navigateTo = Screens.SETTINGS,
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(bottom = 8.dp, start = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    //Make quest
                    NavButton(
                        vm = vm,
                        iconId = R.drawable.round_add_box_24,
                        navigateTo = Screens.MAKE_QUEST,
                    )

                    //Profile Button
                    NavButton(
                        vm = vm,
                        iconId = R.drawable.baseline_person,
                        navigateTo = Screens.PROFILE,
                    ) {
                        ProfileScreenViewModel.getInstance().userUID = currentUserUid()
                    }

                }
            }

            FloatingActionButton(
                shape = ShapeDefaults.Medium,
                modifier = Modifier
                    .fillMaxHeightSquare(),
                onClick = { vm.navigateToScreen(Screens.MAP) }
            ) {
                NavButton(
                    vm = vm,
                    iconId = R.drawable.photo_quest_vector,
                    navigateTo = Screens.MAP
                )
            }
        }
    }
}

@Composable
fun NavButton(
    vm: NavBarViewModel,
    navigateTo: Screens,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    prepare: () -> Unit = {}
) {
    IconButton(
        onClick = {
            prepare()
            vm.navigateToScreen(navigateTo)
        },
        modifier = Modifier
            .fillMaxHeight(),
        enabled = !vm.selected(navigateTo),
    ) {
        Icon(
            modifier = modifier
                .fillMaxHeight(),
            painter = painterResource(iconId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )

    }
}

//@Preview(name = "LightTheme", showBackground = true)
@Preview(
    name = "DarkTheme",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true
)
@Composable
fun NavBarPreview() {
    PhotoQuestTheme {
        Scaffold(
            modifier = Modifier.background(Color.Red),
            containerColor = Color.Red,
            bottomBar = {
                NavBar(navController = NavController(LocalContext.current))
            }) { pv ->
            Text(text = "Sample text", modifier = Modifier.padding(pv))
        }
    }
}