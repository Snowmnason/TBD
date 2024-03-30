package com.threegroup.tobedated.composables.datingScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.R
import com.threegroup.tobedated.ui.theme.AppTheme

data class BotNavItem(
    val title:String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNew: Boolean = false,
    val badgeCount:Int? = null
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
    settingsButton: () -> Unit,
    state:ScrollState,
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
            containerColor = if (isSystemInDarkTheme()) Color(0xFF181618) else Color(0xFFCDC2D0),
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
