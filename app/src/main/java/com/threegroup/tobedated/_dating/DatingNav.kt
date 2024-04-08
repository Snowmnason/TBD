package com.threegroup.tobedated._dating

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.threegroup.tobedated._dating.composables.BioEdit
import com.threegroup.tobedated._dating.composables.ChangePhoto
import com.threegroup.tobedated._dating.composables.Comeback
import com.threegroup.tobedated._dating.composables.PromptEdit
import com.threegroup.tobedated.shareclasses.MyApp

@Composable
fun DatingNav(dating: DatingActivity, token:String, location:String) {
    val potentialUserDataLoaded = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val viewModelDating = viewModel { DatingViewModel(MyApp.x) }
    LaunchedEffect(Unit) {
        viewModelDating.setLoggedInUser(token, location)
        viewModelDating.getPotentialUserData {
            // Callback function executed when data retrieval is complete
            potentialUserDataLoaded.value = true
        }

    }

    NavHost(navController = navController, startDestination = Dating.SearchingScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
        composable(route = Dating.SearchingScreen.name) {
            if(potentialUserDataLoaded.value){
                SearchingScreen(navController, viewModelDating)
            }else{
                Comeback(text = "currently loading your future connection")
                //do nothing yet
            }

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