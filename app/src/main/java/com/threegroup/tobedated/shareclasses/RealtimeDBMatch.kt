package com.threegroup.tobedated

import com.google.firebase.database.ServerValue

data class RealtimeDBMatch(
    var id: String="",
    val usersMatched: List<String> = emptyList(),
    val timestamp: Map<String, String> = mapOf(),
    val lastMessage: String?=null
)
object RealtimeDBMatchProperties{
    const val usersMatched = "usersMatched"
    const val timestamp = "timestamp"
    const val lastMessage = "lastMessage"

    fun toData(user1: String, user2:String): Map<String, Any>{
        return mapOf(
            usersMatched to listOf(user1, user2),
            timestamp to ServerValue.TIMESTAMP
        )
    }
}
