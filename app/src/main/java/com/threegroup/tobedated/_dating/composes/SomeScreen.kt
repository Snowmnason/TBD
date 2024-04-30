package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated._dating.TopAndBotBarsDating
import com.threegroup.tobedated._dating.notifiChat
import com.threegroup.tobedated._dating.notifiGroup
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun SomeScreen(
    navController: NavHostController,
    dating: DatingActivity,
    vmDating: DatingViewModel
) {
    val userId = vmDating.getUser().number
    var passed by remember { mutableIntStateOf(0) }
    var liked by remember { mutableIntStateOf(0) }
    var seen by remember { mutableIntStateOf(0) }
    vmDating.getPasses(
        userId,
        onComplete = {
            total -> passed = total
        }
    )
    vmDating.getPasses(
        userId,
        onComplete = {
                total -> liked = total
        }
    )
    vmDating.getLikes(
        userId,
        onComplete = {
                total -> passed = total
        }
    )
    vmDating.getLikedAndPassedby(
        userId,
        onComplete = {
                total -> seen = total
        }
    )

    val unmeet = 1 //viewmodel call here
    TopAndBotBarsDating(
        dating = dating,
        notifiChat = notifiChat,
        notifiGroup = notifiGroup,
        titleText = "Stats",
        isPhoto = false,
        nav = navController,
        selectedItemIndex = 0,
        settingsButton = { },
        star = vmDating.getUser().star,
        currentScreen = {
            Column(
                modifier = Modifier.padding(horizontal = 25.dp)
            ) {
                GenericTitleText(text = "Your Stats", style = AppTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(12.dp))
                GenericTitleText(text = "• People you passed on: $passed")
                Spacer(modifier = Modifier.height(8.dp))
                GenericTitleText(text = "• People you liked: $liked")
                Spacer(modifier = Modifier.height(8.dp))
                GenericTitleText(text = "• People who seen you: $seen")
                Spacer(modifier = Modifier.height(8.dp))
//                GenericTitleText(text = "• Missed connections: $missed")
//                Spacer(modifier = Modifier.height(24.dp))
                GenericTitleText(
                    text = "Unmeet connections",
                    style = AppTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(12.dp))
                GenericTitleText(text = "• Currently: $unmeet")
                Spacer(modifier = Modifier.height(24.dp))
                GenericTitleText(
                    text = "Profile Suggestions",
                    style = AppTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(12.dp))
                GenericTitleText(text = "• ")
            }

        }
    )
}