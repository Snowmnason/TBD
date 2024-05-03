package com.threegroup.tobedated._friends

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
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated.shareclasses.api.ApiViewModel

@Composable
fun FriendNav(friend: FriendsActivity, viewModelFriend:FriendViewModel) {
    val navController = rememberNavController()
    val vmApi = viewModel { ApiViewModel(MyApp.x) }

    LaunchedEffect(Unit) {

    }

    NavHost(navController = navController, startDestination = Friend.SearchingScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
        composable(route = Friend.SearchingScreen.name) {
            if(true){//potentialUserDataLoaded.value
                SearchingScreen(navController, friend, vmApi)
            }else{
                ComeBackScreen(navController, friend, vmApi)
                //do nothing yet
            }

        }
        composable(route = Friend.ProfileScreen.name) {
            ProfileScreen(navController, friend, vmApi)
        }
        composable(route = Friend.EditProfileScreen.name) {
            EditProfileScreen(navController, friend, viewModelFriend)
        }
        composable(route = Friend.SearchPreferenceScreen.name) {
            SearchPreferenceScreen(navController, viewModelFriend)
        }
        composable(route = Friend.ChatsScreen.name) {
            ChatsScreen(navController, friend, vmApi)
        }
        composable(route = Friend.GroupsScreen.name) {
            GroupsScreen(navController, friend, vmApi)
        }
        composable(route = Friend.SomeScreen.name) {
            SomeScreen(navController, friend, vmApi)
        }
        composable(route = Friend.MessagerScreen.name) {
            MessagerScreen(navController, viewModelFriend)
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
            ChangePreference(navController, myParam, myIndex, viewModelFriend)
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
            ChangeProfileScreen(navController, myParam, myIndex, viewModelFriend)
        }

    }

}
//        composable(route = "BioEdit") {
//            BioEdit(nav = navController, vmFriend= viewModelFriend)
//        }
//        composable(
//            route = "PromptEdit/{index}",
//            arguments = listOf(navArgument("index") { type = NavType.IntType },)
//        ) { backStackEntry ->
//            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
//            PromptEdit(navController, viewModelFriend, myIndex)
//        }
//        composable(route = "ChangePhoto") {
//            ChangePhoto(nav = navController, vmFriend= viewModelFriend, friend = friend)
//        }
//composable(route = "MatchedUserProfile") {
//    MatchedUserProfile(nav = navController, vmFriend= viewModelFriend)
//}