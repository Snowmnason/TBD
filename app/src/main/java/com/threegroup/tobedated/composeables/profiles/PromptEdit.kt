package com.threegroup.tobedated.composeables.profiles

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated._signUp.composables.questions.PromptAnswer
import com.threegroup.tobedated.composeables.composables.PlainTextButton
import com.threegroup.tobedated.composeables.composables.SimpleBox
import com.threegroup.tobedated.composeables.searching.ChangePreferenceTopBar
import com.threegroup.tobedated.shareclasses.models.curiositiesANDImaginations
import com.threegroup.tobedated.shareclasses.models.insightsANDReflections
import com.threegroup.tobedated.shareclasses.models.passionsANDInterests
import com.threegroup.tobedated.shareclasses.models.tabs
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun PromptEdit(nav: NavHostController, vmDating: DatingViewModel, questionNumber:Int){
    var question by rememberSaveable { mutableStateOf(true) }
    val currentUser = vmDating.getUser()
    var tabIndex by remember { mutableIntStateOf(0) }
    val state = ScrollState(0)
    var prompt by rememberSaveable { mutableStateOf("") }
    ChangePreferenceTopBar(
        nav = nav,
        title = "IceBreaker $questionNumber",
        changeSettings = {
            if (question) {
                Column(modifier = Modifier.fillMaxSize()) {
                    ScrollableTabRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .scrollable(state, orientation = Orientation.Horizontal),
                        selectedTabIndex = tabIndex,
                        contentColor = AppTheme.colorScheme.secondary,
                        containerColor = Color(0xFFB39DB7)
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                text = {
                                    Text(
                                        text = title,
                                        style = AppTheme.typography.bodySmall
                                    )
                                },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index }
                            )
                        }
                    }
                    var questionsToUse = insightsANDReflections
                    when (tabIndex) {
                        0 -> questionsToUse = insightsANDReflections
                        1 -> questionsToUse = passionsANDInterests
                        2 -> questionsToUse = curiositiesANDImaginations
                    }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 25.dp, vertical = 15.dp)
                        //.scrollable(state, orientation = Orientation.Vertical)
                    ) {
                        questionsToUse.forEach { quest ->
                            if (quest == currentUser.promptQ1 || quest == currentUser.promptQ2 || quest == currentUser.promptQ3) {
                                PlainTextButton(
                                    question = quest,
                                    onClick = { },
                                    enabled = false
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            } else {
                                PlainTextButton(
                                    question = quest,
                                    onClick = {
                                        //nav.popBackStack()
                                        question = false
                                        when (questionNumber) {
                                            1 -> currentUser.promptQ1 = quest
                                            2 -> currentUser.promptQ2 = quest
                                            3 -> currentUser.promptQ3 = quest
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                        }
                    }
                }
            } else {
                //prompt = ""
                Column(
                    modifier = Modifier.padding(horizontal = 15.dp).fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    SimpleBox(
                        edit = true,
                        whatsInsideTheBox = {
                            Column(
                                modifier = Modifier
                                    .padding(12.dp, 0.dp, 12.dp, 12.dp)
                            ) {
                                PromptAnswer(
                                    input = prompt,
                                    onInputChanged = { input -> prompt = input },
                                    isEnables = true,
                                    height = 150

                                )
                            }
                        }
                    )
                }
            }
        },
        save = {
            Button(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                enabled = prompt.length <= 200,
                modifier = Modifier.offset(y = 5.dp),
                onClick = {
                    when (questionNumber) {
                        1 -> currentUser.promptA1 = prompt
                        2 -> currentUser.promptA2 = prompt
                        3 -> currentUser.promptA3 = prompt
                    }
                    nav.popBackStack()
                    vmDating.updateUser(currentUser)
                }
            ) {
                Text(
                    text = "Confirm",
                    style = AppTheme.typography.titleSmall,
                    color = Color(0xFF93C47D)
                )
            }
        }
    )
}