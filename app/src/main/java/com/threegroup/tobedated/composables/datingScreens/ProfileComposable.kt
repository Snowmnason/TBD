package com.threegroup.tobedated.composables.datingScreens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.threegroup.tobedated.R
import com.threegroup.tobedated.activities.DatingActivity
import com.threegroup.tobedated.callclass.calcAge
import com.threegroup.tobedated.composables.GenericBodyText
import com.threegroup.tobedated.composables.GenericTitleSmall
import com.threegroup.tobedated.composables.baseAppTextTheme
import com.threegroup.tobedated.composables.signUp.BigButton
import com.threegroup.tobedated.composables.signUp.LabelText
import com.threegroup.tobedated.composables.signUp.PhotoQuestion
import com.threegroup.tobedated.composables.signUp.PlainTextButton
import com.threegroup.tobedated.composables.signUp.PromptAnswer
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.childrenOptions
import com.threegroup.tobedated.models.curiositiesANDImaginations
import com.threegroup.tobedated.models.drinkOptions
import com.threegroup.tobedated.models.educationOptions
import com.threegroup.tobedated.models.ethnicityOptions
import com.threegroup.tobedated.models.familyOptions
import com.threegroup.tobedated.models.genderOptions
import com.threegroup.tobedated.models.getMBTIColor
import com.threegroup.tobedated.models.getSmallerTextStyle
import com.threegroup.tobedated.models.getStarSymbol
import com.threegroup.tobedated.models.insightsANDReflections
import com.threegroup.tobedated.models.intentionsOptions
import com.threegroup.tobedated.models.meetUpOptions
import com.threegroup.tobedated.models.passionsANDInterests
import com.threegroup.tobedated.models.politicsOptions
import com.threegroup.tobedated.models.pronounOptions
import com.threegroup.tobedated.models.relationshipOptions
import com.threegroup.tobedated.models.religionOptions
import com.threegroup.tobedated.models.sexOrientationOptions
import com.threegroup.tobedated.models.smokeOptions
import com.threegroup.tobedated.models.starColorMap
import com.threegroup.tobedated.models.starColors
import com.threegroup.tobedated.models.starOptions
import com.threegroup.tobedated.models.tabs
import com.threegroup.tobedated.models.weedOptions
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.viewModels.DatingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsideProfileSettings(
    nav: NavHostController,
    editProfile: @Composable () -> Unit = {},
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
                    title = { TopBarText(title= "Edit Profile", isPhoto = false) },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) { //Showing in stuff like messages, editing profile and stuff
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back), contentDescription = "Go back")
                        }
                    },
                    actions = {}
                )
            },
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
                editProfile()
            }
        }
    }
}
@Composable
fun LogOut(dating: DatingActivity){
    BigButton(
        text = "Log Out",
        onClick = {
            dating.clearUserToken()
    }, isUse = true)
}
@Composable
fun DeleteAccount(
    onClick: () -> Unit
){
    OutlinedButton(onClick = onClick,
        border = BorderStroke(2.dp , Color.Red)

    ) {
        GenericTitleSmall(text = "Delete Account", color = Color.Red)
    }
}
@Composable
fun DeactivateAccount(
    onClick: () -> Unit
){
    OutlinedButton(
        onClick = onClick,
        border = BorderStroke(2.dp , Color.Red)
    ) {
        GenericTitleSmall(text = "Deactivate Account")
    }
}

@Composable
fun SimpleEditBox(
    whatsInsideTheBox: @Composable () -> Unit = {},
    edit:Boolean = false,
    onClick: () -> Unit = {}
){
    val thickness = 1
    val boardColor= Color(0xFFB39DB7)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(thickness.dp, boardColor, shape = RoundedCornerShape(4.dp)),
        color = AppTheme.colorScheme.background,
        contentColor = AppTheme.colorScheme.onBackground,
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(modifier = Modifier
            .padding(4.dp, 8.dp)
            .clickable(enabled = edit, onClick = onClick)) {
            whatsInsideTheBox()
        }
    }
}

@Composable
fun SimpleIconEditBox(
    //whatsInsideTheBox: @Composable () -> Unit = {},
    verify:String = "false",
    answer: String,
    icon: ImageVector?,
    divider:Boolean = false,
    color:Color = AppTheme.colorScheme.onBackground,
){

    Box(modifier = Modifier) {
        if (icon != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    imageVector = icon, contentDescription = "icon", modifier = Modifier
                        .offset(y = (-2).dp)
                        .size(25.dp), tint = AppTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = answer,
                    style = getSmallerTextStyle(color),
                    modifier = Modifier.offset(y = 3.dp),
                )
            }
        } else {
            Text(
                text = answer,
                style = getSmallerTextStyle(color),
                modifier = Modifier.offset(y = 3.dp)
            )
        }
    }
    if(divider){
        VerticalDivider(
            modifier = Modifier.height(20.dp),
            color = Color(0xFFB39DB7),
            thickness = 2.dp
        )
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CurrentUserInfo(
    user: UserModel,
    bioClick: () -> Unit = {},
    prompt1Click: () -> Unit = {},
    prompt2Click: () -> Unit = {},
    prompt3Click: () -> Unit = {},
    photoClick: () -> Unit = {},
) {
    val photos = listOf(user.image1, user.image2, user.image3, user.image4)
    var subtract = 0
    if (photos[3] == "") {
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
        SimpleEditBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconEditBox(answer = calcAge(user.birthday.split("/")), icon = null, divider = true)
                    SimpleIconEditBox(answer = user.ethnicity, icon = null, divider = true)
                    SimpleIconEditBox(answer = user.pronoun, icon = ImageVector.vectorResource(id = R.drawable.height))
                }
            }
        )
        //Sexual orientation, Pronouns, Gender
                        Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconEditBox(answer = user.gender, icon = null, divider = true)
                    SimpleIconEditBox(answer = user.sexOrientation, icon = null, divider = true)
                    SimpleIconEditBox(answer = user.height, icon = ImageVector.vectorResource(id = R.drawable.height))
                }
            }
        )


        //BIO
                        Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(
            whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
//                    Text(text = "Bio", style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.bio, style = AppTheme.typography.bodySmall) //Bio

            }
        },
            edit = true,
            onClick = bioClick
        )
        //MEET UP AND LOCATION
                Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconEditBox(answer = user.meetUp, icon = ImageVector.vectorResource(id = R.drawable.first_date), divider = true)
                    SimpleIconEditBox(answer = "0 miles", icon = ImageVector.vectorResource(id = R.drawable.location))
                }
            }
        )
        //relationship type, Intentions
                Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconEditBox(answer = user.relationship, icon =  ImageVector.vectorResource(id = R.drawable.relationship_type), divider = true)
                    SimpleIconEditBox(answer = user.intentions, icon = ImageVector.vectorResource(id = R.drawable.intentions))
                }
            }
        )
        //First prompt question
                Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = user.promptQ1, style = AppTheme.typography.titleSmall)//Prompt question
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA1, style = AppTheme.typography.bodyLarge) //Prompt Answer
            }
        },
            edit = true,
            onClick = prompt1Click
        )
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
        SimpleEditBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = user.promptQ2,
                    style = AppTheme.typography.titleSmall
                )///Prompt Questions2
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA2, style = AppTheme.typography.bodyLarge) //Prompt Answer 2
            }
        },
            edit = true,
            onClick = prompt2Click
        )
        //Family, Kids
                Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconEditBox(answer = user.children, icon =  ImageVector.vectorResource(id = R.drawable.children), divider = true)
                    SimpleIconEditBox(answer = user.family, icon = ImageVector.vectorResource(id = R.drawable.family))
                }
            }
        )
        //Third Prompt Question
                Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = user.promptQ3,
                    style = AppTheme.typography.titleSmall
                )//Prompt Question 3
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA3, style = AppTheme.typography.bodyLarge) ///Prompt answer 3
            }
        },
            edit = true,
            onClick = prompt3Click
        )
        //Smokes, drinks, weeds
                Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconEditBox(answer = user.drink, icon = ImageVector.vectorResource(id = R.drawable.smoking), divider = true)
                    SimpleIconEditBox(answer = user.smoke, icon = ImageVector.vectorResource(id = R.drawable.drinking), divider = true)
                    SimpleIconEditBox(answer = user.weed, icon = ImageVector.vectorResource(id = R.drawable.weeds))
                }
            }
        )
        /*
        TODO add another breaker it will look nicer
         */

        /*
        TODO add another breaker it will look nicer
         */
        //Politics Religion School
                Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconEditBox(answer = user.politics, icon = ImageVector.vectorResource(id = R.drawable.politics), divider = true)
                    SimpleIconEditBox(answer = user.religion, icon = ImageVector.vectorResource(id = R.drawable.religion), divider = true)
                    SimpleIconEditBox(answer = user.education, icon = ImageVector.vectorResource(id = R.drawable.school))
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        val pagerState = rememberPagerState(pageCount = { photos.size - subtract })
        Column {
            Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End){
                Button(onClick = photoClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB39DB7),)
                ) {
                    GenericBodyText(text = "Change Photos", color= AppTheme.colorScheme.onSurface)
                }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.aspectRatio(2f / 3f)) { page ->
                AsyncImage(
                    modifier = Modifier.aspectRatio(2f / 3f),
                    model = photos[page],
                    contentDescription = "Profile Photo $page",
                    contentScale = ContentScale.FillBounds  // Ensure photos fill the box without distortion
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
    }
}
@Composable
fun EditProfile(
    title: String,
    navController: NavController,
    userSetting: String,
    clickable: Boolean = false,
    index:Int
) {

    val modifier = if (clickable) {
        Modifier.clickable { navController.navigate("ChangeProfileScreen/$title/$index") }
    } else {
        Modifier
    }

    SimpleBox(
        whatsInsideTheBox = {
            Row(
                modifier = Modifier
                    .padding(15.dp, 15.dp, 15.dp, 0.dp)
                    .fillMaxWidth()
                    .then(modifier),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GenericTitleSmall(text = "$title:")
                //Spacer(modifier = Modifier.height(4.dp))
                GenericBodyText(text = userSetting, modifier.offset(y=0.dp))
            }
        }
    )
}
@Composable
fun ChangeProfile(
    nav: NavHostController,
    vmDating: DatingViewModel,
    title: String = "",
    index:Int
) {
    val currentUser = vmDating.getUser()
    val (opts, userSet) = when (title) {
        "Ethnicity" -> ethnicityOptions to currentUser.ethnicity
        "Pronoun" -> pronounOptions to currentUser.pronoun
        "Gender" -> genderOptions to currentUser.gender
        "Sexual Orientation" -> sexOrientationOptions to currentUser.sexOrientation
        "Meeting Up" -> meetUpOptions to currentUser.meetUp
        "Relationship Type" -> relationshipOptions to currentUser.relationship
        "Intentions" -> intentionsOptions to currentUser.intentions
        "Zodiac Sign" -> starOptions to currentUser.star
        "Children" -> childrenOptions to currentUser.children
        "Family" -> familyOptions to currentUser.family
        "Drink" -> drinkOptions to currentUser.drink
        "Smokes" -> smokeOptions to currentUser.smoke
        "Weed" -> weedOptions to currentUser.weed
        "Political Views" -> politicsOptions to currentUser.politics
        "Education" -> educationOptions to currentUser.education
        "Religion" -> religionOptions to currentUser.religion
        else -> listOf("") to ""
    }
    var userSettings by remember { mutableStateOf(userSet) }
    val checkedItems = remember { mutableStateListOf<String>().apply { add(userSettings) } }
    ChangePreferenceTopBar(
        nav = nav,
        title = title,
        changeSettings = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                opts.forEach { option ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                        .clickable(onClick = {
                            if (!userSettings.contains(option)) {
                                checkedItems.clear()
                                checkedItems.add(option)
                                userSettings = option
                            }
                        }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        GenericTitleSmall(text = option)
                        Checkbox(
                            checked = userSettings.contains(option),
                            onCheckedChange = {
                                if (!userSettings.contains(option)) {
                                    checkedItems.clear()
                                    checkedItems.add(option)
                                    userSettings = option
                                }
                            },
                        )
                    }
                }
            }
        },
        save = {
            Button(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                modifier = Modifier.offset(y = 5.dp),
                onClick = {
                    nav.popBackStack()
                    when (title) {
                        "Ethnicity" -> currentUser.ethnicity = userSettings
                        "Pronoun" -> currentUser.pronoun = userSettings
                        "Gender" -> currentUser.gender = userSettings
                        "Sexual Orientation" -> currentUser.sexOrientation = userSettings
                        "Meeting Up" -> currentUser.meetUp = userSettings
                        "Relationship Type" -> currentUser.relationship = userSettings
                        "Intentions" -> currentUser.intentions = userSettings
                        "Zodiac Sign" -> currentUser.star = userSettings
                        "Children" -> currentUser.children = userSettings
                        "Family" -> currentUser.family = userSettings
                        "Drink" -> currentUser.drink = userSettings
                        "Smokes" -> currentUser.smoke = userSettings
                        "Weed" -> currentUser.weed = userSettings
                        "Political Views" -> currentUser.politics = userSettings
                        "Education" -> currentUser.education = userSettings
                        "Religion" -> currentUser.religion = userSettings
                    }
                    vmDating.updateUser(currentUser)
                    checkedItems.clear()
                }
            ) {
                Text(
                    text = "Confirm",
                    style = AppTheme.typography.titleSmall,
                    color = Color(0xFF93C47D)
                )
            }
        }
    )
}
@Composable
fun BioEdit(
    nav: NavHostController,
    vmDating: DatingViewModel,
) {
    val currentUser = vmDating.getUser()
    var bio by rememberSaveable { mutableStateOf(currentUser.bio) }
    ChangePreferenceTopBar(
        nav = nav,
        title = "Edit Bio",
        changeSettings = {
            Spacer(modifier = Modifier.height(24.dp))
            SimpleBox(
                whatsInsideTheBox = {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        val maxLength = 500
                        val remainingChars = maxLength - bio.length
                        TextField(
                            value = bio,
                            onValueChange = { input -> bio = input },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp)
                                .height(200.dp),
                            textStyle = baseAppTextTheme(),
                            maxLines = 10,
                            colors = OutlinedTextFieldDefaults.colors(
                                cursorColor = AppTheme.colorScheme.primary, // Set cursor color
                                focusedBorderColor = AppTheme.colorScheme.secondary, // Set focused border color
                                unfocusedBorderColor = AppTheme.colorScheme.onSurface, // Set unfocused border color
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrect = true,
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            LabelText(
                                label = "$remainingChars/$maxLength",
                                color = if (remainingChars < 0) Color.Red else AppTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            )
        },
        save = {
            Button(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                modifier = Modifier.offset(y = 5.dp),
                onClick = {
                    nav.popBackStack()
                    currentUser.bio = bio
                    vmDating.updateUser(currentUser)
                }
            ) {
                Text(
                    text = "Confirm",
                    style = AppTheme.typography.titleSmall,
                    color = Color(0xFF93C47D)
                )
            }
        }
    )
}

@Composable
fun PromptEdit(nav:NavHostController, vmDating: DatingViewModel, questionNumber:Int){
    var question by rememberSaveable { mutableStateOf(true) }
    val currentUser = vmDating.getUser()
    var tabIndex by remember { mutableIntStateOf(0) }
    val state = ScrollState(0)
    var prompt by rememberSaveable { mutableStateOf("" ) }
    ChangePreferenceTopBar(
        nav = nav,
        title = "IceBreaker $questionNumber",
        changeSettings = {
            if(question) {
                Column(modifier = Modifier.fillMaxSize()) {
                    ScrollableTabRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scrollable(state, orientation = Orientation.Horizontal),
                        selectedTabIndex = tabIndex,
                        contentColor = AppTheme.colorScheme.secondary,
                        containerColor = Color(0xFFB39DB7)
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                text = {
                                    Text(
                                        text = title,
                                        style = AppTheme.typography.bodySmall
                                    )
                                },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index }
                            )
                        }
                    }
                    var questionsToUse = insightsANDReflections
                    when (tabIndex) {
                        0 -> questionsToUse = insightsANDReflections
                        1 -> questionsToUse = passionsANDInterests
                        2 -> questionsToUse = curiositiesANDImaginations
                    }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 25.dp, vertical = 15.dp)
                        //.scrollable(state, orientation = Orientation.Vertical)
                    ) {
                        questionsToUse.forEach { quest ->
                            if (quest == currentUser.promptQ1 || quest == currentUser.promptQ2 || quest == currentUser.promptQ3) {
                                PlainTextButton(
                                    question = quest,
                                    onClick = { },
                                    enabled = false
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            } else {
                                PlainTextButton(
                                    question = quest,
                                    onClick = {
                                        //nav.popBackStack()
                                        question = false
                                        when (questionNumber) {
                                            1 -> currentUser.promptQ1 = quest
                                            2 -> currentUser.promptQ2 = quest
                                            3 -> currentUser.promptQ3 = quest
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }
            }else{
                //prompt = ""
                SimpleBox(
                    whatsInsideTheBox = {
                        PromptAnswer(
                            input = prompt,
                            onInputChanged = { input  ->  prompt = input },
                            isEnables = true

                        )
                    }
                )
            }
        },
        save = {
            Button(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                enabled = prompt.length <= 200,
                modifier = Modifier.offset(y = 5.dp),
                onClick = {
                    when (questionNumber) {
                        1 -> currentUser.promptA1 = prompt
                        2 -> currentUser.promptA2 = prompt
                        3 -> currentUser.promptA3 = prompt
                    }
                    nav.popBackStack()
                    vmDating.updateUser(currentUser)
                }
            ) {
                Text(
                    text = "Confirm",
                    style = AppTheme.typography.titleSmall,
                    color = Color(0xFF93C47D)
                )
            }
    }
    )
}
@Composable
fun ChangePhoto(
    nav: NavHostController,
    vmDating: DatingViewModel,
    dating: DatingActivity
) {
    val currentUser = vmDating.getUser()
    var photo1 by rememberSaveable { mutableStateOf(currentUser.image1) }
    var photo2 by rememberSaveable { mutableStateOf(currentUser.image2) }
    var photo3 by rememberSaveable { mutableStateOf(currentUser.image3) }
    var photo4 by rememberSaveable { mutableStateOf(currentUser.image4) }
    val nullPhoto = painterResource(id = R.drawable.photoholder)
    var imageUri1 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri2 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri3 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri4 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var upload1 by rememberSaveable { mutableStateOf(false) }
    var upload2 by rememberSaveable { mutableStateOf(false) }
    var upload3 by rememberSaveable { mutableStateOf(false) }
    var upload4 by rememberSaveable { mutableStateOf(false) }

    val galleryLauncher1 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri1 = it
            photo1 = it.toString()
          } })//  newUser.image1 = it.toString()
    val galleryLauncher2 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri2 = it
            photo2 = it.toString()
            } })//newUser.image2 = it.toString()
    val galleryLauncher3 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri3 = it
            photo3 = it.toString()
            } })//newUser.image3 = it.toString()
    val galleryLauncher4 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri4 = it
            photo4 = it.toString()
            } })//newUser.image4 = it.toString()
    ChangePreferenceTopBar(
        nav = nav,
        title = "Change Photos",
        changeSettings = {
            Spacer(modifier = Modifier.height(24.dp))
            SimpleBox(
                whatsInsideTheBox = {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        PhotoQuestion(
                            photo1 = if(imageUri1 != null){ rememberAsyncImagePainter(model = imageUri1) }else{rememberAsyncImagePainter(model = photo1)},
                            photo2 = if(imageUri2 != null){ rememberAsyncImagePainter(model = imageUri2) }else{rememberAsyncImagePainter(model = photo2)},
                            photo3 = if(imageUri3 != null){ rememberAsyncImagePainter(model = imageUri3) }else{rememberAsyncImagePainter(model = photo3)},
                            photo4 = if(imageUri4 != null){ rememberAsyncImagePainter(model = imageUri4) }else if(photo4 != ""){rememberAsyncImagePainter(model = photo4)}else{nullPhoto},
                            onClick1= { galleryLauncher1.launch("image/*")
                                upload1 = true},
                            onClick2= { galleryLauncher2.launch("image/*")
                                upload2 = true},
                            onClick3= { galleryLauncher3.launch("image/*")
                                upload3 = true},
                            onClick4= { galleryLauncher4.launch("image/*")
                                upload4 = true},
                        )
                    }
                }
            )
        },
        save = {
            Button(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                modifier = Modifier.offset(y = 5.dp),
                onClick = {
                    if(upload1){
                        dating.uploadPhotos(photo1, 1, currentUser.number){ newResult ->
                            currentUser.image1 = newResult
                        }
                    }
                    if(upload2){
                        dating.uploadPhotos(photo2, 2, currentUser.number){ newResult ->
                            currentUser.image2 = newResult
                        }
                    }
                    if(upload3){
                        dating.uploadPhotos(photo3, 3, currentUser.number){ newResult ->
                            currentUser.image3 = newResult
                        }
                    }
                    if(upload4){
                        dating.uploadPhotos(photo4, 4, currentUser.number){ newResult ->
                            currentUser.image4 = newResult
                        }
                    }
                    nav.popBackStack()
                    vmDating.updateUser(currentUser)
                }
            ) {
                Text(
                    text = "Confirm",
                    style = AppTheme.typography.titleSmall,
                    color = Color(0xFF93C47D)
                )
            }
        }
    )
}


