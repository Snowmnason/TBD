package com.threegroup.tobedated

import android.app.Application
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    companion object {
        lateinit var x: Repository
    }
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        // Perform initialization tasks here
        // Setup global resources, libraries, etc.
        //x = Repository()
    }
}