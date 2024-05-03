package com.threegroup.tobedated._dating.composes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated._dating.TopAndBotBarsDating
import com.threegroup.tobedated._dating.notifiChat
import com.threegroup.tobedated._dating.notifiGroup
import com.threegroup.tobedated.composeables.composables.Comeback
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.models.AgeRange

@Composable
fun ComeBackScreen(navController: NavHostController, dating: DatingActivity, vmApi: ApiViewModel, vmDating: DatingViewModel) {
    val currUser =vmDating.getUser()
    val userPref = currUser.userPref
    val hasPreferencesOtherThanDoesntMatter = userPref.run {
        listOf(
            gender, children, zodiac, mbti, familyPlans, drink,
            education, intentions, meetUp, politicalViews, relationshipType,
            religion, sexualOri, smoke, weed
        ).any { it[0] != "Doesn't Matter" && it.size > 1 }
    }
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 2,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        vmApi = vmApi,
        currentScreen = {
            if(vmDating.getMatchSize() >= 3){
                Comeback(text = "You exceeded your match limit", todo = "Chat with the connections you already have!")
            }else if(hasPreferencesOtherThanDoesntMatter || currUser.seeking != "Everyone" || userPref.ageRange != AgeRange(18, 80) || userPref.maxDistance != 100){
                Comeback(text = "There are no users that fit your current filters",
                    todo = "Open your filters to allow more possible connections" //TODO maybe explain each case
                )
            }
            else{
                Comeback(text = "trouble getting possible connections", todo = "Check your internet connection, move screen to update")
            }
        }
    )
}