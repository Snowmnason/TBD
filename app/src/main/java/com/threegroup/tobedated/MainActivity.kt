package com.threegroup.tobedated

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.ui.theme.AppTheme.colorScheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                PolkaDotCanvas()
                    val photo = if (isSystemInDarkTheme()) painterResource(id = R.drawable.logo) else painterResource(id = R.drawable.logodark)
                    SplashScreen(photo)
                }
        }
        lifecycleScope.launch {
            val sharedPreferences = getSharedPreferences("firebase_user", Context.MODE_PRIVATE)
            val userToken = sharedPreferences.getString("firebase_user_token", null)
            delay(100)
            val check: Boolean = userToken != null
            checkLogin(true)//1 goes to sign up for testing
        }
    }

    private fun checkLogin(check:Boolean){
        if (check){
            //Change this to main activity
            val intent = Intent(this,SignUpActivity::class.java)
            //val intent = Intent(this,DatingActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}



@Composable
fun SplashScreen(photo:Painter) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            //.background(AppTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Box(modifier = Modifier
                .size(250.dp, 100.dp)
            ){
                Image(
                    painter = photo,
                    contentDescription = "logo",
                    contentScale = ContentScale.FillBounds)
            }
            Text(
                text = "To Be Dated",
                modifier = Modifier,
                fontSize = 24.sp,
                style = AppTheme.typography.bodyMedium,
                color = colorScheme.onBackground
            )
            Text(
                text = "the dating app made for connections",
                modifier = Modifier,
                fontSize = 15.sp,
                style = AppTheme.typography.labelMedium,
                color = colorScheme.onBackground
            )
        }
    }
}

