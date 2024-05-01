package com.threegroup.tobedated._signUp.composes

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated._signUp.composables.questions.PromptAnswer
import com.threegroup.tobedated._signUp.composables.SignUpFormat
import com.threegroup.tobedated.composeables.composables.GenericLabelText

@Composable
fun promptQuestionsScreen(nav: NavController, signUpVM: SignUpViewModel):Boolean{
    var promptA1 by rememberSaveable { mutableStateOf(signUpVM.getUser().promptA1) }
    var promptA2 by rememberSaveable { mutableStateOf(signUpVM.getUser().promptA2) }
    var promptA3 by rememberSaveable { mutableStateOf(signUpVM.getUser().promptA3) }
    var isEnable1 by rememberSaveable { mutableStateOf(false) }
    var isEnable2 by rememberSaveable { mutableStateOf(false) }
    var isEnable3 by rememberSaveable { mutableStateOf(false) }
    var isAnswered1 by rememberSaveable { mutableStateOf(false) }
    var isAnswered2 by rememberSaveable { mutableStateOf(false) }
    var isAnswered3 by rememberSaveable { mutableStateOf(false) }
    var question1 by rememberSaveable { mutableStateOf("Question 1") }
    var question2 by rememberSaveable { mutableStateOf("Question 2") }
    var question3 by rememberSaveable { mutableStateOf("Question 3") }
    LaunchedEffect(Unit, question1, question2, question3) {
        if (isEnable1) {
            question1 = signUpVM.getUser().promptQ1
        }
        if (isEnable2) {
            question2 = signUpVM.getUser().promptQ2
        }
        if (isEnable3) {
            question3 = signUpVM.getUser().promptQ3
        }
    }
    SignUpFormat(
        title = "Some Ice breakers!",
        label = "Don't be shy, the ice will melt anyway!",
        enterField = {

            OutlinedButton(
                onClick = {
                    nav.navigate("PromptQuestions/1")
                    isEnable1 = true
                })
            {
                GenericLabelText(text = question1)
            }
            PromptAnswer(
                isEnables = isEnable1,
                input = promptA1,
                onInputChanged = { input ->
                    promptA1 = input
                    signUpVM.setUser("promptA1", input)
                    isAnswered1 = promptA1.length <= 200
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    nav.navigate("PromptQuestions/2")
                    isEnable2 = true
                })
            {

                GenericLabelText(text = question2)
            }
            PromptAnswer(
                isEnables = isEnable2,
                input = promptA2,
                onInputChanged = { input ->
                    promptA2 = input
                    signUpVM.setUser("promptA2", input)
                    isAnswered2 = promptA2.length <= 200
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    nav.navigate("PromptQuestions/3")
                    isEnable3 = true
                })
            {
                GenericLabelText(text = question3)
            }
            PromptAnswer(
                isEnables = isEnable3,
                input = promptA3,
                onInputChanged = { input ->
                    promptA3 = input
                    signUpVM.setUser("promptA3", input)
                    isAnswered3 = promptA3.length <= 200
                },
            )
        },
    )
    return (isAnswered1 && isAnswered2 && isAnswered3)
}