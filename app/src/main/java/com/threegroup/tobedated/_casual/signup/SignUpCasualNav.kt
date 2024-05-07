package com.threegroup.tobedated._casual.signup

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._casual.signup.composables.PromptQuestionsC
import com.threegroup.tobedated._signUp.composables.BackButton
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated.composeables.composables.AlertDialogBox
import com.threegroup.tobedated.composeables.composables.ProgressBar
import kotlinx.coroutines.runBlocking

@Composable
fun SignUpCasualNav(mainNav: NavHostController){
    val vmCasual = viewModel { CasualSignUpViewModel(MyApp.x) }
    vmCasual.setLoggedInUser()

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
            if (isFirstScreen) {
                showDialog = true
            } else {
                questionIndex--
                //isButtonEnabled = false
                navController.popBackStack()
            }
        })
    }
    if (showDialog) {
        AlertDialogBox(
            onDismissRequest = { showDialog = false },
            onConfirmation = { mainNav.navigate("Dating")},//casual.switchActivities("dating")
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
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { 1000 },
                animationSpec = tween(durationMillis = 150)
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -1000 },
                animationSpec = tween(durationMillis = 150)
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -1000 },
                animationSpec = tween(durationMillis = 150)
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { 1000 },
                animationSpec = tween(durationMillis = 150)
            )
        }) {
        composable(route = CasualSign.WelcomeScreenC.name) {
            isButtonEnabled = welcomeScreenC()
        }
        composable(route = CasualSign.LeaningScreen.name) {
            isButtonEnabled = leaningScreen(vmCasual)
        }
        composable(route = CasualSign.LookingForScreen.name) {
            isButtonEnabled = lookingForScreen(vmCasual)
        }
        composable(route = CasualSign.ExperienceScreen.name) {
            isButtonEnabled = experienceScreen(vmCasual)
        }
        composable(route = CasualSign.LocationScreen.name) {
            isButtonEnabled = locationScreen(vmCasual)
        }
        composable(route = CasualSign.CommScreen.name) {
            isButtonEnabled = commScreen(vmCasual)
        }
        composable(route = CasualSign.SexHealthScreen.name) {
            isButtonEnabled = sexHealthScreen(vmCasual)
        }
        composable(route = CasualSign.AfterCareScreen.name) {
            isButtonEnabled = afterCareScreen(vmCasual)
        }
        composable(route = CasualSign.NewBioScreen.name) {
            isButtonEnabled = newBioScreen(vmCasual, onNavigate = {
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
            isButtonEnabled = promptQuestionsScreenC(navController, vmCasual)
        }
        composable(
            route = "PromptQuestions/{index}", arguments = listOf(
                navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            noShow = false
            PromptQuestionsC(navController, vmCasual, myIndex)
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
                        vmCasual.storeDataC()
                        mainNav.navigate("Casual")
                        //TODO BACKSTACK
                    }
                }
                //println(userInfoArray.joinToString(separator = ", "))
                //println("$newUser in fun")
            },
            isUse = isButtonEnabled
        )
    }
}