package com.threegroup.tobedated.shareclasses

import com.threegroup.tobedated.shareclasses.models.UserModel

/*
    We might want to delete this as it's not doing anything right now
*/
class FirebaseDAO(private val authDataSource: FirebaseDataSource) {

    suspend fun getUserData(): ArrayList<UserModel>? {
        return authDataSource.getUserData()
    }

}
