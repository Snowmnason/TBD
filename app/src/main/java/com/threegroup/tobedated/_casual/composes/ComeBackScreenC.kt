package com.threegroup.tobedated._casual.composes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated.composeables.composables.Comeback
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.models.AgeRange

@Composable
fun ComeBackScreenC(navController: NavHostController, vmApi: ApiViewModel, vmCasual: CasualViewModel){
    val currUser = vmCasual.getUser()
    val userPref = currUser.userPref
    val hasPreferencesOtherThanDoesntMatter = userPref.run {
        listOf(gender, intentions, meetUp, relationshipType, sexualOri
            ).any { it[0] != "Doesn't Matter" && it.size > 1 }
    }//TODO check to see if this works out actually

    if(vmCasual.getMatchSize() >= 3){
        Comeback(
            text = "You exceeded your match limit",
            todo = "Chat with the connections you already have!",
            vmApi = vmApi
        )
    }else if(hasPreferencesOtherThanDoesntMatter || userPref.ageRange != AgeRange(18, 80) || userPref.maxDistance != 100){
        Comeback(
            text = "There are no users that fit your current filters",
            todo = "Open your filters to allow more possible connections" //TODO maybe explain each case
            , vmApi = vmApi
        )
    }
    else{
        Comeback(
            text = "trouble getting possible connections",
            todo = "Check your internet connection, move screen to update",
            vmApi = vmApi
        )
    }
}