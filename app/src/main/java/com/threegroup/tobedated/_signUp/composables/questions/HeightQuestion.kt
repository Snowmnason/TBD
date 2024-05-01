package com.threegroup.tobedated._signUp.composables.questions

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated.composeables.composables.BasicPicker
import com.threegroup.tobedated.composeables.composables.PickerState
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun HeightQuestion(
    feet:List<String>,
    cm:List<String>,
    pickedState: PickerState
) {
    Column {
        var checked by remember { mutableStateOf(false) }
        Box(Modifier.height(275.dp)) {
            if (!checked) {
                BasicPicker(
                    values = feet,
                    valuesPickerState = pickedState,
                    style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
                    start = feet.size / 2,
                    visible = 5
                )
            } else {
                BasicPicker(
                    values = cm,
                    valuesPickerState = pickedState,
                    style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
                    start = cm.size / 2,
                    visible = 5
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Ft",
                style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
                color = AppTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = "Cm",
                style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
                color = AppTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}