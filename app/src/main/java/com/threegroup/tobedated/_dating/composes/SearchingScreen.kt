package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated._dating.TopAndBotBarsDating
import com.threegroup.tobedated._dating.notifiChat
import com.threegroup.tobedated._dating.notifiGroup
import com.threegroup.tobedated.composeables.composables.AlertDialogBox
import com.threegroup.tobedated.composeables.composables.Comeback
import com.threegroup.tobedated.composeables.composables.baseAppTextTheme
import com.threegroup.tobedated.composeables.searching.SearchingButtons
import com.threegroup.tobedated.composeables.searching.SeekingUserInfo
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.calcDistance
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.theme.AppTheme

/*
Start of Seeking Screen
 */
@Composable
fun SearchingScreen(
    vmDating: DatingViewModel,
    dating: DatingActivity,
    navController: NavHostController,
    vmApi: ApiViewModel
) {
    val currentUser = vmDating.getUser()
    val currentPotentialPairList by vmDating.potentialUserData.collectAsState()
    var currentProfileIndex by rememberSaveable { mutableIntStateOf(0) }
    val state = rememberScrollState()

    val currentPotentialList = currentPotentialPairList

    var showReport by rememberSaveable { mutableStateOf(false) }
    var showSuggest by rememberSaveable { mutableStateOf(false) }

    var isNext = currentPotentialList.isNotEmpty() && currentProfileIndex < currentPotentialList.size

    var currPotential = if (isNext) currentPotentialList[currentProfileIndex] else MatchedUserModel()


//    val currentUser = vmDating.getUser()
//    var isNext by rememberSaveable { mutableStateOf(true) }
//    var showReport by rememberSaveable { mutableStateOf(false) }
//    var showSuggest by rememberSaveable { mutableStateOf(false) }
//    val currentPotentialPairList by vmDating.potentialUserData.collectAsState()
//    var currentProfileIndex by rememberSaveable { mutableIntStateOf(0)}
//    val state = rememberScrollState()
//    val currentPotentialList = currentPotentialPairList
//    var currPotential = currentPotentialList[currentProfileIndex]



    // Reset scroll state when currentProfileIndex or isNext changes
    LaunchedEffect(currentProfileIndex, isNext) {
        state.scrollTo(0)
    }

    LaunchedEffect(Unit) {
        //TODO This checks to see if the list is empty or not, This NEEDs to be available some hows
        if (currPotential.name != "") {

        } else {
            isNext =
                false//This is important, if there are no users this shows a blank screen and not crash
        }
    }


    ///TODO THIS DOES THE SAME CHECK AS ABOVE to see if there is an available user to prevent crashes
    fun nextProfile() {
        val newPotential: MatchedUserModel = currPotential
        if (newPotential.name != "") {
            currPotential = newPotential///MIGHT change this
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
        vmApi = vmApi,
        currentScreen = {
            if (isNext && vmDating.getMatchSize() < 3) {
                currPotential.let { currentPotential ->
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
                                onClickSuggest = { showSuggest = true /*TODO Add an animation or something*/ },
                            )
                        },
                    )
                }
            } else {
                if(vmDating.getMatchSize() >= 3){
                    currentUser.hasThree
                    vmDating.updateUser(currentUser)
                    Comeback(text = "You exceeded your match limit", todo = "Chat with the connections you already have!")
                }else{
                    Comeback(text = "There are no users that fit your current filters", todo = "Open your filters to allow more possible connections")
                }

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
                vmDating.passCurrentProfile(currentUser.number, currPotential)
                vmDating.reportUser(currPotential.number, currentUser.number)
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
                vmDating.passCurrentProfile(currentUser.number, currPotential)
                vmDating.suggestCurrentProfile(currPotential.number, selectedSuggest)
                nextProfile()
            }
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionDropDown(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    selectedSuggestion:String,
    onSuggestionSelect: (String) -> Unit,

    ){
    val opts = listOf("Fill out Bio", "Better Prompt Questions", "Fill out Prompt Questions", "Different Photos")
    AlertDialog(
        containerColor = AppTheme.colorScheme.surface,
        title = {
            Text(text = "Help make their profile send out!", style = AppTheme.typography.titleLarge, color = AppTheme.colorScheme.onSurface)
        },
        text = {
            Column {
                var isExpanded by remember { mutableStateOf(false) }
                Text(text = "Please select what you think would help improve their profile!\nYou will pass them on confirm",
                    style = AppTheme.typography.body, color = AppTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(isExpanded, { isExpanded = it }) {
                    TextField(
                        value = selectedSuggestion,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(),
                        colors =  TextFieldDefaults.colors(
                            focusedContainerColor = AppTheme.colorScheme.secondary,
                            unfocusedContainerColor = AppTheme.colorScheme.secondary
                        ),
                        textStyle = baseAppTextTheme(),
                    )
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                        modifier = Modifier
                            .background(AppTheme.colorScheme.secondary)
                            .height(250.dp)
                    ) {
                        opts.forEach{ result ->
                            DropdownMenuItem(
                                text = { Text(text = result, style = baseAppTextTheme()) },
                                onClick = {
                                    onSuggestionSelect(result)
                                    isExpanded = false
                                })
                        }
                    }
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },

        confirmButton = {
            TextButton(

                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = "Submit", color = AppTheme.colorScheme.secondary)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = "Cancel", color = AppTheme.colorScheme.secondary)
            }
        }
    )
}
