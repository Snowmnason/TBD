package com.threegroup.tobedated.shareclasses

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated.shareclasses.models.MessageModel
import com.threegroup.tobedated.shareclasses.models.UserModel
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
        Message related functions
     */
    //TODO check all code related to chats/messages for functionality
    /**
     * Function to get chats from database
     * takes the chat id
     */
    suspend fun getChatData(chatId: String?) {
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
    suspend fun storeChatData(message: String) {
        var senderId: String? = null
        var chatId: String? = null
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

