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
import com.threegroup.tobedated.shareclasses.models.religionOptions

@Composable
fun religiousScreen(signUpVM: SignUpViewModel): Boolean {
    var religious by rememberSaveable { mutableStateOf(signUpVM.getUser().religion) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().religion) }

    SignUpFormat(
        title = "Religion",
        label = "We believe in you, but what do you believe in?",
        enterField = {
            RadioButtonGroup(
                options = religionOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    religious = religionOptions[selectedOptionIndex]
                    signUpVM.setUser("religion", religious)
                    signUpVM.setUserIndex("religion", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return religious.isNotEmpty()
}