package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.composeables.composables.GenericBodyText
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.messages.MessageStart
import com.threegroup.tobedated.theme.AppTheme

/*
End of Message Screens
Start of Groups Screens
 */
@Composable
fun BlindScreen(navController: NavHostController) {
    val state = rememberScrollState(0)
    Column(
        Modifier
            //.padding(paddingValues)
            .verticalScroll(state)
            .fillMaxSize()
    ) {
        Column(
            Modifier.padding(25.dp, 0.dp),
            verticalArrangement = Arrangement.Center
        ) {
            GenericTitleText(text = "Blind Dates are available Thursday!")
            Spacer(modifier = Modifier.height(2.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = AppTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(12.dp))
            GenericBodyText(text = "This weeks Blind topic will be Anti-Modesty\nWe will encourage you to brag a little to your blind date\nAnd we hope you find a connection")
            HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = AppTheme.colorScheme.tertiary)
        }



        //This is for the feed back system
        MessageStart(
            userPhoto = "blind",
            userName = "Blind date system ",
            userLast = "Message success",
            openChat = {
                //navController.navigate("FeedBackMessagerScreen")
            })
    }
}