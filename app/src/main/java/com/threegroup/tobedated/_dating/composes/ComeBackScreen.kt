package com.threegroup.tobedated._dating.composes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.composables.Comeback
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.models.AgeRange

@Composable
fun ComeBackScreen(navController: NavHostController, vmApi: ApiViewModel, vmDating: DatingViewModel, inProfile:Boolean=false) {
    val currUser =vmDating.getUser()
    val userPref = currUser.userPref
    val hasPreferencesOtherThanDoesntMatter = userPref.run {
        listOf(
            gender, children, zodiac, mbti, familyPlans, drink,
            education, intentions, meetUp, politicalViews, relationshipType,
            religion, sexualOri, smoke, weed
        ).any { it[0] != "Doesn't Matter" && it.size > 1 }
    }
            if(inProfile){
                Comeback(text = "We had trouble loading your profile", todo = "Everything is there, but please come back later to see it", vmApi = vmApi)
            }else{
                if(vmDating.getMatchSize() >= 3){
                    Comeback(text = "You exceeded your match limit", todo = "Chat with the connections you already have!", vmApi = vmApi)
                }else if(hasPreferencesOtherThanDoesntMatter || userPref.ageRange != AgeRange(18, 80) || userPref.maxDistance != 100){
                    Comeback(text = "There are no users that fit your current filters",
                        todo = "Open your filters to allow more possible connections" //TODO maybe explain each case
                        , vmApi = vmApi
                    )
                }
                else{
                    Comeback(text = "trouble getting possible connections", todo = "Check your internet connection, move screen to update", vmApi = vmApi)
                }
            }

}