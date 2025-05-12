package com.example.photoquest.extensions

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import com.google.firebase.Timestamp
import java.util.Calendar

fun Double.roundTo(decimals: Int): Double {
    return "%.${decimals}f".format(this).toDouble()
}

fun Float.roundTo(decimals: Int): Float {
    return "%.${decimals}f".format(this).toFloat()
}

fun Timestamp.moveByXMonths(x: Int): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.time = this.toDate()
    calendar.add(Calendar.MONTH, x)
    return Timestamp(calendar.time)
}


/**
 * Sets height to the same value as width
 *
 * Width is set to fill available space
 */
fun Modifier.fillMaxWidthSquare(): Modifier = composed {
    var size by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    this
        .fillMaxWidth()
        .onSizeChanged { size = it.width }
        .height(with(density) { size.toDp() })
}

/**
 * Sets width to the same value as height
 *
 * Height is set to fill available space
 */
fun Modifier.fillMaxHeightSquare(): Modifier = composed {
    var size by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    this
        .fillMaxHeight()
        .onSizeChanged { size = it.height }
        .width(with(density) { size.toDp() })
}