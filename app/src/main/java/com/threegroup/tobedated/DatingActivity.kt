package com.threegroup.tobedated

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.composables.DatingNav
import com.threegroup.tobedated.composables.DatingScreens.InsideMessages
import com.threegroup.tobedated.composables.DatingScreens.InsideSearchSettings
import com.threegroup.tobedated.composables.DatingScreens.MessageStart
import com.threegroup.tobedated.composables.DatingScreens.TheirMessage
import com.threegroup.tobedated.composables.DatingScreens.TopAndBotBars
import com.threegroup.tobedated.composables.DatingScreens.UserInfo
import com.threegroup.tobedated.composables.DatingScreens.UserMessage
import com.threegroup.tobedated.composables.DatingScreens.ageSlider
import com.threegroup.tobedated.composables.DatingScreens.distanceSlider
import com.threegroup.tobedated.models.profiles
import com.threegroup.tobedated.ui.theme.AppTheme
import kotlin.random.Random

val notifiGroup = Random.nextBoolean()
val notifiChat = Random.nextInt(0, 41) // Generates a random integer between 0 and 40
//val notifiSearching = Random.nextBoolean()
class DatingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                DatingNav()
                //AgeSlider()
            }
        }
    }
}

@Composable
fun SearchingScreen(navController: NavHostController) {
    val profileEdit by remember { mutableStateOf("Profile")}
    val usersArray =  profiles.random()
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = profileEdit, //Change if they are editing profile, or go to different compose dunno
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 2,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        currentScreen = {
            UserInfo(
                usersArray,
                onClickLike = { /*TODO*/ },
                onClickPass= { /*TODO*/ },
                onClickReport = { /*TODO*/ },
                onClickSuggest= { /*TODO*/ },
            )
        }
    )
}
@Composable
fun SearchPreferenceScreen(navController: NavHostController){

    InsideSearchSettings(
        nav = navController,
        searchSettings = {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp)
            ) {
                val preferredRange = ageSlider(
                    preferredMin = 18,
                    preferredMax = 100,
                )
                Spacer(modifier = Modifier.height(14.dp))
                val maxPreferredDistance = distanceSlider(
                    preferredMax = 25)

            }
        }
    )
}
@Composable
fun ProfileScreen(navController: NavHostController){
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        isPhoto = false,
        titleText = "Profile",
        nav = navController,
        selectedItemIndex = 4,
        settingsButton = { /*TODO Edit profile */ },
        currentScreen = {

        }
    )
}
@Composable
fun ChatsScreen(navController: NavHostController){
    //val inChat by rememberSaveable { mutableStateOf(false)}

    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "Messages", //Change based on name
        isPhoto = false,
        nav = navController,
        selectedItemIndex = 1,
        settingsButton = { },
        currentScreen = {
            MessageStart(
                noMatches = false,
                userPhoto = "https://media.vanityfair.com/photos/63765577474812eb37ec70bc/master/w_1600,c_limit/Headshot%20-%20credit%20%E2%80%9CNational%20Geographic%20for%20Disney+%E2%80%9D.jpg", //Need this to accept URI
                userName = "Dom",
                userLastMessage = "LOL hows have you been? \nLOL hows have you been?",
                openChat = {
                    navController.navigate("MessagerScreen")

                }
            )


        }
    )
}
@Composable
fun GroupsScreen(navController: NavHostController){
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        isPhoto = false,
        titleText = "Groups",
        nav = navController,
        selectedItemIndex = 3,
        settingsButton = { },
        currentScreen = {

        }
    )
}
@Composable
fun SomeScreen(navController: NavHostController){
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { },
        currentScreen = {

        }
    )
}
@Composable
fun MessagerScreen(navController: NavHostController){
    //TODO need to make this nested I think
    var message by rememberSaveable { mutableStateOf("") }

    InsideMessages(
        nav = navController,
        titleText = "Dom",
        value = message,
        onValueChange = { message = it},
        sendMessage = {/* TODO Send Message*/ },
        titleButton = {/* TODO Go to Profile from name*/ },
        messages = {
            UserMessage("Oh my god I totally agree")
            TheirMessage(replyMessage = "That's crazy because I don't nerd...",
                userPhoto = "https://media.vanityfair.com/photos/63765577474812eb37ec70bc/master/w_1600,c_limit/Headshot%20-%20credit%20%E2%80%9CNational%20Geographic%20for%20Disney+%E2%80%9D.jpg",
                photoClick = { /* TODO Go to Profile image*/ }
            )

        }
    )
}
enum class Dating {
    SearchingScreen,
    SearchPreferenceScreen,
    ProfileScreen,
    ChatsScreen,
    GroupsScreen,
    SomeScreen,
    MessagerScreen,
}