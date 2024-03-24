package com.threegroup.tobedated

import com.google.firebase.auth.FirebaseUser

class FirebaseDAO(private val authDataSource: FirebaseDataSource) {

    suspend fun getUserData() {
        return authDataSource.getUserData()
    }

}
