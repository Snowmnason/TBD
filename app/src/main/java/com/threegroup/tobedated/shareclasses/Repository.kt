package com.threegroup.tobedated.shareclasses

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.threegroup.tobedated.RealtimeDBMatch
import com.threegroup.tobedated.shareclasses.models.MessageModel
import com.threegroup.tobedated.shareclasses.models.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Repository(
    private var firebaseDataSource: FirebaseDataSource
) {
    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        return firebaseDataSource.signInWithPhoneAuthCredential(credential)
    }

    suspend fun checkUserExist(number: String) {
        return firebaseDataSource.checkUserExist(number)
    }

    suspend fun getCurrentUserSenderId(): String? {
        return firebaseDataSource.getCurrentUserSenderId()
    }

    suspend fun storeUserData(data: UserModel) {
        return firebaseDataSource.storeUserData(data)
    }

    suspend fun getUserData(): ArrayList<UserModel>? {
        return firebaseDataSource.getUserData()
    }

    suspend fun updateUserData(userUpdates: UserModel) {
        return firebaseDataSource.updateUserData(userUpdates)
    }

    suspend fun likeUser(userId: String, likedUserId: String, isLike: Boolean): RealtimeDBMatch? {
        return firebaseDataSource.likeUser(userId,likedUserId,isLike)
    }

    fun getChatData(chatId: String?): Flow<List<MessageModel>> =
        firebaseDataSource.getChatData(chatId).map { list ->
            list.map {
                val sender = FirebaseAuth.getInstance().currentUser!!.phoneNumber
                val text = it.message
                MessageModel(sender, text)
            }
        }

    suspend fun storeChatData(chatId:String, message: String) {
        return firebaseDataSource.storeChatData(chatId, message)
    }

    suspend fun displayChats() {
        return firebaseDataSource.displayChats()
    }

}

