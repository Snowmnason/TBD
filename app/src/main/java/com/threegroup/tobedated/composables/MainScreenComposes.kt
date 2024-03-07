package com.threegroup.tobedated.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.R
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.ui.theme.zodiac

data class BotNavItem(
    val title:String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNew: Boolean = false,
    val badgeCount:Int? = null
)
val starColors = listOf(Color(0xFFf07019), Color(0xFF874b2f), Color(0xFFecab2b),
    Color(0xFF9a68bf), Color(0xFF6ca169), Color(0xFF0e4caf),
    Color(0xFF5c5463), Color(0xFFb9361a), Color(0xFF345c42),
    Color(0xFF366b8d), Color(0xFF0a434c), Color(0xFFa0467c), Color.Gray
)
val starColorMap = mapOf(
    "a" to 0, "b" to 1, "c" to 2, "d" to 3, "e" to 4, "f" to 5,
    "g" to 6, "h" to 7, "i" to 8, "j" to 9, "k" to 10, "l" to 11, "m" to 12
)
val mbtiColors = listOf(
    Color(0xFF834e69), Color(0xFF617c44), Color(0xFF176363), Color(0xFFD1A00C)
)
val mbtiColorMap = mapOf(
    "INTJ" to 0, "INTP" to 0, "ENTJ" to 0, "ENTP" to 0,
    "ENFP" to 1, "ENFJ" to 1, "INFP" to 1, "INFJ" to 1,
    "ESFJ" to 2, "ESTJ" to 2, "ISFJ" to 2, "ISTJ" to 2,
    "ISTP" to 3, "ISFP" to 3, "ESTP" to 3, "ESFP" to 3
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAndBotBars(
    notifiChat:Int = 0,
    notifiGroup:Boolean = false,
    notifiSearching:Boolean = false,
    currentScreen: @Composable () -> Unit = {},
    titleText:String = "To Be Dated",
    isPhoto:Boolean,
    nav: NavHostController,
    selectedItemIndex: Int,
    settingsButton: () -> Unit
) {
    val items = listOf(
        BotNavItem(
            title = "SomeScreen",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.some_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.some_outlined),
        ),
        BotNavItem(
            title = "ChatsScreen",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.chats_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.chats_outlined),
            badgeCount = notifiChat,
        ),
        BotNavItem(
            title = "SearchingScreen",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.logo_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.logo_outlined),
            hasNew = notifiSearching,
        ),
        BotNavItem(
            title = "GroupsScreen",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.groups_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.groups_outlined),
            hasNew = notifiGroup,
        ),
        BotNavItem(
            title = "ProfileScreen",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.profile_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.profile_outlined),
        ),
    )

    //var selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent,
    ) {
        Scaffold(
            containerColor = AppTheme.colorScheme.background,
            bottomBar = {
                NavigationBar(
                    containerColor = AppTheme.colorScheme.onTertiary,
                    modifier = Modifier.height(46.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            colors =  NavigationBarItemColors(
                                selectedIconColor = AppTheme.colorScheme.primary,
                                selectedTextColor = AppTheme.colorScheme.primary,
                                selectedIndicatorColor = Color.Transparent,
                                unselectedIconColor = AppTheme.colorScheme.onBackground,
                                unselectedTextColor = AppTheme.colorScheme.onBackground,
                                disabledIconColor = Color.Black,
                                disabledTextColor = Color.Black
                            ),
                            selected = selectedItemIndex == index,
                            onClick = { //selectedItemIndex = index
                                        nav.navigate(item.title)
                                      },//HANDLE NAVIGATION
                            label = {  },
                            alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.badgeCount != null) {
                                            Badge {
                                                Text(text = item.badgeCount.toString())
                                            }
                                        } else if (item.hasNew) {
                                            Badge()
                                        }
                                    }) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        } else {
                                            item.unselectedIcon
                                        },
                                        contentDescription = item.title
                                    )
                                }
                            })
                    }
                }
            },
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
                    title = { TopBarText(title= titleText, isPhoto = isPhoto) },//TitleTextGen(title= titleText)},
                    navigationIcon = {
                            IconButton(onClick = { /*TODO*/ }) {//Show my default
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.hamburger),
                                    contentDescription = "Change Looking tab"
                                )
                            }
                    },
                    actions = {
                        IconButton(onClick = settingsButton) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.settings), contentDescription = "Settings")
                        }
                    }
                )
            }
        ) {
           paddingValues ->
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(0) }
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(state)
                    .fillMaxSize()
            ){
                Spacer(modifier = Modifier.height(24.dp))
                    currentScreen() //All 5 screens go here
            }
        }
    }
}
@Composable
fun TopBarText(
    title:String,
    size: TextStyle = AppTheme.typography.titleLarge,
    isPhoto:Boolean
){
    if(isPhoto){
        val photo = if (isSystemInDarkTheme()) painterResource(id = R.drawable.logo) else painterResource(id = R.drawable.logodark)
        Box(modifier = Modifier
            .height(30.dp)
            .offset(y = (8).dp)){
            Image(painter = photo, contentDescription = "Logo")
        }
    }else{
        Text(
            modifier = Modifier.offset(y = (16).dp),
            text = title,
            style = size,
            color = AppTheme.colorScheme.onBackground,
        )
    }
}
@Composable
fun SimpleBox(
    whatsInsideTheBox: @Composable () -> Unit = {},
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFB39DB7), shape = RoundedCornerShape(4.dp)),
        color = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(4.dp)
    ){
        Box(modifier = Modifier.padding(4.dp, 8.dp)){
            whatsInsideTheBox()
        }
    }
}

/*
THIS IS SEARCHING SCREEN STUFF
 */
@Composable
fun UserInfo(
    name:String, bio:String, age:String, height:String,
    pronouns:String, gender:String, sign:String = "m",
    mbti:String = "Not Taken",
    promptQ1:String, promptA1:String,
    sexOri:String = "Ask me", relationshipType:String = "Ask me",
    promptQ2:String, promptA2:String,
    smokes: String = "Ask me", drinks:String = "Ask me", weeds:String = "Ask me",
    promptQ3:String, promptA3:String,
    work:String = "Ask me", school:String = "Ask me", kids:String = "Ask me",
    exercise:String = "Ask me", politics:String = "Ask me", religion:String = "Ask me",
){
    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp, 0.dp)
    ) {
        //Name
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = name, style = AppTheme.typography.titleMedium)
            }
        })//Age, Pronouns, Gender
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Text(text = age, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Text(text = pronouns, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Text(text = gender, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.height), contentDescription = "Height", modifier = Modifier.offset(y = (-4).dp), tint = AppTheme.colorScheme.primary)
                Text(text = height, style = AppTheme.typography.titleSmall)
            }
        })
        //BIO
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
                Column(modifier = Modifier.fillMaxSize()){
                    Text(text = "Bio", style = AppTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = bio, style = AppTheme.typography.bodySmall)
                }
            })
        val mbtiColor:Color  = if(mbti != "Not Taken"){
            val parts = mbti.split("-")
            val mainType = parts.first()
            mbtiColors[mbtiColorMap[mainType]!!]
        }else {
            AppTheme.colorScheme.onSurface
        }
        //Sign Sign MBTI
        val oppositeIndex = (starColorMap[sign]!! + 2)  % starColorMap.size
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Text(text = sign, style =TextStyle(
                    fontFamily = zodiac,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp,
                    shadow = Shadow(
                        color = starColors[oppositeIndex],
                        offset = Offset(3f, 2f),
                        blurRadius = 4f
                    )
                ), color = starColors[starColorMap[sign]!!])
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Text(text = mbti, style = AppTheme.typography.titleLarge, color = mbtiColor)
            }
        })
        //First prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = promptQ1, style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = promptA1, style = AppTheme.typography.bodySmall)
            }
        })
        //Sexual orientation, relationship type
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Text(text = sexOri, style = AppTheme.typography.titleLarge)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Text(text = relationshipType, style = AppTheme.typography.titleLarge)
            }
        })
        //Second prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = promptQ2, style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = promptA2, style = AppTheme.typography.bodySmall)
            }
        })
        //Smokes, drinks, weeds
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.smoking), contentDescription = "Weeds", modifier = Modifier.offset(y = (-4).dp), tint = AppTheme.colorScheme.primary)
                Text(text = smokes, style = AppTheme.typography.titleLarge)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.drinking), contentDescription = "Weeds", modifier = Modifier.offset(y = (-4).dp), tint = AppTheme.colorScheme.primary)
                Text(text = drinks, style = AppTheme.typography.titleLarge)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.weeds), contentDescription = "Weeds", Modifier.size(20.dp), tint = AppTheme.colorScheme.primary)
                Text(text = weeds, style = AppTheme.typography.titleLarge)
            }
        })
        //Third Prompt Question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = promptQ3, style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = promptA3, style = AppTheme.typography.bodySmall)
            }
        })
        //Word School Kids
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.work), contentDescription = "work", modifier = Modifier.offset(y = (-4).dp), tint = AppTheme.colorScheme.primary)
                Text(text = work, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.school), contentDescription = "School", modifier = Modifier.offset(y = (-4).dp), tint = AppTheme.colorScheme.primary)
                Text(text = school, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.children), contentDescription = "Children", modifier = Modifier.offset(y = (-4).dp), tint = AppTheme.colorScheme.primary)
                Text(text = kids, style = AppTheme.typography.titleSmall)
            }
        })
        /*
        TODO add another breaker it will look nicer
         */
        //Exercise
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.exercise), contentDescription = "Exercise", modifier = Modifier.offset(y = (-4).dp), tint = AppTheme.colorScheme.primary)
                Text(text = exercise, style = AppTheme.typography.titleSmall)

            }
        })
        /*
        TODO add another breaker it will look nicer
         */
        //Politics Religion
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.politics), contentDescription = "Politics", modifier = Modifier.offset(y = (-4).dp), tint = AppTheme.colorScheme.primary)
                Text(text = politics, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.religion), contentDescription = "Religion", modifier = Modifier.offset(y = (-4).dp), tint = AppTheme.colorScheme.primary)
                Text(text = religion, style = AppTheme.typography.titleSmall)
            }
        })
    }
}

@Composable
fun MessageStart(
    noMatches:Boolean = true,
    userPhoto:Painter,
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
                Image(painter = userPhoto, contentDescription = "UserPfp", contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(58.dp)
                        .clip(shape = CircleShape)
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
    titleButton: () -> Unit,
    nav: NavHostController
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
                    title = { Button(onClick = titleButton,
                            colors = ButtonColors(
                                contentColor = AppTheme.colorScheme.onBackground,
                                containerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            ),
                        ) { Text(text = titleText, style = AppTheme.typography.titleMedium)}
                    },//TitleTextGen(title= titleText)},
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) { //Showing in stuff like messages, editing profile and stuff
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back), contentDescription = "Go back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
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
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        TextField(
//                            modifier = Modifier.fillMaxWidth(),
                            value = value, onValueChange = onValueChange,
                            textStyle = baseAppTextTheme(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrect = true,
                            )
                        )
                        IconButton(onClick = sendMessage,
                            colors= IconButtonDefaults.iconButtonColors(
                                containerColor = AppTheme.colorScheme.secondary,
                                contentColor = AppTheme.colorScheme.onBackground,
                                disabledContainerColor = Color.Black,
                                disabledContentColor = Color.Black,
                            ),
                        ) {
                            Icon(imageVector = Icons.Default.Done, contentDescription = "Send")
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
    color:Color = AppTheme.colorScheme.surface
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
                    Text(text = myMessage, style = AppTheme.typography.body, color=AppTheme.colorScheme.onSurface)
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}
@Composable
fun TheirMessage(
    replyMessage:String,
    userPhoto:Painter,
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
                Image(painter = userPhoto,
                    contentDescription = "UserPfp",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(shape = CircleShape)
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
