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
import com.threegroup.tobedated.shareclasses.models.starOptions

@Composable
fun starScreen(signUpVM: SignUpViewModel): Boolean {
    var star by rememberSaveable { mutableStateOf(signUpVM.getUser().star) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().star) }

    SignUpFormat(
        title = "What's your sign?",
        label = "Do the stars say we are in favor?",
        enterField = {
            RadioButtonGroup(
                options = starOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    star = starOptions[selectedOptionIndex]
                    signUpVM.setUser("star", star)
                    signUpVM.setUserIndex("star", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return star.isNotEmpty()
}