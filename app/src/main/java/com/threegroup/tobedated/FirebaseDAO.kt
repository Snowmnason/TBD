package com.threegroup.tobedated

import com.google.firebase.auth.FirebaseUser
import com.threegroup.tobedated.models.UserModel

/*
    We might want to delete this as it's not doing anything right now
*/
class FirebaseDAO(private val authDataSource: FirebaseDataSource) {

    suspend fun getUserData(): ArrayList<UserModel>? {
        return authDataSource.getUserData()
    }

}
