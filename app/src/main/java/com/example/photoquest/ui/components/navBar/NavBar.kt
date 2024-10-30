package com.example.photoquest.ui.components.navBar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.photoquest.R
import com.example.photoquest.Screens

@Composable
fun NavBar() {

    val vm = NavBarViewModel.getInstance()

    NavigationBar {

        //Leaderboard Button
        IconButton(
            onClick = { vm.navigateToScreen(Screens.LEADERBOARD) },
            modifier = Modifier.padding(8.dp),
            enabled = !vm.selected(Screens.LEADERBOARD),
        ) {

            Icon(
                painter = painterResource(R.drawable.baseline_leaderboard),
                contentDescription = null
            )
        }

        //Profile Button
        IconButton(
            onClick = { vm.navigateToScreen(Screens.PROFILE) },
            modifier = Modifier.padding(8.dp),
            enabled = !vm.selected(Screens.PROFILE),
        ) {

            Icon(
                painter = painterResource(R.drawable.baseline_person),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun NavBarPreview() {
    MaterialTheme {
        NavBar()
    }
}