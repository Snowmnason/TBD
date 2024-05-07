package com.threegroup.tobedated.composeables.searching

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.composeables.composables.TopBarText
import com.threegroup.tobedated.composeables.composables.getTopColors
import com.threegroup.tobedated.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePreferenceTopBar(
    nav: NavHostController,
    title:String = "",
    changeSettings: @Composable () -> Unit = {},
    save: @Composable RowScope.() -> Unit = {}
) {
    val newTitle = when(title){
        "Sexual Orientation" -> "Orientation"
        "Relationship Type" -> "Relationship"
        else-> title
    }
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = if (isSystemInDarkTheme()) Color(0xFF181618) else Color(0xFFCDC2D0),
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.height(46.dp),
                    colors = getTopColors(),
                    title = { TopBarText(title = newTitle, isPhoto = false, activity = "dating") },
                    navigationIcon = {
                        Button(
                            onClick = { nav.popBackStack() },
                            colors = ButtonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            ),
                            modifier = Modifier.offset(y = 4.dp),
                        ) { //Showing in stuff like messages, editing profile and stuff
                            Text(
                                text = "Cancel",
                                style = AppTheme.typography.titleSmall,
                                color = Color(0xFFB45A76)
                            )
                        }
                    },
                    actions = save
                )
            },
        ) { paddingValues ->
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(state.value) }//state.maxValue
            Column(
                Modifier
                    .padding(paddingValues)
                    .verticalScroll(state)
                    .fillMaxSize()
            ) {
                //Spacer(modifier = Modifier.height(24.dp))
                changeSettings()
            }
        }
    }
}