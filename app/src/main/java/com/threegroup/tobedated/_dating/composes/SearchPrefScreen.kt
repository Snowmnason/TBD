package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.searching.AgeSlider
import com.threegroup.tobedated.composeables.searching.ChangePreferenceScreen
import com.threegroup.tobedated.composeables.searching.ChangeSeekingScreen
import com.threegroup.tobedated.composeables.searching.DistanceSlider
import com.threegroup.tobedated.composeables.searching.InsideSearchSettings
import com.threegroup.tobedated.composeables.searching.OtherPreferences
import com.threegroup.tobedated.composeables.searching.SeekingBox
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun SearchPreferenceScreen(navController: NavHostController, vmDating: DatingViewModel) {
    val currentUser = vmDating.getUser()
    val searchPref by remember { mutableStateOf(currentUser.userPref) }

    val userPref = listOf(searchPref.gender, searchPref.zodiac, searchPref.sexualOri, searchPref.mbti, searchPref.children, searchPref.familyPlans, searchPref.meetUp, searchPref.education,
        searchPref.religion, searchPref.politicalViews, searchPref.relationshipType, searchPref.intentions, searchPref.drink, searchPref.smoke, searchPref.weed
    )

    val pref = listOf("Gender", "Zodiac Sign", "Sexual Orientation", "Mbti", "Children", "Family Plans", "Meeting Up",
        "Education", "Religion", "Political Views", "Relationship Type", "Intentions", "Drink", "Smokes", "Weed"
    )
    InsideSearchSettings(
        nav = navController,
        searchSettings = {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(15.dp, 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AgeSlider(
                    preferredMin = currentUser.userPref.ageRange.min,
                    preferredMax = currentUser.userPref.ageRange.max,
                    vmDating = vmDating,
                    currentUser = currentUser
                )
                Spacer(modifier = Modifier.height(14.dp))
                DistanceSlider(
                    preferredMax = currentUser.userPref.maxDistance,
                    vmDating = vmDating,
                    currentUser = currentUser
                )
                Spacer(modifier = Modifier.height(14.dp))
                SeekingBox(desiredSex = currentUser.seeking, navController)
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(
                    Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.onBackground,
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(6.dp))
                GenericTitleText(text = "Premium Settings")
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.onBackground,
                    thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(14.dp))
                for (i in pref.indices) {
                    OtherPreferences(
                        title = pref[i],
                        navController = navController,
                        searchPref = userPref[i],
                        clickable = true,
                        index = i
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    )
}

@Composable
fun ChangePreference(
    navController: NavHostController,
    title: String,
    index: Int,
    vmDating: DatingViewModel
) {
    if (index == 69420) {
        ChangeSeekingScreen(
            navController,
            title = title,
            vmDating = vmDating,
            index = index,
        )
    } else {
        ChangePreferenceScreen(
            navController,
            title = title,
            vmDating = vmDating,
            index = index,
        )
    }
}