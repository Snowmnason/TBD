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
import com.threegroup.tobedated.shareclasses.models.sexOptions

@Composable
fun sexScreen(signUpVM: SignUpViewModel): Boolean {
    var sex by rememberSaveable { mutableStateOf(signUpVM.getUser().sex) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().sex) }

    SignUpFormat(
        title = "Your Sex",
        label = "What search category will you be in?",
        enterField = {
            RadioButtonGroup(
                options = sexOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    sex = sexOptions[selectedOptionIndex]
                    signUpVM.setUser("sex", sex)
                    signUpVM.setUserIndex("sex", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return sex.isNotEmpty()
}