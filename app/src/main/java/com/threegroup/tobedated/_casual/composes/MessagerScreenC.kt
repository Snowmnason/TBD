package com.threegroup.tobedated._casual.composes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated.composeables.messages.InsideMessages
import com.threegroup.tobedated.composeables.messages.KeyBoard
import com.threegroup.tobedated.composeables.messages.MessageViewModel
import com.threegroup.tobedated.composeables.messages.MessagerDraw
import com.threegroup.tobedated.composeables.messages.TextSection
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.getChatId

@Composable
fun MessagerScreenC(navController: NavHostController, vmCasual: CasualViewModel, vmApi: ApiViewModel){
    val talkedUser by vmCasual.selectedUser.collectAsState()
    val avail by remember { mutableStateOf(talkedUser == null) }
    if (avail && talkedUser == null) {
        InsideMessages(
            nav = navController,
            titleText = "Loading",
            chatSettings = {},
            messages = {}
        )
    } else {
        val chatId = getChatId(
            vmCasual.getUser().number,
            talkedUser!!.number
        ) //change to UID later need to account for reverses
        var message by rememberSaveable { mutableStateOf("") }

        val messageModel = viewModel { MessageViewModel(MyApp.x) }
        val messageList by messageModel.getChatData(chatId, inOther = "casual").collectAsState(listOf())

        //TODO need to make this work
        var scrollValue by remember { mutableIntStateOf(Int.MAX_VALUE) }
        val lazyListState = rememberLazyListState()
        LaunchedEffect(messageList) {
            lazyListState.scrollToItem(scrollValue) //NEED this to update when keyboard opens
        }
        InsideMessages(
            nav = navController,
            titleText = talkedUser!!.name,
            goToProfile = { navController.navigate("MatchedUserProfile") },
            chatSettings = {
                MessagerDraw(
                    vmApi = vmApi,
                    reportButton = {
                        vmCasual.reportUser(talkedUser!!.number, vmCasual.getUser().number)
                        navController.navigate("ChatsScreen")
                        vmCasual.getMatchesFlow(vmCasual.getUser().number)
                    },
                    unmatchButton = {
                        vmCasual.deleteMatch(talkedUser!!.number, vmCasual.getUser().number)
                        navController.navigate("ChatsScreen")
                        vmCasual.getMatchesFlow(vmCasual.getUser().number)
                    },
                    currentMatch = talkedUser!!,
                )
            },
            startCall = {/* TODO Start normal Call (Need to make a screen for it)*/ },
            startVideoCall = {/* TODO Start Video Call (Need to make a screen for it)*/ },
            messageBar = {
                KeyBoard(
                    modifier = Modifier.imePadding()
                    ,message = message,
                    messageChange = { message = it },
                    sendMessage = {
                        if (message != "") {
                            messageModel.storeChatData(chatId, message, "casual")
                        }
                        message = ""
                        scrollValue = messageList.size + 2
                    },
                    sendAttachment = {/* TODO photos or attachments Message...advise if we should keep*/ }
                )
            },
            messages = {
                TextSection(
                    modifier = Modifier.fillMaxSize()
                        .imePadding()
                    ,lazyListState = lazyListState,
                    messageList = messageList,
                    currentUserSenderId = messageModel.getCurrentUserSenderId(),
                    match = talkedUser!!,
                    isRead = false
                )
            },
        )
    }
}

@Composable
fun FeedBackMessagerScreenC(navController: NavHostController, vmCasual: CasualViewModel,) {
    val senderId = vmCasual.getUser().number
    val receiverId = "feedback"
    val chatId =
        getChatId(senderId, receiverId) //change to UID later need to account for reverses
    //TODO need to make this nested I think
    var message by rememberSaveable { mutableStateOf("") }
    val messageModel = viewModel { MessageViewModel(MyApp.x) }
    val messageList by messageModel.getChatData(chatId).collectAsState(listOf())
    var scrollValue by remember { mutableIntStateOf(Int.MAX_VALUE) }
    val lazyListState = rememberLazyListState()
    LaunchedEffect(messageList) {
        lazyListState.scrollToItem(scrollValue) //NEED this to update when keyboard opens
    }
    InsideMessages(
        nav = navController,
        hideCallButtons = false,
        titleText = "Feedback",
        chatSettings = {},
        messageBar = {
            KeyBoard(
                modifier = Modifier.imePadding(),
                message = message,
                messageChange = { message = it },
                sendMessage = {
                    if (message != "") {
                        messageModel.storeChatData(chatId, message, "casual")
                    }
                    message = ""
                    scrollValue = messageList.size + 2
                },
                sendAttachment = {/* TODO photos or attachments Message...advise if we should keep*/ }
            )
        },
        messages = {
            TextSection(
                modifier = Modifier.fillMaxSize()
                    .imePadding(),
                lazyListState = lazyListState,
                messageList = messageList,
                currentUserSenderId = messageModel.getCurrentUserSenderId(),
                feedBack = true,
                isRead = false
            )
        },
    )
}