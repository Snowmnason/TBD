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
import com.threegroup.tobedated.shareclasses.models.ethnicityOptions

@Composable
fun ethnicityScreen(signUpVM: SignUpViewModel): Boolean {
    var ethnicity by rememberSaveable { mutableStateOf(signUpVM.getUser().ethnicity) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().ethnicity) }

    SignUpFormat(
        title = "Ethnicity",
        label = "We're curious about where you are from!",
        enterField = {
            RadioButtonGroup(
                options = ethnicityOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    ethnicity = ethnicityOptions[selectedOptionIndex]
                    signUpVM.setUser("ethnicity", ethnicity)
                    signUpVM.setUserIndex("ethnicity", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return ethnicity.isNotEmpty()
}