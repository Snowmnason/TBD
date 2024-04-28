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
import com.threegroup.tobedated.shareclasses.models.drinkOptions

@Composable
fun drinkScreen(signUpVM: SignUpViewModel): Boolean {
    var drink by rememberSaveable { mutableStateOf(signUpVM.getUser().drink) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().drink) }

    SignUpFormat(
        title = "Do You Drink?",
        label = "How do you like to party?",
        enterField = {
            RadioButtonGroup(
                options = drinkOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    drink = drinkOptions[selectedOptionIndex]
                    signUpVM.setUser("drink", drink)
                    signUpVM.setUserIndex("drink", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return drink.isNotEmpty()
}