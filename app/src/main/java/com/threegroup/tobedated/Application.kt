package com.threegroup.tobedated

import android.app.Application
import com.google.firebase.FirebaseApp
import com.threegroup.tobedated.shareclasses.FirebaseDataSource
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyApp : Application() {
    companion object {
        lateinit var x: Repository
        var _signedInUser = MutableStateFlow<UserModel?>(null)
        var signedInUser: StateFlow<UserModel?> = _signedInUser
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        // Perform initialization tasks here
        // Setup global resources, libraries, etc.
        x = Repository(FirebaseDataSource())
    }
}