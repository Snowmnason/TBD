package com.threegroup.tobedated.shareclasses.models

data class Match(
    var id: String = "",//Match id
    //var userAge: Int = 0,
    var userId: String = "",//phone number
    var userName: String = "",
    var userPicture: String = "",
    //var formattedDate: String = "",
    var lastMessage: String? = "",
)
