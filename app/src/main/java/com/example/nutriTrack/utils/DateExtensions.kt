package com.example.nutriTrack.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getCurrDate(): String? {
    val current = LocalDateTime.now()

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    return current.format(formatter)
}