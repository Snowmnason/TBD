package com.threegroup.tobedated.composables

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
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
import com.threegroup.tobedated.DatingViewModel
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated.activities.ChangePreference
import com.threegroup.tobedated.activities.ChatsScreen
import com.threegroup.tobedated.activities.Dating
import com.threegroup.tobedated.activities.DatingActivity
import com.threegroup.tobedated.activities.EditProfileScreen
import com.threegroup.tobedated.activities.GroupsScreen
import com.threegroup.tobedated.activities.Login
import com.threegroup.tobedated.activities.LoginActivity
import com.threegroup.tobedated.activities.LoginMainScreen
import com.threegroup.tobedated.activities.LoginScreen
import com.threegroup.tobedated.activities.MessagerScreen
import com.threegroup.tobedated.activities.ProfileScreen
import com.threegroup.tobedated.activities.SearchPreferenceScreen
import com.threegroup.tobedated.activities.SearchingScreen
import com.threegroup.tobedated.activities.SignUp
import com.threegroup.tobedated.activities.SignUpActivity
import com.threegroup.tobedated.activities.SomeScreen
import com.threegroup.tobedated.activities.VerificationCodeView
import com.threegroup.tobedated.activities.bioScreen
import com.threegroup.tobedated.activities.birthScreen
import com.threegroup.tobedated.activities.childrenScreen
import com.threegroup.tobedated.activities.drinkScreen
import com.threegroup.tobedated.activities.educationScreen
import com.threegroup.tobedated.activities.ethnicityScreen
import com.threegroup.tobedated.activities.familyScreen
import com.threegroup.tobedated.activities.genderScreen
import com.threegroup.tobedated.activities.heightScreen
import com.threegroup.tobedated.activities.intentionsScreen
import com.threegroup.tobedated.activities.mbtiScreen
import com.threegroup.tobedated.activities.nameScreen
import com.threegroup.tobedated.activities.newUser
import com.threegroup.tobedated.activities.ourTestScreen
import com.threegroup.tobedated.activities.photoScreen
import com.threegroup.tobedated.activities.politicsScreen
import com.threegroup.tobedated.activities.pronounScreen
import com.threegroup.tobedated.activities.relationshipScreen
import com.threegroup.tobedated.activities.religiousScreen
import com.threegroup.tobedated.activities.searchScreen
import com.threegroup.tobedated.activities.sexOriScreen
import com.threegroup.tobedated.activities.sexScreen
import com.threegroup.tobedated.activities.smokeScreen
import com.threegroup.tobedated.activities.starScreen
import com.threegroup.tobedated.activities.weedScreen
import com.threegroup.tobedated.activities.welcomeScreen
import com.threegroup.tobedated.composables.signUp.BigButton
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

@Composable
fun LoginNav(loginActivity: LoginActivity) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login.LoginMainScreen.name,
        enterTransition = { EnterTransition.None }, exitTransition =   { ExitTransition.None},
        popEnterTransition = { EnterTransition.None }, popExitTransition = { ExitTransition.None}) {
        composable(route = Login.LoginMainScreen.name) {
            LoginMainScreen(navController)
        }
        composable(route = Login.LoginScreen.name) {
            LoginScreen(navController, loginActivity)
        }
        composable(
            route = "VerificationCodeView/{number}",
            arguments = listOf(navArgument("number") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("number") ?: ""
            VerificationCodeView(navController, phoneNumber, loginActivity)
        }
    }
}
@Composable
fun SignUpNav(signUpActivity: SignUpActivity) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isFirstScreen = currentBackStackEntry?.destination?.route == SignUp.WelcomeScreen.name
    val isLastScreen = currentBackStackEntry?.destination?.route == SignUp.PhotoScreen.name
    var showDialog by remember { mutableStateOf(false) }
    var questionIndex by rememberSaveable { mutableIntStateOf(0) }
    var isButtonEnabled by rememberSaveable { mutableStateOf(false) }
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
        SignUp.DrinkScreen.name,
        SignUp.SmokeScreen.name,
        SignUp.WeedScreen.name,
        SignUp.BioScreen.name,
        SignUp.PhotoScreen.name
    )

    val currentDestinationIndex = currentBackStackEntry?.destination?.route?.let { screenOrder.indexOf(it) }
    val nextDestinationIndex = currentDestinationIndex?.plus(1)

    BackButton(onClick = {
        if(isFirstScreen){
            showDialog = true
        }else{
            questionIndex--
            navController.popBackStack()
        }
    })
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
            isButtonEnabled = mbtiScreen()
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
        composable(route = SignUp.DrinkScreen.name) {
            isButtonEnabled = drinkScreen()
        }
        composable(route = SignUp.SmokeScreen.name) {
            isButtonEnabled = smokeScreen()
        }
        composable(route = SignUp.WeedScreen.name) {
            isButtonEnabled = weedScreen()
        }
        composable(route = SignUp.BioScreen.name) {
            isButtonEnabled = bioScreen()
        }
        composable(route = SignUp.PhotoScreen.name) {
            isButtonEnabled = photoScreen()
        }

    }
    val buttonText = if (isFirstScreen) "I Agree" else if (isLastScreen) "Finish" else "Enter"

    BigButton(
        text = buttonText,
        onClick = {
            if(!isFirstScreen){
                questionIndex++
            }

            nextDestinationIndex?.let { screenOrder.getOrNull(it)
                ?.let { it1 -> navController.navigate(it1) } }

            if(buttonText == "Finish"){
                runBlocking {
                    val hold = async { signUpActivity.uploadImage() }
                    hold.await()
                    signUpActivity.storeData()
                    signUpActivity.goNextScreen()
                }
            }
            //println(userInfoArray.joinToString(separator = ", "))
            println("$newUser in fun")
        },
        isUse = isButtonEnabled
    )
}

@Composable
fun DatingNav(dating:DatingActivity) {
    val navController = rememberNavController()
    val viewModelDating = viewModel { DatingViewModel(MyApp.x) }

    NavHost(navController = navController, startDestination = Dating.SearchingScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }) {
        composable(route = Dating.SearchingScreen.name) {
            SearchingScreen(navController, viewModelDating)
        }
        composable(route = Dating.ProfileScreen.name) {
            ProfileScreen(navController, viewModelDating)
        }
        composable(route = Dating.EditProfileScreen.name) {
            EditProfileScreen(navController, dating)
        }
        composable(route = Dating.SearchPreferenceScreen.name) {
            SearchPreferenceScreen(navController, viewModelDating)
        }
        composable(route = Dating.ChatsScreen.name) {
            ChatsScreen(navController)
        }
        composable(route = Dating.GroupsScreen.name) {
            GroupsScreen(navController)
        }
        composable(route = Dating.SomeScreen.name) {
            SomeScreen(navController)
        }
        composable(route = Dating.MessagerScreen.name) {
            MessagerScreen(navController)
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
    }

}