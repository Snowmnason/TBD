package com.threegroup.tobedated

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated.models.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class Repository (
    private var firebaseDataSource: FirebaseDataSource
) {
    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential){
        return firebaseDataSource.signInWithPhoneAuthCredential(credential)
    }
    suspend fun checkUserExist(number: String) {
        return firebaseDataSource.checkUserExist(number)
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
    suspend fun getChatData(chatId: String?) {
        return firebaseDataSource.getChatData(chatId)
    }

    suspend fun storeChatData(message: String) {
        return firebaseDataSource.storeChatData(message)
    }

    suspend fun displayChats() {
        return firebaseDataSource.displayChats()
    }



    /*
    Not sure where to put this function at the moment
    /**
     * Function to verify the chat id
     */
    suspend fun verifyChatId() {
        val receiverId = intent.getStringExtra("userId")
        val senderId: String? = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        var chatId = senderId + receiverId
        val reverseChatId = receiverId + senderId
        val reference = FirebaseDatabase.getInstance().getReference("chats")
        //.child(chatId)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.hasChild(chatId!!)) {
                    runBlocking {
                        firebaseDataSource.getChatData(chatId)
                    }
                } else if (snapshot.hasChild(reverseChatId)) {
                    runBlocking {
                        chatId = reverseChatId
                        firebaseDataSource.getChatData(chatId)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Toast.makeText(this@MessageActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }
     */
}

