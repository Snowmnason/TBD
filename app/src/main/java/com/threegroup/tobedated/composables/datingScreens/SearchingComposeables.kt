package com.threegroup.tobedated.composables.datingScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.threegroup.tobedated.R
import com.threegroup.tobedated.composables.GenericBodyText
import com.threegroup.tobedated.composables.GenericTitleSmall
import com.threegroup.tobedated.models.AgeRange
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.childrenList
import com.threegroup.tobedated.models.drinkList
import com.threegroup.tobedated.models.educationList
import com.threegroup.tobedated.models.familyPlansList
import com.threegroup.tobedated.models.genderList
import com.threegroup.tobedated.models.intentionsList
import com.threegroup.tobedated.models.mbtiList
import com.threegroup.tobedated.models.meetUpList
import com.threegroup.tobedated.models.politicalViewsList
import com.threegroup.tobedated.models.relationshipTypeList
import com.threegroup.tobedated.models.religionList
import com.threegroup.tobedated.models.sexualOriList
import com.threegroup.tobedated.models.smokeList
import com.threegroup.tobedated.models.weedList
import com.threegroup.tobedated.models.zodiacList
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.viewModels.DatingViewModel
import kotlin.math.roundToInt


@Composable
fun SimpleButton(
    modifier: Modifier,
    onClick: () -> Unit,
    colorButton:Color,
    colorText:Color,
    text:String,
    enabled:Boolean = true,
){
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = colorButton,
            contentColor = colorText,
            disabledContentColor = Color.Gray,
            disabledContainerColor = AppTheme.colorScheme.primary,
        ),
        enabled = enabled
    ) {
        Text(text = text)
    }
}
@Composable
fun SimpleOutLinedButton(
    modifier: Modifier,
    onClick: () -> Unit,
    colorText:Color,
    text:String,
    enabled:Boolean = true,
){
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = colorText,
            disabledContentColor = Color.Gray,
            disabledContainerColor = AppTheme.colorScheme.primary,
        ),
        contentPadding = PaddingValues(),
        enabled = enabled
    ) {
        Text(text = text)
    }
}
@Composable
fun SearchingButtons(
    onClickLike: () -> Unit,
    onClickPass: () -> Unit,
    onClickReport: () -> Unit,
    onClickSuggest: () -> Unit,
    ){
    Column {
        Row {
            SimpleButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = onClickPass,
                colorButton = Color.Gray,
                colorText = AppTheme.colorScheme.primary,
                text = "Pass",
            )
            Spacer(modifier = Modifier.width(6.dp))
            SimpleButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = onClickLike,
                colorButton = AppTheme.colorScheme.primary,
                colorText = AppTheme.colorScheme.onPrimary,
                text = "Like",
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            SimpleOutLinedButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(24.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                onClick = onClickReport,
                colorText = Color.Red,
                text = "Report",
            )
            Spacer(modifier = Modifier.width(6.dp))
            SimpleOutLinedButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(24.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                onClick = onClickSuggest,
                colorText = Color.Green,
                text = "Suggestion",
            )
        }
    }
}
@Composable
fun Comeback(){
    GenericBodyText(text = "Come Back when theres more people to see =0)")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsideSearchSettings(
    nav: NavHostController,
    searchSettings: @Composable () -> Unit = {},
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
                    title = { TopBarText(title= "Search Preferences", isPhoto = false) },
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
                searchSettings()
            }
        }
    }
}
@Composable
fun AgeSlider(
    preferredMin: Int,
    preferredMax: Int,
    vmDating: DatingViewModel,
    currentUser: UserModel,
) {
    var sliderPosition by remember { mutableStateOf(preferredMin.toFloat()..preferredMax.toFloat()) }

    // Extracting min and max values from the slider position
    val min = (sliderPosition.start.roundToInt())
    val max = (sliderPosition.endInclusive.roundToInt())

    SimpleBox(
        whatsInsideTheBox = {
            Column(modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 0.dp)) {
                // Displaying the age range
                GenericTitleSmall(text = "Age Range: $min - $max")
                Spacer(modifier = Modifier.height(4.dp))

                // Range slider for selecting age range
                RangeSlider(
                    value = sliderPosition,
                    steps = 82,
                    onValueChange = { range -> sliderPosition = range },
                    valueRange = 18f..80f,
                    onValueChangeFinished = {
                        currentUser.userPref.ageRange = AgeRange(min, max)
                        vmDating.updateUser(currentUser)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = AppTheme.colorScheme.primary,
                        activeTrackColor = AppTheme.colorScheme.primary,
                        activeTickColor = Color.Transparent,
                        inactiveTickColor = Color.Transparent,
                        inactiveTrackColor = Color.White,
                    )
                )
            }
        }
    )
}

@Composable
fun DistanceSlider(
    preferredMax: Int,
    vmDating: DatingViewModel,
    currentUser: UserModel,
) {
    var sliderPosition by remember { mutableFloatStateOf(preferredMax.toFloat()) }

    SimpleBox(
        whatsInsideTheBox = {
            Column(modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 0.dp)) {
                val max = sliderPosition.roundToInt()

                // Displaying the maximum distance
                GenericTitleSmall(text = "Maximum Distance: $max")
                Spacer(modifier = Modifier.height(4.dp))

                // Slider for selecting maximum distance
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 1f..100f,
                    onValueChangeFinished = {
                        // Update user preferences with the selected maximum distance
                        currentUser.userPref.maxDistance = max
                        // Update user preferences via the view model
                        vmDating.updateUser(currentUser)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = AppTheme.colorScheme.primary,
                        activeTrackColor = AppTheme.colorScheme.primary,
                        inactiveTrackColor = Color.White,
                        disabledThumbColor = Color.Gray,
                        disabledActiveTrackColor = Color.Gray
                    )
                )
            }
        }
    )
}


@Composable
fun SeekingBox(
    desiredSex:String,
    navController: NavController
    ) {
    val lookingFor by remember { mutableStateOf(desiredSex) }
    SimpleBox(
        whatsInsideTheBox = {
            Column(
                modifier = Modifier
                    .padding(15.dp, 15.dp, 15.dp, 0.dp)
                    .clickable { navController.navigate("ChangePreference/Searching For/69420") }
                    .fillMaxWidth()) {
                GenericTitleSmall(text = "Looking for:")
                Spacer(modifier = Modifier.height(4.dp))
                GenericBodyText(text = lookingFor)
            }
        }
    )
}



@Composable
fun OtherPreferences(
    title: String,
    navController: NavController,
    searchPref: List<String>,
    clickable: Boolean = false,
    index:Int
) {

    val modifier = if (clickable) {
        Modifier.clickable { navController.navigate("ChangePreference/$title/$index") }
    } else {
        Modifier
    }

    SimpleBox(
        whatsInsideTheBox = {
            Column(
                modifier = Modifier
                    .padding(15.dp, 15.dp, 15.dp, 0.dp)
                    .fillMaxWidth()
                    .then(modifier)
            ) {
                GenericTitleSmall(text = "$title:")
                Spacer(modifier = Modifier.height(4.dp))
                GenericBodyText(text = searchPref.joinToString(", "))
            }
        }
    )
}
@Composable
fun ChangePreferenceScreen(
    nav: NavHostController,
    vmDating: DatingViewModel,
    title: String = "",
    index:Int
) {
    val currentUser = vmDating.getUser()
    //val currPref = currentUser.userPref
    val (opts, userSet) = when (title) {
        "Gender" -> genderList to currentUser.userPref.gender
        "Zodiac Sign" -> zodiacList to currentUser.userPref.zodiac
        "Sexual Orientation" -> sexualOriList to currentUser.userPref.sexualOri
        "Mbti" -> mbtiList to currentUser.userPref.mbti
        "Children" -> childrenList to currentUser.userPref.children
        "Family Plans" -> familyPlansList to currentUser.userPref.familyPlans
        "Education" -> educationList to currentUser.userPref.education
        "Religion" -> religionList to currentUser.userPref.religion
        "Political Views" -> politicalViewsList to currentUser.userPref.politicalViews
        "Relationship Type" -> relationshipTypeList to currentUser.userPref.relationshipType
        "Intentions" -> intentionsList to currentUser.userPref.intentions
        "Drink" -> drinkList to currentUser.userPref.drink
        "Smokes" -> smokeList to currentUser.userPref.smoke
        "Weed" -> weedList to currentUser.userPref.weed
        "Meeting Up" -> meetUpList to currentUser.userPref.meetUp
        else -> listOf("") to listOf("")
    }
    var userPrefList by remember { mutableStateOf(userSet)   }

    val checkedItems = remember { mutableStateListOf<String>().apply { addAll(userPrefList) } }
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
                            if (!userPrefList.contains(option)) {
                                userPrefList = if (option == "Doesn't Matter") {
                                    checkedItems.clear()
                                    checkedItems.add(option)
                                    listOf(option)
                                } else {
                                    checkedItems.add(option)
                                    checkedItems.remove("Doesn't Matter")
                                    checkedItems.toList()
                                }
                            } else {
                                checkedItems.remove(option)
                                userPrefList = if (checkedItems.isEmpty()) {
                                    checkedItems.add("Doesn't Matter")
                                    listOf("Doesn't Matter")
                                } else {
                                    checkedItems.toList()
                                }
                            }
                            val allOptionsSelected =
                                checkedItems.containsAll(opts.filter { it != "Doesn't Matter" })
                            if (allOptionsSelected) {
                                checkedItems.clear()
                                checkedItems.add("Doesn't Matter")
                                userPrefList = listOf("Doesn't Matter")
                            }
                            // Update currentPreference with checkedItems
                            userPrefList = checkedItems
                        }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        GenericTitleSmall(text = option)
                        Checkbox(
                            checked = userPrefList.contains(option),
                            onCheckedChange = {},
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
                        "Gender" -> currentUser.userPref.gender = userPrefList.sorted()
                        "Zodiac Sign" -> currentUser.userPref.zodiac = userPrefList.sorted()
                        "Sexual Orientation" -> currentUser.userPref.sexualOri = userPrefList.sorted()
                        "Mbti" -> currentUser.userPref.mbti = userPrefList.sorted()
                        "Children" -> currentUser.userPref.children = userPrefList.sorted()
                        "Family Plans" -> currentUser.userPref.familyPlans = userPrefList.sorted()
                        "Education" -> currentUser.userPref.education = userPrefList.sorted()
                        "Religion" -> currentUser.userPref.religion = userPrefList.sorted()
                        "Political Views" -> currentUser.userPref.politicalViews = userPrefList.sorted()
                        "Relationship Type" -> currentUser.userPref.relationshipType = userPrefList.sorted()
                        "Intentions" -> currentUser.userPref.intentions = userPrefList.sorted()
                        "Drink" -> currentUser.userPref.drink = userPrefList.sorted()
                        "Smokes" -> currentUser.userPref.smoke = userPrefList.sorted()
                        "Weed" -> currentUser.userPref.weed = userPrefList.sorted()
                        "Meeting Up" -> currentUser.userPref.meetUp = userPrefList.sorted()
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
fun ChangeSeekingScreen(
    nav: NavHostController,
    vmDating: DatingViewModel,
    title: String = "",
    index: Int
) {
    val opts = listOf("Man", "Woman", "Everyone")

    val currentUser = vmDating.getUser()
    val currPref = currentUser.seeking

    var currentPreference by remember { mutableStateOf(currPref) }

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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = currentPreference == option,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    currentPreference = option
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        GenericTitleSmall(text = option)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
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
                    currentUser.seeking = currentPreference
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


