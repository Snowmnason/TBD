package com.threegroup.tobedated._causal

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
import com.threegroup.tobedated.shareclasses.MyApp
import kotlin.random.Random

@Composable
fun CausalNav(causal: CausalActivity, token:String, location:String) {
    val navController = rememberNavController()
    val viewModelCausal = viewModel { CausalViewModel(MyApp.x) }

    val notifiGroup = Random.nextBoolean()
    val notifiChat = Random.nextInt(0, 41) // Generates a random integer between 0 and 40
    LaunchedEffect(Unit) {

    }

    NavHost(navController = navController, startDestination = Causal.SearchingScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
        composable(route = Causal.SearchingScreen.name) {
            if(true){//potentialUserDataLoaded.value
                SearchingScreen(navController, causal, viewModelCausal)
            }else{
                ComeBackScreen(navController, causal)
                //do nothing yet
            }

        }
        composable(route = Causal.ProfileScreen.name){
            ProfileScreen(navController, causal, viewModelCausal)
        }
        composable(route = Causal.EditProfileScreen.name) {
            EditProfileScreen(navController, causal, viewModelCausal)
        }
        composable(route = Causal.SearchPreferenceScreen.name) {
            SearchPreferenceScreen(navController, viewModelCausal)
        }
        composable(route = Causal.ChatsScreen.name) {
            ChatsScreen(navController, causal, viewModelCausal)
        }
        composable(route = Causal.GroupsScreen.name) {
            GroupsScreen(navController, causal)
        }
        composable(route = Causal.SomeScreen.name) {
            SomeScreen(navController, causal)
        }
        composable(route = Causal.MessagerScreen.name) {
            MessagerScreen(navController, viewModelCausal)
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
            ChangePreference(navController, myParam, myIndex, viewModelCausal)
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
            ChangeProfileScreen(navController, myParam, myIndex, viewModelCausal)
        }

    }

}
//        composable(route = "BioEdit") {
//            BioEdit(nav = navController, vmCausal= viewModelCausal)
//        }
//        composable(
//            route = "PromptEdit/{index}",
//            arguments = listOf(navArgument("index") { type = NavType.IntType },)
//        ) { backStackEntry ->
//            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
//            PromptEdit(navController, viewModelCausal, myIndex)
//        }
//        composable(route = "ChangePhoto") {
//            ChangePhoto(nav = navController, vmCausal= viewModelCausal, causal = causal)
//        }
//composable(route = "MatchedUserProfile") {
//    MatchedUserProfile(nav = navController, vmCausal= viewModelCausal)
//}