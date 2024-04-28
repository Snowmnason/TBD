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
import com.threegroup.tobedated.shareclasses.models.meetUpOptions

@Composable
fun meetUpScreen(signUpVM: SignUpViewModel): Boolean {
    var metUp by rememberSaveable { mutableStateOf(signUpVM.getUser().meetUp) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().meetUp) }

    SignUpFormat(
        title = "How Comfortable Are You Meeting Up?",
        label = "When's the first date?",
        enterField = {
            RadioButtonGroup(
                options = meetUpOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    metUp = meetUpOptions[selectedOptionIndex]
                    signUpVM.setUser("meetUp", metUp)
                    signUpVM.setUserIndex("meetUp", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return metUp.isNotEmpty()
}