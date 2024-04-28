package com.threegroup.tobedated._signUp.composes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated._signUp.composables.NameQuestion
import com.threegroup.tobedated._signUp.composables.SignUpFormat

@Composable
fun nameScreen(signUpVM: SignUpViewModel):Boolean {
    var name by rememberSaveable { mutableStateOf(signUpVM.getUser().name) }
    SignUpFormat(
        title = "Your Name",
        label = "This is what people will know you ass \nJust your first name is needed",
        enterField = {
            NameQuestion(
                input = name,
                onInputChanged = { input ->
                    name = input
                    signUpVM.setUser("name", input)
                },
            )
        },
    )
    return name.isNotEmpty()
}