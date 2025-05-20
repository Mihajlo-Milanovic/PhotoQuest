package com.example.photoquest.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeToRevealContentCard(
    modifier: Modifier = Modifier,

    leftContentExists: Boolean = false,
    maxRightCardOffset: Float = with(LocalDensity.current) { 100.dp.toPx() },
    leftContent: @Composable (modifier: Modifier) -> Unit = {},

    rightContentExists: Boolean = false,
    maxLeftCardOffset: Float = with(LocalDensity.current) { 50.dp.toPx() },
    rightContent: @Composable (modifier: Modifier) -> Unit = {},

    cardClickEnabled: Boolean = true,
    onCardClick: () -> Unit = {},
    cardContent: @Composable () -> Unit
) {
    val swipeOffset = remember { Animatable(0f) }

    var mod = modifier
        .fillMaxWidth()
        .background(Color.Transparent)
        .clip(CardDefaults.shape)

    if (leftContentExists || rightContentExists) {
        val animationCoroutine = rememberCoroutineScope()

        mod = mod.pointerInput(Unit) {
            detectHorizontalDragGestures(
                onHorizontalDrag = { change, dragAmount ->
                    change.consume()

                    val newOffset = (swipeOffset.value + dragAmount)
                        .coerceIn(
                            if (rightContentExists) -maxLeftCardOffset else 0f,
                            if (leftContentExists) maxRightCardOffset else 0f
                        )

                    animationCoroutine.launch(Dispatchers.Default) {
                        swipeOffset.snapTo(newOffset)
                    }
                },
                onDragEnd = {
                    animationCoroutine.launch(Dispatchers.Default) {
                        swipeOffset.animateTo(
                            if (swipeOffset.value > 0)
                                if (swipeOffset.value > maxRightCardOffset / 2)
                                    maxRightCardOffset
                                else 0f
                            else
                                if (-swipeOffset.value > maxLeftCardOffset / 2)
                                    -maxLeftCardOffset
                                else 0f
                        )
                    }
                }
            )
        }
    }

    Box(
        modifier = mod
    ) {

        Row(
            modifier = Modifier
                .matchParentSize()
                .background(MaterialTheme.colorScheme.tertiaryContainer),
            horizontalArrangement = when (true) {
                (leftContentExists && rightContentExists) -> Arrangement.SpaceBetween
                leftContentExists -> Arrangement.Start
                rightContentExists -> Arrangement.End
                else -> Arrangement.Absolute.SpaceBetween
            }
        ) {
            leftContent(Modifier.width(maxLeftCardOffset.dp))
            rightContent(Modifier.width(maxRightCardOffset.dp))
        }

        Card(
            modifier = Modifier
                .offset { IntOffset(swipeOffset.value.roundToInt(), 0) }
                .fillMaxSize()
                .clickable(enabled = cardClickEnabled) { onCardClick() },
        ) {
            cardContent()
        }

    }
}