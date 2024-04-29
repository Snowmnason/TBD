package com.threegroup.tobedated._dating

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.threegroup.tobedated.composeables.composables.Comeback

@Composable
fun ComeBackScreen(navController: NavHostController, dating: DatingActivity) {
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "To Be Dated",
        isPhoto = true,
        nav = navController,
        selectedItemIndex = 2,
        settingsButton = { navController.navigate("SearchPreferenceScreen") },
        star = "Ask me",
        currentScreen = {
            Comeback(text = "currently loading your future connection")
        }
    )
}