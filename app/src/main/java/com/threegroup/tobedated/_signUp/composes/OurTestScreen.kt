package com.threegroup.tobedated._signUp.composes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated._signUp.composables.PersonalityTest
import com.threegroup.tobedated._signUp.composables.SignUpFormatLong
import com.threegroup.tobedated.shareclasses.models.ourTestQuestions

@Composable
fun ourTestScreen(signUpVM: SignUpViewModel):Boolean{
    val answersList: MutableList<Int> = signUpVM.getOurTest().toMutableList()
    var results by remember { mutableIntStateOf((signUpVM.getUser().testResultTbd)) }

    SignUpFormatLong(
        title = "Our Test",
        label = "We just want to know you a little more",
        enterField = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ourTestQuestions.forEachIndexed { index, quest ->
                    var selectedIndex by remember { mutableIntStateOf(-1) }
                    selectedIndex = if (answersList[index] != -1) {
                        answersList[index]
                    } else {
                        -1
                    }
                    PersonalityTest(
                        //TODO some how get value from this shit, I am thinking don't return the values return the result. so math
                        selectedIndex = selectedIndex,
                        onSelectionChange = { newIndex ->
                            selectedIndex = newIndex
                            // Save the index and the new value in answersList
                            answersList[index] = newIndex
                            signUpVM.setOurTest(index, newIndex)
                            if (!answersList.contains(-1)) {
                                results = 0
                                answersList.forEach { va ->
                                    results += va
                                }
                                //newUser.testResultTbd = results
                                signUpVM.setUser("testResultTbd", results)
                            }
                        },
                        question = quest
                    )
                }
            }
        },
    )
    return true //results != -1//TODO
}