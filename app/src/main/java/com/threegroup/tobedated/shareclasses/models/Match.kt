package com.threegroup.tobedated.shareclasses.models

data class Match(
    val id: String,//Match id
    val userAge: Int,
    val userId: String,//phone number
    val userName: String,
    val userPicture: String,
    val formattedDate: String,
    val lastMessage: String?
)
