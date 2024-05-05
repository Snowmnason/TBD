package com.threegroup.tobedated._dating

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.threegroup.tobedated.theme.AppTheme

//val notifiSearching = Random.nextBoolean()

const val notifiGroup = false
const val notifiChat = 0//Random.nextInt(0, 41) // Generates a random integer between 0 and 40

class DatingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreference = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("activityToken", "dating")
        editor.apply()
        setContent {
            AppTheme(
                activity = "dating"
            ) {
                //TopAndBotBarsDating(this@DatingActivity)
                //DatingNav(this@DatingActivity)
            }

        }
    }
}

enum class Dating {
    SearchingScreen,
    SearchPreferenceScreen,
    ProfileScreen,
    EditProfileScreen,
    ChatsScreen,
    BlindScreen,
    SomeScreen,
    MessagerScreen,
    FeedBackMessagerScreen
}