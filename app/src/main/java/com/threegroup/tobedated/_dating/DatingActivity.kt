package com.threegroup.tobedated._dating

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.threegroup.tobedated.MessageViewModel
import com.threegroup.tobedated._causal.CausalActivity
import com.threegroup.tobedated._dating.composables.AgeSlider
import com.threegroup.tobedated._dating.composables.ChangePreferenceScreen
import com.threegroup.tobedated._dating.composables.ChangeProfile
import com.threegroup.tobedated._dating.composables.ChangeSeekingScreen
import com.threegroup.tobedated._dating.composables.DistanceSlider
import com.threegroup.tobedated._dating.composables.EditProfile
import com.threegroup.tobedated._dating.composables.InsideMatchedProfile
import com.threegroup.tobedated._dating.composables.InsideMessages
import com.threegroup.tobedated._dating.composables.InsideProfileSettings
import com.threegroup.tobedated._dating.composables.InsideSearchSettings
import com.threegroup.tobedated._dating.composables.LogOut
import com.threegroup.tobedated._dating.composables.MessageScreen
import com.threegroup.tobedated._dating.composables.MessageStart
import com.threegroup.tobedated._dating.composables.OtherPreferences
import com.threegroup.tobedated._dating.composables.SearchingButtons
import com.threegroup.tobedated._dating.composables.SeekingBox
import com.threegroup.tobedated._dating.composables.SimpleBox
import com.threegroup.tobedated._dating.composables.TopAndBotBarsDating
import com.threegroup.tobedated._dating.composables.UserInfo
import com.threegroup.tobedated._friends.FriendsActivity
import com.threegroup.tobedated._login.LoginActivity
import com.threegroup.tobedated.shareclasses.MyApp
import com.threegroup.tobedated.shareclasses.calcDistance
import com.threegroup.tobedated.shareclasses.composables.AlertDialogBox
import com.threegroup.tobedated.shareclasses.composables.Comeback
import com.threegroup.tobedated.shareclasses.composables.GenericTitleText
import com.threegroup.tobedated.shareclasses.composables.OutLinedButton
import com.threegroup.tobedated.shareclasses.models.UserModel
import com.threegroup.tobedated.shareclasses.storeImageAttempt
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

//val notifiSearching = Random.nextBoolean()

val notifiGroup = Random.nextBoolean()
val notifiChat = Random.nextInt(0, 41) // Generates a random integer between 0 and 40

class DatingActivity : ComponentActivity() {
    private lateinit var token :String
    private lateinit var location :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        token = intent.getStringExtra("token").toString()
        location = intent.getStringExtra("location").toString()
        if(location.isEmpty()){
            location = "/"
        }
        val sharedPreference = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("activityToken", "dating")
        editor.apply()
        setContent {
            AppTheme {
                DatingNav(this@DatingActivity, token, location)
            }
        }
    }
    fun uploadPhotos(newImage: String, imageNumber: Int, imageName: String, callback: (String) -> Unit) {
        lifecycleScope.launch {
            val result = storeImageAttempt(newImage, contentResolver, imageNumber, imageName)
            callback(result)
        }
    }
    fun clearUserToken() {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("user_login")
        editor.apply()

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun switchActivities(switchActivity:String){
        val intent = when (switchActivity) {
            "dating" -> {
                Intent(this, DatingActivity::class.java)
            }

            "causal" -> {
                Intent(this, CausalActivity::class.java)
            }

            "friends" -> {
                Intent(this, FriendsActivity::class.java)
            }
            else -> {
                Intent(this, DatingActivity::class.java)
            }
        }
        intent.putExtra("token", token)
        intent.putExtra("location", location)
        startActivity(intent)
        finish()
    }
}

/*
Start of Seeking Screen
 */
@Composable
fun SearchingScreen(vmDating: DatingViewModel, dating: DatingActivity, navController: NavHostController) {
    var isNext by rememberSaveable { mutableStateOf(true) }
    var showReport by rememberSaveable { mutableStateOf(false) }
    var currentProfileIndex by rememberSaveable { mutableIntStateOf(0) } ///MIGHT CHANGE THIS
    val currentPotential = remember { mutableStateOf<UserModel?>(null) }
    val state = rememberScrollState()
    LaunchedEffect(Unit) {
        //TODO This checks to see if the list is empty or not, This NEEDs to be avilialbe some hows
        if(vmDating.getNextPotential(currentProfileIndex) != null) {
            currentPotential.value = vmDating.getNextPotential(currentProfileIndex)//MIGHT CHANGE THIS
        }else{
            isNext = false//This is important, if there are no users this shows a blank screen and not crash
        }
        state.scrollTo(0) // After resetting, reset the state variable to false
    }

    ///TODO THIS DOES THE SAME CHECK AS ABOVE to see if there is an avilibe user to prevent crashes
    fun nextProfile(newPotential: UserModel?){
        if(newPotential != null){
            currentPotential.value = newPotential///MIGHT change this
        }else{
            isNext = false//This is important, if there are no users this shows a blank screen and not crash
        }
    }

    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 2,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        state = state,
        currentScreen = {
            if (isNext) {
                currentPotential.value?.let { user ->
                    var location  = "x miles"
                    if(user.location != "" && vmDating.getUser().location != ""){
                        location = calcDistance(user.location, vmDating.getUser().location)
                    }
                    UserInfo(
                        user = user,//usersArray[currentProfileIndex]
                        location = location,
                        bottomButtons = {
                            SearchingButtons(
                                onClickLike = {
                                    currentProfileIndex++
                                    nextProfile(vmDating.likedCurrentPotential(currentProfileIndex, currentPotential.value!!))
                                    /*TODO Add an animation or something*/
                                },
                                onClickPass = {
                                    currentProfileIndex++//THIS SHIT CAN GO
                                    nextProfile(vmDating.passedCurrentPotential(currentProfileIndex, currentPotential.value!!))
                                    /*TODO Add an animation or something*/
                                },
                                onClickReport = { showReport = true /*TODO Add an animation or something*/},
                                onClickSuggest = { /*TODO Add an animation or something*/  },
                            )
                        },
                    )
                }
            } else {
                Comeback(text = "Come Back when theres more people to see =0)")
            }
        })
    if(showReport){
        AlertDialogBox(
            dialogTitle = "Report!",
            onDismissRequest = { showReport = false },
            dialogText = "This account will be looked into and they will not be able to view your profile",
            onConfirmation = { showReport = false
                currentProfileIndex++
                nextProfile(vmDating.reportedCurrentPotential(currentProfileIndex, currentPotential.value!!))
            }
        )
    }

}

@Composable
fun SearchPreferenceScreen(navController: NavHostController, vmDating: DatingViewModel){
    val currentUser = vmDating.getUser()
    val searchPref by remember { mutableStateOf( currentUser.userPref) }

    val userPref= listOf(searchPref.gender, searchPref.zodiac, searchPref.sexualOri, searchPref.mbti,
        searchPref.children, searchPref.familyPlans, searchPref.meetUp, searchPref.education, searchPref.religion, searchPref.politicalViews,
        searchPref.relationshipType, searchPref.intentions, searchPref.drink, searchPref.smoke, searchPref.weed)

    val pref = listOf("Gender", "Zodiac Sign", "Sexual Orientation", "Mbti", "Children", "Family Plans", "Meeting Up",
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
                GenericTitleText(text = "Premium Settings")
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
/*
End of Seeking Screens
Start of Profile Screens
 */
@Composable
fun ProfileScreen(navController: NavHostController, vmDating: DatingViewModel, dating:DatingActivity){
    val currentUser = vmDating.getUser()
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (currentUser.name.isNotEmpty()) {
                isLoading.value = false
        }
    }
    val state = rememberScrollState()
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        isPhoto = false,
        titleText = "Profile",
        nav = navController,
        selectedItemIndex = 4,
        settingsButton = { navController.navigate("EditProfileScreen") },
        state = state,
        currentScreen = {
            UserInfo(
                currentUser,
                bioClick = { navController.navigate("BioEdit") },
                prompt1Click = {navController.navigate("PromptEdit/1")},
                prompt2Click = {navController.navigate("PromptEdit/2")},
                prompt3Click = {navController.navigate("PromptEdit/3")},
                photoClick = {navController.navigate("ChangePhoto")},
                doesEdit = true
            )
        }
    )
}
@Composable
fun EditProfileScreen(navController: NavHostController, dating: DatingActivity, vmDating: DatingViewModel){
    val currentUser = vmDating.getUser()
    var seen by remember { mutableStateOf(currentUser.seeMe)    }

    val userSettings= listOf(currentUser.ethnicity, currentUser.pronoun, currentUser.gender, currentUser.sexOrientation,
        currentUser.meetUp, currentUser.relationship,  currentUser.intentions, currentUser.star, //currentUser.mbti,
        currentUser.children, currentUser.family,  currentUser.drink, currentUser.smoke, currentUser.weed,
        currentUser.politics, currentUser.education, currentUser.religion,)

    val pref = listOf("Ethnicity", "Pronoun", "Gender", "Sexual Orientation",
        "Meeting Up", "Relationship Type", "Intentions", "Zodiac Sign", //"Mbti",
        "Children", "Family",  "Drink", "Smokes", "Weed", "Political Views", "Education", "Religion")
    InsideProfileSettings(
        nav = navController,
        editProfile = {
            SimpleBox(
                whatsInsideTheBox = {
                    Row(
                        modifier = Modifier
                            .padding(15.dp, 0.dp, 15.dp, 0.dp)
                            .fillMaxWidth()
                            .clickable {
                                seen = !seen
                                currentUser.seeMe = seen
                                vmDating.updateUser(currentUser)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GenericTitleText(text = "Only be seen by people you like")
                        Checkbox(checked = seen,
                            onCheckedChange = {seen = !seen
                                currentUser.seeMe = seen
                                vmDating.updateUser(currentUser)
                            })
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            for (i in pref.indices){
                EditProfile(title = pref[i], navController = navController, userSetting = userSettings[i], clickable = true, index = i)
                Spacer(modifier = Modifier.height(14.dp))
            }
            LogOut(dating)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp, 0.dp)
            ) {
                OutLinedButton(onClick = {/*TODO deactivate account*/   }, text = "Deactivate Account", outLineColor = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
                OutLinedButton(onClick = {/*TODO report and delete account*/   }, text = "Delete Account", outLineColor = Color.Red, textColor = Color.Red)
            }
        }
    )
}
/*
End of Profile Screens
*/
@Composable
fun ChangeProfileScreen(navController: NavHostController, title:String, index:Int, vmDating: DatingViewModel){
    ChangeProfile(navController,
            title = title,
            vmDating = vmDating,
            index = index,
        )
}
/*
Start of Message Screens
 */
@Composable
fun ChatsScreen(navController: NavHostController, vmDating: DatingViewModel, dating: DatingActivity){
    val matchedUsers = vmDating.getMatches() //TODO this has to be changed to be matches users
    //TODO ORDER MATCHED USERS HERE
    //val inChat by rememberSaveable { mutableStateOf(false)}
    val state = rememberScrollState()
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "Messages", //Change based on name
        isPhoto = false,
        nav = navController,
        selectedItemIndex = 1,
        settingsButton = { },
        state = state,
        currentScreen = {
            matchedUsers.forEach { matchUser ->
                MessageStart(
                    noMatches = false,
                    notification = true, //TODO set this passed on if they have a new message
                    userPhoto = matchUser.image1,
                    userName = matchUser.name,
                    userLastMessage = matchUser.bio, //TODO Last message goes here message.message (some how last)
                    openChat = {
                        navController.navigate("MessagerScreen")
                        vmDating.setTalkedUser(matchUser)
                    }
                )
            }
        }
    )
}
@Composable
fun MessagerScreen(navController: NavHostController, vmDating: DatingViewModel){
    val talkedUser = vmDating.getTalkedUser()
    val senderId = vmDating.getUser().number
    val receiverId = talkedUser.number
    val chatId = vmDating.getChatId(senderId, receiverId) //change to UID later need to account for reverses
    //TODO need to make this nested I think
    var message by rememberSaveable { mutableStateOf("") }
    val messageModel = viewModel { MessageViewModel(MyApp.x) }

    val messageList by messageModel.getChatData(chatId).collectAsState(listOf())


    InsideMessages(
        nav = navController,
        titleText = talkedUser.name,
        value = message,
        onValueChange = { message = it},
        sendMessage = { messageModel.storeChatData(chatId, message)
                      message = ""},
        goToProfile = { navController.navigate("MatchedUserProfile") },
        chatSettings = {},
        startCall = {/* TODO Start normal Call (Need to make a screen for it)*/},
        startVideoCall = {/* TODO Start Video Call (Need to make a screen for it)*/},
        sendAttachment = {/* TODO photos or attachments Message...advise if we should keep*/},
        messages = {
            MessageScreen(
                chatId = chatId,
                viewModel = messageModel,
                match = talkedUser,
                messageList = messageList,
                currentUserSenderId = messageModel.getCurrentUserSenderId(),
            )
        }
    )
}
@Composable
fun MatchedUserProfile(nav: NavHostController, vmDating: DatingViewModel){
    val talkedUser = vmDating.getTalkedUser()
    InsideMatchedProfile(
        nav = nav,
        title = talkedUser.name,
        editProfile = {
            val location = calcDistance(talkedUser.location, vmDating.getUser().location)
            UserInfo(
                user = talkedUser,//usersArray[currentProfileIndex]
                location = location,
                bottomButtons = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(25.dp, 0.dp)
                    ) {
                        OutLinedButton(
                            onClick = {/*TODO report and unmatch account*/
                                      nav.navigate("ChatsScreen")
                            },
                            text = "Report",
                            outLineColor = Color.Red,
                            textColor = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutLinedButton(
                            onClick = {/*TODO unmatch account*/
                                nav.navigate("ChatsScreen")
                            },
                            text = "Unmatch",
                            outLineColor = Color.Red
                        )
                    }
                },
            )
        }
    )
}
/*
End of Message Screens
Start of Groups Screens
 */
@Composable
fun GroupsScreen(navController: NavHostController, dating: DatingActivity){
    val state = rememberScrollState()
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        isPhoto = false,
        titleText = "Groups",
        nav = navController,
        selectedItemIndex = 3,
        settingsButton = { },
        state = state,
        currentScreen = {
        }
    )
}
@Composable
fun SomeScreen(navController: NavHostController, dating: DatingActivity, vmDating: DatingViewModel){
    val passed = 12 //viewmodel call here
    val liked = 12 //viewmodel call here
    val seen = 12 //viewmodel call here
    val missed = 12 //viewmodel call here
    val unmeet = 1 //viewmodel call here
    val state = rememberScrollState()
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { },
        state = state,
        currentScreen = {
            Column(
                modifier = Modifier.padding(horizontal = 25.dp)
            ) {
                GenericTitleText(text ="You're Stats", style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                GenericTitleText(text = "People you passed on: $passed")
                Spacer(modifier = Modifier.height(8.dp))
                GenericTitleText(text = "People you liked: $liked")
                Spacer(modifier = Modifier.height(8.dp))
                GenericTitleText(text = "People who seen you: $seen")
                Spacer(modifier = Modifier.height(8.dp))
                GenericTitleText(text = "Missed connections: $missed")
                Spacer(modifier = Modifier.height(24.dp))
                GenericTitleText(text ="Unmeet connections", style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(12.dp))
                GenericTitleText(text = "Currently: $unmeet")
            }

        }
    )
}
@Composable
fun ComeBackScreen(navController: NavHostController, dating: DatingActivity){
    val state = rememberScrollState()
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        currentScreen = {
            Comeback(text = "currently loading your future connection")
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