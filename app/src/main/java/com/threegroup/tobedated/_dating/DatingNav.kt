package com.threegroup.tobedated._dating

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.threegroup.tobedated._dating.composables.BioEdit
import com.threegroup.tobedated._dating.composables.ChangePhoto
import com.threegroup.tobedated._dating.composables.PromptEdit
import com.threegroup.tobedated.shareclasses.MyApp

@Composable
fun DatingNav(dating: DatingActivity, token:String, location:String) {
    val navController = rememberNavController()
    val viewModelDating = viewModel { DatingViewModel(MyApp.x) }
    LaunchedEffect(Unit) {
        viewModelDating.setLoggedInUser(token, location)
    }
    NavHost(navController = navController, startDestination = Dating.SearchingScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
        composable(route = Dating.SearchingScreen.name) {
            SearchingScreen(navController, viewModelDating)
        }
        composable(route = Dating.ProfileScreen.name) {
            ProfileScreen(navController, viewModelDating)
        }
        composable(route = Dating.EditProfileScreen.name) {
            EditProfileScreen(navController, dating, viewModelDating)
        }
        composable(route = Dating.SearchPreferenceScreen.name) {
            SearchPreferenceScreen(navController, viewModelDating)
        }
        composable(route = Dating.ChatsScreen.name) {
            ChatsScreen(navController, viewModelDating)
        }
        composable(route = Dating.GroupsScreen.name) {
            GroupsScreen(navController)
        }
        composable(route = Dating.SomeScreen.name) {
            SomeScreen(navController)
        }
        composable(route = Dating.MessagerScreen.name) {
            MessagerScreen(navController, viewModelDating)
        }
        composable(
            route = "ChangePreference/{my_param}/{index}",
            arguments = listOf(
                navArgument("my_param") { type = NavType.StringType },
                navArgument("index") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val myParam = backStackEntry.arguments?.getString("my_param") ?: ""
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            ChangePreference(navController, myParam, myIndex, viewModelDating)
        }
        composable(
            route = "ChangeProfileScreen/{my_param}/{index}",
            arguments = listOf(
                navArgument("my_param") { type = NavType.StringType },
                navArgument("index") { type = NavType.IntType },
                )
        ) { backStackEntry ->
            val myParam = backStackEntry.arguments?.getString("my_param") ?: ""
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            ChangeProfileScreen(navController, myParam, myIndex, viewModelDating)
        }
        composable(route = "BioEdit") {
            BioEdit(nav = navController, vmDating= viewModelDating)
        }
        composable(
            route = "PromptEdit/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType },)
        ) { backStackEntry ->
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            PromptEdit(navController, viewModelDating, myIndex)
        }
        composable(route = "ChangePhoto") {
            ChangePhoto(nav = navController, vmDating= viewModelDating, dating = dating)
        }
        composable(route = "MatchedUserProfile") {
            MatchedUserProfile(nav = navController, vmDating= viewModelDating)
        }
    }

}