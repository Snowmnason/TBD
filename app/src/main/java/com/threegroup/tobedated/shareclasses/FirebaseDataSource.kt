package com.threegroup.tobedated.shareclasses

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated.RealtimeDBMatch
import com.threegroup.tobedated.RealtimeDBMatchProperties
import com.threegroup.tobedated.shareclasses.models.MessageModel
import com.threegroup.tobedated.shareclasses.models.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FirebaseDataSource() {
    /*
       Log in and authentication functions
     */
    /**
     * Function for user to sign in by phone number and verification code
     */
    suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
    }

    /**
     * Function to check if a user exists in the database
     * takes a phone number as a parameter
     */
    suspend fun checkUserExist(number: String) {
        FirebaseDatabase.getInstance().getReference("users").child("+1$number")
    }

    /*
      Sign up functions
    */
    /**
     * Function to store users data after making an account
     * takes an UserModel type object which will contain all the user's input and adds it to database
     */
    suspend fun storeUserData(data: UserModel) {
        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data)
    }

    /*
          User data related functions
     */
    suspend fun getCurrentUserSenderId(): String? {
        val currentUser = getCurrentFirebaseUser() ?: return null
        return currentUser.uid
    }

    private fun getCurrentFirebaseUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    /**
     * Function to pull the list of users (not including current user)
     * meant to be used for dating discovery
     */
    suspend fun getUserData(): ArrayList<UserModel>? { //TODO look into converting from DataSnapshot to Flow and StateFLow
        var list: ArrayList<UserModel>? = null
        try {
            FirebaseDatabase.getInstance().getReference("users")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d("MYTAG", "onDataChange: ${snapshot.toString()}")

                        if (snapshot.exists()) {
                            list = arrayListOf()
                            for (data in snapshot.children) {
                                val model = data.getValue(UserModel::class.java)

                                if (model!!.number != FirebaseAuth.getInstance().currentUser!!.phoneNumber) { // TODO check this; not sure if it works
                                    list!!.add(model)
                                }

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("MY_DEBUGGER", Log.ERROR.toString())
                    }
                })
        } catch (e: Exception) {
            Log.d("MY_DEBUGGER", Log.ERROR.toString())
        }
        return list
    }

    /**
     * Function to update user's profile
     * currently takes a UserModel as a parameter but we may want to change this to the specific variables being updated
     */
    suspend fun updateUserData(userUpdates: UserModel) {
        //TODO check if this properly updates the changed values the user put in and does not create a duplicate user (resulting in an error most likely) or anything else strange
        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(userUpdates)
    }

    /*
        Likes and match related functions
     */
    suspend fun likeUser(userId: String, likedUserId: String, isLike: Boolean): RealtimeDBMatch? {
        val database = FirebaseDatabase.getInstance()

        // Update user's liked or passed list
        val userRef = database.getReference("users/$userId")
        userRef.child(if (isLike) "liked" else "passed").push().setValue(likedUserId)

        // Mark the liked user as existing in the liked list
        val likedUserRef = database.getReference("users/$userId/liked/$likedUserId")
        likedUserRef.setValue(true)

        // Check if there's a match
        val hasUserLikedBack = hasUserLikedBack(userId, likedUserId)
        if (hasUserLikedBack) {
            val matchId = getMatchId(userId, likedUserId)

            // Create a new match in the database
            val matchRef = database.getReference("matches/$matchId")
            val matchData = RealtimeDBMatchProperties.toData(likedUserId, userId)
            matchRef.setValue(matchData)

            // Retrieve the match data and return
            val matchSnapshot = matchRef.get().await()
            return matchSnapshot.getValue(RealtimeDBMatch::class.java)
        }
        return null
    }

    private suspend fun hasUserLikedBack(userId: String, likedUserId: String): Boolean {
        val likedRef =
            FirebaseDatabase.getInstance().getReference("users/$likedUserId/liked/$userId")
        val likedSnapshot = likedRef.get().await()
        return likedSnapshot.exists()
    }

    private fun getMatchId(userId1: String, userId2: String): String {
        // Here you can define your logic to generate a match ID
        // For simplicity, let's concatenate user IDs
        return if (userId1 < userId2) {
            "$userId1-$userId2"
        } else {
            "$userId2-$userId1"
        }
    }


    /*
        Message related functions
     */
//TODO check all code related to chats/messages for functionality
    /**
     * Function to get chats from database
     * takes the chat id
     */
    fun getChatData(chatId: String?): Flow<List<MessageModel>> = callbackFlow {
        FirebaseDatabase.getInstance().getReference("chats")
            .child(chatId!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<MessageModel>()

                    for (show in snapshot.children) {
                        list.add(show.getValue(MessageModel::class.java)!!)
                    }

                    // binding.recyclerView2.adapter = MessageAdapter(this@MessageActivity, list)

                }

                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(this@MessageActivity, error.message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    /**
     * Function to store messages between users
     * takes the message as a parameter
     */

    suspend fun storeChatData(chatId: String?, message: String) {
        val senderId = FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).toString()

        val currentTime: String = SimpleDateFormat("HH:mm a", Locale.getDefault()).format(Date())
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        val map = hashMapOf<String, String>()
        map["message"] = message
        map["senderId"] = senderId!!
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)

        reference.child(reference.push().key!!).setValue(map)
        /*
        .addOnCompleteListener {
        if (it.isSuccessful) {
            binding.yourMessage.text = null
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

         */
    }

    /**
     * Function to display the chats in the messages screen
     */
    suspend fun displayChats() {
        val currentId = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var list = arrayListOf<String>()
                    var newList = arrayListOf<String>()

                    for (data in snapshot.children) {
                        if (data.key!!.contains(currentId!!)) {
                            list.add(data.key!!.replace(currentId!!, ""))
                            newList.add(data.key!!)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}

