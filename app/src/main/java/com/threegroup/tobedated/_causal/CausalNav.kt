package com.threegroup.tobedated._causal

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.threegroup.tobedated._causal.composables.PromptQuestionsC
import com.threegroup.tobedated._signUp.composables.BackButton
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated.shareclasses.composables.AlertDialogBox
import com.threegroup.tobedated.shareclasses.composables.ProgressBar
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

@Composable
fun CausalNav(causal: CausalActivity, viewModelCausal: CausalViewModel){
    val navController = rememberNavController()
    navController.popBackStack()

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
            if (true) {//potentialUserDataLoaded.value
                SearchingScreen(navController, causal, viewModelCausal)
            } else {
                ComeBackScreen(navController, causal)
                //do nothing yet
            }

        }
        composable(route = Causal.ProfileScreen.name) {
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
@Composable
fun SignUpCausalNav(causal: CausalActivity, vmCausal: CausalViewModel){
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isFirstScreen = currentBackStackEntry?.destination?.route == CasualSign.WelcomeScreenC.name
    val isLastScreen = currentBackStackEntry?.destination?.route == CasualSign.PromptQuestionsScreenC.name
    var showDialog by remember { mutableStateOf(false) }
    var questionIndex by rememberSaveable { mutableIntStateOf(0) }
    var isButtonEnabled by rememberSaveable { mutableStateOf(false) }
    var noShow by rememberSaveable { mutableStateOf(true) }

    val screenOrder = listOf(
        CasualSign.WelcomeScreenC.name,
        CasualSign.LeaningScreen.name,
        CasualSign.LookingForScreen.name,
        CasualSign.ExperienceScreen.name,
        CasualSign.LocationScreen.name,
        CasualSign.CommScreen.name,
        CasualSign.SexHealthScreen.name,
        CasualSign.AfterCareScreen.name,
        CasualSign.NewBioScreen.name,
        CasualSign.PromptQuestionsScreenC.name,
    )
    val currentDestinationIndex = currentBackStackEntry?.destination?.route?.let { screenOrder.indexOf(it) }
    val nextDestinationIndex = currentDestinationIndex?.plus(1)
    if(noShow){
        BackButton(onClick = {
            if(isFirstScreen){
                showDialog = true
            }else{
                questionIndex--
                //isButtonEnabled = false
                navController.popBackStack()
            }
        })
    }
    if (showDialog) {
        AlertDialogBox(
            onDismissRequest = { showDialog = false },
            onConfirmation = { causal.switchActivities("dating") },
            dialogTitle = "Leave Signup",
            dialogText = "Are you sure, all your progress will be loss"
        )
    }
    if(!isFirstScreen) {
        ProgressBar(
            questionIndex = questionIndex,
            totalQuestionCount = 9,
        )
    }

    NavHost(navController = navController, startDestination = CasualSign.WelcomeScreenC.name,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(durationMillis = 150)) },
        exitTransition = { slideOutHorizontally(targetOffsetX  = { -1000 }, animationSpec = tween(durationMillis = 150)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(durationMillis = 150)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX  = { 1000 }, animationSpec = tween(durationMillis = 150)) }) {
        composable(route = CasualSign.WelcomeScreenC.name) {
            isButtonEnabled = welcomeScreenC()
        }
        composable(route = CasualSign.LeaningScreen.name) {
            isButtonEnabled = leaningScreen(vmCausal)
        }
        composable(route = CasualSign.LookingForScreen.name) {
            isButtonEnabled = lookingForScreen(vmCausal)
        }
        composable(route = CasualSign.ExperienceScreen.name) {
            isButtonEnabled = experienceScreen(vmCausal)
        }
        composable(route = CasualSign.LocationScreen.name) {
            isButtonEnabled = locationScreen(vmCausal)
        }
        composable(route = CasualSign.CommScreen.name) {
            isButtonEnabled = commScreen(vmCausal)
        }
        composable(route = CasualSign.SexHealthScreen.name) {
            isButtonEnabled = sexHealthScreen(vmCausal)
        }
        composable(route = CasualSign.AfterCareScreen.name) {
            isButtonEnabled = afterCareScreen(vmCausal)
        }
        composable(route = CasualSign.NewBioScreen.name) {
            isButtonEnabled = newBioScreen(vmCausal, onNavigate = {
                // Code to navigate to the next screen or perform any other action
                questionIndex++
                nextDestinationIndex?.let { nextIndex ->
                    screenOrder.getOrNull(nextIndex)?.let { nextScreen ->
                        navController.navigate(nextScreen)
                    }
                }
            })
        }
        composable(route = CasualSign.PromptQuestionsScreenC.name) {
            noShow = true
            isButtonEnabled = promptQuestionsScreenC(navController, vmCausal)
        }
        composable(route = "PromptQuestions/{index}", arguments = listOf(
            navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            noShow = false
            PromptQuestionsC(navController, vmCausal, myIndex)
        }
    }
    var buttonText = if (isFirstScreen) "I Agree" else if (isLastScreen) "Finish" else "Enter"
    if(noShow) {
        BigButton(
            text = buttonText,
            onClick = {
                if (!isFirstScreen) {
                    questionIndex++
                }

                nextDestinationIndex?.let {
                    screenOrder.getOrNull(it)
                        ?.let { it1 -> navController.navigate(it1) }
                }

                if (buttonText == "Finish") {
                    isButtonEnabled = false
                    buttonText = "Loading..."
                    runBlocking {
                        vmCausal.storeDataC()
                        causal.newContent(vmCausal)

                    }
                }
                //println(userInfoArray.joinToString(separator = ", "))
                //println("$newUser in fun")
            },
            isUse = isButtonEnabled
        )
    }
}
