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
import com.threegroup.tobedated.shareclasses.models.educationOptions

@Composable
fun educationScreen(signUpVM: SignUpViewModel): Boolean {
    var education by rememberSaveable { mutableStateOf(signUpVM.getUser().education) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().education) }

    SignUpFormat(
        title = "Education Level",
        label = "Your heart is always smarter than your brain",
        enterField = {
            RadioButtonGroup(
                options = educationOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    education = educationOptions[selectedOptionIndex]
                    signUpVM.setUser("education", education)
                    signUpVM.setUserIndex("education", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return education.isNotEmpty()
}