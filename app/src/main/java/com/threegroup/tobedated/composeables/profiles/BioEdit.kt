package com.threegroup.tobedated.composeables.profiles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.composables.GenericLabelText
import com.threegroup.tobedated.composeables.composables.SimpleBox
import com.threegroup.tobedated.composeables.composables.baseAppTextTheme
import com.threegroup.tobedated.composeables.searching.ChangePreferenceTopBar
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun BioEdit(
    nav: NavHostController,
    vmDating: DatingViewModel,
) {
    val currentUser = vmDating.getUser()
    var bio by rememberSaveable { mutableStateOf(currentUser.bio) }
    ChangePreferenceTopBar(
        nav = nav,
        title = "Edit Bio",
        changeSettings = {
            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                SimpleBox(
                    edit = true,
                    whatsInsideTheBox = {
                        Column(
                            modifier = Modifier
                                .padding(12.dp, 5.dp, 12.dp, 12.dp)
                                .fillMaxWidth()
                        ) {
                            val maxLength = 500
                            val remainingChars = maxLength - bio.length
                            TextField(
                                value = bio,
                                onValueChange = { input -> bio = input },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp)
                                    .height(200.dp),
                                textStyle = baseAppTextTheme(),
                                maxLines = 10,
                                colors = OutlinedTextFieldDefaults.colors(
                                    cursorColor = AppTheme.colorScheme.primary, // Set cursor color
                                    focusedBorderColor = AppTheme.colorScheme.secondary, // Set focused border color
                                    unfocusedBorderColor = AppTheme.colorScheme.onSurface, // Set unfocused border color
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Sentences,
                                    autoCorrect = true,
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                GenericLabelText(
                                    text = "$remainingChars/$maxLength",
                                    color = if (remainingChars < 0) Color.Red else AppTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                )
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
                    currentUser.bio = bio
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

@Composable
fun BioEditC(
    nav: NavHostController,
    vmCasual: CasualViewModel,
) {
    var bio by rememberSaveable { mutableStateOf("Bio") }
    val currentUser = vmCasual.getUser()
    val casualBio = currentUser.casualAdditions.casualBio
    if(casualBio != ""){
        bio = casualBio
    }else{
        bio = currentUser.bio
    }
    ChangePreferenceTopBar(
        nav = nav,
        title = "Edit Bio",
        changeSettings = {
            Column(
                modifier = Modifier.padding(horizontal = 15.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                SimpleBox(
                    edit = true,
                    whatsInsideTheBox = {
                        Column(
                            modifier = Modifier
                                .padding(12.dp, 5.dp, 12.dp, 12.dp)
                                .fillMaxWidth()
                        ) {
                            val maxLength = 500
                            val remainingChars = maxLength - bio.length
                            TextField(
                                value = bio,
                                onValueChange = { input -> bio = input },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp)
                                    .height(200.dp),
                                textStyle = baseAppTextTheme(),
                                maxLines = 10,
                                colors = OutlinedTextFieldDefaults.colors(
                                    cursorColor = AppTheme.colorScheme.primary, // Set cursor color
                                    focusedBorderColor = AppTheme.colorScheme.secondary, // Set focused border color
                                    unfocusedBorderColor = AppTheme.colorScheme.onSurface, // Set unfocused border color
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Sentences,
                                    autoCorrect = true,
                                )
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                GenericLabelText(
                                    text = "$remainingChars/$maxLength",
                                    color = if (remainingChars < 0) Color.Red else AppTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                )
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
                    currentUser.casualAdditions.casualBio = bio
                    vmCasual.updateUser(currentUser)
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