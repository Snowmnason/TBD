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
import com.threegroup.tobedated.shareclasses.models.genderOptions

@Composable
fun genderScreen(signUpVM: SignUpViewModel):Boolean {
    var gender by rememberSaveable { mutableStateOf(signUpVM.getUser().gender) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().gender) }
    SignUpFormat(
        title = "Your Gender",
        label = "What do you identify as",
        enterField = {
            RadioButtonGroup(
                options = genderOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    gender = genderOptions[selectedOptionIndex]
                    signUpVM.setUser("gender", gender)
                    signUpVM.setUserIndex("gender", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return gender.isNotEmpty()
}