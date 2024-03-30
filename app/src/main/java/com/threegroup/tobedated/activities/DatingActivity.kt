package com.threegroup.tobedated.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.composables.AlertDialogBox
import com.threegroup.tobedated.composables.DatingNav
import com.threegroup.tobedated.composables.GenericTitleSmall
import com.threegroup.tobedated.composables.datingScreens.AgeSlider
import com.threegroup.tobedated.composables.datingScreens.ChangePreferenceScreen
import com.threegroup.tobedated.composables.datingScreens.ChangeSeekingScreen
import com.threegroup.tobedated.composables.datingScreens.Comeback
import com.threegroup.tobedated.composables.datingScreens.CurrentUserInfo
import com.threegroup.tobedated.composables.datingScreens.DistanceSlider
import com.threegroup.tobedated.composables.datingScreens.EditProfile
import com.threegroup.tobedated.composables.datingScreens.InsideMessages
import com.threegroup.tobedated.composables.datingScreens.InsideProfileSettings
import com.threegroup.tobedated.composables.datingScreens.InsideSearchSettings
import com.threegroup.tobedated.composables.datingScreens.MessageStart
import com.threegroup.tobedated.composables.datingScreens.OtherPreferences
import com.threegroup.tobedated.composables.datingScreens.SeekingBox
import com.threegroup.tobedated.composables.datingScreens.TheirMessage
import com.threegroup.tobedated.composables.datingScreens.TopAndBotBars
import com.threegroup.tobedated.composables.datingScreens.UserInfo
import com.threegroup.tobedated.composables.datingScreens.UserMessage
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.viewModels.DatingViewModel
import kotlin.random.Random


val notifiGroup = Random.nextBoolean()
val notifiChat = Random.nextInt(0, 41) // Generates a random integer between 0 and 40
//val notifiSearching = Random.nextBoolean()

class DatingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = intent.getStringExtra("token").toString()

        setContent {
            AppTheme {
                DatingNav(this@DatingActivity, token)
            }
        }
    }
    fun clearUserToken() {
        val sharedPreferences = getSharedPreferences("firebase_user", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("firebase_user_token")
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun SearchingScreen(navController: NavHostController, vmDating: DatingViewModel) {
    var isNext by rememberSaveable { mutableStateOf(true) }
    var showReport by rememberSaveable { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }

    var newPotential:UserModel
    var currentProfileIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentPotential = remember { mutableStateOf<UserModel?>(null) }
    LaunchedEffect(Unit) {
        vmDating.getPotentialUserData {
            //currentPotential.value = profiles.random()
            currentPotential.value = vmDating.getNextPotential(currentProfileIndex)
            isLoading.value = false // Set loading state to false after data is fetched
        }
    }


    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 2,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        currentScreen = {
            currentPotential.value?.let { user ->
                if (isNext || isLoading.value) {
                    UserInfo(
                        user,//usersArray[currentProfileIndex]
                        onClickLike = {
                            currentProfileIndex++
                            newPotential = vmDating.likedCurrentPotential(currentProfileIndex, currentPotential.value!!)
                            if(newPotential.name.isNotEmpty()){
                                currentPotential.value = newPotential
                            }else{
                                isNext = false
                            }

                                      /*nextProfile()TODO NEED TO SCROLL TO TOP WHEN CLICKED*/ },
                        onClickPass = {
                            currentProfileIndex++
                            newPotential = vmDating.passedCurrentPotential(currentProfileIndex, currentPotential.value!!)
                            if(newPotential.name.isNotEmpty()){
                                currentPotential.value = newPotential
                            }else{
                                isNext = false
                            }
                                      /*nextProfile()TODO NEED TO SCROLL TO TOP WHEN CLICKED*/ },
                        onClickReport = { showReport = true },
                        onClickSuggest = { /*TODO NEED TO SCROLL TO TOP WHEN CLICKED*/ },
                    )
                } else {
                    Comeback()
                }
            }
        },

    )
    if(showReport){
        AlertDialogBox(
            dialogTitle = "Report!",
            onDismissRequest = { showReport = false },
            dialogText = "This account will be looked into and they will not be able to view your profile",
            onConfirmation = { showReport = false
                currentProfileIndex++
                newPotential = vmDating.reportedCurrentPotential(currentProfileIndex, currentPotential.value!!)
                if(newPotential.name.isNotEmpty()){
                    currentPotential.value = newPotential
                }else{
                    isNext = false
                }}
        )
    }
}



@Composable
fun SearchPreferenceScreen(navController: NavHostController, vmDating: DatingViewModel){
    val currentUser = vmDating.getUser()
    val searchPref by remember { mutableStateOf( currentUser.userPref) }

    val userPref= listOf(searchPref.gender, searchPref.zodiac, searchPref.sexualOri, searchPref.mbti,
        searchPref.children, searchPref.familyPlans, searchPref.education, searchPref.religion, searchPref.politicalViews,
        searchPref.relationshipType, searchPref.intentions, searchPref.drink, searchPref.smoke, searchPref.weed)

    val pref = listOf("Gender", "Zodiac Sign", "Sexual Orientation", "Mbti", "Children", "Family Plans",
        "Education", "Religion", "Political Views", "Relationship Type","Intentions", "Drink", "Smokes", "Weed")
    InsideSearchSettings(
        nav = navController,
        searchSettings = {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AgeSlider(preferredMin = currentUser.userPref.ageRange.min, preferredMax = currentUser.userPref.ageRange.max, vmDating = vmDating, currentUser = currentUser)
                Spacer(modifier = Modifier.height(14.dp))
                DistanceSlider(preferredMax = currentUser.userPref.maxDistance, vmDating = vmDating, currentUser = currentUser)
                Spacer(modifier = Modifier.height(14.dp))
                SeekingBox(desiredSex = currentUser.seeking, navController )
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(Modifier.fillMaxWidth(), color = AppTheme.colorScheme.onBackground, thickness = 2.dp)
                Spacer(modifier = Modifier.height(6.dp))
                GenericTitleSmall(text = "Premium Settings")
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(Modifier.fillMaxWidth(), color = AppTheme.colorScheme.onBackground, thickness = 2.dp)
                Spacer(modifier = Modifier.height(14.dp))
                for (i in pref.indices){
                    OtherPreferences(title = pref[i], navController = navController, searchPref = userPref[i], clickable = true, index = i)
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    )
}
@Composable
fun ChangePreference(navController: NavHostController, title:String, index:Int, vmDating: DatingViewModel){
    if(index == 69420){
        ChangeSeekingScreen(navController,
            title = title,
            vmDating = vmDating,
            index = index,
        )
    }else{
        ChangePreferenceScreen(navController,
            title = title,
            vmDating = vmDating,
            index = index,
        )
    }
}
@Composable
fun ProfileScreen(navController: NavHostController, vmDating: DatingViewModel){
    val currentUser = vmDating.getUser()
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (currentUser.name.isNotEmpty()) {
                isLoading.value = false
        }
    }
    TopAndBotBars(
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        isPhoto = false,
        titleText = "Profile",
        nav = navController,
        selectedItemIndex = 4,
        settingsButton = { navController.navigate("EditProfileScreen") },
        currentScreen = {
            CurrentUserInfo(
                currentUser
            )
        }
    )
}
@Composable
fun EditProfileScreen(navController: NavHostController, dating:DatingActivity){
    InsideProfileSettings(
        nav = navController,
        editProfile = {
            EditProfile(dating)
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

enum class Dating {
    SearchingScreen,
    SearchPreferenceScreen,
    ProfileScreen,
    EditProfileScreen,
    ChatsScreen,
    GroupsScreen,
    SomeScreen,
    MessagerScreen,
}