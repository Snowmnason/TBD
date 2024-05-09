package com.threegroup.tobedated.composeables.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.threegroup.tobedated.R
import com.threegroup.tobedated.composeables.composables.GenericLabelText
import com.threegroup.tobedated.composeables.composables.baseAppTextTheme
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.shareclasses.models.MessageModel
import com.threegroup.tobedated.theme.AppTheme


@Composable
fun TextSection(
    lazyListState:LazyListState,
    messageList:List<MessageModel>,
    currentUserSenderId:String,
    match:MatchedUserModel = MatchedUserModel(),
    feedBack:Boolean = false,
    isRead:Boolean,
    modifier: Modifier,
){
    LazyColumn(
        modifier = modifier,
        state = lazyListState
    ) {
        itemsIndexed(messageList) { index, message ->
            val last = index == (messageList.size -1)
            val isCurrentUser = message.senderId.contains(currentUserSenderId.replaceFirstChar { "" })
            val time = message.currentTime
            if(feedBack){
                MessageItemFeedBack(message = message, isCurrentUser = isCurrentUser, timeStamp = time, last)
            }else{
                MessageItem(match = match ,message = message, isCurrentUser = isCurrentUser, timeStamp = time, last, isRead)
            }
        }
    }
}
@Composable
fun KeyBoard(
    modifier: Modifier,
    message:String,
    messageChange: (String) -> Unit,
    sendMessage: () -> Unit,
    sendAttachment: () -> Unit
){
    Row(modifier = modifier
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            //horizontalArrangement = Arrangement.SpaceEvenly
        ){
            IconButton(onClick = sendAttachment,
                modifier = Modifier
                    .offset(y = 0.dp)
                    .weight(1.0F),
                colors= IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = AppTheme.colorScheme.secondary,
                ),
            ) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.attachment), contentDescription = "Send")
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(7.5F),
                value = message, onValueChange = messageChange,
                textStyle = baseAppTextTheme(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                ),
                maxLines = 4,

                )
            IconButton(onClick = sendMessage,
                modifier = Modifier
                    .offset(y = 0.dp)
                    .weight(1.0F),
                colors= IconButtonDefaults.iconButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = AppTheme.colorScheme.secondary,
                ),
            ) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.send), contentDescription = "Send")
            }
        }
    }
}
@Composable
fun UserMessage(
    myMessage:String,
    color: Color = AppTheme.colorScheme.surface,
    time:String,
    last: Boolean,
    read:Boolean,

    ){
    Column {
        Row(
            modifier = Modifier
                .padding(45.dp, 0.dp, 15.dp, 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom // Align bottom vertically
        ) {
            GenericLabelText(text = time)
            Surface(
                modifier = Modifier.padding(5.dp),
                color = color,
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(modifier = Modifier.padding(15.dp)) {
                    Text(text = myMessage, style = AppTheme.typography.body, color= AppTheme.colorScheme.onSurface)
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(45.dp, 0.dp, 20.dp, 0.dp)
                .offset(y = (-10).dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom // Align bottom vertically
        ) {
            if (last) {
                if (!read) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.sent),
                        contentDescription = "sent",
                        tint = AppTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.read),
                        contentDescription = "read",
                        tint = AppTheme.colorScheme.primary
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}
@Composable
fun TheirMessage(
    replyMessage:String,
    userPhoto:String = "",
    photoClick: () -> Unit = {},
    time:String,
    last:Boolean,
){
    Column {
        val bubbleColor = Color.LightGray
        Row(
            modifier = Modifier
                .padding(15.dp, 0.dp, 45.dp, 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            if(last){
                if(userPhoto == "feedback"){
                    Image(painterResource(id = R.drawable.feedback), contentDescription = "Feedback photo",
                        modifier = Modifier.size(44.dp).clip(shape = CircleShape), contentScale = ContentScale.Crop)
                }else{
                    IconButton(onClick = photoClick) {
                        AsyncImage(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(shape = CircleShape),
                            model = userPhoto,
                            contentDescription = "UserPfp",
                            contentScale = ContentScale.Crop,
                        )
                    }
                }
            }
            Surface(
                modifier = Modifier.padding(5.dp),
                color = bubbleColor,
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(modifier = Modifier.padding(15.dp) ){
                    Text(text= replyMessage, style = AppTheme.typography.body)
                }
            }
            GenericLabelText(text = time)
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}
@Composable
fun MessageItem(match: MatchedUserModel, message: MessageModel, isCurrentUser: Boolean, timeStamp: String, last: Boolean, isRead: Boolean) {
    if (!isCurrentUser) {
        if(!last){
            TheirMessage(replyMessage = message.message, time =  timeStamp, last = false, userPhoto = match.image1)
        }else{
            TheirMessage(replyMessage = message.message, time =  timeStamp, last = true, userPhoto = match.image1)
        }
    }
    if (isCurrentUser) {
        if(!last){
            UserMessage(myMessage = message.message, time =  timeStamp, last = false, read = isRead)
        }else{
            UserMessage(myMessage = message.message, time =  timeStamp, last = true, read = isRead)
        }
    }
}
@Composable
fun MessageItemFeedBack(message: MessageModel, isCurrentUser: Boolean, timeStamp: String, last: Boolean) {
    if (!isCurrentUser) {
        if(!last){
            TheirMessage(replyMessage = message.message, time =  timeStamp, last = false, userPhoto = "feedback")
        }else{
            TheirMessage(replyMessage = message.message, time =  timeStamp, last = true, userPhoto = "feedback")
        }
    }

    if (isCurrentUser) {
        UserMessage(myMessage = message.message, time =  timeStamp, last = false, read = false)
    }
}
