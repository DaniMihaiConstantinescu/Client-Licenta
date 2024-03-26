package com.example.testapp.utils.funcs

fun parseTime(timeString: String): Map<String, Int> {
    val parts = timeString.split(":")
    val hour = parts[0].toIntOrNull() ?: 0
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0
    return mapOf("h" to hour, "m" to minute)
}