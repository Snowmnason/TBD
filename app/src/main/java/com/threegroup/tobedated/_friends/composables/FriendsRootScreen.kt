package com.threegroup.tobedated._friends.composables

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.R
import com.threegroup.tobedated._friends.FriendsActivity
import com.threegroup.tobedated.shareclasses.composables.NavDraw
import com.threegroup.tobedated.shareclasses.composables.TopBarText
import com.threegroup.tobedated.shareclasses.composables.getBottomColors
import com.threegroup.tobedated.shareclasses.composables.getTopColors
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import kotlinx.coroutines.launch

data class BotNavItem(
    val title:String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNew: Boolean = false,
    val badgeCount:Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAndBotBarsFriends(
    notifiChat:Int = 0,
    notifiGroup:Boolean = false,
    notifiSearching:Boolean = false,
    currentScreen: @Composable () -> Unit = {},
    titleText:String = "To Be Friended",
    isPhoto:Boolean,
    nav: NavHostController,
    selectedItemIndex: Int,
    settingsButton: () -> Unit,
    state: ScrollState = rememberScrollState(),
    friend: FriendsActivity
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
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        ModalNavigationDrawer(

            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = AppTheme.colorScheme.background

                    //AppTheme.colorScheme.background
                ) {
                    NavDraw(
                        colorFriends = AppTheme.colorScheme.primary,
                        datingClickable = {
                            friend.switchActivities("dating")
                        },
                        causalClickable = {
                            friend.switchActivities("causal")
                        },
                        friendsClickable = {}
                    )
                }
            },
        ) {
            Scaffold(
                containerColor = if (isSystemInDarkTheme()) Color(0xFF181816) else Color(0xFFD0CDC2),
                bottomBar = {
                    NavigationBar(
                        containerColor = AppTheme.colorScheme.onTertiary,
                        modifier = Modifier.height(46.dp)
                    ) {
                        items.forEachIndexed { index, item ->
                            NavigationBarItem(
                                colors = getBottomColors(),
                                selected = selectedItemIndex == index,
                                onClick = { //selectedItemIndex = index
                                    nav.navigate(item.title)
                                },//HANDLE NAVIGATION
                                label = { },
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
                        colors = getTopColors(),
                        title = {
                            TopBarText(
                                title = titleText,
                                isPhoto = isPhoto
                            )
                        },//TitleTextGen(title= titleText)},


                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }

                            }) {//Show my default
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.hamburger),
                                    contentDescription = "Change Looking tab"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = settingsButton) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                                    contentDescription = "Settings"
                                )
                            }
                        }
                    )
                },
            ) { paddingValues ->
                LaunchedEffect(Unit) { state.animateScrollTo(0) }
                Column(
                    Modifier
                        .padding(paddingValues)
                        .verticalScroll(state)
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    currentScreen() //All 5 screens go here
                }
            }
        }
    }
}