package com.threegroup.tobedated._login

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun LoginNav(loginActivity: LoginActivity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login.LoginMainScreen.name,
        enterTransition = { EnterTransition.None }, exitTransition =   { ExitTransition.None},
        popEnterTransition = { EnterTransition.None }, popExitTransition = { ExitTransition.None}) {
        composable(route = Login.LoginMainScreen.name) {
            LoginMainScreen(navController)
        }
        composable(route = Login.LoginScreen.name) {
            LoginScreen(navController, loginActivity)
        }
        composable(
            route = "VerificationCodeView/{number}",
            arguments = listOf(navArgument("number") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("number") ?: ""
            VerificationCodeView(navController, phoneNumber, loginActivity)
        }
    }
}