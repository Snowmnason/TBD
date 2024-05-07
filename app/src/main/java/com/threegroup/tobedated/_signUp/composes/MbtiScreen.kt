package com.threegroup.tobedated._signUp.composes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated._signUp.composables.SignUpFormatLong
import com.threegroup.tobedated._signUp.composables.questions.MBTIDropDown
import com.threegroup.tobedated._signUp.composables.questions.PersonalityTest
import com.threegroup.tobedated.composeables.composables.GenTextOnlyButton
import com.threegroup.tobedated.shareclasses.models.mbtiQuestion

@Composable
fun mbtiScreen(signUpVM: SignUpViewModel, onNavigate: () -> Unit):Boolean{
    var mbti by rememberSaveable { mutableStateOf(signUpVM.getUser().testResultsMbti) }
    var isSkip by rememberSaveable { mutableStateOf(false) }

    val answersList: MutableList<Int> = signUpVM.getMbti().toMutableList()
    var results by remember { mutableIntStateOf(-1) }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.End
    ) {
        GenTextOnlyButton(
            onClick = { isSkip = true },
            text = "Skip",
            color = Color.Gray
        )
    }
    SignUpFormatLong(
        title = "MBTI",
        label = "What does your personality say about you?",
        enterField = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                mbtiQuestion.forEachIndexed { index, quest ->
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
                            answersList[index] = selectedIndex
                            signUpVM.setMbti(index, selectedIndex)
                            if (!answersList.contains(-1)) {
                                results = 0
                                answersList.forEach { va ->
                                    results += va
                                }
                                //mbti = "Not Taken"
                                mbti = when (results) {
                                    0 -> "Not Taken"
                                    in (1..5) -> "INTP"
                                    in (6..10) -> "ENTJ"
                                    in (11..15) -> "ENTP"
                                    in (16..20) -> "ENFP"
                                    in (21..25) -> "ENFJ"
                                    in (26..30) -> "INFP"
                                    in (31..35) -> "INFJ"
                                    in (36..40) -> "ESFJ"
                                    in (41..46) -> "ESTJ"
                                    in (47..50) -> "ISFJ"
                                    in (51..55) -> "ISTJ"
                                    in (56..58) -> "ISTP"
                                    in (59..65) -> "ISFP"
                                    in (66..69) -> "ESTP"
                                    in (70..80) -> "ESFP"
                                    else -> "INTJ"
                                }
                                signUpVM.setUser("testResultsMbti", mbti)
                            }
                        },
                        question = quest
                    )
                }
            }
        },
    )

    if(isSkip){
        var selectedResult by rememberSaveable { mutableStateOf("Not Taken") }
        MBTIDropDown(
            onConfirmation = {
                isSkip = false
                //newUser.testResultsMbti = selectedResult
                signUpVM.setUser("testResultsMbti", selectedResult)
                onNavigate()
            },
            onDismissRequest = {
                isSkip = false
                //newUser.testResultsMbti = "Not Taken"
                signUpVM.setUser("testResultsMbti", "Not Taken")
                onNavigate()
            },
            selectedMBTI = selectedResult,
            onMBTISelect = { newResult -> selectedResult = newResult }
        )
    }

    return mbti.isNotEmpty()//TODO
}