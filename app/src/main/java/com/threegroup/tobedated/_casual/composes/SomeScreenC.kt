package com.threegroup.tobedated._casual.composes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated.R
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun SomeScreenC(vmCasual: CasualViewModel){
    val userId = vmCasual.getUser().number
    var passed by remember { mutableIntStateOf(0) }
    var liked by remember { mutableIntStateOf(0) }
    var seen by remember { mutableIntStateOf(0) }
    var suggestions by remember { mutableStateOf(listOf("")) }
    vmCasual.getPasses(
        userId,
        onComplete = {
                total -> passed = total
        }
    )
    vmCasual.getLikes(
        userId,
        onComplete = {
                total -> liked = total
        }
    )
    vmCasual.getLikedAndPassedby(
        userId,
        onComplete = {
                total -> seen = total
        }
    )
    vmCasual.getSuggestion(
        userId,
        onComplete = { list ->
            suggestions = list
        }
    )
    val state = rememberScrollState(0)
    val unmeet = 1 //viewmodel call here
    LaunchedEffect(Unit) { state.animateScrollTo(0) }

    Column(
        modifier = Modifier
            .verticalScroll(state)
            .padding(horizontal = 25.dp)
            .fillMaxSize()
    ) {
        GenericTitleText(text = "Your Stats", style = AppTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(2.dp))

        HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = AppTheme.colorScheme.tertiary)
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.padding(horizontal = 0.dp).fillMaxSize())
        {
            Row( modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GenericTitleText(text = "• People you Liked on: ")
                GenericTitleText(text = liked.toString())
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row( modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GenericTitleText(text = "• People you passed on: ")
                GenericTitleText(text = passed.toString())
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row( modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GenericTitleText(text = "• People who saw you: ")
                GenericTitleText(text = seen.toString())
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
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
        Column {
            GenericTitleText(text = "• Currently: $unmeet")
        }
        Spacer(modifier = Modifier.height(24.dp))
        if(suggestions.isNotEmpty()){
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
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                suggestions.forEach{ suggestion->
                    Row( modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        GenericTitleText(text = "• $suggestion",)
                        IconButton(modifier = Modifier
                            .offset(y = (-14).dp),
                            onClick = { /*TODO*/ }) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.close), contentDescription = "delete", tint = AppTheme.colorScheme.secondary)
                        }
                    }
                }

            }
        }
    }
}