package com.example.photoquest.ui.components.bottomBar

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.Screens
import com.example.photoquest.ui.theme.PhotoQuestTheme

@Composable
fun NavBar(navController: NavController) {

    val vm = NavBarViewModel.getInstance()
    if (vm.navController == null)
        vm.setNavCtrl(navController)

    NavigationBar(
        modifier = Modifier
            .navigationBarsPadding()
            .height(56.dp),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            //Leaderboard Button
            NavButton(
                vm = vm,
                iconId = R.drawable.baseline_leaderboard,
                navigateTo = Screens.LEADERBOARD,
            )

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
            )

        }
    }
}

@Composable
fun NavButton(vm: NavBarViewModel, navigateTo: Screens, @DrawableRes iconId: Int) {
    IconButton(
        onClick = { vm.navigateToScreen(navigateTo) },
        modifier = Modifier
            .fillMaxHeight(),
        enabled = !vm.selected(navigateTo),
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(iconId),
            contentDescription = null
        )

    }
}

//@Preview(name = "LightTheme", showBackground = true)
@Preview(name = "DarkTheme", uiMode = UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun NavBarPreview() {
    PhotoQuestTheme {
        NavBar(navController = NavController(LocalContext.current))
    }
}