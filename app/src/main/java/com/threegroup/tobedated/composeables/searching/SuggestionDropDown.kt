package com.threegroup.tobedated.composeables.searching

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated.composeables.composables.baseAppTextTheme
import com.threegroup.tobedated.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionDropDown(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    selectedSuggestion:String,
    onSuggestionSelect: (String) -> Unit,

    ){
    val opts = listOf("Fill out Bio", "Better Prompt Questions", "Fill out Prompt Questions", "Different Photos")
    AlertDialog(
        containerColor = AppTheme.colorScheme.surface,
        title = {
            Text(
                text = "Help make their profile send out!",
                style = AppTheme.typography.titleLarge,
                color = AppTheme.colorScheme.onSurface
            )
        },
        text = {
            Column {
                var isExpanded by remember { mutableStateOf(false) }
                Text(
                    text = "Please select what you think would help improve their profile!\nYou will pass them on confirm",
                    style = AppTheme.typography.body, color = AppTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(isExpanded, { isExpanded = it }) {
                    TextField(
                        value = selectedSuggestion,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = AppTheme.colorScheme.secondary,
                            unfocusedContainerColor = AppTheme.colorScheme.secondary
                        ),
                        textStyle = baseAppTextTheme(),
                    )
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false },
                        modifier = Modifier
                            .background(AppTheme.colorScheme.secondary)
                            .height(250.dp)
                    ) {
                        opts.forEach { result ->
                            DropdownMenuItem(
                                text = { Text(text = result, style = baseAppTextTheme()) },
                                onClick = {
                                    onSuggestionSelect(result)
                                    isExpanded = false
                                })
                        }
                    }
                }
            }
        },
        onDismissRequest = {
            onDismissRequest()
        },

        confirmButton = {
            TextButton(

                onClick = {
                    onConfirmation()
                }
            ) {
                Text(text = "Submit", color = AppTheme.colorScheme.secondary)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(text = "Cancel", color = AppTheme.colorScheme.secondary)
            }
        }
    )
}