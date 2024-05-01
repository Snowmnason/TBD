package com.threegroup.tobedated._signUp.composes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated._signUp.composables.questions.HeightQuestion
import com.threegroup.tobedated._signUp.composables.SignUpFormat
import com.threegroup.tobedated.composeables.composables.rememberPickerState

@Composable
fun heightScreen(signUpVM: SignUpViewModel):Boolean {
    var height by rememberSaveable { mutableStateOf(signUpVM.getUser().height) }
    val valuesPickerState = rememberPickerState()
    DisposableEffect(height) {
        onDispose {
            signUpVM.setUser("height", height)
        }
    }

    height = valuesPickerState.selectedItem
    val feet = remember {
        val heights = mutableListOf<String>()
        heights.add(" ")
        heights.add(" ")
        for (feet in 4..6) {
            for (inches in 0..11) {
                heights.add("$feet'$inches\"")
            }
        }
        heights
    }
    val cms = listOf(" ", " ") + (122..214).map { "$it cm" }
    val cm = remember { cms }
    SignUpFormat(
        title = "How tall are you?",
        label = "Did you know your arms length is as long as your hieght?\nWe just want to know how big your hug is!",
        enterField = {
            HeightQuestion(
                feet = feet,
                cm = cm,
                pickedState = valuesPickerState
            )
        })
    return height.isNotEmpty()
}