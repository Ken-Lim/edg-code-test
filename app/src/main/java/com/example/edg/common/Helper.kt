package com.example.edg.common

import java.text.NumberFormat
import java.util.*

fun Double.toCurrencyString(): String {
    return NumberFormat.getCurrencyInstance(Locale.US).format(this)
}
