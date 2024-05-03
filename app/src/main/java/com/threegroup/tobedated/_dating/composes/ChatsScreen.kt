package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated._dating.TopAndBotBarsDating
import com.threegroup.tobedated._dating.notifiChat
import com.threegroup.tobedated._dating.notifiGroup
import com.threegroup.tobedated.composeables.messages.MessageStart
import com.threegroup.tobedated.shareclasses.api.ApiViewModel

/*
Start of Message Screens
 */
@Composable
fun ChatsScreen(
    navController: NavHostController,
    vmDating: DatingViewModel,
    dating: DatingActivity,
    vmApi: ApiViewModel
) {
    val matchedUsers by vmDating.matchList.collectAsState() //TODO this has to be changed to be matches user
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
        vmApi = vmApi,
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