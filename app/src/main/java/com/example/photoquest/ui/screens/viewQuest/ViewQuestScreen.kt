package com.example.photoquest.ui.screens.viewQuest

import android.content.res.Configuration
import android.net.Uri
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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
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
import com.example.photoquest.models.data.Quest
import com.example.photoquest.ui.components.bottomBar.NavBar
import com.example.photoquest.ui.theme.PhotoQuestTheme
import com.google.firebase.Timestamp

@Composable
fun ViewQuestScreen(
    navController: NavController
) {

    val context = LocalContext.current
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

        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(pv)
        ) {
            item {
                AsyncImage(
                    model = vm.quest.pictureDownloadURL,
                    contentDescription = vm.quest.description,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { vm.questImageOnClick() }
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondaryContainer,
//                        shape = RoundedCornerShape(size = 8.dp)
                        )
//                    .clip(RoundedCornerShape(size = 8.dp))
                        .sizeIn(
                            maxHeight = LocalView.current.width.dp
                        )
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                )
            }

            item {
                TitleAndDescription(vm = vm)
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
                if (!vm.showFullDescription)
                    Text(
                        text = "Click to see more",
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
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun ViewQuestPreView() {

    PhotoQuestTheme {

        ViewQuestScreenViewModel.getInstance().quest = Quest(
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

        ViewQuestScreen(navController = NavController(LocalContext.current))
    }
}