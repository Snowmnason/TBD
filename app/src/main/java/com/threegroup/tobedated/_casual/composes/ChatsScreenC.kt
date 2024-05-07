package com.threegroup.tobedated._casual.composes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated.composeables.messages.MessageStart
import com.threegroup.tobedated.shareclasses.getChatId
import com.threegroup.tobedated.shareclasses.getMatchId

@Composable
fun ChatsScreenC(navController: NavHostController, vmCasual: CasualViewModel){
    val matchedUsers by vmCasual.matchList.collectAsState()
    //TODO ORDER MATCHED USERS HERE
    //val inChat by rememberSaveable { mutableStateOf(false)}
    val state = rememberScrollState(0)

    Column(
        Modifier
            //.padding(paddingValues)
            .verticalScroll(state)
            .fillMaxSize()
    ) {
        matchedUsers.forEach { matchUser ->
            MessageStart(
                notification = true, //TODO set this passed on if they have a new message
                userPhoto = matchUser.userPicture,
                userName = matchUser.userName,
                userLast = matchUser.lastMessage, //TODO Last message goes here message.message (some how last)
                openChat = {
                    navController.navigate("MessagerScreen")
                    vmCasual.setTalkedUser(matchUser.userId)
                    vmCasual.openChat(getChatId(vmCasual.getUser().number,matchUser.userId))
                    vmCasual.markMatchAsViewed(getMatchId(vmCasual.getUser().number,matchUser.userId), vmCasual.getUser().number)
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
}