package com.threegroup.tobedated.composeables.messages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.R
import com.threegroup.tobedated.composeables.composables.GenericBodyText
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.composables.OutLinedButton
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.composeables.composables.getTopColors
import com.threegroup.tobedated.shareclasses.api.ApiViewModel
import com.threegroup.tobedated.shareclasses.calcAge
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.theme.AppTheme
import kotlinx.coroutines.launch

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
    chatSettings:@Composable () -> Unit = {},
    hideCall: Boolean = true,
    hideCallButtons:Boolean = true
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AppTheme.colorScheme.background,
    ) {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                        ModalDrawerSheet(
                            drawerContainerColor = AppTheme.colorScheme.background
                        ) {
                            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
                                chatSettings()
                            }
                        }
                },
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ) {
                    Scaffold(
                        modifier = Modifier,//.imePadding()
                        containerColor = Color.Transparent,
                        topBar = {
                            CenterAlignedTopAppBar(
                                modifier = Modifier.height(46.dp).imePadding(),
                                colors = getTopColors(),
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
                                            style = getAddShadow(
                                                style = AppTheme.typography.titleMedium,
                                                "med"
                                            )
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
                                    IconButton(onClick = {
                                        scope.launch {
                                            drawerState.apply {
                                                if (isClosed) open() else close()
                                            }
                                        }
                                    }) {
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
        }
    }
}
@Composable
fun MessagerDraw(
    unmatchButton: () -> Unit = {},
    reportButton: () -> Unit = {},
    currentMatch: MatchedUserModel,
    vmApi: ApiViewModel,
){
    Column(
        modifier = Modifier.padding(25.dp)
    ) {
        Box{
            Spacer(modifier = Modifier.height(24.dp))
            Column {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GenericTitleText(text = currentMatch.name, style = getAddShadow(AppTheme.typography.titleMedium, "med"))
                        GenericTitleText(text = currentMatch.pronoun, style = AppTheme.typography.titleLarge, modifier = Modifier.offset(y = 10.dp))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            GenericBodyText(text = calcAge(currentMatch.birthday).toString())
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GenericBodyText(text = currentMatch.testResultsMbti)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    GenericBodyText(text = currentMatch.bio)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 20.dp)){
                        GenericTitleText(text = "Chat Suggestion", style = getAddShadow(AppTheme.typography.titleMedium, "med"))
                    }

                    Spacer(modifier = Modifier.height(2.dp))
                    HorizontalDivider(modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(6.dp))
                    GenericBodyText(text = "Chat suggestion is coming soon")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Column{
            Spacer(modifier = Modifier.height(8.dp))
            GenericTitleText(text = "Report", style = getAddShadow(AppTheme.typography.titleMedium, "med"))
            Spacer(modifier = Modifier.height(2.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(6.dp))
            OutLinedButton(
                onClick = unmatchButton,
                text = "Unmatch",
                outLineColor = Color.Red
            )
            Spacer(modifier = Modifier.height(12.dp))
            GenericTitleText(text = "Unmatch", style = getAddShadow(AppTheme.typography.titleMedium, "med"))
            Spacer(modifier = Modifier.height(2.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(6.dp))
            OutLinedButton(
                onClick = reportButton,
                text = "Report",
                outLineColor = Color.Red,
                textColor = Color.Red
            )
        }
    }
}