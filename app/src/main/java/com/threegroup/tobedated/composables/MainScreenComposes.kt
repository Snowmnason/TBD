package com.threegroup.tobedated.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    Color(0xFF366b8d), Color(0xFF0a434c), Color(0xFFa0467c),
)
val starColorMap = mapOf<String, Int>(
    "a" to 0, "b" to 1, "c" to 2, "d" to 3, "e" to 4, "f" to 5,
    "g" to 6, "h" to 7, "i" to 8, "j" to 9, "k" to 10, "l" to 11
)
val mbtiColors = listOf(
    Color(0xFF834e69), Color(0xFF617c44), Color(0xFF176363), Color(0xFFD1A00C)
)
val mbtiColorMap = mapOf<String, Int>(
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
    whatScreen:Boolean = true,
    titleText:String = "To Be Dated",
    isPhoto:Boolean
) {
    val items = listOf(
        BotNavItem(
            title = "some",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.some_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.some_outlined),
        ),
        BotNavItem(
            title = "Chats",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.chats_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.chats_outlined),
            badgeCount = notifiChat,
        ),
        BotNavItem(
            title = "Searching",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.logo_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.logo_outlined),
            hasNew = notifiSearching,
        ),
        BotNavItem(
            title = "groups",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.groups_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.groups_outlined),
            hasNew = notifiGroup,
        ),
        BotNavItem(
            title = "Profile",
            selectedIcon = ImageVector.vectorResource(id = R.drawable.profile_filled),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.profile_outlined),
        ),
    )

    var selectedItemIndex by rememberSaveable { mutableIntStateOf(2) }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent,
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                NavigationBar(
                    containerColor = AppTheme.colorScheme.background,
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
                            onClick = { selectedItemIndex = index },//HANDLE NAVIGATION
                            label = { Text(text = item.title) },
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
                    colors = TopAppBarColors(
                        containerColor = AppTheme.colorScheme.background,
                        navigationIconContentColor = AppTheme.colorScheme.primary,
                        titleContentColor = AppTheme.colorScheme.secondary,
                        actionIconContentColor = AppTheme.colorScheme.primary,
                        scrolledContainerColor = AppTheme.colorScheme.background
                    ),
                    title = { TopBarText(title= titleText, isPhoto = isPhoto) },//TitleTextGen(title= titleText)},
                    navigationIcon = {
                        if(whatScreen){
                            IconButton(onClick = { /*TODO*/ }) {//Show my default
                                Icon(imageVector = Icons.Default.Menu, contentDescription = "Change Looking tab")
                            }
                        }else{
                            IconButton(onClick = { /*TODO*/ }) { //Showing in stuff like messages, editing profile and stuff
                                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Go back")
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
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
        Box(modifier = Modifier.height(30.dp)){
            Image(painter = photo, contentDescription = "Logo")
        }
    }else{
        Text(
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
        color = AppTheme.colorScheme.surface,
        contentColor = AppTheme.colorScheme.onSurface,
        shape = RoundedCornerShape(4.dp)
    ){
        Box(modifier = Modifier.padding(4.dp, 8.dp)){
            whatsInsideTheBox()
        }
    }
}
@Composable
fun UserInfo(
    name:String,
    bio:String,
    age:String,
    pronouns:String,
    gender:String,
    sign:String,
    mbti:String = "Not Taken",
    promptQ1:String,
    promptA1:String,
    sexOri:String,
    relationshipType:String,
    promptQ2:String,
    promptA2:String,
    smokes: String,
    drinks:String,
    weeds:String,
    promptQ3:String,
    promptA3:String,
    work:String,
    school:String,
    kids:String,
    exercise:String,
    height:String,
    politics:String,
    religion:String,
){
    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp, 0.dp)
    ) {
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = name, style = AppTheme.typography.titleMedium)
            }
        })
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Text(text = age, style = AppTheme.typography.titleLarge)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Text(text = pronouns, style = AppTheme.typography.titleLarge)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Text(text = gender, style = AppTheme.typography.titleLarge)
            }
        })
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
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = promptQ1, style = AppTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = promptA1, style = AppTheme.typography.bodySmall)
            }
        })
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Text(text = sexOri, style = AppTheme.typography.titleLarge)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Text(text = relationshipType, style = AppTheme.typography.titleLarge)
            }
        })
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = promptQ2, style = AppTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = promptA2, style = AppTheme.typography.bodySmall)
            }
        })
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.smoking), contentDescription = "Weeds")
                Text(text = smokes, style = AppTheme.typography.titleLarge)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.drinking), contentDescription = "Weeds")
                Text(text = drinks, style = AppTheme.typography.titleLarge)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.weeds), contentDescription = "Weeds", Modifier.size(25.dp))
                Text(text = weeds, style = AppTheme.typography.titleLarge)
            }
        })
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = promptQ3, style = AppTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = promptA3, style = AppTheme.typography.bodySmall)
            }
        })
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.work), contentDescription = "Weeds")
                Text(text = work, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.school), contentDescription = "Weeds")
                Text(text = school, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.children), contentDescription = "Weeds")
                Text(text = kids, style = AppTheme.typography.titleSmall)
            }
        })
        /*
        TODO add another breaker it will look nicer
         */
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.exercise), contentDescription = "Weeds")
                Text(text = exercise, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.height), contentDescription = "Weeds")
                Text(text = height, style = AppTheme.typography.titleSmall)
            }
        })
        /*
        TODO add another breaker it will look nicer
         */
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.politics), contentDescription = "Weeds")
                Text(text = politics, style = AppTheme.typography.titleSmall)
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.religion), contentDescription = "Weeds")
                Text(text = religion, style = AppTheme.typography.titleSmall)
            }
        })
    }
}