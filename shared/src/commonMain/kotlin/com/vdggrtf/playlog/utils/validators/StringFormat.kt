package com.vdggrtf.playlog.utils.validators

import kotlin.math.round

fun Double.formatPercent(): String {
    val rounded = round(this * 10) / 10.0
    return "$rounded%"
}

fun Float.formatOneDecimal(): String {
    val rounded = round(this * 10) / 10f
    // Если число целое (например, 3.0), сохраняем красивый вид с нулём
    return if (rounded % 1 == 0f) "${rounded.toInt()}.0" else "$rounded"
}
