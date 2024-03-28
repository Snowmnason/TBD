package com.threegroup.tobedated.composables.datingScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.ui.theme.JoseFinSans
import com.threegroup.tobedated.ui.theme.zodiac

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
    val mbtiColor: Color = if (user.testResultsMbti != "Not Taken") {
        val parts = user.testResultsMbti.split("-")
        val mainType = parts.first()
        mbtiColors[mbtiColorMap[mainType]!!]
    } else {
        AppTheme.colorScheme.onSurface
    }
    //Sign Sign MBTI
    val starSymbol = zodiacToLetterMap[user.star]!!
    val oppositeIndex = (starColorMap[starSymbol]!! + 2) % starColorMap.size
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
            answerList = listOf(user.location, "24 miles"),
            iconList = listOf(ImageVector.vectorResource(id = R.drawable.location), null)
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
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.promptA1, style = AppTheme.typography.bodyLarge) //Prompt Answer
            }
        })
        //Star Sign, MBTI
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row {
                    Text(
                        text = starSymbol,
                        style = getStarTextStyle(oppositeIndex, zodiac),
                        color = starColors[starColorMap[starSymbol]!!]
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = user.star,
                        style = getStarTextStyle(oppositeIndex, JoseFinSans),
                        color = starColors[starColorMap[starSymbol]!!]
                    )//Star Sign
                }
                VerticalDivider(
                    modifier = Modifier.height(20.dp),
                    color = Color(0xFFB39DB7),
                    thickness = 2.dp
                )
                Text(
                    text = user.testResultsMbti,
                    style = AppTheme.typography.titleLarge,
                    color = mbtiColor
                ) //MBTI Results
            }
        })
        //Second prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = user.promptQ2,
                    style = AppTheme.typography.titleSmall
                )///Prompt Questions2
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
    }
}