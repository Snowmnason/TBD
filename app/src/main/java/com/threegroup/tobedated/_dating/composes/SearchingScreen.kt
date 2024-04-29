package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated._dating.TopAndBotBarsDating
import com.threegroup.tobedated._dating.notifiChat
import com.threegroup.tobedated._dating.notifiGroup
import com.threegroup.tobedated.composeables.composables.AlertDialogBox
import com.threegroup.tobedated.composeables.composables.Comeback
import com.threegroup.tobedated.composeables.searching.SearchingButtons
import com.threegroup.tobedated.composeables.searching.SeekingUserInfo
import com.threegroup.tobedated.shareclasses.calcDistance
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel

/*
Start of Seeking Screen
 */
@Composable
fun SearchingScreen(
    vmDating: DatingViewModel,
    dating: DatingActivity,
    navController: NavHostController
) {
    val currentUser = vmDating.getUser()
    var isNext by rememberSaveable { mutableStateOf(true) }
    var showReport by rememberSaveable { mutableStateOf(false) }
    var currentProfileIndex by rememberSaveable { mutableIntStateOf(0) } ///MIGHT CHANGE THIS
    val currentPotential = remember { mutableStateOf<MatchedUserModel?>(null) }
    val state = rememberScrollState()

    // Reset scroll state when currentProfileIndex or isNext changes
    LaunchedEffect(currentProfileIndex, isNext) {
        state.scrollTo(0)
    }

    LaunchedEffect(Unit) {
        //TODO This checks to see if the list is empty or not, This NEEDs to be avilialbe some hows
        if (vmDating.getNextPotential(currentProfileIndex) != null) {
            currentPotential.value =
                vmDating.getNextPotential(currentProfileIndex)//MIGHT CHANGE THIS
        } else {
            isNext =
                false//This is important, if there are no users this shows a blank screen and not crash
        }
    }


    ///TODO THIS DOES THE SAME CHECK AS ABOVE to see if there is an avilibe user to prevent crashes
    fun nextProfile() {
        val newPotential = vmDating.getNextPotential(currentProfileIndex)
        if (newPotential != null) {
            currentPotential.value = newPotential///MIGHT change this
        } else {
            isNext =
                false//This is important, if there are no users this shows a blank screen and not crash
        }
    }

    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 2,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        state = state,
        star = currentUser.star,
        currentScreen = {
            if (isNext) {
                currentPotential.value?.let { currentPotential ->
                    var location = "x miles"
                    if (currentPotential.location != "error/" && vmDating.getUser().location != "error/") {
                        location = calcDistance(currentPotential.location, currentUser.location) + " miles"
                    }
                    SeekingUserInfo(
                        user = currentPotential,//usersArray[currentProfileIndex]
                        location = location,
                        bottomButtons = {
                            SearchingButtons(
                                onClickLike = {
                                    currentProfileIndex++
                                    vmDating.likeCurrentProfile(
                                        currentUser.number,
                                        currentPotential
                                    )
                                    nextProfile()
                                    /*TODO Add an animation or something*/
                                },
                                onClickPass = {
                                    currentProfileIndex++//THIS SHIT CAN GO
                                    vmDating.passCurrentProfile(
                                        currentUser.number,
                                        currentPotential
                                    )
                                    nextProfile()
                                    /*TODO Add an animation or something*/
                                },
                                onClickReport = {
                                    showReport = true /*TODO Add an animation or something*/
                                },
                                onClickSuggest = { /*TODO Add an animation or something*/ },
                            )
                        },
                    )
                }
            } else {
                Comeback(text = "Come Back when theres more people to see =0)")
            }
        })
    if (showReport) {
        AlertDialogBox(
            dialogTitle = "Report!",
            onDismissRequest = { showReport = false },
            dialogText = "This account will be looked into and they will not be able to view your profile",
            onConfirmation = {
                showReport = false
                currentProfileIndex++
                vmDating.passCurrentProfile(currentUser.number, currentPotential.value!!)
                vmDating.reportUser(currentPotential.value!!.number, currentUser.number)
                nextProfile()
                //nextProfile(vmDating.reportedCurrentPotential(currentProfileIndex, currentPotential.value!!))
            }
        )
    }
}