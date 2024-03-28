package com.threegroup.tobedated.composables.datingScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.threegroup.tobedated.DatingViewModel
import com.threegroup.tobedated.R
import com.threegroup.tobedated.callclass.calcAge
import com.threegroup.tobedated.composables.GenericBodyText
import com.threegroup.tobedated.composables.GenericTitleSmall
import com.threegroup.tobedated.models.AgeRange
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.UserSearchPreferenceModel
import com.threegroup.tobedated.models.childrenList
import com.threegroup.tobedated.models.drinkList
import com.threegroup.tobedated.models.educationList
import com.threegroup.tobedated.models.familyPlansList
import com.threegroup.tobedated.models.genderList
import com.threegroup.tobedated.models.intentionsList
import com.threegroup.tobedated.models.mbtiList
import com.threegroup.tobedated.models.politicalViewsList
import com.threegroup.tobedated.models.relationshipTypeList
import com.threegroup.tobedated.models.religionList
import com.threegroup.tobedated.models.sexualOriList
import com.threegroup.tobedated.models.smokeList
import com.threegroup.tobedated.models.weedList
import com.threegroup.tobedated.models.zodiacList
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.ui.theme.JoseFinSans
import com.threegroup.tobedated.ui.theme.zodiac
import kotlin.math.roundToInt

@Composable
fun getStarTextStyle(oppositeIndex:Int, font: FontFamily): TextStyle {
    return TextStyle(
        fontFamily = font,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        shadow = Shadow(
            color = starColors[oppositeIndex],
            offset = Offset(3f, 4f),
            blurRadius = 4f
        )
    )
}
@Composable
fun getSmallerTextStyle(): TextStyle {
    return TextStyle(
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    )
}
/*
A=Aries         Orange          0xFFf07019
B=Taurus        Brown           0xFF874b2f
C=Capricorn     Yellow          0xFFecab2b
D=Aquarius      Purple          0xFF9a68bf
E=Gemini        Light Green     0xFF6ca169
F=Pisces        Blue            0xFF0e4caf
G=Cancer        Grey            0xFF5c5463
H=Leo           Red             0xFFb9361a
I=Virgo         Dark Green      0xFF345c42
J=Libra         light Blue      0xFF366b8d
K=Scorpio       Blue            0xFF0a434c
L=Sagittarius   Pink            0xFFa0467c

INTJ INTP ENTJ ENTP 834e69
ENFP ENFJ INFP INFJ 617c44
ESFJ ESTJ ISFJ ISTJ 176363
ISTP ISFP ESTP ESFP 71531e

*/
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
val zodiacToLetterMap = mapOf(
    "Aries" to "a", "Taurus" to "b", "Capricorn" to "c", "Aquarius" to "d",
    "Gemini" to "e", "Pisces" to "f", "Cancer" to "g", "Leo" to "h",
    "Virgo" to "i", "Libra" to "j", "Scorpio" to "k", "Sagittarius" to "l", "Ask me" to "m"
)
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
fun SimpleIconBox(
    //whatsInsideTheBox: @Composable () -> Unit = {},
    verify:String = "false",
    answerList: List<String>,
    iconList: List<ImageVector?>
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
    ){
        Box(modifier = Modifier.padding(4.dp, 8.dp)){
            //whatsInsideTheBox()
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                for (index in answerList.indices){
                    iconList[index]?.let { Icon(imageVector = it, contentDescription = "icon", modifier = Modifier
                        .offset(y = (-4).dp)
                        .size(25.dp), tint = AppTheme.colorScheme.primary) }
                    Text(text = answerList[index], style = getSmallerTextStyle(), modifier = Modifier.offset(y = 3.dp))
                    if(index != answerList.size -1){
                        VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                    }
                }
            }
        }
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
    ){
        Box(modifier = Modifier.padding(4.dp, 8.dp)){
            whatsInsideTheBox()
        }
    }
}
@Composable
fun Comeback(){
    GenericBodyText(text = "Come Back when theres more people to see =0)")
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInfo(
    user: UserModel,
    onClickLike: () -> Unit,
    onClickPass: () -> Unit,
    onClickReport: () -> Unit,
    onClickSuggest: () -> Unit,
){
    val photos = listOf(user.image1, user.image2, user.image3, user.image4)
    var subtract = 0
    if(photos[3] == ""){
        subtract = 1
    }
    val mbtiColor: Color = if(user.testResultsMbti != "Not Taken"){
        val parts = user.testResultsMbti.split("-")
        val mainType = parts.first()
        mbtiColors[mbtiColorMap[mainType]!!]
    }else {
        AppTheme.colorScheme.onSurface
    }
    //Sign Sign MBTI
    val starSymbol = zodiacToLetterMap[user.star]!!
    val oppositeIndex = (starColorMap[starSymbol]!! + 2)  % starColorMap.size
    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp, 0.dp)
    ) {


        //Name
        SimpleBox(verify = user.verified, whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = user.name, style = AppTheme.typography.titleMedium) //Name
            }
        })
        //Age, Ethnicity
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(calcAge(user.birthday.split("/")),user.ethnicity,user.pronoun),
            iconList = listOf(null, null, null)
        )
        //Sexual orientation, Pronouns, Gender
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.gender, user.sexOrientation, user.height),
            iconList = listOf( null, null, ImageVector.vectorResource(id = R.drawable.height)),
        )
        //BIO
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
//                    Text(text = "Bio", style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.bio, style = AppTheme.typography.bodySmall) //Bio
            }
        })
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.location,  "24 miles"),
            iconList = listOf(ImageVector.vectorResource(id = R.drawable.location), null)
        )
        //relationship type, Intentions
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.relationship, user.intentions),
            iconList = listOf(ImageVector.vectorResource(id = R.drawable.relationship_type), ImageVector.vectorResource(id = R.drawable.intentions))
        )
        //First prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = user.promptQ1, style = AppTheme.typography.titleSmall)//Prompt question
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA1, style = AppTheme.typography.bodyLarge) //Prompt Answer
            }
        })
        //Star Sign, MBTI
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceEvenly){
                Row{
                    Text(text = starSymbol, style = getStarTextStyle(oppositeIndex, zodiac), color = starColors[starColorMap[starSymbol]!!])
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = user.star, style = getStarTextStyle(oppositeIndex, JoseFinSans), color = starColors[starColorMap[starSymbol]!!])//Star Sign
                }
                VerticalDivider(modifier = Modifier.height(20.dp),color = Color(0xFFB39DB7), thickness = 2.dp)
                Text(text = user.testResultsMbti, style = AppTheme.typography.titleLarge, color = mbtiColor) //MBTI Results
            }
        })
        //Second prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = user.promptQ2, style = AppTheme.typography.titleSmall)///Prompt Questions2
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA2, style = AppTheme.typography.bodyLarge) //Prompt Answer 2
            }
        })
        //Family, Kids
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.children, user.family),
            iconList = listOf(ImageVector.vectorResource(id = R.drawable.children), ImageVector.vectorResource(id = R.drawable.family)),
        )
        //Third Prompt Question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = user.promptQ3, style = AppTheme.typography.titleSmall)//Prompt Question 3
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA3, style = AppTheme.typography.bodyLarge) ///Prompt answer 3
            }
        })
        //Smokes, drinks, weeds
        Spacer(modifier = Modifier.height(12.dp))
        SimpleIconBox(
            answerList = listOf(user.drink,user.smoke,user.weed),
            iconList = listOf(ImageVector.vectorResource(id = R.drawable.smoking), ImageVector.vectorResource(id = R.drawable.drinking), ImageVector.vectorResource(id = R.drawable.weeds)),
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
            answerList = listOf(user.politics,user.religion,user.education),
            iconList = listOf(ImageVector.vectorResource(id = R.drawable.politics), ImageVector.vectorResource(id = R.drawable.religion), ImageVector.vectorResource(id = R.drawable.school)),
        )
        Spacer(modifier = Modifier.height(12.dp))
        val pagerState = rememberPagerState(pageCount = { photos.size - subtract})
        HorizontalPager(state = pagerState, modifier = Modifier.aspectRatio(2f / 3f)) { page ->
            AsyncImage(
                modifier = Modifier.aspectRatio(2f / 3f),
                model = photos[page],
                contentDescription = "Profile Photo $page",
                contentScale = ContentScale.FillBounds  // Ensure photos fill the box without distortion
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
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
                colorText =  Color.Red,
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
        Spacer(modifier = Modifier.height(12.dp))
    }
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
                        vmDating.updateUserPreferences(currentUser.userPref)
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
fun DistanceSlider(
    preferredMax: Int,
    vmDating: DatingViewModel,
    currentUser: UserModel,
) {
    var sliderPosition by remember { mutableStateOf(preferredMax.toFloat()) }

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
                        vmDating.updateUserPreferences(currentUser.userPref)
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
                    /*
                    navigationIcon = {
                        Button(onClick = { nav.popBackStack() },
                            colors = ButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            ))  { //Showing in stuff like messages, editing profile and stuff
                            Text(text = "Cancel",
                                style = AppTheme.typography.titleSmall,
                                color = Color(0xFFB45A76))
                        }
                    }, */
                    actions = save
                )
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
                changeSettings()
            }
        }
    }
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
    vmDating:DatingViewModel,
    title: String = "",
    index:Int
) {

    val opts = when (title) {
        "Gender" -> genderList
        "Zodiac Sign" -> zodiacList
        "Sexual Orientation" -> sexualOriList
        "Mbti" -> mbtiList
        "Children" -> childrenList
        "Family Plans" -> familyPlansList
        "Education" -> educationList
        "Religion" -> religionList
        "Political Views" -> politicalViewsList
        "Relationship Type" -> relationshipTypeList
        "Intentions" -> intentionsList
        "Drink" -> drinkList
        "Smokes" -> smokeList
        "Weed" -> weedList
        else -> listOf("Man", "Woman", "Everyone")
    }
    val currentUser = vmDating.getUser()
    val currPref = currentUser.userPref

    val userPrefList = remember { mutableStateListOf(
        currPref.gender, currPref.zodiac, currPref.sexualOri, currPref.mbti,
        currPref.children, currPref.familyPlans, currPref.education, currPref.religion,
        currPref.politicalViews, currPref.relationshipType, currPref.intentions,
        currPref.drink, currPref.smoke, currPref.weed
    )}
    var currentPreference by remember { mutableStateOf(userPrefList[index]) }

    val checkedItems = remember { mutableStateListOf<String>().apply { addAll(currentPreference) } }
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
                            checked = currentPreference.contains(option),
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    if (option == "Doesn't Matter") {
                                        checkedItems.clear()
                                        checkedItems.add(option)
                                        userPrefList[index] = listOf(option)
                                    } else {
                                        checkedItems.add(option)
                                        checkedItems.remove("Doesn't Matter")
                                        userPrefList[index] = checkedItems.toList()
                                    }
                                } else {
                                    checkedItems.remove(option)
                                    if (checkedItems.isEmpty()) {
                                        checkedItems.add("Doesn't Matter")
                                        userPrefList[index] = listOf("Doesn't Matter")
                                    } else {
                                        userPrefList[index] = checkedItems.toList()
                                    }
                                }
                                val allOptionsSelected = checkedItems.containsAll(opts.filter { it != "Doesn't Matter" })
                                if (allOptionsSelected) {
                                    checkedItems.clear()
                                    checkedItems.add("Doesn't Matter")
                                    userPrefList[index] = listOf("Doesn't Matter")
                                }
                                // Update currentPreference with checkedItems
                                currentPreference = checkedItems
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
                    //userPrefList[index] = currentPreference
                    userPrefList[index] = userPrefList[index].sorted()
                    currentUser.userPref = UserSearchPreferenceModel(
                        gender = userPrefList[0],
                        zodiac = userPrefList[1],
                        sexualOri = userPrefList[2],
                        mbti = userPrefList[3],
                        children = userPrefList[4],
                        familyPlans = userPrefList[5],
                        education = userPrefList[6],
                        religion = userPrefList[7],
                        politicalViews = userPrefList[8],
                        relationshipType = userPrefList[9],
                        intentions = userPrefList[10],
                        drink = userPrefList[11],
                        smoke = userPrefList[12],
                        weed = userPrefList[13]
                    )
                    vmDating.updateUserPreferences(currentUser.userPref)
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


