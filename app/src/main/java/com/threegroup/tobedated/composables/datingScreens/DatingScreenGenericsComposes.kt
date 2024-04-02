package com.threegroup.tobedated.composables.datingScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.threegroup.tobedated.R
import com.threegroup.tobedated.callclass.calcAge
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.getMBTIColor
import com.threegroup.tobedated.models.getSmallerTextStyle
import com.threegroup.tobedated.models.getStarSymbol
import com.threegroup.tobedated.models.starColorMap
import com.threegroup.tobedated.models.starColors
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

@Composable
fun SimpleBox(
    whatsInsideTheBox: @Composable () -> Unit = {},
    verify:Boolean = false,
){
    var thickness = 1
    var boardColor= Color(0xFFB39DB7)
    if(verify){
        thickness = 3
        boardColor = Color(0xFF729CBD)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(thickness.dp, boardColor, shape = RoundedCornerShape(4.dp)),
        color = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(modifier = Modifier.padding(4.dp, 8.dp)) {
            whatsInsideTheBox()
        }
    }
}

@Composable
fun SimpleIconBox(
    //whatsInsideTheBox: @Composable () -> Unit = {},
    verify:String = "false",
    answerList: List<String>,
    iconList: List<ImageVector?>,
){
    var thickness = 1
    var boardColor= Color(0xFFB39DB7)
    if(verify == "true"){
        thickness = 3
        boardColor = Color(0xFF729CBD)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(thickness.dp, boardColor, shape = RoundedCornerShape(4.dp)),
        color = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(modifier = Modifier.padding(4.dp, 8.dp)) {
            //whatsInsideTheBox()
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (index in answerList.indices) {
                    iconList[index]?.let {
                        Icon(
                            imageVector = it, contentDescription = "icon", modifier = Modifier
                                .offset(y = (-4).dp)
                                .size(25.dp), tint = AppTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = answerList[index],
                        style = getSmallerTextStyle(AppTheme.colorScheme.onBackground),
                        modifier = Modifier.offset(y = 3.dp)
                    )
                    if (index != answerList.size - 1) {
                        VerticalDivider(
                            modifier = Modifier.height(20.dp),
                            color = Color(0xFFB39DB7),
                            thickness = 2.dp
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInfo(
    user: UserModel,
    bottomButtons: @Composable () -> Unit = {},
    location:String = "X Miles"
){
    val photos = listOf(user.image1, user.image2, user.image3, user.image4)
    var subtract = 0
    if(photos[3] == ""){
        subtract = 1
    }
    //Sign Sign MBTI
    val mbtiColor = getMBTIColor(user.testResultsMbti)
    val starSymbol = getStarSymbol(user.star)
    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp, 0.dp)
    ) {


        //Name
        SimpleBox(verify = user.verified, whatsInsideTheBox = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = user.name, style = AppTheme.typography.titleMedium) //Name
            }
        })
        //Age, Ethnicity
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(calcAge(user.birthday.split("/")), user.ethnicity, user.pronoun),
            iconList = listOf(null, null, null)
        )
        //Sexual orientation, Pronouns, Gender
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.gender, user.sexOrientation, user.height),
            iconList = listOf(null, null, ImageVector.vectorResource(id = R.drawable.height)),
        )
        //BIO
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
//                    Text(text = "Bio", style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.bio, style = AppTheme.typography.bodySmall) //Bio
            }
        })
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.meetUp, location),
            iconList = listOf(ImageVector.vectorResource(id = R.drawable.first_date), ImageVector.vectorResource(id = R.drawable.location))
        )
        //relationship type, Intentions
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.relationship, user.intentions),
            iconList = listOf(
                ImageVector.vectorResource(id = R.drawable.relationship_type),
                ImageVector.vectorResource(id = R.drawable.intentions)
            )
        )
        //First prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = user.promptQ1, style = AppTheme.typography.titleSmall)//Prompt question
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFB39DB7),
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA1, style = AppTheme.typography.bodyLarge) //Prompt Answer
            }
        })
        //Star Sign, MBTI
        Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconEditBox(answer = user.star, icon = starSymbol, divider = true, color= starColors[starColorMap[user.star]!!])
                    SimpleIconEditBox(answer = user.testResultsMbti, icon = null, color = mbtiColor)
                }
            }
        )
        //Second prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = user.promptQ2,
                    style = AppTheme.typography.titleSmall
                )///Prompt Questions2
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFB39DB7),
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA2, style = AppTheme.typography.bodyLarge) //Prompt Answer 2
            }
        })
        //Family, Kids
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.children, user.family),
            iconList = listOf(
                ImageVector.vectorResource(id = R.drawable.children),
                ImageVector.vectorResource(id = R.drawable.family)
            ),
        )
        //Third Prompt Question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = user.promptQ3,
                    style = AppTheme.typography.titleSmall
                )//Prompt Question 3
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFB39DB7),
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA3, style = AppTheme.typography.bodyLarge) ///Prompt answer 3
            }
        })
        //Smokes, drinks, weeds
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.drink, user.smoke, user.weed),
            iconList = listOf(
                ImageVector.vectorResource(id = R.drawable.smoking),
                ImageVector.vectorResource(id = R.drawable.drinking),
                ImageVector.vectorResource(id = R.drawable.weeds)
            ),
        )
        /*
        TODO add another breaker it will look nicer
         */

        /*
        TODO add another breaker it will look nicer
         */
        //Politics Religion School
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.politics, user.religion, user.education),
            iconList = listOf(
                ImageVector.vectorResource(id = R.drawable.politics),
                ImageVector.vectorResource(id = R.drawable.religion),
                ImageVector.vectorResource(id = R.drawable.school)
            ),
        )
        Spacer(modifier = Modifier.height(12.dp))
        val pagerState = rememberPagerState(pageCount = { photos.size - subtract })
        HorizontalPager(state = pagerState, modifier = Modifier.aspectRatio(2f / 3f)) { page ->
            AsyncImage(
                modifier = Modifier.aspectRatio(2f / 3f),
                model = photos[page],
                contentDescription = "Profile Photo $page",
                contentScale = ContentScale.FillBounds  // Ensure photos fill the box without distortion
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        bottomButtons()
        Spacer(modifier = Modifier.height(12.dp))
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePreferenceTopBar(
    nav: NavHostController,
    title:String = "",
    changeSettings: @Composable () -> Unit = {},
    save: @Composable RowScope.() -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = if (isSystemInDarkTheme()) Color(0xFF181618) else Color(0xFFCDC2D0),
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
                    title = { TopBarText(title= title, isPhoto = false) },
                    navigationIcon = {
                        Button(onClick = { nav.popBackStack() },
                            colors = ButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            ),
                            modifier = Modifier.offset(y=4.dp),
                        )  { //Showing in stuff like messages, editing profile and stuff
                            Text(text = "Cancel",
                                style = AppTheme.typography.titleSmall,
                                color = Color(0xFFB45A76))
                        }
                    },
                    actions = save
                )
            },
        ) {
                paddingValues ->
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(state.value) }//state.maxValue
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(state)
                    .fillMaxSize()
            ){
                //Spacer(modifier = Modifier.height(24.dp))
                changeSettings()
            }
        }
    }
}