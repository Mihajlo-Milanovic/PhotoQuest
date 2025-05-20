package com.example.photoquest.ui.components.bottomBar

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
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
            .height(64.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.secondaryContainer),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            for (button in vm.buttons) {

                NavButton(
                    vm = vm,
                    iconId = button.icon,
                    navigateTo = button.name,
                ) {
                    if (button.name == Screens.PROFILE)
                        ProfileScreenViewModel.getInstance().userUID = currentUserUid()
                }
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
        modifier = modifier
            .fillMaxHeightSquare(),
        colors = IconButtonColors(
            Color.Transparent,
            MaterialTheme.colorScheme.onSecondaryContainer,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        enabled = !vm.selected(navigateTo),
    ) {
        Icon(
            modifier = Modifier
                .fillMaxHeight(),
            painter = painterResource(iconId),
            contentDescription = null,
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
            containerColor = Color.Green,
            bottomBar = {
                NavBar(navController = NavController(LocalContext.current))
            }) { pv ->
            Text(text = "Sample text", modifier = Modifier.padding(pv))
        }
    }
}