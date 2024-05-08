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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.threegroup.tobedated.MainActivity
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._signUp.composables.BackButton
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated._signUp.composables.questions.PromptQuestions
import com.threegroup.tobedated._signUp.composes.bioScreen
import com.threegroup.tobedated._signUp.composes.birthScreen
import com.threegroup.tobedated._signUp.composes.childrenScreen
import com.threegroup.tobedated._signUp.composes.drinkScreen
import com.threegroup.tobedated._signUp.composes.educationScreen
import com.threegroup.tobedated._signUp.composes.ethnicityScreen
import com.threegroup.tobedated._signUp.composes.familyScreen
import com.threegroup.tobedated._signUp.composes.genderScreen
import com.threegroup.tobedated._signUp.composes.heightScreen
import com.threegroup.tobedated._signUp.composes.intentionsScreen
import com.threegroup.tobedated._signUp.composes.mbtiScreen
import com.threegroup.tobedated._signUp.composes.meetUpScreen
import com.threegroup.tobedated._signUp.composes.nameScreen
import com.threegroup.tobedated._signUp.composes.ourTestScreen
import com.threegroup.tobedated._signUp.composes.photoScreen
import com.threegroup.tobedated._signUp.composes.politicsScreen
import com.threegroup.tobedated._signUp.composes.promptQuestionsScreen
import com.threegroup.tobedated._signUp.composes.pronounScreen
import com.threegroup.tobedated._signUp.composes.relationshipScreen
import com.threegroup.tobedated._signUp.composes.religiousScreen
import com.threegroup.tobedated._signUp.composes.searchScreen
import com.threegroup.tobedated._signUp.composes.sexOriScreen
import com.threegroup.tobedated._signUp.composes.sexScreen
import com.threegroup.tobedated._signUp.composes.smokeScreen
import com.threegroup.tobedated._signUp.composes.starScreen
import com.threegroup.tobedated._signUp.composes.weedScreen
import com.threegroup.tobedated._signUp.composes.welcomeScreen
import com.threegroup.tobedated.composeables.composables.AlertDialogBox
import com.threegroup.tobedated.composeables.composables.ProgressBar

@Composable
fun SignUpNav(mainActivity: MainActivity, location: String, number: String, mainNav: NavHostController) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isFirstScreen = currentBackStackEntry?.destination?.route == SignUp.WelcomeScreen.name
    val isLastScreen = currentBackStackEntry?.destination?.route == SignUp.PhotoScreen.name
    var showDialog by remember { mutableStateOf(false) }
    var showFinish by remember { mutableStateOf(false) }
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
    signUpVM.setUser("location", location)
    signUpVM.setUser("number", number)
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
            onConfirmation = { mainNav.navigate("Login"){
                popUpTo("SignUp/$location/$number") {
                    inclusive = true
                    saveState = false
                }
            } },
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
            isButtonEnabled = nameScreen(signUpVM)
        }
        composable(route = SignUp.BirthScreen.name) {
            isButtonEnabled = birthScreen(signUpVM)
        }
        composable(route = SignUp.PronounScreen.name) {
            isButtonEnabled = pronounScreen(signUpVM)
        }
        composable(route = SignUp.GenderScreen.name) {
            isButtonEnabled = genderScreen(signUpVM)
        }
        composable(route = SignUp.SexOriScreen.name) {
            isButtonEnabled = sexOriScreen(signUpVM)
        }
        composable(route = SignUp.HieghtScreen.name) {
            isButtonEnabled = heightScreen(signUpVM)
        }
        composable(route = SignUp.EthnicityScreen.name) {
            isButtonEnabled = ethnicityScreen(signUpVM)
        }
        composable(route = SignUp.StarScreen.name) {
            isButtonEnabled = starScreen(signUpVM)
        }
        composable(route = SignUp.SearchScreen.name) {
            isButtonEnabled = searchScreen(signUpVM)
        }
        composable(route = SignUp.SexScreen.name) {
            isButtonEnabled = sexScreen(signUpVM)
        }
        composable(route = SignUp.MbtiScreen.name) {
            isButtonEnabled = mbtiScreen(signUpVM, onNavigate = {
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
            isButtonEnabled = ourTestScreen(signUpVM)
        }
        composable(route = SignUp.ChildrenScreen.name) {
            isButtonEnabled = childrenScreen(signUpVM)
        }
        composable(route = SignUp.FamilyScreen.name) {
            isButtonEnabled = familyScreen(signUpVM)
        }
        composable(route = SignUp.EducationScreen.name) {
            isButtonEnabled = educationScreen(signUpVM)
        }
        composable(route = SignUp.SexScreen.name) {
            isButtonEnabled = sexScreen(signUpVM)
        }
        composable(route = SignUp.ReligiousScreen.name) {
            isButtonEnabled = religiousScreen(signUpVM)
        }
        composable(route = SignUp.PoliticsScreen.name) {
            isButtonEnabled = politicsScreen(signUpVM)
        }
        composable(route = SignUp.RelationshipScreen.name) {
            isButtonEnabled = relationshipScreen(signUpVM)
        }
        composable(route = SignUp.IntentionsScreen.name) {
            isButtonEnabled = intentionsScreen(signUpVM)
        }
        composable(route = SignUp.MeetUpScreen.name){
            isButtonEnabled = meetUpScreen(signUpVM)
        }
        composable(route = SignUp.DrinkScreen.name) {
            isButtonEnabled = drinkScreen(signUpVM)
        }
        composable(route = SignUp.SmokeScreen.name) {
            isButtonEnabled = smokeScreen(signUpVM)
        }
        composable(route = SignUp.WeedScreen.name) {
            isButtonEnabled = weedScreen(signUpVM)
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
            isButtonEnabled = bioScreen(signUpVM)
        }
        composable(route = SignUp.PhotoScreen.name) {
            isButtonEnabled = photoScreen(signUpVM)
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
                    signUpVM.finishingUp(signUpVM, mainActivity, location, mainNav){
                        value -> showFinish = value
                    }
                }
                //println(userInfoArray.joinToString(separator = ", "))
                //println("$newUser in fun")
            },
            isUse = isButtonEnabled
        )
    }
    if (showFinish) {
        AlertDialogBox(
            onDismissRequest = { showFinish = false },
            onConfirmation = { mainNav.navigate("Dating") {
                popUpTo("SignUp/$location/$number") {
                    inclusive = true
                    saveState = false
                }
            } },
            dialogTitle = "You're all done!",
            dialogText = "Finish up to start your new connection"
        )
    }

}

enum class SignUp {
    WelcomeScreen,
    NameScreen,//
    BirthScreen,//
    PronounScreen,//
    GenderScreen,//
    SexOriScreen,//
    HieghtScreen,//
    EthnicityScreen,//
    StarScreen,//
    SearchScreen,////
    SexScreen,////
    MbtiScreen,//
    OurTestScreen,////
    ChildrenScreen,//
    FamilyScreen,//
    EducationScreen,//
    ReligiousScreen,//
    PoliticsScreen,//
    RelationshipScreen,//
    IntentionsScreen,//
    MeetUpScreen,
    DrinkScreen,//
    SmokeScreen,//
    WeedScreen,//
    BioScreen,//
    PromptQuestionsScreen,
    PhotoScreen,
}