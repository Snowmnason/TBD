package com.threegroup.tobedated.shareclasses.api.horoscope

data class HoroscopeResponse(
    val description: String,
    val color: String,
    val mood: String,
    val compatibility: String,
    val lucky_number: String,
    val lucky_time: String
)
