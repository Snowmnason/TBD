package com.threegroup.tobedated.composables.datingScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.threegroup.tobedated.R
import com.threegroup.tobedated.composables.baseAppTextTheme
import com.threegroup.tobedated.ui.theme.AppTheme


@Composable
fun MessageStart(
    noMatches:Boolean = true,
    userPhoto:String,
    userName:String,
    userLastMessage:String,
    openChat: () -> Unit,

    ) {
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
        if(noMatches){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You have no connects at this moment\nGo try and met with some people!",
                    style = AppTheme.typography.titleMedium,
                    color = AppTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
            }
        }else{
            Spacer(modifier = Modifier.height(16.dp))
            Row(Modifier.clickable(onClick = openChat)){
                AsyncImage(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(shape = CircleShape),
                    model = userPhoto,
                    contentDescription = "UserPfp",
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier.padding(15.dp, 6.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = userName, style = AppTheme.typography.titleSmall, color = AppTheme.colorScheme.onBackground)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(text = displayedMessage,
                        style = AppTheme.typography.labelSmall, color = AppTheme.colorScheme.onBackground)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.height(20.dp),color = Color(0xDDB39DB7), thickness = 1.dp)
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsideMessages(
    messages: @Composable () -> Unit = {},
    titleText:String = "Name",
    value:String,
    onValueChange: (String) -> Unit,
    sendMessage: () -> Unit,
    goToProfile: () -> Unit,
    nav: NavHostController,
    startVideoCall: () -> Unit,
    startCall:() -> Unit,
    chatSettings:() -> Unit,
    sendAttachment:() -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AppTheme.colorScheme.background,
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.height(46.dp),
                    colors = TopAppBarColors(
                        containerColor = AppTheme.colorScheme.onTertiary,
                        navigationIconContentColor = AppTheme.colorScheme.primary,
                        titleContentColor = AppTheme.colorScheme.secondary,
                        actionIconContentColor = AppTheme.colorScheme.primary,
                        scrolledContainerColor = AppTheme.colorScheme.background
                    ),
                    title = { Button(onClick = goToProfile,
                        colors = ButtonColors(
                            contentColor = AppTheme.colorScheme.onBackground,
                            containerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Transparent
                        ),
                    ) { Text(text = titleText, style = AppTheme.typography.titleMedium) }
                    },//TitleTextGen(title= titleText)},
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) { //Showing in stuff like messages, editing profile and stuff
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back), contentDescription = "Go back")
                        }
                    },
                    actions = {
                        IconButton(onClick = startVideoCall) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.videocall), contentDescription = "Settings")
                        }
                        IconButton(onClick = startCall) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.call), contentDescription = "Settings")
                        }
                        IconButton(onClick = chatSettings) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.settings), contentDescription = "Settings")
                        }
                    }
                )
            },
            bottomBar = {
                Box(modifier = Modifier
                    .background(AppTheme.colorScheme.onTertiary)
                    .fillMaxWidth()
                    .padding(12.dp)){
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        //horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        IconButton(onClick = sendAttachment,
                            modifier = Modifier.offset(y=5.dp).weight(1.0F),
                            colors= IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = AppTheme.colorScheme.secondary,
                            ),
                        ) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.attachment), contentDescription = "Send")
                        }
                        OutlinedTextField(
                           modifier = Modifier.fillMaxWidth().weight(7.5F),
                            value = value, onValueChange = onValueChange,
                            textStyle = baseAppTextTheme(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrect = true,
                            ),
                            maxLines = 4,

                        )
                        IconButton(onClick = sendMessage,
                            modifier = Modifier.offset(y=5.dp).weight(1.0F),
                            colors= IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = AppTheme.colorScheme.secondary,
                            ),
                        ) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.send), contentDescription = "Send")
                        }
                    }
                }
            },
        ) {
                paddingValues ->
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(state.maxValue) }
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(state)
                    .fillMaxSize()
            ){
                Spacer(modifier = Modifier.height(24.dp))
                messages()
            }
        }
    }
}
@Composable
fun UserMessage(
    myMessage:String,
    color: Color = AppTheme.colorScheme.surface
){
    Column {
        Row(
            modifier = Modifier
                .padding(45.dp, 0.dp, 15.dp, 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
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
        Spacer(modifier = Modifier.height(6.dp))
    }
}
@Composable
fun TheirMessage(
    replyMessage:String,
    userPhoto:String,
    photoClick: () -> Unit
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

            Surface(
                modifier = Modifier.padding(5.dp),
                color = bubbleColor,
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(modifier = Modifier.padding(15.dp) ){
                    Text(text= replyMessage, style = AppTheme.typography.body)
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}

