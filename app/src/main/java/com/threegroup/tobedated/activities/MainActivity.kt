package com.threegroup.tobedated.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.composables.SplashScreen
import com.threegroup.tobedated.ui.theme.AppTheme
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                    PolkaDotCanvas()
                    SplashScreen()
                }
        }
        lifecycleScope.launch {
            val sharedPreferences = getSharedPreferences("firebase_user", Context.MODE_PRIVATE)
            val userTokenDeferred = async { sharedPreferences.getString("firebase_user_token", null) }

            val userToken = userTokenDeferred.await()
            val check: Boolean = userToken != null
            checkLogin(check)//1 goes to sign up for testing
        }
    }

    private fun checkLogin(check:Boolean){
        if (check){
            val intent = Intent(this, DatingActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}





