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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.threegroup.tobedated.BioScreen
import com.threegroup.tobedated.BirthScreen
import com.threegroup.tobedated.ChatsScreen
import com.threegroup.tobedated.Dating
import com.threegroup.tobedated.EthnicityScreen
import com.threegroup.tobedated.GenderScreen
import com.threegroup.tobedated.GroupsScreen
import com.threegroup.tobedated.HeightScreen
import com.threegroup.tobedated.MbtiScreen
import com.threegroup.tobedated.MessagerScreen
import com.threegroup.tobedated.NameScreen
import com.threegroup.tobedated.OurTestScreen
import com.threegroup.tobedated.PhotoScreen
import com.threegroup.tobedated.ProfileScreen
import com.threegroup.tobedated.PronounScreen
import com.threegroup.tobedated.SearchScreen
import com.threegroup.tobedated.SearchingScreen
import com.threegroup.tobedated.SexOriScreen
import com.threegroup.tobedated.SexScreen
import com.threegroup.tobedated.SignUp
import com.threegroup.tobedated.SignUpActivity
import com.threegroup.tobedated.SomeScreen
import com.threegroup.tobedated.WelcomeScreen
import kotlinx.coroutines.runBlocking

@Composable
fun SignUpNav(signUpActivity: SignUpActivity, userInfoArray: Array<String>, indexArray:Array<Int>) {
    val photoQuestion = 12
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isFirstScreen = currentBackStackEntry?.destination?.route == SignUp.WelcomeScreen.name
    val isLastScreen = currentBackStackEntry?.destination?.route == SignUp.PhotoScreen.name
    var showDialog by remember { mutableStateOf(false) }
    var questionIndex by rememberSaveable { mutableIntStateOf(0) }
    val onNameChanged: (String, Int) -> Unit = { newAnswer, index -> userInfoArray[index] = newAnswer }
    val onPhotoChanged: (String, String, String, String) -> Unit = { newAnswer1, newAnswer2, newAnswer3, newAnswer4 ->
        userInfoArray[photoQuestion] = newAnswer1
        userInfoArray[photoQuestion+1] = newAnswer2
        userInfoArray[photoQuestion+2] = newAnswer3
        userInfoArray[photoQuestion+3] = newAnswer4}
    val onIndexChange: (Int, Int) -> Unit = { newAnswer, index -> indexArray[index] = newAnswer }
    var isButtonEnabled by rememberSaveable { mutableStateOf(false) }
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
            totalQuestionCount = 10,
        )
    }
    val updateButtonState: (String) -> Unit = { input ->
        isButtonEnabled = checkButtonState(input)
    }
    val updateButtonStateBio: (String) -> Unit = { input ->
        isButtonEnabled = checkButtonStateBio(input)
    }
    NavHost(navController = navController, startDestination = SignUp.WelcomeScreen.name,
        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(durationMillis = 150)) },
        exitTransition = { slideOutHorizontally(targetOffsetX  = { -1000 }, animationSpec = tween(durationMillis = 150)) },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(durationMillis = 150)) },
        popExitTransition = { slideOutHorizontally(targetOffsetX  = { 1000 }, animationSpec = tween(durationMillis = 150)) }) {
        composable(route = SignUp.WelcomeScreen.name) {
            WelcomeScreen()
        }
        composable(route = SignUp.NameScreen.name) {
            NameScreen(userInfo = userInfoArray, onAnswerChanged = onNameChanged, updateButtonState)
        }
        composable(route = SignUp.BirthScreen.name) {
            BirthScreen(userInfo = userInfoArray, onAnswerChanged = onNameChanged, updateButtonState)
        }
        composable(route = SignUp.PronounScreen.name) {
            PronounScreen(userInfo = userInfoArray, index= indexArray, onAnswerChanged = onNameChanged, onIndexChange = onIndexChange, updateButtonState)
        }
        composable(route = SignUp.GenderScreen.name) {
            GenderScreen(userInfo = userInfoArray, index= indexArray, onAnswerChanged = onNameChanged, onIndexChange = onIndexChange, updateButtonState)
        }
        composable(route = SignUp.SexOriScreen.name) {
            SexOriScreen(userInfo = userInfoArray, index= indexArray, onAnswerChanged = onNameChanged, onIndexChange = onIndexChange, updateButtonState)
        }
        composable(route = SignUp.HieghtScreen.name) {
            HeightScreen(userInfo = userInfoArray, onAnswerChanged = onNameChanged, updateButtonState)
        }
        composable(route = SignUp.EthnicityScreen.name) {
            EthnicityScreen(userInfo = userInfoArray, index= indexArray, onAnswerChanged = onNameChanged, onIndexChange = onIndexChange, updateButtonState)
        }
        composable(route = SignUp.SearchScreen.name) {
            SearchScreen(userInfo = userInfoArray, index= indexArray, onAnswerChanged = onNameChanged, onIndexChange = onIndexChange, updateButtonState)
        }
        composable(route = SignUp.SexScreen.name) {
            SexScreen(userInfo = userInfoArray, index= indexArray, onAnswerChanged = onNameChanged, onIndexChange = onIndexChange, updateButtonState)
        }
        composable(route = SignUp.MbtiScreen.name) {
            MbtiScreen(userInfo = userInfoArray, onAnswerChanged = onNameChanged, updateButtonState)
        }
        composable(route = SignUp.OurTestScreen.name) {
            OurTestScreen(userInfo = userInfoArray, onAnswerChanged = onNameChanged, updateButtonState)
        }
        composable(route = SignUp.BioScreen.name) {
            BioScreen(userInfo = userInfoArray, onAnswerChanged = onNameChanged, updateButtonStateBio)
        }
        composable(route = SignUp.PhotoScreen.name) {
            PhotoScreen(userInfo = userInfoArray, onAnswerChanged = onPhotoChanged, updateButtonState)
        }

    }
    var buttonText = "I Agree"
    if(!isFirstScreen){
        buttonText = "Enter"
    }
    if(isLastScreen){
        buttonText = "Finish"
    }



    if(isFirstScreen){
        isButtonEnabled = true
    }
    BigButton(
        text = buttonText,
        onClick = {
            if(!isFirstScreen){
                questionIndex++
            }
            val nextDestination = when (navController.currentDestination?.route) {
                SignUp.WelcomeScreen.name -> SignUp.NameScreen.name
                SignUp.NameScreen.name -> SignUp.BirthScreen.name
                SignUp.BirthScreen.name -> SignUp.PronounScreen.name
                SignUp.PronounScreen.name -> SignUp.GenderScreen.name
                SignUp.GenderScreen.name -> SignUp.HieghtScreen.name
                SignUp.HieghtScreen.name -> SignUp.EthnicityScreen.name
                SignUp.EthnicityScreen.name -> SignUp.SexOriScreen.name

                SignUp.SexOriScreen.name -> SignUp.SearchScreen.name
                SignUp.SearchScreen.name -> SignUp.SexScreen.name
                SignUp.SexScreen.name -> SignUp.MbtiScreen.name
                SignUp.MbtiScreen.name -> SignUp.OurTestScreen.name
                SignUp.OurTestScreen.name -> SignUp.BioScreen.name
                SignUp.BioScreen.name -> SignUp.PhotoScreen.name
                else -> null // Handle unknown destinations or end of flow
            }
            if(buttonText == "Finish"){
                runBlocking {
                    signUpActivity.storeData()
                    signUpActivity.goNextScreen()
                }
            }
            println(userInfoArray.joinToString(separator = ", "))
            nextDestination?.let { navController.navigate(it) }
        },
        isUse = isButtonEnabled
    )
}
fun checkButtonState(index:String): Boolean {
    return index != ""
}
fun checkButtonStateBio(index:String): Boolean {
    return index.length >= 15
}

@Composable
fun DatingNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Dating.SearchingScreen.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }){
        composable(route = Dating.SearchingScreen.name) {
            SearchingScreen(navController)
        }
        composable(route = Dating.ProfileScreen.name) {
            ProfileScreen(navController)
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
    }

}