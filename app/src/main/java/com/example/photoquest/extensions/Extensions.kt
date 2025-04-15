package com.example.photoquest.extensions

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