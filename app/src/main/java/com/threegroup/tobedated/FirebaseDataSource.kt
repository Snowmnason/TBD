package com.threegroup.tobedated

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.threegroup.tobedated.models.UserModel
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class FirebaseDataSource() {
    companion object {
        var list: ArrayList<UserModel>? = null
    }

    // TODO: May need to change this function because we are not using the card stack anymore, look at
    suspend fun getUserData() {
        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("TAG", "onDataChange: ${snapshot.toString()}")

                    if (snapshot.exists()) {
                        list = arrayListOf()
                        for (data in snapshot.children) {
                            val model = data.getValue(UserModel::class.java)

                            if (model!!.number != FirebaseAuth.getInstance().currentUser!!.phoneNumber) { // TODO check this; not sure if it works
                                list!!.add(model)
                            }

                        }
                        list!!.shuffle()

                    } else {
                        TODO()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // TODO:
                }
            })

        FirebaseDatabase.getInstance().getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("TAG", "onDataChange: ${snapshot.toString()}")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO()
                }

            })

    }
}
