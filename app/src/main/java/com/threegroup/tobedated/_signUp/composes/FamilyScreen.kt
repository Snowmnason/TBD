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
import com.threegroup.tobedated.shareclasses.models.familyOptions

@Composable
fun familyScreen(signUpVM: SignUpViewModel): Boolean {
    var family by rememberSaveable { mutableStateOf(signUpVM.getUser().family) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().family) }

    SignUpFormat(
        title = "Do You Want Children?",
        label = "What are your plans for your family?",
        enterField = {
            RadioButtonGroup(
                options = familyOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    family = familyOptions[selectedOptionIndex]
                    signUpVM.setUser("family", family)
                    signUpVM.setUserIndex("family", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return family.isNotEmpty()
}