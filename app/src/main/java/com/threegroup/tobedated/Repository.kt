package com.threegroup.tobedated

import com.threegroup.tobedated.models.UserModel

class Repository (
    var FirebaseDataSource: FirebaseDataSource
) {
    suspend fun getUserData() {
        return FirebaseDataSource.getUserData()
    }

}

