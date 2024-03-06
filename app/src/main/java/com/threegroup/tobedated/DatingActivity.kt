package com.threegroup.tobedated

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.threegroup.tobedated.composables.TopAndBotBars
import com.threegroup.tobedated.composables.UserInfo
import com.threegroup.tobedated.ui.theme.AppTheme

val notifiGroup = true
val notifiChat = 3
val notifiSearching = false
class DatingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                //PolkaDotCanvas()
                //TypicallyDisplay()
                SearchingScreen()
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
fun SearchingScreen(){
    val profileEdit by remember { mutableStateOf("Profile")}
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        whatScreen = true,
        titleText = profileEdit, //Change if they are editing profile, or go to different compose dunno
        isPhoto = true,
        currentScreen = {
            UserInfo(
                name = "Dom",
                bio = "WORDS WORDS \n WORDS WORDS WORDS",
                age = "24",
                pronouns = "They/Them",
                gender = "Non-Binary",
                sign = "i",
                mbti = "ISTP-T",
                promptQ1 = "Do you even?",
                promptA1 = "No I can't fucking even, I am actually so odd",
                sexOri = "Bisexual",
                relationshipType = "Monogamous",
                promptQ2 = "Do you odd?",
                promptA2 = "No I am so fucking even, I am actually can't odd",
                smokes = "Sometimes",
                drinks = "Socially",
                weeds = "Never",
                promptQ3 = "Do you amalgamate?",
                promptA3 = "No I am even so fucking , I odd actually can't am",
                school = "Undergrad",
                work = "simple A?",
                kids = "Someday",
                exercise = "Sometimes",
                height = "5'11\"",
                politics = "Moderate",
                religion = "Budda"

            )
        }
    )
}
@Composable
fun ProfileScreen(){
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        whatScreen = true,
        isPhoto = true,
        currentScreen = {
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(100) }
            Column(
                Modifier
                    .verticalScroll(state)
                    .fillMaxSize()
            ){

            }
        }
    )
}
@Composable
fun ChatsScreen(){
    val inChat by remember { mutableStateOf(false)}
    val personChat by remember { mutableStateOf("Message")}
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        whatScreen = inChat, //Change when in
        titleText = personChat, //Change based on name
        isPhoto = false,
        currentScreen = {
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(100) }
            Column(
                Modifier
                    .verticalScroll(state)
                    .fillMaxSize()
            ){

            }
        }
    )
}
@Composable
fun Groups(){
    val groupName by remember { mutableStateOf("Groups")}
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        whatScreen = true,
        titleText = groupName,
        isPhoto = true,
        currentScreen = {
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(100) }
            Column(
                Modifier
                    .verticalScroll(state)
                    .fillMaxSize()
            ){

            }
        }
    )
}
@Composable
fun SomeScreen(){
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        whatScreen = true,
        titleText = "To Be Dated",
        isPhoto = true,
        currentScreen = {
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(100) }
            Column(
                Modifier
                    .verticalScroll(state)
                    .fillMaxSize()
            ){

            }
        }
    )
}