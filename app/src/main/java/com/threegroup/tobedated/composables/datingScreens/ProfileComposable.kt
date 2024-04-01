package com.threegroup.tobedated.composables.datingScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.threegroup.tobedated.activities.DatingActivity
import com.threegroup.tobedated.callclass.calcAge
import com.threegroup.tobedated.composables.signUp.BigButton
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.getMBTIColor
import com.threegroup.tobedated.models.getSmallerTextStyle
import com.threegroup.tobedated.models.getStarSymbol
import com.threegroup.tobedated.models.starColorMap
import com.threegroup.tobedated.models.starColors
import com.threegroup.tobedated.ui.theme.AppTheme

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
fun EditProfile(dating: DatingActivity){
    BigButton(
        text = "Log Out",
        onClick = {
            dating.clearUserToken()
    }, isUse = true)
}

@Composable
fun SimpleEditBox(
    whatsInsideTheBox: @Composable () -> Unit = {},
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
        Box(modifier = Modifier.padding(4.dp, 8.dp)) {
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
    color:Color = AppTheme.colorScheme.onBackground
){

    Box() {
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
        SimpleEditBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
//                    Text(text = "Bio", style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.bio, style = AppTheme.typography.bodySmall) //Bio
            }
        })
        //MEET UP AND LOCATION
                Spacer(modifier = Modifier.height(12.dp))
        SimpleEditBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconEditBox(answer = user.meetUp, icon = ImageVector.vectorResource(id = R.drawable.first_date), divider = true)
                    SimpleIconEditBox(answer = user.location, icon = ImageVector.vectorResource(id = R.drawable.location))//TODO Change to "0 miles"
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
        })
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
        })
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
        HorizontalPager(state = pagerState, modifier = Modifier.aspectRatio(2f / 3f)) { page ->
            AsyncImage(
                modifier = Modifier.aspectRatio(2f / 3f),
                model = photos[page],
                contentDescription = "Profile Photo $page",
                contentScale = ContentScale.FillBounds  // Ensure photos fill the box without distortion
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}