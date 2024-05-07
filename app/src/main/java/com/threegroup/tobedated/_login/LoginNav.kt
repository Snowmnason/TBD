package com.threegroup.tobedated._login

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.threegroup.tobedated.MainActivity
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._login.composes.LoginMainScreen
import com.threegroup.tobedated._login.composes.LoginScreen
import com.threegroup.tobedated._login.composes.VerificationCodeView

@Composable
fun LoginNav(mainNav:NavHostController, mainActivity: MainActivity, location:String) {
    val navController = rememberNavController()
    val viewModelLogin = viewModel { LoginViewModel(MyApp.x) }
    viewModelLogin.setLocation(location)
    NavHost(navController = navController, startDestination = Login.LoginMainScreen.name,
        enterTransition = { EnterTransition.None }, exitTransition =   { ExitTransition.None},
        popEnterTransition = { EnterTransition.None }, popExitTransition = { ExitTransition.None}) {
        composable(route = Login.LoginMainScreen.name) {
            LoginMainScreen(navController)
        }
        composable(route = Login.LoginScreen.name) {
            LoginScreen(navController, viewModelLogin, mainActivity)
        }
        composable(
            route = "VerificationCodeView/{number}",
            arguments = listOf(navArgument("number") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("number") ?: ""
            VerificationCodeView(navController, phoneNumber, mainActivity, viewModelLogin, mainNav)
        }
    }
}
enum class Login {
    LoginMainScreen,
    LoginScreen,
}