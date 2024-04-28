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
import com.threegroup.tobedated.shareclasses.models.politicsOptions

@Composable
fun politicsScreen(signUpVM: SignUpViewModel): Boolean {
    var politics by rememberSaveable { mutableStateOf(signUpVM.getUser().politics) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().politics) }

    SignUpFormat(
        title = "Politics",
        label = "We vote for you to find a meaningful connection",
        enterField = {
            RadioButtonGroup(
                options = politicsOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    politics = politicsOptions[selectedOptionIndex]
                    signUpVM.setUser("politics", politics)
                    signUpVM.setUserIndex("politics", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return politics.isNotEmpty()
}