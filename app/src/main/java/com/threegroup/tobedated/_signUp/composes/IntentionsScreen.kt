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
import com.threegroup.tobedated.shareclasses.models.intentionsOptions

@Composable
fun intentionsScreen(signUpVM: SignUpViewModel): Boolean {
    var intention by rememberSaveable { mutableStateOf(signUpVM.getUser().intentions) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().intentions) }

    SignUpFormat(
        title = "Dating Intentions",
        label = "What are you looking for?",
        enterField = {
            RadioButtonGroup(
                options = intentionsOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    intention = intentionsOptions[selectedOptionIndex]
                    signUpVM.setUser("intentions", intention)
                    signUpVM.setUserIndex("intentions", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return intention.isNotEmpty()
}