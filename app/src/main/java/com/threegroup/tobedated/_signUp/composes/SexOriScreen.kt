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
import com.threegroup.tobedated.shareclasses.models.sexOrientationOptions

@Composable
fun sexOriScreen(signUpVM: SignUpViewModel): Boolean {
    var sexOri by rememberSaveable { mutableStateOf(signUpVM.getUser().sexOrientation) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().sexOrientation) }

    SignUpFormat(
        title = "Sexual Orientation",
        label = "Who do you like?",
        enterField = {
            RadioButtonGroup(
                options = sexOrientationOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    sexOri = sexOrientationOptions[selectedOptionIndex]
                    signUpVM.setUser("sexOrientation", sexOri)
                    signUpVM.setUserIndex("sexOrientation", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return sexOri.isNotEmpty()
}