package com.threegroup.tobedated._dating.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.threegroup.tobedated.R
import com.threegroup.tobedated.shareclasses.calcAge
import com.threegroup.tobedated.shareclasses.composables.GenericBodyText
import com.threegroup.tobedated.shareclasses.composables.PagerIndicator
import com.threegroup.tobedated.shareclasses.composables.TopBarText
import com.threegroup.tobedated.shareclasses.composables.getAddShadow
import com.threegroup.tobedated.shareclasses.models.UserModel
import com.threegroup.tobedated.shareclasses.models.getMBTIColor
import com.threegroup.tobedated.shareclasses.models.getSmallerTextStyle
import com.threegroup.tobedated.shareclasses.models.getStarSymbol
import com.threegroup.tobedated.shareclasses.models.starColorMap
import com.threegroup.tobedated.shareclasses.models.starColors
import com.threegroup.tobedated.shareclasses.theme.AppTheme

@Composable
fun SimpleBox(
    verify: Boolean = false,
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
fun SimpleIconBox(
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
fun UserInfo(
    user: UserModel = UserModel(),
    //matchUser: MatchUserModel = MatchUserModel(),
    bioClick: () -> Unit = {},
    prompt1Click: () -> Unit = {},
    prompt2Click: () -> Unit = {},
    prompt3Click: () -> Unit = {},
    photoClick: () -> Unit = {},
    bottomButtons: @Composable () -> Unit = {},
    location:String = "X Miles",
    doesEdit:Boolean = false
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
                Text(text = user.name, style = getAddShadow(style = AppTheme.typography.titleMedium, "med")) //Name
            }
        })
        //Age, Ethnicity
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = (calcAge(user.birthday).toString()), icon = null, divider = true)
                    SimpleIconBox(answer = user.ethnicity, icon = null, divider = true)
                    SimpleIconBox(answer = user.pronoun, icon = null)
                }
            }
        )
        //Sexual orientation, Pronouns, Gender
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.gender, icon = null, divider = true)
                    SimpleIconBox(answer = user.sexOrientation, icon = null, divider = true)
                    SimpleIconBox(answer = user.height, icon = ImageVector.vectorResource(id = R.drawable.height))
                }
            }
        )


        //BIO
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(text = "About me", style = AppTheme.typography.labelSmall)//Prompt question
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = user.bio, style = AppTheme.typography.bodySmall) //Bio

                }
            },
            edit = doesEdit,
            onClick = bioClick
        )
        //MEET UP AND LOCATION
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.meetUp, icon = ImageVector.vectorResource(id = R.drawable.first_date), divider = true)
                    SimpleIconBox(answer = location, icon = ImageVector.vectorResource(id = R.drawable.location))
                }
            }
        )
        //relationship type, Intentions
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.relationship, icon =  ImageVector.vectorResource(id = R.drawable.relationship_type), divider = true)
                    SimpleIconBox(answer = user.intentions, icon = ImageVector.vectorResource(id = R.drawable.intentions))
                }
            }
        )
        //First prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = user.promptQ1, style = AppTheme.typography.labelSmall)//Prompt question
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA1, style = AppTheme.typography.titleSmall) //Prompt Answer
            }
        },
            edit = doesEdit,
            onClick = prompt1Click
        )
        //Star Sign, MBTI
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.star, icon = starSymbol, divider = true, color= starColors[starColorMap[user.star]!!])
                    SimpleIconBox(answer = user.testResultsMbti, icon = null, color = mbtiColor)
                }
            }
        )
        //Second prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = user.promptQ2,
                    style = AppTheme.typography.labelSmall
                )///Prompt Questions2
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA2, style = AppTheme.typography.titleSmall) //Prompt Answer 2
            }
        },
            edit = doesEdit,
            onClick = prompt2Click
        )
        //Family, Kids
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.children, icon =  ImageVector.vectorResource(id = R.drawable.children), divider = true)
                    SimpleIconBox(answer = user.family, icon = ImageVector.vectorResource(id = R.drawable.family))
                }
            }
        )
        //Third Prompt Question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = user.promptQ3,
                    style = AppTheme.typography.labelSmall
                )//Prompt Question 3
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA3, style = AppTheme.typography.titleSmall) ///Prompt answer 3
            }
        },
            edit = doesEdit,
            onClick = prompt3Click
        )
        //Smokes, drinks, weeds
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.drink, icon = ImageVector.vectorResource(id = R.drawable.smoking), divider = true)
                    SimpleIconBox(answer = user.smoke, icon = ImageVector.vectorResource(id = R.drawable.drinking), divider = true)
                    SimpleIconBox(answer = user.weed, icon = ImageVector.vectorResource(id = R.drawable.weeds))
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
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.politics, icon = ImageVector.vectorResource(id = R.drawable.politics), divider = true)
                    SimpleIconBox(answer = user.religion, icon = ImageVector.vectorResource(id = R.drawable.religion), divider = true)
                    SimpleIconBox(answer = user.education, icon = ImageVector.vectorResource(id = R.drawable.school))
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        val pagerState = rememberPagerState(pageCount = { photos.size - subtract })
        Column {
            if(doesEdit){
                Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.End){
                    Button(onClick = photoClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB39DB7))
                    ) {
                        GenericBodyText(text = "Change Photos", color= AppTheme.colorScheme.onSurface)
                    }
                }
            }
            HorizontalPager(state = pagerState, modifier = Modifier.aspectRatio(2f / 3f)) { page ->
                AsyncImage(
                    modifier = Modifier.aspectRatio(2f / 3f),
                    model = photos[page],
                    contentDescription = "Profile Photo $page",
                    contentScale = ContentScale.Fit  // Ensure photos fill the box without distortion
                )
            }
            Box(modifier = Modifier.padding(vertical = 2.dp)){
                PagerIndicator(
                    pagerState = pagerState,
                    pageCount = ( photos.size - subtract)
                )
            }

        }

        Spacer(modifier = Modifier.height(12.dp))
        if(!doesEdit){
            bottomButtons()
            Spacer(modifier = Modifier.height(12.dp))
        }

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
                    title = { TopBarText(title= title, isPhoto = false, activity = "dating") },
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