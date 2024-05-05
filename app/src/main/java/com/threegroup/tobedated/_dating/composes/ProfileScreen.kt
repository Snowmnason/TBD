package com.threegroup.tobedated._dating.composes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.profiles.UserInfo

@Composable
fun ProfileScreen(
    navController: NavHostController,
    vmDating: DatingViewModel,
) {
    val currentUser = vmDating.getUser()
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (currentUser.name.isNotEmpty()) {
            isLoading.value = false
        }
    }


    UserInfo(
        currentUser,
        bioClick = { navController.navigate("BioEdit") },
        prompt1Click = { navController.navigate("PromptEdit/1") },
        prompt2Click = { navController.navigate("PromptEdit/2") },
        prompt3Click = { navController.navigate("PromptEdit/3") },
        photoClick = { navController.navigate("ChangePhoto") },
        doesEdit = true
    )

}