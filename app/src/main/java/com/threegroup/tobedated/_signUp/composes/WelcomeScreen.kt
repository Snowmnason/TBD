package com.threegroup.tobedated._signUp.composes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated.composeables.composables.GenericBodyText
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun welcomeScreen():Boolean {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Spacer(modifier = Modifier.height(55.dp))
        GenericTitleText(
            style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
            text = "Welcome to: To Be Dated"
        )
        Spacer(modifier = Modifier.height(55.dp))
        GenericTitleText(
            style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
            text = "Be Kind"
        )
        Spacer(modifier = Modifier.height(5.dp))
        GenericBodyText(
            style = getAddShadow(style = AppTheme.typography.bodyMedium, "body"),
            text = "Treat everyone with respect!"
        )
        Spacer(modifier = Modifier.height(25.dp))
        GenericTitleText(
            style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
            text = "Be Safe"
        )
        Spacer(modifier = Modifier.height(5.dp))
        GenericBodyText(
            style = getAddShadow(style = AppTheme.typography.bodyMedium, "body"),
            text = "Don't jump into things too quick"
        )
        Spacer(modifier = Modifier.height(25.dp))
        GenericTitleText(
            style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
            text = "Be Dated"
        )
        Spacer(modifier = Modifier.height(5.dp))
        GenericBodyText(
            style = getAddShadow(style = AppTheme.typography.bodyMedium, "body"),
            text = "Have fun an be yourself"
        )
    }
    return true
}