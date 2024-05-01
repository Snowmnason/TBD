package com.threegroup.tobedated._signUp.composables.questions

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated.composeables.composables.PlainTextButton
import com.threegroup.tobedated.shareclasses.models.curiositiesANDImaginations
import com.threegroup.tobedated.shareclasses.models.insightsANDReflections
import com.threegroup.tobedated.shareclasses.models.passionsANDInterests
import com.threegroup.tobedated.shareclasses.models.tabs
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun PromptQuestions(nav: NavController, signUpVM: SignUpViewModel, questionNumber:Int){
    var tabIndex by remember { mutableIntStateOf(0) }
    val state = ScrollState(0)
    val stateCol = rememberScrollState()
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
                    text = { Text(text = title, style = AppTheme.typography.bodySmall) },
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
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 15.dp).verticalScroll(stateCol)
            //.scrollable(state, orientation = Orientation.Vertical)
        ) {
            questionsToUse.forEach { quest ->
                if (quest == signUpVM.getUser().promptQ1 || quest == signUpVM.getUser().promptQ2 || quest == signUpVM.getUser().promptQ3) {
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
                            nav.popBackStack()
                            when (questionNumber) {
                                1 -> signUpVM.setUser("promptQ1", quest)
                                2 -> signUpVM.setUser("promptQ2", quest)
                                3 -> signUpVM.setUser("promptQ3", quest)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}