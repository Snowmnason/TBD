package com.threegroup.tobedated.composeables.messages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.R
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsideMessages(
    messageBar: @Composable () -> Unit = {},
    messages: @Composable () -> Unit,
    titleText:String,
    goToProfile: () -> Unit = {},
    nav: NavHostController,
    startVideoCall: () -> Unit = {},
    startCall:() -> Unit = {},
    chatSettings:() -> Unit,
    hideCall: Boolean = true,
    hideCallButtons:Boolean = true
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AppTheme.colorScheme.background,
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.height(46.dp),
                    colors = TopAppBarColors(
                        containerColor = AppTheme.colorScheme.onTertiary,
                        navigationIconContentColor = AppTheme.colorScheme.primary,
                        titleContentColor = AppTheme.colorScheme.secondary,
                        actionIconContentColor = AppTheme.colorScheme.primary,
                        scrolledContainerColor = AppTheme.colorScheme.background
                    ),
                    title = {
                        Button(
                            onClick = goToProfile,
                            colors = ButtonColors(
                                contentColor = AppTheme.colorScheme.onBackground,
                                containerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = Color.Transparent
                            ),
                        ) {
                            Text(
                                text = titleText,
                                style = getAddShadow(style = AppTheme.typography.titleMedium, "med")
                            )
                        }
                    },//TitleTextGen(title= titleText)},
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) { //Showing in stuff like messages, editing profile and stuff
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.arrow_back),
                                contentDescription = "Go back"
                            )
                        }
                    },
                    actions = {
                        if (hideCall && hideCallButtons) {
                            IconButton(onClick = startVideoCall) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.videocall),
                                    contentDescription = "Settings"
                                )
                            }
                            IconButton(onClick = startCall) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.call),
                                    contentDescription = "Settings"
                                )
                            }
                        }
                        IconButton(onClick = chatSettings) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.settings),
                                contentDescription = "Settings"
                            )
                        }
                    }
                )
            },
            bottomBar = {
                messageBar()
            },
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                messages()

            }
        }
    }
}