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
import com.threegroup.tobedated.shareclasses.models.weedOptions

@Composable
fun weedScreen(signUpVM: SignUpViewModel): Boolean {
    var weed by rememberSaveable { mutableStateOf(signUpVM.getUser().weed) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().weed) }

    SignUpFormat(
        title = "Do You Smoke Marijuana?",
        label = "How do you like to relax or party?",
        enterField = {
            RadioButtonGroup(
                options = weedOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    weed = weedOptions[selectedOptionIndex]
                    signUpVM.setUser("weed", weed)
                    signUpVM.setUserIndex("weed", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return weed.isNotEmpty()
}