package com.threegroup.tobedated.composeables.messages

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.threegroup.tobedated.R
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun MessageStart(
    userPhoto:String,
    userName:String,
    userLast:String?,
    openChat: () -> Unit,
    notification:Boolean = false,
    ) {
    var userLastMessage = "Start your connections"
    if (userLast != null && userLast != "") {
        userLastMessage = userLast
    }

    val maxLength = 35
    val cleanedMessage = userLastMessage.replace("\n", " ")
    val displayedMessage = if (cleanedMessage.length > maxLength) {
        "${cleanedMessage.take(maxLength)}..."
    } else {
        cleanedMessage
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp, 0.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(Modifier.clickable(onClick = openChat).fillMaxWidth()) {
            when (userPhoto) {
                "feedback" -> {
                    Image(
                        painterResource(id = R.drawable.feedback),
                        contentDescription = "Feedback photo",
                        modifier = Modifier.size(58.dp).clip(shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                "blind" -> {
                    Image(
                        painterResource(id = R.drawable.blind_feedback),
                        contentDescription = "Feedback photo",
                        modifier = Modifier.size(58.dp).clip(shape = CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                else -> {
                    AsyncImage(
                        modifier = Modifier
                            .size(58.dp)
                            .clip(shape = CircleShape),
                        model = userPhoto,
                        contentDescription = "UserPfp",
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Column(
                modifier = Modifier.padding(15.dp, 6.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row {
                    Text(
                        text = userName,
                        style = AppTheme.typography.titleSmall,
                        color = AppTheme.colorScheme.onBackground
                    )
                    if (notification) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.notification),
                            contentDescription = "New Message",
                            modifier = Modifier.offset(y = (-4).dp),
                            tint = AppTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = displayedMessage,
                    style = AppTheme.typography.labelSmall,
                    color = AppTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(
            modifier = Modifier.height(20.dp),
            color = Color(0xDDB39DB7),
            thickness = 1.dp
        )
    }
}