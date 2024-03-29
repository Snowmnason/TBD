package com.threegroup.tobedated

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.UserSearchPreferenceModel

class DatingViewModel(private var repository: Repository) : ViewModel() {
    val list = ArrayList<UserModel>()
    fun getPotentialUserData(callback: (ArrayList<UserModel>) -> Unit) {
        try {
            FirebaseDatabase.getInstance().getReference("users")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (data in snapshot.children) {
                                val model = data.getValue(UserModel::class.java)
                                if (model?.number != FirebaseAuth.getInstance().currentUser?.phoneNumber) {
                                    model?.let {
                                        list.add(it)
                                    }
                                }
                            }
                        }
                        // Invoke the callback with the fetched list
                        callback(list)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("MY_DEBUGGER", "ERROR")
                        // Handle error, maybe invoke the callback with an empty list
                        callback(ArrayList())
                    }
                })
        } catch (e: Exception) {
            Log.d("MY_DEBUGGER", "ERROR")
            // Handle error, maybe invoke the callback with an empty list
            callback(ArrayList())
        }
    }

    fun getNextPotential(currentProfileIndex:Int):UserModel{
        val user:UserModel = if (currentProfileIndex < list.size - 1) {
            list[currentProfileIndex]
        } else {
            UserModel()
        }
        return user
    }

    fun likedCurrentPotential(currentProfileIndex:Int, currentPotential:UserModel):UserModel{

        return getNextPotential(currentProfileIndex)
    }
    fun passedCurrentPotential(currentProfileIndex:Int,currentPotential:UserModel):UserModel{

        return getNextPotential(currentProfileIndex)
    }
    fun reportedCurrentPotential(currentProfileIndex:Int,currentPotential:UserModel):UserModel{

        return getNextPotential(currentProfileIndex)
    }


    private var signedInUser:UserModel = UserModel() //= repository.getUser()

    fun setLongedInUser(){


    }

    fun updateUserPreferences(updatedPreferences: UserSearchPreferenceModel) {
        signedInUser.userPref = updatedPreferences
    }
    fun updateUser(updatedUser: UserModel) {
        signedInUser = updatedUser
    }
    fun getUser(): UserModel {
        return signedInUser
    }
}