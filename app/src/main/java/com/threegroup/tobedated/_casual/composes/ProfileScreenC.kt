package com.threegroup.tobedated._casual.composes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated.composeables.profiles.UserInfoC

@Composable
fun ProfileScreenC(navController: NavHostController, vmCasual: CasualViewModel){
    val currentUser = vmCasual.getUser()
    val isLoading = remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        if (currentUser.name.isNotEmpty()) {
            isLoading.value = false
        }
    }
    UserInfoC(
        currentUser,
        bioClick = { navController.navigate("BioEdit") },
        prompt1Click = { navController.navigate("PromptEdit/1") },
        prompt2Click = { navController.navigate("PromptEdit/2") },
        prompt3Click = { navController.navigate("PromptEdit/3") },
        photoClick = { /*navController.navigate("ChangePhoto") */},
        doesEdit = true
    )

}