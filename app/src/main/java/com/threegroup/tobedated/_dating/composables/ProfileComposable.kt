package com.threegroup.tobedated._dating.composables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarColors
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.threegroup.tobedated.R
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated._signUp.composables.PhotoQuestion
import com.threegroup.tobedated._signUp.composables.PromptAnswer
import com.threegroup.tobedated.shareclasses.composables.GenericBodyText
import com.threegroup.tobedated.shareclasses.composables.GenericLabelText
import com.threegroup.tobedated.shareclasses.composables.GenericTitleText
import com.threegroup.tobedated.shareclasses.composables.PlainTextButton
import com.threegroup.tobedated.shareclasses.composables.TopBarText
import com.threegroup.tobedated.shareclasses.composables.baseAppTextTheme
import com.threegroup.tobedated.shareclasses.models.childrenOptions
import com.threegroup.tobedated.shareclasses.models.curiositiesANDImaginations
import com.threegroup.tobedated.shareclasses.models.drinkOptions
import com.threegroup.tobedated.shareclasses.models.educationOptions
import com.threegroup.tobedated.shareclasses.models.ethnicityOptions
import com.threegroup.tobedated.shareclasses.models.familyOptions
import com.threegroup.tobedated.shareclasses.models.genderOptions
import com.threegroup.tobedated.shareclasses.models.insightsANDReflections
import com.threegroup.tobedated.shareclasses.models.intentionsOptions
import com.threegroup.tobedated.shareclasses.models.meetUpOptions
import com.threegroup.tobedated.shareclasses.models.passionsANDInterests
import com.threegroup.tobedated.shareclasses.models.politicsOptions
import com.threegroup.tobedated.shareclasses.models.pronounOptions
import com.threegroup.tobedated.shareclasses.models.relationshipOptions
import com.threegroup.tobedated.shareclasses.models.religionOptions
import com.threegroup.tobedated.shareclasses.models.sexOrientationOptions
import com.threegroup.tobedated.shareclasses.models.smokeOptions
import com.threegroup.tobedated.shareclasses.models.starOptions
import com.threegroup.tobedated.shareclasses.models.tabs
import com.threegroup.tobedated.shareclasses.models.weedOptions
import com.threegroup.tobedated.shareclasses.theme.AppTheme

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
                GenericTitleText(text = "$title:")
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
                        GenericTitleText(text = option)
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
            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                SimpleBox(
                    edit = true,
                    whatsInsideTheBox = {
                        Column(
                            modifier = Modifier
                                .padding(12.dp, 5.dp, 12.dp, 12.dp)
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
                                GenericLabelText(
                                    text = "$remainingChars/$maxLength",
                                    color = if (remainingChars < 0) Color.Red else AppTheme.colorScheme.onBackground
                                )
                            }
                        }
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
            }else {
                //prompt = ""
                Column(
                    modifier = Modifier.padding(horizontal = 15.dp).fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    SimpleBox(
                        edit = true,
                        whatsInsideTheBox = {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp, 0.dp, 12.dp, 12.dp)
                            ) {
                                PromptAnswer(
                                    input = prompt,
                                    onInputChanged = { input -> prompt = input },
                                    isEnables = true,
                                    height = 150

                                )
                            }
                        }
                    )
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
        title = "Photos",
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


