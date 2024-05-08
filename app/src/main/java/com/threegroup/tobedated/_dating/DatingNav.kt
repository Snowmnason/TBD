package com.threegroup.tobedated._dating

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.threegroup.tobedated.MainActivity
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._dating.composes.BlindScreen
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
fun DatingNav(
    main: MainActivity,
    vmApi: ApiViewModel,
    mainNav:NavHostController,
    navController:NavHostController,
    insideWhat: (String) -> Unit,
    ){
    LaunchedEffect(Unit) {
        //navController.popBackStack()
    }

    val viewModelDating = viewModel { DatingViewModel(MyApp.x) }
    viewModelDating.setLoggedInUser()
    viewModelDating.getMatchesFlow(viewModelDating.getUser().number)
    viewModelDating.fetchPotentialUserData()


    val userList by viewModelDating.potentialUserData.collectAsState()
    val isPotentialUserDataLoaded = userList.isNotEmpty()





    NavHost(navController = navController, startDestination = Dating.SearchingScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
        composable(route = Dating.SearchingScreen.name) {
            if (isPotentialUserDataLoaded) {
                SearchingScreen(viewModelDating, vmApi)
            }else{
                ComeBackScreen(navController, vmApi, viewModelDating)
            }
            insideWhat("Main")
        }
        composable(route = Dating.ProfileScreen.name) {
            ProfileScreen(navController, viewModelDating, vmApi)
            insideWhat("Main")
        }
        composable(route = Dating.EditProfileScreen.name) {
            EditProfileScreen(navController, main, viewModelDating, mainNav)
            insideWhat("Settings")
        }
        composable(route = Dating.SearchPreferenceScreen.name) {
            SearchPreferenceScreen(navController, viewModelDating)
            insideWhat("Settings")
        }
        composable(route = Dating.ChatsScreen.name) {
            ChatsScreen(navController, viewModelDating)
            insideWhat("Main")
        }
        composable(route = Dating.BlindScreen.name) {
            BlindScreen(navController)
            insideWhat("Main")
        }
        composable(route = Dating.SomeScreen.name) {
            SomeScreen(viewModelDating)
            insideWhat("Main")
        }
        composable(route = Dating.MessagerScreen.name) {
            MessagerScreen(navController, viewModelDating, vmApi)
            insideWhat("Messages")
        }
        composable(route = Dating.FeedBackMessagerScreen.name) {
            FeedBackMessagerScreen(navController, viewModelDating)
            insideWhat("Messages")
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
            insideWhat("")
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
            insideWhat("")
        }
        composable(route = "BioEdit") {
            BioEdit(nav = navController, vmDating = viewModelDating)
            insideWhat("")
        }
        composable(
            route = "PromptEdit/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            PromptEdit(navController, viewModelDating, myIndex)
            insideWhat("")
        }
        composable(route = "ChangePhoto") {
            ChangePhoto(nav = navController, vmDating = viewModelDating, dating = main)
            insideWhat("")
        }
        composable(route = "MatchedUserProfile") {
            MatchedUserProfile(nav = navController, vmDating = viewModelDating)
            insideWhat("Match")
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