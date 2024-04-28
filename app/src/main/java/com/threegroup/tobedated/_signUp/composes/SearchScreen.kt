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
import com.threegroup.tobedated.shareclasses.models.seekingOptions

@Composable
fun searchScreen(signUpVM: SignUpViewModel): Boolean {
    var search by rememberSaveable { mutableStateOf(signUpVM.getUser().seeking) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().seeking) }

    SignUpFormat(
        title = "Searching For?",
        label = "Who are you looking to connect with?",
        enterField = {
            RadioButtonGroup(
                options = seekingOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    search = seekingOptions[selectedOptionIndex]
                    signUpVM.setUser("seeking", search)
                    signUpVM.setUserIndex("seeking", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return search.isNotEmpty()
}