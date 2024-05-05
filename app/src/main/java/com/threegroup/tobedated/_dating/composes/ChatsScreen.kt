package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated._dating.TopAndBotBarsDating
import com.threegroup.tobedated._dating.notifiChat
import com.threegroup.tobedated._dating.notifiGroup
import com.threegroup.tobedated.composeables.messages.MessageStart
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.getChatId
import com.threegroup.tobedated.shareclasses.getMatchId

/*
Start of Message Screens
 */
@Composable
fun ChatsScreen(
    navController: NavHostController,
    vmDating: DatingViewModel,
    dating: DatingActivity,
    vmApi: ApiViewModel,
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
                MessageStart(
                    notification = true, //TODO set this passed on if they have a new message
                    userPhoto = matchUser.userPicture,
                    userName = matchUser.userName,
                    userLast = matchUser.lastMessage, //TODO Last message goes here message.message (some how last)
                    openChat = {
                        navController.navigate("MessagerScreen")
                        vmDating.setTalkedUser(matchUser.userId)
                        vmDating.openChat(getChatId(vmDating.getUser().number,matchUser.userId))
                        vmDating.markMatchAsViewed(getMatchId(vmDating.getUser().number,matchUser.userId), vmDating.getUser().number)
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