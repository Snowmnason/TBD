package com.threegroup.tobedated.composeables.searching

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.threegroup.tobedated.R
import com.threegroup.tobedated.composeables.composables.GenericBodyText
import com.threegroup.tobedated.composeables.composables.PagerIndicator
import com.threegroup.tobedated.composeables.composables.SimpleBox
import com.threegroup.tobedated.composeables.composables.SimpleIconBox
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.shareclasses.calcAge
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.shareclasses.models.getMBTIColor
import com.threegroup.tobedated.shareclasses.models.getStarSymbol
import com.threegroup.tobedated.shareclasses.models.starColorMap
import com.threegroup.tobedated.shareclasses.models.starColors
import com.threegroup.tobedated.theme.AppTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeekingUserInfo(
    user: MatchedUserModel = MatchedUserModel(),
    bioClick: () -> Unit = {},
    prompt1Click: () -> Unit = {},
    prompt2Click: () -> Unit = {},
    prompt3Click: () -> Unit = {},
    photoClick: () -> Unit = {},
    bottomButtons: @Composable () -> Unit = {},
    location:String,
    doesEdit:Boolean = false,
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
                Text(
                    text = user.name,
                    style = getAddShadow(style = AppTheme.typography.titleMedium, "med")
                ) //Name
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
                    SimpleIconBox(
                        answer = (calcAge(user.birthday).toString()),
                        icon = null,
                        divider = true
                    )
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
                    SimpleIconBox(
                        answer = user.height,
                        icon = ImageVector.vectorResource(id = R.drawable.height)
                    )
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
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFB39DB7),
                        thickness = 2.dp
                    )
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
                    SimpleIconBox(
                        answer = user.meetUp,
                        icon = ImageVector.vectorResource(id = R.drawable.first_date),
                        divider = true
                    )
                    SimpleIconBox(
                        answer = location,
                        icon = ImageVector.vectorResource(id = R.drawable.location)
                    )
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
                    SimpleIconBox(
                        answer = user.relationship,
                        icon = ImageVector.vectorResource(id = R.drawable.relationship_type),
                        divider = true
                    )
                    SimpleIconBox(
                        answer = user.intentions,
                        icon = ImageVector.vectorResource(id = R.drawable.intentions)
                    )
                }
            }
        )
        //First prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = user.promptQ1,
                        style = AppTheme.typography.labelSmall
                    )//Prompt question
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFB39DB7),
                        thickness = 2.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = user.promptA1,
                        style = AppTheme.typography.titleSmall
                    ) //Prompt Answer
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
                    SimpleIconBox(
                        answer = user.star,
                        icon = starSymbol,
                        divider = true,
                        color = starColors[starColorMap[user.star]!!]
                    )
                    SimpleIconBox(answer = user.testResultsMbti, icon = null, color = mbtiColor)
                }
            }
        )
        //Second prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = user.promptQ2,
                        style = AppTheme.typography.labelSmall
                    )///Prompt Questions2
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFB39DB7),
                        thickness = 2.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = user.promptA2,
                        style = AppTheme.typography.titleSmall
                    ) //Prompt Answer 2
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
                    SimpleIconBox(
                        answer = user.children,
                        icon = ImageVector.vectorResource(id = R.drawable.children),
                        divider = true
                    )
                    SimpleIconBox(
                        answer = user.family,
                        icon = ImageVector.vectorResource(id = R.drawable.family)
                    )
                }
            }
        )
        //Third Prompt Question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = user.promptQ3,
                        style = AppTheme.typography.labelSmall
                    )//Prompt Question 3
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFB39DB7),
                        thickness = 2.dp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = user.promptA3,
                        style = AppTheme.typography.titleSmall
                    ) ///Prompt answer 3
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
                    SimpleIconBox(
                        answer = user.drink,
                        icon = ImageVector.vectorResource(id = R.drawable.smoking),
                        divider = true
                    )
                    SimpleIconBox(
                        answer = user.smoke,
                        icon = ImageVector.vectorResource(id = R.drawable.drinking),
                        divider = true
                    )
                    SimpleIconBox(
                        answer = user.weed,
                        icon = ImageVector.vectorResource(id = R.drawable.weeds)
                    )
                }
            }
        )
        //Politics Religion School
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(
                        answer = user.politics,
                        icon = ImageVector.vectorResource(id = R.drawable.politics),
                        divider = true
                    )
                    SimpleIconBox(
                        answer = user.religion,
                        icon = ImageVector.vectorResource(id = R.drawable.religion),
                        divider = true
                    )
                    SimpleIconBox(
                        answer = user.education,
                        icon = ImageVector.vectorResource(id = R.drawable.school)
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        val pagerState = rememberPagerState(pageCount = { photos.size - subtract })
        Column {
            if (doesEdit) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = photoClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB39DB7))
                    ) {
                        GenericBodyText(
                            text = "Change Photos",
                            color = AppTheme.colorScheme.onSurface
                        )
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
            Box(modifier = Modifier.padding(vertical = 2.dp)) {
                PagerIndicator(
                    pagerState = pagerState,
                    pageCount = (photos.size - subtract)
                )
            }

        }

        Spacer(modifier = Modifier.height(12.dp))
        if (!doesEdit) {
            bottomButtons()
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SeekingUserInfoC(
    user: MatchedUserModel = MatchedUserModel(),
    bioClick: () -> Unit = {},
    prompt1Click: () -> Unit = {},
    prompt2Click: () -> Unit = {},
    prompt3Click: () -> Unit = {},
    photoClick: () -> Unit = {},
    bottomButtons: @Composable () -> Unit = {},
    location:String,
    doesEdit:Boolean = false,
) {
    val photos = listOf(user.image1, user.image2, user.image3, user.image4)
    var subtract = 0
    if (photos[3] == "") {
        subtract = 1
    }
    //val state = rememberScrollState()
    Column(
        modifier = Modifier
            //.verticalScroll(state)
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
                    if(user.casualAdditions.casualBio != ""){
                        Text(text = user.casualAdditions.casualBio, style = AppTheme.typography.bodySmall) //Bio
                    }else{
                        Text(text = user.bio, style = AppTheme.typography.bodySmall) //Bio
                    }
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
        //relationship type, leaning
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.relationship, icon =  ImageVector.vectorResource(id = R.drawable.relationship_type), divider = true)
                    SimpleIconBox(answer = user.casualAdditions.leaning, icon = ImageVector.vectorResource(id = R.drawable.icecream))
                }
            }
        )
        //First prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = user.casualAdditions.promptQ1, style = AppTheme.typography.labelSmall)//Prompt question
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.casualAdditions.promptA1, style = AppTheme.typography.titleSmall) //Prompt Answer
            }
        },
            edit = doesEdit,
            onClick = prompt1Click
        )
        //Looking, Experience
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.casualAdditions.lookingFor, icon = ImageVector.vectorResource(id = R.drawable.looking), divider = true)
                    SimpleIconBox(answer = user.casualAdditions.experience, icon = null)
                }
            }
        )
        //Second prompt question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = user.casualAdditions.promptQ2, style = AppTheme.typography.labelSmall)///Prompt Questions2
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.casualAdditions.promptA2, style = AppTheme.typography.titleSmall) //Prompt Answer 2
            }
        },
            edit = doesEdit,
            onClick = prompt2Click
        )
        //after care, location
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.casualAdditions.afterCare, icon =  ImageVector.vectorResource(id = R.drawable.aftercare), divider = true)
                    SimpleIconBox(answer = user.casualAdditions.location, icon = ImageVector.vectorResource(id = R.drawable.map))
                }
            }
        )
        //Third Prompt Question
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(whatsInsideTheBox = {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = user.casualAdditions.promptQ3, style = AppTheme.typography.labelSmall)//Prompt Question 3
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = Color(0xFFB39DB7), thickness = 2.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = user.casualAdditions.promptA3, style = AppTheme.typography.titleSmall) ///Prompt answer 3
            }
        },
            edit = doesEdit,
            onClick = prompt3Click
        )
        //Comm, SexHealth
        Spacer(modifier = Modifier.height(12.dp))
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    SimpleIconBox(answer = user.casualAdditions.comm, icon = ImageVector.vectorResource(id = R.drawable.comm), divider = true)
                    SimpleIconBox(answer = user.casualAdditions.sexHealth, icon = ImageVector.vectorResource(id = R.drawable.sexhealth))
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