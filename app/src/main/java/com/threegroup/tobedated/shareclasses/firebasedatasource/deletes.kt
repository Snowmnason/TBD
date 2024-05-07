package com.threegroup.tobedated.shareclasses.firebasedatasource

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

suspend fun removeLikeOrPassData(database: FirebaseDatabase, userId: String, inOther: String) {
    try {
        val likeOrPassRef = database.getReference("likeorpass$inOther")
        val likeOrPassSnapshot = likeOrPassRef.get().await()

        // Delete instances of the user in other user's likeorpass children
        likeOrPassSnapshot.children.forEach { userSnapshot ->
            userSnapshot.children.forEach { subSnapshot ->
                subSnapshot.children.forEach { numberSnapshot ->
                    if (numberSnapshot.key == userId) {
                        numberSnapshot.ref.removeValue().await()
                    }
                }
            }
        }

        // Delete the user's likeorpass data
        likeOrPassRef.child(userId).removeValue().await()

        Log.d("RemoveLikeOrPassData", "Like or pass data removed successfully for user $userId")
    } catch (e: Exception) {
        Log.e("RemoveLikeOrPassData", "Error removing like or pass data: ${e.message}", e)
    }
}

suspend fun deleteMatches(database: FirebaseDatabase, userId: String, inOther: String) {
    try {
        val matchesRef = database.getReference("matches$inOther")
        val matchesSnapshot = matchesRef.get().await()

        matchesSnapshot.children.forEach { matchSnapshot ->
            val matchId = matchSnapshot.key ?: ""
            if (matchId.contains(userId)) {
                matchSnapshot.ref.removeValue().await()
            }
        }

        Log.d("DeleteMatches", "Matches deleted successfully for user $userId")
    } catch (e: Exception) {
        Log.e("DeleteMatches", "Error deleting matches: ${e.message}", e)
    }
}

suspend fun deleteChats(database: FirebaseDatabase, userId: String, inOther: String) {
    val chatsRef = database.getReference("chats$inOther")
    val chatsSnapshot = chatsRef.get().await()

    chatsSnapshot.children.forEach { chatSnapshot ->
        val chatId = chatSnapshot.key ?: ""
        if (chatId.contains(userId)) {
            chatSnapshot.ref.removeValue().await()
        }
    }
}

suspend fun deleteUserDataFromStorage(userId: String) {
    try {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val userStorageRef = storageRef.child("users/$userId")

        // Delete all files and subdirectories under the user's storage folder
        userStorageRef.listAll().await().items.forEach { fileRef ->
            fileRef.delete().await()
        }

        // Delete the user's storage folder itself
        userStorageRef.delete().await()

        Log.d(
            "DeleteUserDataFromStorage",
            "User data deleted successfully from Firebase Storage"
        )
    } catch (e: Exception) {
        Log.e(
            "DeleteUserDataFromStorage",
            "Error deleting user data from Firebase Storage: ${e.message}",
            e
        )
    }
}

suspend fun deleteUserFromAuthentication(): Boolean {
    return try {
        val user = FirebaseAuth.getInstance().currentUser
        user?.delete()?.await()
        Log.d(
            "DeleteUserFromAuthentication",
            "User successfully deleted from Firebase Authentication"
        )
        true
    } catch (e: Exception) {
        Log.e(
            "DeleteUserFromAuthentication",
            "Error deleting user from Firebase Authentication: ${e.message}",
            e
        )
        false
    }
}