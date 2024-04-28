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
import com.threegroup.tobedated.shareclasses.models.relationshipOptions

@Composable
fun relationshipScreen(signUpVM: SignUpViewModel): Boolean {
    var relationship by rememberSaveable { mutableStateOf(signUpVM.getUser().relationship) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().relationship) }

    SignUpFormat(
        title = "Relationship Type",
        label = "There is always enough to go around",
        enterField = {
            RadioButtonGroup(
                options = relationshipOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    relationship = relationshipOptions[selectedOptionIndex]
                    signUpVM.setUser("relationship", relationship)
                    signUpVM.setUserIndex("relationship", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return relationship.isNotEmpty()
}