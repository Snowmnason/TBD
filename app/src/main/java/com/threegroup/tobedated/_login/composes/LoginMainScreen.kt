package com.threegroup.tobedated._login.composes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.threegroup.tobedated._login.composables.LoginSplash
import com.threegroup.tobedated._signUp.composables.BigButton

@Composable
fun LoginMainScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
        //.background(AppTheme.colorScheme.background)
    ) {
        LoginSplash()
        BigButton(
            text = "Connect with the app",
            onClick = {
                navController.navigate("LoginScreen")
            },
            isUse = true
        )
    }
}