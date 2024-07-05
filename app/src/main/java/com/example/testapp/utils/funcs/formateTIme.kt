package com.example.testapp.utils.funcs

fun formatTime(timeMap: Map<String, Int>): String {
    val hours = timeMap["h"] ?: 0
    val minutes = timeMap["m"] ?: 0

    val formattedHours = String.format("%02d", hours)
    val formattedMinutes = String.format("%02d", minutes)

    return "$formattedHours:$formattedMinutes"
}