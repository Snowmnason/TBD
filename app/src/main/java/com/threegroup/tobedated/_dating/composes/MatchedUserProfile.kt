package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.composables.OutLinedButton
import com.threegroup.tobedated.composeables.profiles.InsideMatchedProfile
import com.threegroup.tobedated.composeables.searching.SeekingUserInfo
import com.threegroup.tobedated.shareclasses.calcDistance

@Composable
fun MatchedUserProfile(nav: NavHostController, vmDating: DatingViewModel) {
    val talkedUser = vmDating.getTalkedUser()
    val currUser = vmDating.getUser()
    InsideMatchedProfile(
        nav = nav,
        title = talkedUser.name,
        editProfile = {
            val location = calcDistance(talkedUser.location, currUser.location) + " miles"
            SeekingUserInfo(
                user = talkedUser,//usersArray[currentProfileIndex]
                location = location,
                bottomButtons = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(25.dp, 0.dp)
                    ) {
                        OutLinedButton(
                            onClick = {
                                vmDating.reportUser(talkedUser.number, currUser.number)
                                nav.navigate("ChatsScreen")
                            },
                            text = "Report",
                            outLineColor = Color.Red,
                            textColor = Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutLinedButton(
                            onClick = {
                                vmDating.deleteMatch(talkedUser.number, currUser.number)
                                nav.navigate("ChatsScreen")
                                vmDating.getMatchesFlow(currUser.number)
                            },
                            text = "Unmatch",
                            outLineColor = Color.Red
                        )
                    }
                },
            )
        }
    )
}