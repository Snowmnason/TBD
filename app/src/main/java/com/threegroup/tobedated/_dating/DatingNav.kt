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
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._dating.composes.ChangePreference
import com.threegroup.tobedated._dating.composes.ChangeProfileScreen
import com.threegroup.tobedated._dating.composes.ChatsScreen
import com.threegroup.tobedated._dating.composes.ComeBackScreen
import com.threegroup.tobedated._dating.composes.EditProfileScreen
import com.threegroup.tobedated._dating.composes.FeedBackMessagerScreen
import com.threegroup.tobedated._dating.composes.MatchedUserProfile
import com.threegroup.tobedated._dating.composes.MessagerScreen
import com.threegroup.tobedated._dating.composes.ProfileScreen
import com.threegroup.tobedated._dating.composes.SearchPreferenceScreen
import com.threegroup.tobedated._dating.composes.SearchingScreen
import com.threegroup.tobedated._dating.composes.SomeScreen
import com.threegroup.tobedated.composeables.profiles.BioEdit
import com.threegroup.tobedated.composeables.profiles.ChangePhoto
import com.threegroup.tobedated.composeables.profiles.PromptEdit
import com.threegroup.tobedated.shareclasses.api.ApiViewModel

@Composable
fun DatingNav(dating: DatingActivity){
    val potentialUserDataLoaded = remember { mutableStateOf(false) }
    val navController = rememberNavController()
    val viewModelDating = viewModel { DatingViewModel(MyApp.x) }
    viewModelDating.setLoggedInUser() //TODO make sure location works....
    viewModelDating.getMatchesFlow(viewModelDating.getUser().number)
    val vmApi = viewModel { ApiViewModel(MyApp.x) }
    vmApi.fetchWordOfTheDay()
    vmApi.fetchHoroscope(viewModelDating.getUser().star)
    vmApi.fetchPoem()
    var update = 0

    LaunchedEffect(update) {
        viewModelDating.potentialUserData.collect { userList ->
            if (userList.isNotEmpty()) {
                potentialUserDataLoaded.value = true
            }
        }
    }



    NavHost(navController = navController, startDestination = Dating.SearchingScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
        composable(route = Dating.SearchingScreen.name) {
            if(potentialUserDataLoaded.value){
                SearchingScreen(viewModelDating, dating, navController, vmApi)
            }else{
                update++
                ComeBackScreen(navController, dating, vmApi, viewModelDating)
            }
        }
        composable(route = Dating.ProfileScreen.name) {
            ProfileScreen(navController, viewModelDating, dating, vmApi)
        }
        composable(route = Dating.EditProfileScreen.name) {
            EditProfileScreen(navController, dating, viewModelDating)
        }
        composable(route = Dating.SearchPreferenceScreen.name) {
            SearchPreferenceScreen(navController, viewModelDating)
        }
        composable(route = Dating.ChatsScreen.name) {
            ChatsScreen(navController, viewModelDating, dating, vmApi)
        }
        composable(route = Dating.GroupsScreen.name) {
            GroupsScreen(navController, dating, vmApi)
        }
        composable(route = Dating.SomeScreen.name) {
            SomeScreen(navController, dating, viewModelDating, vmApi)
        }
        composable(route = Dating.MessagerScreen.name) {
            MessagerScreen(navController, viewModelDating)
        }
        composable(route = Dating.FeedBackMessagerScreen.name) {
            FeedBackMessagerScreen(navController, viewModelDating)
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