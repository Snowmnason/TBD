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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.threegroup.tobedated._dating.composables.MessageStart
import com.threegroup.tobedated._dating.composables.MessagingBar
import com.threegroup.tobedated._dating.composables.OtherPreferences
import com.threegroup.tobedated._dating.composables.SearchingButtons
import com.threegroup.tobedated._dating.composables.SeekingBox
import com.threegroup.tobedated._dating.composables.SeekingUserInfo
import com.threegroup.tobedated._dating.composables.TextSectionAndKeyBoard
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
import com.threegroup.tobedated.shareclasses.composables.SimpleBox
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.shareclasses.storeImageAttempt
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import kotlinx.coroutines.launch

//val notifiSearching = Random.nextBoolean()

const val notifiGroup = false
const val notifiChat = 0//Random.nextInt(0, 41) // Generates a random integer between 0 and 40

class DatingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreference = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putString("activityToken", "dating")
        editor.apply()
        setContent {
            AppTheme(
                activity = "dating"
            ) {
                DatingNav(this@DatingActivity)
            }
        }
    }

    fun uploadPhotos(
        newImage: String,
        imageNumber: Int,
        imageName: String,
        callback: (String) -> Unit
    ) {
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

    fun switchActivities(switchActivity: String) {
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
        startActivity(intent)
        finish()
    }
}

/*
Start of Seeking Screen
 */
@Composable
fun SearchingScreen(
    vmDating: DatingViewModel,
    dating: DatingActivity,
    navController: NavHostController
) {
    val currentUser = vmDating.getUser()
    var isNext by rememberSaveable { mutableStateOf(true) }
    var showReport by rememberSaveable { mutableStateOf(false) }
    var currentProfileIndex by rememberSaveable { mutableIntStateOf(0) } ///MIGHT CHANGE THIS
    val currentPotential = remember { mutableStateOf<MatchedUserModel?>(null) }
    val state = rememberScrollState()

    // Reset scroll state when currentProfileIndex or isNext changes
    LaunchedEffect(currentProfileIndex, isNext) {
        state.scrollTo(0)
    }

    LaunchedEffect(Unit) {
        //TODO This checks to see if the list is empty or not, This NEEDs to be avilialbe some hows
        if (vmDating.getNextPotential(currentProfileIndex) != null) {
            currentPotential.value =
                vmDating.getNextPotential(currentProfileIndex)//MIGHT CHANGE THIS
        } else {
            isNext =
                false//This is important, if there are no users this shows a blank screen and not crash
        }
    }


    ///TODO THIS DOES THE SAME CHECK AS ABOVE to see if there is an avilibe user to prevent crashes
    fun nextProfile() {
        val newPotential = vmDating.getNextPotential(currentProfileIndex)
        if (newPotential != null) {
            currentPotential.value = newPotential///MIGHT change this
        } else {
            isNext =
                false//This is important, if there are no users this shows a blank screen and not crash
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
        star = currentUser.star,
        currentScreen = {
            if (isNext) {
                currentPotential.value?.let { currentPotential ->
                    var location = "x miles"
                    if (currentPotential.location != "" && vmDating.getUser().location != "") {
                        location = calcDistance(currentPotential.location, currentUser.location)
                    }
                    SeekingUserInfo(
                        user = currentPotential,//usersArray[currentProfileIndex]
                        location = location,
                        bottomButtons = {
                            SearchingButtons(
                                onClickLike = {
                                    currentProfileIndex++
                                    vmDating.likeCurrentProfile(
                                        currentUser.number,
                                        currentPotential
                                    )
                                    nextProfile()
                                    /*TODO Add an animation or something*/
                                },
                                onClickPass = {
                                    currentProfileIndex++//THIS SHIT CAN GO
                                    vmDating.passCurrentProfile(
                                        currentUser.number,
                                        currentPotential
                                    )
                                    nextProfile()
                                    /*TODO Add an animation or something*/
                                },
                                onClickReport = {
                                    showReport = true /*TODO Add an animation or something*/
                                },
                                onClickSuggest = { /*TODO Add an animation or something*/ },
                            )
                        },
                    )
                }
            } else {
                Comeback(text = "Come Back when theres more people to see =0)")
            }
        })
    if (showReport) {
        AlertDialogBox(
            dialogTitle = "Report!",
            onDismissRequest = { showReport = false },
            dialogText = "This account will be looked into and they will not be able to view your profile",
            onConfirmation = {
                showReport = false
                currentProfileIndex++
                vmDating.passCurrentProfile(currentUser.number, currentPotential.value!!)
                nextProfile()
                //nextProfile(vmDating.reportedCurrentPotential(currentProfileIndex, currentPotential.value!!))
            }
        )
    }
}

@Composable
fun SearchPreferenceScreen(navController: NavHostController, vmDating: DatingViewModel) {
    val currentUser = vmDating.getUser()
    val searchPref by remember { mutableStateOf(currentUser.userPref) }

    val userPref = listOf(
        searchPref.gender,
        searchPref.zodiac,
        searchPref.sexualOri,
        searchPref.mbti,
        searchPref.children,
        searchPref.familyPlans,
        searchPref.meetUp,
        searchPref.education,
        searchPref.religion,
        searchPref.politicalViews,
        searchPref.relationshipType,
        searchPref.intentions,
        searchPref.drink,
        searchPref.smoke,
        searchPref.weed
    )

    val pref = listOf(
        "Gender",
        "Zodiac Sign",
        "Sexual Orientation",
        "Mbti",
        "Children",
        "Family Plans",
        "Meeting Up",
        "Education",
        "Religion",
        "Political Views",
        "Relationship Type",
        "Intentions",
        "Drink",
        "Smokes",
        "Weed"
    )
    InsideSearchSettings(
        nav = navController,
        searchSettings = {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AgeSlider(
                    preferredMin = currentUser.userPref.ageRange.min,
                    preferredMax = currentUser.userPref.ageRange.max,
                    vmDating = vmDating,
                    currentUser = currentUser
                )
                Spacer(modifier = Modifier.height(14.dp))
                DistanceSlider(
                    preferredMax = currentUser.userPref.maxDistance,
                    vmDating = vmDating,
                    currentUser = currentUser
                )
                Spacer(modifier = Modifier.height(14.dp))
                SeekingBox(desiredSex = currentUser.seeking, navController)
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(
                    Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.onBackground,
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(6.dp))
                GenericTitleText(text = "Premium Settings")
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.onBackground,
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(14.dp))
                for (i in pref.indices) {
                    OtherPreferences(
                        title = pref[i],
                        navController = navController,
                        searchPref = userPref[i],
                        clickable = true,
                        index = i
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    )
}

@Composable
fun ChangePreference(
    navController: NavHostController,
    title: String,
    index: Int,
    vmDating: DatingViewModel
) {
    if (index == 69420) {
        ChangeSeekingScreen(
            navController,
            title = title,
            vmDating = vmDating,
            index = index,
        )
    } else {
        ChangePreferenceScreen(
            navController,
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
fun ProfileScreen(
    navController: NavHostController,
    vmDating: DatingViewModel,
    dating: DatingActivity
) {
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
        star = vmDating.getUser().star,
        currentScreen = {
            UserInfo(
                currentUser,
                bioClick = { navController.navigate("BioEdit") },
                prompt1Click = { navController.navigate("PromptEdit/1") },
                prompt2Click = { navController.navigate("PromptEdit/2") },
                prompt3Click = { navController.navigate("PromptEdit/3") },
                photoClick = { navController.navigate("ChangePhoto") },
                doesEdit = true
            )
        }
    )
}

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    dating: DatingActivity,
    vmDating: DatingViewModel
) {
    val currentUser = vmDating.getUser()
    var seen by remember { mutableStateOf(currentUser.seeMe) }

    val userSettings = listOf(
        currentUser.ethnicity,
        currentUser.pronoun,
        currentUser.gender,
        currentUser.sexOrientation,
        currentUser.meetUp,
        currentUser.relationship,
        currentUser.intentions,
        currentUser.star, //currentUser.mbti,
        currentUser.children,
        currentUser.family,
        currentUser.drink,
        currentUser.smoke,
        currentUser.weed,
        currentUser.politics,
        currentUser.education,
        currentUser.religion,
    )

    val pref = listOf(
        "Ethnicity", "Pronoun", "Gender", "Sexual Orientation",
        "Meeting Up", "Relationship Type", "Intentions", "Zodiac Sign", //"Mbti",
        "Children", "Family", "Drink", "Smokes", "Weed", "Political Views", "Education", "Religion"
    )
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
                            onCheckedChange = {
                                seen = !seen
                                currentUser.seeMe = seen
                                vmDating.updateUser(currentUser)
                            })
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            for (i in pref.indices) {
                EditProfile(
                    title = pref[i],
                    navController = navController,
                    userSetting = userSettings[i],
                    clickable = true,
                    index = i
                )
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
                OutLinedButton(
                    onClick = {/*TODO deactivate account*/ },
                    text = "Deactivate Account",
                    outLineColor = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutLinedButton(
                    onClick = { vmDating.deleteUserAndData(currentUser.number) },
                    text = "Delete Account",
                    outLineColor = Color.Red,
                    textColor = Color.Red
                ) // vmDating.deleteProfile(currentUser.number, dating)
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    )
}

/*
End of Profile Screens
*/
@Composable
fun ChangeProfileScreen(
    navController: NavHostController,
    title: String,
    index: Int,
    vmDating: DatingViewModel
) {
    ChangeProfile(
        navController,
        title = title,
        vmDating = vmDating,
        index = index,
    )
}

/*
Start of Message Screens
 */
@Composable
fun ChatsScreen(
    navController: NavHostController,
    vmDating: DatingViewModel,
    dating: DatingActivity
) {
    val matchedUsers = vmDating.getMatches() //TODO this has to be changed to be matches user
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
        star = vmDating.getUser().star,
        currentScreen = {
            matchedUsers.forEach { matchUser ->
//                println(matchedUsers)
                MessageStart(
                    notification = true, //TODO set this passed on if they have a new message
                    userPhoto = matchUser.userPicture,
                    userName = matchUser.userName,
                    userLast = matchUser.lastMessage, //TODO Last message goes here message.message (some how last)
                    openChat = {
                        navController.navigate("MessagerScreen")
                        vmDating.setTalkedUser(matchUser.userId)
                    }
                )
            }

            //This is for the feed back system
            MessageStart(
                userPhoto = "feedback",
                userName = "FeedBack System",
                userLast = "Message success",
                openChat = {
                    navController.navigate("FeedBackMessagerScreen")
                })
        }
    )
}

@Composable
fun MessagerScreen(navController: NavHostController, vmDating: DatingViewModel) {
    val talkedUser by vmDating.selectedUser.collectAsState()
    val avail by remember { mutableStateOf(talkedUser == null) }
    if (avail && talkedUser == null) {
        InsideMessages(
            nav = navController,
            titleText = "Loading",
            chatSettings = {},
            messages = {}
        )
    } else {
        val chatId = vmDating.getChatId(
            vmDating.getUser().number,
            talkedUser!!.number
        ) //change to UID later need to account for reverses
        var message by rememberSaveable { mutableStateOf("") }
        val messageModel = viewModel { MessageViewModel(MyApp.x) }
        val messageList by messageModel.getChatData(chatId).collectAsState(listOf())
        //TODO need to make this work
        var scrollValue by remember { mutableIntStateOf(Int.MAX_VALUE) }
        val lazyListState = rememberLazyListState()
        LaunchedEffect(messageList) {
            lazyListState.scrollToItem(scrollValue) //NEED this to update when keybaord opens
        }
        InsideMessages(
            nav = navController,
            titleText = talkedUser!!.name,
            goToProfile = { navController.navigate("MatchedUserProfile") },
            chatSettings = {},
            startCall = {/* TODO Start normal Call (Need to make a screen for it)*/ },
            startVideoCall = {/* TODO Start Video Call (Need to make a screen for it)*/ },
            messageBar = {
                MessagingBar(
                    modifier = Modifier.imePadding(),
                    message = message,
                    messageChange = { message = it },
                    sendMessage = {
                        if (message != "") {
                            messageModel.storeChatData(chatId, message)
                        }
                        message = ""
                        scrollValue = messageList.size + 2
                    },
                    sendAttachment = {/* TODO photos or attachments Message...advise if we should keep*/ }
                )
            },
            messages = {
                TextSectionAndKeyBoard(
                    lazyListState = lazyListState,
                    messageList = messageList,
                    currentUserSenderId = messageModel.getCurrentUserSenderId(),
                    match = talkedUser!!,
                )
            },
        )
    }
}

@Composable
fun FeedBackMessagerScreen(navController: NavHostController, vmDating: DatingViewModel) {
    val senderId = vmDating.getUser().number
    val receiverId = "feedback"
    val chatId =
        vmDating.getChatId(senderId, receiverId) //change to UID later need to account for reverses
    //TODO need to make this nested I think
    var message by rememberSaveable { mutableStateOf("") }
    val messageModel = viewModel { MessageViewModel(MyApp.x) }
    val messageList by messageModel.getChatData(chatId).collectAsState(listOf())
    var scrollValue by remember { mutableIntStateOf(Int.MAX_VALUE) }
    val lazyListState = rememberLazyListState()
    LaunchedEffect(messageList) {
        lazyListState.scrollToItem(scrollValue) //NEED this to update when keybaord opens
    }
    InsideMessages(
        nav = navController,
        hideCallButtons = false,
        titleText = "Feedback",
        chatSettings = {},
        messageBar = {
            MessagingBar(
                modifier = Modifier.imePadding(),
                message = message,
                messageChange = { message = it },
                sendMessage = {
                    if (message != "") {
                        messageModel.storeChatData(chatId, message)
                    }
                    message = ""
                    scrollValue = messageList.size + 2
                },
                sendAttachment = {/* TODO photos or attachments Message...advise if we should keep*/ }
            )
        },
        messages = {
            TextSectionAndKeyBoard(
                lazyListState = lazyListState,
                messageList = messageList,
                currentUserSenderId = messageModel.getCurrentUserSenderId(),
                feedBack = true,
            )
        },
    )
}

@Composable
fun MatchedUserProfile(nav: NavHostController, vmDating: DatingViewModel) {
    val talkedUser = vmDating.getTalkedUser()
    InsideMatchedProfile(
        nav = nav,
        title = talkedUser.name,
        editProfile = {
            val location = calcDistance(talkedUser.location, vmDating.getUser().location)
            SeekingUserInfo(
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
fun GroupsScreen(navController: NavHostController, dating: DatingActivity) {
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        isPhoto = false,
        titleText = "Groups",
        nav = navController,
        selectedItemIndex = 3,
        settingsButton = { },
        star = "Ask me",
        currentScreen = {
        }
    )
}

@Composable
fun SomeScreen(
    navController: NavHostController,
    dating: DatingActivity,
    vmDating: DatingViewModel
) {

    val passed = 12 //viewmodel call here
    val liked = 12 //viewmodel call here
    val seen = 12 //viewmodel call here
    val missed = 12 //viewmodel call here
    val unmeet = 1 //viewmodel call here
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "Stats",
        isPhoto = false,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { },
        star = vmDating.getUser().star,
        currentScreen = {
            Column(
                modifier = Modifier.padding(horizontal = 25.dp)
            ) {
                GenericTitleText(text = "Your Stats", style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(12.dp))
                GenericTitleText(text = "• People you passed on: $passed")
                Spacer(modifier = Modifier.height(8.dp))
                GenericTitleText(text = "• People you liked: $liked")
                Spacer(modifier = Modifier.height(8.dp))
                GenericTitleText(text = "• People who seen you: $seen")
                Spacer(modifier = Modifier.height(8.dp))
                GenericTitleText(text = "• Missed connections: $missed")
                Spacer(modifier = Modifier.height(24.dp))
                GenericTitleText(
                    text = "Unmeet connections",
                    style = AppTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(12.dp))
                GenericTitleText(text = "• Currently: $unmeet")
                Spacer(modifier = Modifier.height(24.dp))
                GenericTitleText(
                    text = "Profile Suggestions",
                    style = AppTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(12.dp))
                GenericTitleText(text = "• ")
            }

        }
    )
}

@Composable
fun ComeBackScreen(navController: NavHostController, dating: DatingActivity) {
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        star = "Ask me",
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
    FeedBackMessagerScreen
}