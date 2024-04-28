package com.threegroup.tobedated._signUp.composes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated._signUp.composables.SignUpFormat
import com.threegroup.tobedated._signUp.composables.getCustomButtonStyle
import com.threegroup.tobedated.composeables.composables.RadioButtonGroup
import com.threegroup.tobedated.shareclasses.models.smokeOptions

@Composable
fun smokeScreen(signUpVM: SignUpViewModel): Boolean {
    var smoke by rememberSaveable { mutableStateOf(signUpVM.getUser().smoke) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().smoke) }

    SignUpFormat(
        title = "Do You Smoke?",
        label = "How do you like to relax?",
        enterField = {
            RadioButtonGroup(
                options = smokeOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    smoke = smokeOptions[selectedOptionIndex]
                    signUpVM.setUser("smoke", smoke)
                    signUpVM.setUserIndex("smoke", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return smoke.isNotEmpty()
}