package com.threegroup.tobedated._dating

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.UserModel

class DatingViewModel(private var repository: Repository) : ViewModel() {
    val list = ArrayList<UserModel>()
    private var signedInUser: UserModel = UserModel() //= repository.getUser()
    private var selectedUser: UserModel = UserModel() //The chat you open
    fun getPotentialUserData(callback: () -> Unit) {
        try {
            FirebaseDatabase.getInstance().getReference("users")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val tempList =
                                ArrayList<UserModel>() // Temporary list to store potential users
                            for (data in snapshot.children) {
                                val model = data.getValue(UserModel::class.java)
                                if (model?.number != FirebaseAuth.getInstance().currentUser?.phoneNumber) {
                                    model?.let {
                                        if (!it.seeMe) {
                                            tempList.add(it)
                                        }
                                    }
                                }
                            }
                            // Sort the list by status (currentTimeMillis)
                            val sortedList = tempList.sortedByDescending { it.status }

                            // Update the original list with the sorted list
                            list.clear()
                            list.addAll(sortedList)
                        }
                        // Invoke the callback to indicate that data retrieval is complete
                        callback()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("MY_DEBUGGER", "ERROR")
                        // Handle error
                    }
                })
        } catch (e: Exception) {
            Log.d("MY_DEBUGGER", "ERROR")
            // Handle error
        }
    }


    fun getNextPotential(currentProfileIndex: Int): UserModel? {
        return if (currentProfileIndex < list.size) {
            list[currentProfileIndex]
        } else {
            null
        }
    }

    //Stuff for liking and passing
    fun likedCurrentPotential(currentProfileIndex: Int, currentPotential: UserModel): UserModel? {

        return getNextPotential(currentProfileIndex)
    }

    fun passedCurrentPotential(currentProfileIndex: Int, currentPotential: UserModel): UserModel? {

        return getNextPotential(currentProfileIndex)
    }

    fun reportedCurrentPotential(
        currentProfileIndex: Int,
        currentPotential: UserModel
    ): UserModel? {

        return getNextPotential(currentProfileIndex)
    }

    //Stuff for setting and getting matches
    fun getMatches(): ArrayList<UserModel> {
        return list
    }


    //Stuff for chats
    fun setTalkedUser(userModel: UserModel) {
        selectedUser = userModel
    }

    fun getTalkedUser(): UserModel {
        return selectedUser
    }

    /**
     *  generates a unique chatId made from the UIDs of the sender and receiver
     */
    fun getChatId(senderId: String, receiverId: String): String {
        return if (senderId > receiverId) {
            senderId + receiverId
        } else receiverId + senderId
    }

    //Stuff for signed in user
    fun getUser(): UserModel {
        return signedInUser
    }

    fun updateUser(updatedUser: UserModel) {
        val userPhoneNumber = updatedUser.number
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber)
        databaseReference.setValue(updatedUser)
        signedInUser = updatedUser
    }

    fun setLoggedInUser(userPhoneNumber: String, location: String) {
        signedInUser = repository.setUserInfo(userPhoneNumber, location)
    }

}