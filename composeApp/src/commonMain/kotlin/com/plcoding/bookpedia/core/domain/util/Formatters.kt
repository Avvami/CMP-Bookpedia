package com.plcoding.bookpedia.core.domain.util

import kotlin.math.round

fun formatAverageRating(rating: Double): String {
    val roundedValue = round(rating * 10) / 10.0
    return when {
        roundedValue % 1.0 == 0.0 -> roundedValue.toInt().toString()
        else -> roundedValue.toString()
    }
}