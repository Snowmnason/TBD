package com.threegroup.tobedated.composeables.searching

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun ChangeSeekingScreen(
    nav: NavHostController,
    vmDating: DatingViewModel,
    title: String = "",
    index: Int
) {
    val opts = listOf("Male", "Female", "Everyone")

    val currentUser = vmDating.getUser()
    var currPref = currentUser.seeking

    val checkedItems = remember { mutableStateListOf<String>().apply { add(currPref) } }
    ChangePreferenceTopBar(
        nav = nav,
        title = title,
        changeSettings = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                opts.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                            .clickable(onClick = {
                                if (!checkedItems.contains(option)) {
                                    checkedItems.clear()
                                    checkedItems.add(option)
                                    currPref = option
                                }
                            }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GenericTitleText(text = option)
                        Checkbox(
                            checked = checkedItems.contains(option),
                            onCheckedChange = {
                                if (!checkedItems.contains(option)) {
                                    checkedItems.clear()
                                    checkedItems.add(option)
                                    currPref = option
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        },
        save = {
            Button(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                modifier = Modifier.offset(y = 5.dp),
                onClick = {
                    nav.popBackStack()
                    currentUser.seeking = currPref
                    vmDating.updateUser(currentUser)
                }
            ) {
                Text(
                    text = "Confirm",
                    style = AppTheme.typography.titleSmall,
                    color = Color(0xFF93C47D)
                )
            }
        }
    )
}