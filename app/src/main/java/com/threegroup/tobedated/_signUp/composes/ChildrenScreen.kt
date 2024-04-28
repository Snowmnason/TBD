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
import com.threegroup.tobedated.shareclasses.models.childrenOptions

@Composable
fun childrenScreen(signUpVM: SignUpViewModel): Boolean {
    var children by rememberSaveable { mutableStateOf(signUpVM.getUser().children) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().children) }

    SignUpFormat(
        title = "Do You Have Children?",
        label = "Do you have someone else we should think about too?",
        enterField = {
            RadioButtonGroup(
                options = childrenOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    children = childrenOptions[selectedOptionIndex]
                    signUpVM.setUser("children", children)
                    signUpVM.setUserIndex("children", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return children.isNotEmpty()
}