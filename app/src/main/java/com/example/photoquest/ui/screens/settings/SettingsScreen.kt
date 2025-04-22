package com.example.photoquest.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.ui.components.bottomBar.NavBar

@Composable
fun SettingsScreen(
    navController: NavController
) {

    val vm = SettingsScreenViewModel.getInstance()
    if (vm.navController == null)
        vm.setNavCtrl(navController)


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavBar(navController = navController)
        },
    ) { pv ->

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(pv)
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    //.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {

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

            item {
                OutlinedTextField(
                    value = vm.questSearchRadius.toString(),
                    onValueChange = {

                        if (it.isNotEmpty() || it.matches(Regex("""\d+(\.\d*)?|\.\d+"""))) {
                            vm.questSearchRadius = it.toDouble()
                        }
                    },
                    label = { Text("Quest search radius") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    trailingIcon = {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "km",
                                textAlign = TextAlign.Right
                            )

                            IconButton(
                                onClick = { vm.questSearchRadius += 5 }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_plus_sign_24),
                                    contentDescription = "Increase search radius",
                                )
                            }

                            IconButton(
                                onClick = { vm.questSearchRadius -= 5 }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.baseline_minus_sign_24),
                                    contentDescription = "Decrease search radius",
                                )
                            }

                        }
                    },
                    modifier = Modifier
                )
            }

            item {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Switch(
                        checked = vm.automaticallySearchForQuest,
                        onCheckedChange = {
                            vm.automaticallySearchForQuest = !vm.automaticallySearchForQuest
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = stringResource(R.string.searchForQuestsAutomatically))
                }
            }

        }

    }
}


//Row(
//modifier = Modifier
//.fillMaxWidth(),
////.padding(8.dp),
//verticalAlignment = Alignment.CenterVertically,
//horizontalArrangement = Arrangement.SpaceAround,
//) {
//
//    if (vm.showOptions.value) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(MaterialTheme.colorScheme.onSecondary)
//                .padding(8.dp)
//                .align(Alignment.Top),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Top,
//        ) {
//            Button(onClick = { vm.onSignOut() }) {
//                Text(text = stringResource(id = R.string.signOut))
//            }
//        }
//    }
//}