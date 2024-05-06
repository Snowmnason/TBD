package com.threegroup.tobedated._casual

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.threegroup.tobedated.MainActivity
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._casual.composes.BlindScreenC
import com.threegroup.tobedated._casual.composes.ChangePreferenceC
import com.threegroup.tobedated._casual.composes.ChangeProfileScreenC
import com.threegroup.tobedated._casual.composes.ChatsScreenC
import com.threegroup.tobedated._casual.composes.ComeBackScreenC
import com.threegroup.tobedated._casual.composes.EditProfileScreenC
import com.threegroup.tobedated._casual.composes.FeedBackMessagerScreenC
import com.threegroup.tobedated._casual.composes.MessagerScreenC
import com.threegroup.tobedated._casual.composes.ProfileScreenC
import com.threegroup.tobedated._casual.composes.SearchPreferenceScreenC
import com.threegroup.tobedated._casual.composes.SearchingScreenC
import com.threegroup.tobedated._casual.composes.SomeScreenC
import com.threegroup.tobedated._dating.composes.MatchedUserProfileC
import com.threegroup.tobedated.composeables.profiles.BioEditC
import com.threegroup.tobedated.composeables.profiles.PromptEditC
import com.threegroup.tobedated.shareclasses.api.ApiViewModel

@Composable
fun CasualNav(
    main: MainActivity,
    vmApi: ApiViewModel,
    mainNav: NavHostController,
    navController: NavHostController,
    insideWhat: (String) -> Unit,
){
    //val navController = rememberNavController()
    val viewModelCasual = viewModel { CasualViewModel(MyApp.x) }

//    val vmApi = viewModel { ApiViewModel(MyApp.x) }
//    val notifiGroup = Random.nextBoolean()
//    val notifiChat = Random.nextInt(0, 41) // Generates a random integer between 0 and 40
    LaunchedEffect(Unit) {

    }

    NavHost(navController = navController, startDestination = Casual.SearchingScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
        composable(route = Casual.SearchingScreen.name) {
            if (false) {//potentialUserDataLoaded.value
                SearchingScreenC(viewModelCasual, vmApi)//TODO needs more work than expect
            } else {
                ComeBackScreenC(navController, vmApi, viewModelCasual) //TODO needs more work than expect
            }
            insideWhat("Main")
        }
        composable(route = Casual.ProfileScreen.name) {
            ProfileScreenC(navController, viewModelCasual)
            insideWhat("Main")
        }
        composable(route = Casual.EditProfileScreen.name) {
            EditProfileScreenC(navController, main, viewModelCasual, mainNav)
            insideWhat("Settings")
        }
        composable(route = Casual.SearchPreferenceScreen.name) {
            SearchPreferenceScreenC(navController, viewModelCasual)
            insideWhat("Settings")
        }
        composable(route = Casual.ChatsScreen.name) {
            ChatsScreenC(navController, viewModelCasual)
            insideWhat("Main")
        }
        composable(route = Casual.BlindScreen.name) {
            BlindScreenC(navController)
            insideWhat("Main")
        }
        composable(route = Casual.SomeScreen.name) {
            SomeScreenC(viewModelCasual)
            insideWhat("Main")
        }
        composable(route = Casual.MessagerScreen.name) {
            MessagerScreenC(navController, viewModelCasual, vmApi)
            insideWhat("Messages")
        }
        composable(route = Casual.FeedBackMessagerScreen.name) {
            FeedBackMessagerScreenC(navController, viewModelCasual)
            insideWhat("Messages")
        }
        composable(
            route = "ChangePreferenceC/{my_param}/{index}",
            arguments = listOf(
                navArgument("my_param") { type = NavType.StringType },
                navArgument("index") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val myParam = backStackEntry.arguments?.getString("my_param") ?: ""
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            ChangePreferenceC(navController, myParam, myIndex, viewModelCasual)
            insideWhat("")
        }
        composable(
            route = "ChangeProfileScreenC/{my_param}/{index}",
            arguments = listOf(
                navArgument("my_param") { type = NavType.StringType },
                navArgument("index") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val myParam = backStackEntry.arguments?.getString("my_param") ?: ""
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            ChangeProfileScreenC(navController, myParam, myIndex, viewModelCasual)
            insideWhat("")
        }
        composable(route = "BioEdit") {
            BioEditC(nav = navController, vmCasual = viewModelCasual)
            insideWhat("")
        }
        composable(
            route = "PromptEdit/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            PromptEditC(navController, viewModelCasual, myIndex)
            insideWhat("")
        }
        composable(route = "ChangePhoto") {
            //TODO//ChangePhotoC(nav = navController, vmCasual = viewModelCasual, main = main)
            insideWhat("")
        }
        composable(route = "MatchedUserProfile") {
            MatchedUserProfileC(nav = navController, vmCasual = viewModelCasual)
            insideWhat("Match")
        }
    }
}
enum class Casual {
    SearchingScreen,
    SearchPreferenceScreen,
    ProfileScreen,
    EditProfileScreen,
    ChatsScreen,
    BlindScreen,
    SomeScreen,
    FeedBackMessagerScreen,
    MessagerScreen,
}
