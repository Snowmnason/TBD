package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.composables.OutLinedButton
import com.threegroup.tobedated.composeables.searching.SeekingUserInfo
import com.threegroup.tobedated.composeables.searching.SeekingUserInfoC
import com.threegroup.tobedated.shareclasses.calcDistance

@Composable
fun MatchedUserProfile(nav: NavHostController, vmDating: DatingViewModel) {
    val talkedUser = vmDating.getTalkedUser()
    val currUser = vmDating.getUser()
    val state = rememberScrollState(0)

    Column(
        Modifier
            //.padding(paddingValues)
            .verticalScroll(state)
            .fillMaxSize()
    ) {
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
                            vmDating.getMatchesFlow(currUser.number)
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
                            vmDating.getMatchesFlow(currUser.number)
                            nav.navigate("ChatsScreen")
                        },
                        text = "Unmatch",
                        outLineColor = Color.Red
                    )
                }
            },
        )
    }
}
@Composable
fun MatchedUserProfileC(nav: NavHostController, vmCasual: CasualViewModel) {
    val talkedUser = vmCasual.getTalkedUser()
    val currUser = vmCasual.getUser()

    Column(
        Modifier
            //.padding(paddingValues)
            .fillMaxSize()
    ) {
        val location = calcDistance(talkedUser.location, currUser.location) + " miles"
        SeekingUserInfoC(
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
                            vmCasual.reportUser(talkedUser.number, currUser.number)
                            vmCasual.getMatchesFlow(currUser.number)
                            nav.navigate("ChatsScreen")
                        },
                        text = "Report",
                        outLineColor = Color.Red,
                        textColor = Color.Red
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutLinedButton(
                        onClick = {
                            vmCasual.deleteMatch(talkedUser.number, currUser.number)
                            vmCasual.getMatchesFlow(currUser.number)
                            nav.navigate("ChatsScreenC")
                        },
                        text = "Unmatch",
                        outLineColor = Color.Red
                    )
                }
            },
        )
    }
}