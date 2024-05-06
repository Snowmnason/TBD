package com.threegroup.tobedated._casual.composes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated.composeables.composables.AlertDialogBox
import com.threegroup.tobedated.composeables.composables.Comeback
import com.threegroup.tobedated.composeables.searching.SearchingButtons
import com.threegroup.tobedated.composeables.searching.SeekingUserInfoC
import com.threegroup.tobedated.composeables.searching.SuggestionDropDown
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.calcDistance
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel

@Composable
fun SearchingScreenC(vmCasual: CasualViewModel, vmApi: ApiViewModel){
    val currentUser = vmCasual.getUser()
    val currentPotentialPairList by vmCasual.potentialUserData.collectAsState()//TODO
    var currentProfileIndex by rememberSaveable { mutableIntStateOf(0) }
    val state = rememberScrollState(0)

    val currentPotentialList = currentPotentialPairList

    var showReport by rememberSaveable { mutableStateOf(false) }
    var showSuggest by rememberSaveable { mutableStateOf(false) }

    var isNext = currentPotentialList.isNotEmpty() && currentProfileIndex < currentPotentialList.size

    var currPotential = if (isNext) currentPotentialList[currentProfileIndex] else MatchedUserModel()


    // Reset scroll state when currentProfileIndex or isNext changes
    LaunchedEffect(currentProfileIndex, isNext) {
        state.scrollTo(0)
    }

    LaunchedEffect(Unit) {
        //TODO This checks to see if the list is empty or not, This NEEDs to be available some hows
        if (currPotential.name != "") {
            //do nothing
        } else {
            isNext =
                false//This is important, if there are no users this shows a blank screen and not crash
        }
        state.animateScrollTo(0)
    }


    ///TODO THIS DOES THE SAME CHECK AS ABOVE to see if there is an available user to prevent crashes
    fun nextProfile() {
        val newPotential: MatchedUserModel = currPotential
        if (newPotential.name != "") {
            currPotential = newPotential///MIGHT change this
        } else {
            isNext =
                false
        }
    }

    if (isNext && vmCasual.getMatchSize() < 3) {
        currPotential.let { currentPotential ->
            var location = "x miles"
            if (currentPotential.location != "error/" && vmCasual.getUser().location != "error/") {
                location = calcDistance(currentPotential.location, currentUser.location) + " miles"
            }
            Column(
                Modifier
                    .verticalScroll(state)
                    .fillMaxSize()
            ) {
                SeekingUserInfoC(
                    user = currentPotential,
                    location = location,
                    bottomButtons = {
                        SearchingButtons(
                            onClickLike = {
                                currentProfileIndex++
                                vmCasual.likeCurrentProfile(
                                    currentUser.number,
                                    currentPotential
                                )
                                nextProfile()
                                /*Add an animation or something*/
                            },
                            onClickPass = {
                                currentProfileIndex++//THIS SHIT CAN GO
                                vmCasual.passCurrentProfile(
                                    currentUser.number,
                                    currentPotential
                                )
                                nextProfile()
                                /*Add an animation or something*/
                            },
                            onClickReport = {
                                showReport = true /*Add an animation or something*/
                            },
                            onClickSuggest = { showSuggest = true /*Add an animation or something*/ },
                        )
                    },
                )
            }
        }
    } else {
        if(vmCasual.getMatchSize() >= 3){
            currentUser.hasThree //TODO need a new has three
            vmCasual.updateUser(currentUser)
            Comeback(text = "You exceeded your match limit", todo = "Chat with the connections you already have!", vmApi = vmApi)
        }else{
            Comeback(text = "There are no users that fit your current filters", todo = "Open your filters to allow more possible connections", vmApi = vmApi)
        }

    }


    if (showReport) {
        AlertDialogBox(
            dialogTitle = "Report!",
            onDismissRequest = { showReport = false },
            dialogText = "This account will be looked into and they will not be able to view your profile",
            onConfirmation = {
                showReport = false
                currentProfileIndex++
                vmCasual.passCurrentProfile(currentUser.number, currPotential)
                vmCasual.reportUser(currPotential.number, currentUser.number)
                nextProfile()
            }
        )
    }
    if (showSuggest) {
        var selectedSuggest by rememberSaveable { mutableStateOf("Nothing") }
        SuggestionDropDown(
            selectedSuggestion = selectedSuggest,
            onSuggestionSelect = { suggest -> selectedSuggest = suggest},
            onDismissRequest = { showSuggest = false },
            onConfirmation = {
                showSuggest = false
                currentProfileIndex++
                vmCasual.passCurrentProfile(currentUser.number, currPotential)
                vmCasual.suggestCurrentProfile(currPotential.number, selectedSuggest)
                nextProfile()
            }
        )
    }

}