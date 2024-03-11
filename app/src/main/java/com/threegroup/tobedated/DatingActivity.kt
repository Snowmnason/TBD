package com.threegroup.tobedated

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.threegroup.tobedated.composables.DatingNav
import com.threegroup.tobedated.composables.InsideMessages
import com.threegroup.tobedated.composables.MessageStart
import com.threegroup.tobedated.composables.TheirMessage
import com.threegroup.tobedated.composables.TopAndBotBars
import com.threegroup.tobedated.composables.UserInfo
import com.threegroup.tobedated.composables.UserMessage
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
                //MessagerScreen()
            }
        }
    }
}
/*
A=Aries         Orange          0xFFf07019
B=Taurus        Brown           0xFF874b2f
C=Capricorn     Yellow          0xFFecab2b
D=Aquarius      Purple          0xFF9a68bf
E=Gemini        Light Green     0xFF6ca169
F=Pisces        Blue            0xFF0e4caf
G=Cancer        Grey            0xFF5c5463
H=Leo           Red             0xFFb9361a
I=Virgo         Dark Green      0xFF345c42
J=Libra         light Blue      0xFF366b8d
K=Scorpio       Blue            0xFF0a434c
L=Sagittarius   Pink            0xFFa0467c

INTJ INTP ENTJ ENTP 834e69
ENFP ENFJ INFP INFJ 617c44
ESFJ ESTJ ISFJ ISTJ 176363
ISTP ISFP ESTP ESFP 71531e

*/
@Composable
fun SearchingScreen(navController: NavHostController) {
    val profileEdit by remember { mutableStateOf("Profile")}
    val mockArray =  profiles.random()
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = profileEdit, //Change if they are editing profile, or go to different compose dunno
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 2,
        settingsButton = { /* Change search filters */ },
        currentScreen = {
            UserInfo(
                mockArray,
                onClickLike = { /*TODO*/ },
                onClickPass= { /*TODO*/ },
                onClickReport = { /*TODO*/ },
                onClickSuggest= { /*TODO*/ },
            )
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
    ProfileScreen,
    ChatsScreen,
    GroupsScreen,
    SomeScreen,
    MessagerScreen,
}