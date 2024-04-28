package com.threegroup.tobedated._signUp.composes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated._signUp.composables.BioQuestion
import com.threegroup.tobedated._signUp.composables.SignUpFormat

@Composable
fun bioScreen(signUpVM: SignUpViewModel):Boolean{
    var bio by rememberSaveable { mutableStateOf(signUpVM.getUser().bio) }
    SignUpFormat(
        title = "Bio",
        label = "",
        enterField = {
            BioQuestion(
                input = bio,
                onInputChanged = { input ->
                    bio = input
                    signUpVM.setUser("bio", input)
                },
            )
        },
    )
    return (bio.length in 16..499)
}