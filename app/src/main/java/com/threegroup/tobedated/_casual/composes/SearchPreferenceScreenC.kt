package com.threegroup.tobedated._casual.composes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.searching.AgeSliderC
import com.threegroup.tobedated.composeables.searching.ChangePreferenceScreenC
import com.threegroup.tobedated.composeables.searching.ChangeSeekingScreenC
import com.threegroup.tobedated.composeables.searching.DistanceSliderC
import com.threegroup.tobedated.composeables.searching.OtherPreferences
import com.threegroup.tobedated.composeables.searching.SeekingBox
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun SearchPreferenceScreenC(navController: NavHostController, vmCasual: CasualViewModel){
    val currentUser = vmCasual.getUser()
    val searchPref by remember { mutableStateOf(currentUser.userPref) }

    val userPref = listOf(searchPref.leaning, searchPref.lookingFor, searchPref.sexualOri, searchPref.meetUp,
        searchPref.experience, searchPref.location, searchPref.comm, searchPref.sexHealth, searchPref.afterCare,
    )

    val pref = listOf("Leaning", "Looking For","Sexual Orientation", "Meeting Up",
        "Experience", "Location", "Communication", "Sex Health", "After Care",
    )
    val state = rememberScrollState(0)
    Column(
        Modifier
            .verticalScroll(state)
            .fillMaxSize()
            .padding(15.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AgeSliderC(
            preferredMin = currentUser.userPref.ageRange.min,
            preferredMax = currentUser.userPref.ageRange.max,
            vmDating = vmCasual,
            currentUser = currentUser
        )
        Spacer(modifier = Modifier.height(14.dp))
        DistanceSliderC(
            preferredMax = currentUser.userPref.maxDistance,
            vmDating = vmCasual,
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


@Composable
fun ChangePreferenceC(navController: NavHostController, title:String, index:Int, vmCasual: CasualViewModel){
    if (index == 69420) {
        ChangeSeekingScreenC(
            navController,
            title = title,
            vmDating = vmCasual,
            index = index,
        )
    } else {
        ChangePreferenceScreenC(
            navController,
            title = title,
            vmDating = vmCasual,
            index = index,
        )
    }
}
