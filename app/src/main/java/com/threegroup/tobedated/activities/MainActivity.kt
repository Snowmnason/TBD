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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PolkaDotCanvas()
                SplashScreen()
            }
        }
        lifecycleScope.launch {
            val sharedPreferences = getSharedPreferences("firebase_user", Context.MODE_PRIVATE)
            val userToken = sharedPreferences.getString("firebase_user_token", null)

            if (userToken != null) {
//                println(userToken)
//                val viewModelDating = DatingViewModel(MyApp.x)
//                viewModelDating.setUser(userToken)
                navigateToDatingActivity(userToken)
            } else {
                navigateToLoginActivity()
            }
        }
    }

    private fun navigateToDatingActivity(userToken:String) {
        val intent = Intent(this, DatingActivity::class.java)
        intent.putExtra("token", userToken)
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}





