package com.threegroup.tobedated._signUp

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.threegroup.tobedated._signUp.composables.BackButton
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated._signUp.composables.PromptQuestions
import com.threegroup.tobedated.shareclasses.MyApp
import com.threegroup.tobedated.shareclasses.composables.AlertDialogBox
import com.threegroup.tobedated.shareclasses.composables.ProgressBar

@Composable
fun SignUpNav(signUpActivity: SignUpActivity) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isFirstScreen = currentBackStackEntry?.destination?.route == SignUp.WelcomeScreen.name
    val isLastScreen = currentBackStackEntry?.destination?.route == SignUp.PhotoScreen.name
    var showDialog by remember { mutableStateOf(false) }
    var questionIndex by rememberSaveable { mutableIntStateOf(0) }
    var isButtonEnabled by rememberSaveable { mutableStateOf(false) }
    var noShow by rememberSaveable { mutableStateOf(true) }
    val signUpVM = viewModel { SignUpViewModel(MyApp.x) }
    val screenOrder = listOf(
        SignUp.WelcomeScreen.name,
        SignUp.NameScreen.name,
        SignUp.BirthScreen.name,
        SignUp.PronounScreen.name,
        SignUp.GenderScreen.name,
        SignUp.HieghtScreen.name,
        SignUp.EthnicityScreen.name,
        SignUp.StarScreen.name,
        SignUp.SexOriScreen.name,
        SignUp.SearchScreen.name,
        SignUp.SexScreen.name,
        SignUp.MbtiScreen.name,
        SignUp.OurTestScreen.name,
        SignUp.ChildrenScreen.name,
        SignUp.FamilyScreen.name,
        SignUp.EducationScreen.name,
        SignUp.ReligiousScreen.name,
        SignUp.PoliticsScreen.name,
        SignUp.RelationshipScreen.name,
        SignUp.IntentionsScreen.name,
        SignUp.MeetUpScreen.name,
        SignUp.DrinkScreen.name,
        SignUp.SmokeScreen.name,
        SignUp.WeedScreen.name,
        SignUp.PromptQuestionsScreen.name,
        SignUp.BioScreen.name,
        SignUp.PhotoScreen.name
    )


    val currentDestinationIndex = currentBackStackEntry?.destination?.route?.let { screenOrder.indexOf(it) }
    val nextDestinationIndex = currentDestinationIndex?.plus(1)
    if(noShow){
        BackButton(onClick = {
            if(isFirstScreen){
                showDialog = true
            }else{
                questionIndex--
                isButtonEnabled = false
                navController.popBackStack()
            }
        })
    }



    if (showDialog) {
        AlertDialogBox(
            onDismissRequest = { showDialog = false },
            onConfirmation = { signUpActivity.switchBack() },
            dialogTitle = "Leave Signup",
            dialogText = "Are you sure, all your progress will be loss"
        )
    }

    if(!isFirstScreen) {
        ProgressBar(
            questionIndex = questionIndex,
            totalQuestionCount = 24,
        )
    }

    NavHost(navController = navController, startDestination = SignUp.WelcomeScreen.name,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(durationMillis = 150)) },
        exitTransition = { slideOutHorizontally(targetOffsetX  = { -1000 }, animationSpec = tween(durationMillis = 150)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(durationMillis = 150)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX  = { 1000 }, animationSpec = tween(durationMillis = 150)) }) {
        composable(route = SignUp.WelcomeScreen.name) {
            isButtonEnabled = welcomeScreen()
        }
        composable(route = SignUp.NameScreen.name) {
            isButtonEnabled = nameScreen()
        }
        composable(route = SignUp.BirthScreen.name) {
            isButtonEnabled = birthScreen()
        }
        composable(route = SignUp.PronounScreen.name) {
            isButtonEnabled = pronounScreen()
        }
        composable(route = SignUp.GenderScreen.name) {
            isButtonEnabled = genderScreen()
        }
        composable(route = SignUp.SexOriScreen.name) {
            isButtonEnabled = sexOriScreen()
        }
        composable(route = SignUp.HieghtScreen.name) {
            isButtonEnabled = heightScreen()
        }
        composable(route = SignUp.EthnicityScreen.name) {
            isButtonEnabled = ethnicityScreen()
        }
        composable(route = SignUp.StarScreen.name) {
            isButtonEnabled = starScreen()
        }
        composable(route = SignUp.SearchScreen.name) {
            isButtonEnabled = searchScreen()
        }
        composable(route = SignUp.SexScreen.name) {
            isButtonEnabled = sexScreen()
        }
        composable(route = SignUp.MbtiScreen.name) {
            isButtonEnabled = mbtiScreen(onNavigate = {
                // Code to navigate to the next screen or perform any other action
                questionIndex++
                nextDestinationIndex?.let { nextIndex ->
                    screenOrder.getOrNull(nextIndex)?.let { nextScreen ->
                        navController.navigate(nextScreen)
                    }
                }
            })
        }
        composable(route = SignUp.OurTestScreen.name) {
            isButtonEnabled = ourTestScreen()
        }
        composable(route = SignUp.ChildrenScreen.name) {
            isButtonEnabled = childrenScreen()
        }
        composable(route = SignUp.FamilyScreen.name) {
            isButtonEnabled = familyScreen()
        }
        composable(route = SignUp.EducationScreen.name) {
            isButtonEnabled = educationScreen()
        }
        composable(route = SignUp.SexScreen.name) {
            isButtonEnabled = sexScreen()
        }
        composable(route = SignUp.ReligiousScreen.name) {
            isButtonEnabled = religiousScreen()
        }
        composable(route = SignUp.PoliticsScreen.name) {
            isButtonEnabled = politicsScreen()
        }
        composable(route = SignUp.RelationshipScreen.name) {
            isButtonEnabled = relationshipScreen()
        }
        composable(route = SignUp.IntentionsScreen.name) {
            isButtonEnabled = intentionsScreen()
        }
        composable(route = SignUp.MeetUpScreen.name){
            isButtonEnabled = meetUpScreen()
        }
        composable(route = SignUp.DrinkScreen.name) {
            isButtonEnabled = drinkScreen()
        }
        composable(route = SignUp.SmokeScreen.name) {
            isButtonEnabled = smokeScreen()
        }
        composable(route = SignUp.WeedScreen.name) {
            isButtonEnabled = weedScreen()
        }

        composable(route = SignUp.PromptQuestionsScreen.name) {
            noShow = true
            isButtonEnabled = promptQuestionsScreen(navController, signUpVM)
        }
        composable(route = "PromptQuestions/{index}", arguments = listOf(
            navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val myIndex = backStackEntry.arguments?.getInt("index") ?: 0
            noShow = false
            PromptQuestions(navController, signUpVM, myIndex)
        }
        composable(route = SignUp.BioScreen.name) {
            isButtonEnabled = bioScreen()
        }
        composable(route = SignUp.PhotoScreen.name) {
            isButtonEnabled = photoScreen()
        }

    }
    var buttonText = if (isFirstScreen) "I Agree" else if (isLastScreen) "Finish" else "Enter"
    if(noShow){
        BigButton(
            text = buttonText,
            onClick = {
                if(!isFirstScreen){
                    questionIndex++
                }

                nextDestinationIndex?.let { screenOrder.getOrNull(it)
                    ?.let { it1 -> navController.navigate(it1) } }

                if(buttonText == "Finish"){
                    isButtonEnabled = false
                    buttonText = "Loading..."
                    signUpActivity.finishingUp(signUpVM)
                }
                //println(userInfoArray.joinToString(separator = ", "))
                //println("$newUser in fun")
            },
            isUse = isButtonEnabled
        )
    }

}