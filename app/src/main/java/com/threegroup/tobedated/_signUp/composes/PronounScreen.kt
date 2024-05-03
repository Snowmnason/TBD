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
import com.threegroup.tobedated.shareclasses.models.pronounOptions

@Composable
fun pronounScreen(signUpVM: SignUpViewModel): Boolean {
    var pronoun by rememberSaveable { mutableStateOf(signUpVM.getUser().pronoun) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().pronoun) }

    SignUpFormat(
        title = "Pronouns",
        label = "How do you go by?",
        enterField = {
            RadioButtonGroup(
                options = pronounOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    pronoun = pronounOptions[selectedOptionIndex]
                    signUpVM.setUser("pronoun", pronoun)
                    signUpVM.setUserIndex("pronoun", newIndex)
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return pronoun.isNotEmpty()
}