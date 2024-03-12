package com.threegroup.tobedated.composables.DatingScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.threegroup.tobedated.R
import com.threegroup.tobedated.callclass.calcAge
import com.threegroup.tobedated.composables.GenericTitleSmall
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.ageRange
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
    text:String
){
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = colorButton,
            contentColor = colorText,
            disabledContentColor = Color.Gray,
            disabledContainerColor = AppTheme.colorScheme.primary,
        )
    ) {
        Text(text = text)
    }
}
@Composable
fun SimpleOutLinedButton(
    modifier: Modifier,
    onClick: () -> Unit,
    colorText:Color,
    text:String
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
    verify:String = "false",
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
            whatsInsideTheBox()
        }
    }
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
    val photos = listOf(user.image1, user.image2, user.image3, user.image4,)
    var subtract = 0
    if(photos[3] == ""){
        subtract = 1
    }
    val mbtiColor: Color = if(user.image4 != "Not Taken"){
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
            answerList = listOf(user.gender, user.sexOrientation, user.hieght),
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
            answerList = listOf(user.politics,user.religious,user.education),
            iconList = listOf(ImageVector.vectorResource(id = R.drawable.politics), ImageVector.vectorResource(id = R.drawable.religion), ImageVector.vectorResource(id = R.drawable.school)),
        )
        Spacer(modifier = Modifier.height(12.dp))
        val pagerState = rememberPagerState(pageCount = { photos.size - subtract})
        HorizontalPager(state = pagerState, modifier = Modifier.aspectRatio(2f / 3f),) { page ->
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
                text = "Pass"
            )
            Spacer(modifier = Modifier.width(6.dp))
            SimpleButton(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = onClickLike,
                colorButton = AppTheme.colorScheme.primary,
                colorText = AppTheme.colorScheme.onPrimary,
                text = "Like"
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
                onClick = onClickSuggest,
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
            LaunchedEffect(Unit) { state.animateScrollTo(state.maxValue) }
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
fun ageSlider(
    preferredMin:Int,
    preferredMax:Int,
):ageRange{
    var sliderPosition by remember { mutableStateOf(18F..100F) }
    var min:Int by remember { mutableIntStateOf(preferredMin)    }
    var max:Int by remember { mutableIntStateOf(preferredMax)    }
    SimpleBox(
        whatsInsideTheBox = {
            Column(modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 0.dp)) {
                min = (sliderPosition.start.roundToInt())
                max = (sliderPosition.endInclusive.roundToInt())

                GenericTitleSmall(text = "Age Range: $min - $max")
                Spacer(modifier = Modifier.height(4.dp))
                RangeSlider(
                    value = sliderPosition,
                    steps = 82,
                    onValueChange = { range -> sliderPosition = range },
                    valueRange = 18f..100f,
                    onValueChangeFinished = { },
                    colors = SliderColors(
                        thumbColor = AppTheme.colorScheme.primary,
                        activeTrackColor = AppTheme.colorScheme.primary,
                        activeTickColor = Color.Transparent,
                        inactiveTrackColor = Color.White,
                        inactiveTickColor= Color.Transparent,
                        disabledThumbColor= Color.Gray,
                        disabledActiveTrackColor = Color.Gray,
                        disabledActiveTickColor = Color.Gray,
                        disabledInactiveTrackColor = Color.Gray,
                        disabledInactiveTickColor = Color.Gray
                    )
                )
            }
        }
    )
    return ageRange(min, max)
}
@Composable
fun distanceSlider(
    preferredMax:Int,

):Int{
    var sliderPosition by remember { mutableFloatStateOf(25F) }
    var max:Int by remember { mutableIntStateOf(preferredMax)    }
    SimpleBox(
        whatsInsideTheBox = {
            Column(modifier = Modifier.padding(15.dp, 15.dp, 15.dp, 0.dp)) {
                max = (sliderPosition.roundToInt())

                GenericTitleSmall(text = "Maximum Distance: $max")
                Spacer(modifier = Modifier.height(4.dp))
                Slider(
                    value = sliderPosition,
                    steps = 99,
                    onValueChange = { sliderPosition = it },
                    valueRange = 1f..100f,
                    onValueChangeFinished = {  },
                    colors = SliderColors(
                        thumbColor = AppTheme.colorScheme.primary,
                        activeTrackColor = AppTheme.colorScheme.primary,
                        activeTickColor = Color.Transparent,
                        inactiveTrackColor = Color.White,
                        inactiveTickColor= Color.Transparent,
                        disabledThumbColor= Color.Gray,
                        disabledActiveTrackColor = Color.Gray,
                        disabledActiveTickColor = Color.Gray,
                        disabledInactiveTrackColor = Color.Gray,
                        disabledInactiveTickColor = Color.Gray
                    )
                )
            }
        }
    )
    return max
}