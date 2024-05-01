package com.threegroup.tobedated._signUp.composables.questions

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.threegroup.tobedated._signUp.composables.getCustomTextStyle
import com.threegroup.tobedated._signUp.composables.getCustomTextStyleLabel
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun BirthdateQuestion(
    modifier: Modifier = Modifier,
    monthValue:String,
    dayValue:String,
    yearValue:String,
    onMonthChanged: (String) -> Unit,
    onDayChanged: (String) -> Unit,
    onYearChanged: (String) -> Unit,

    ) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val customTextStyle = getCustomTextStyle()
        val customTextStyleLabel = getCustomTextStyleLabel(fontSize = 10.sp, opacity = 1F)

        // Month EditText
        TextField(
            value = monthValue,
            onValueChange = onMonthChanged,
            modifier = Modifier
                .width(80.dp)
                .height(IntrinsicSize.Min),
            textStyle = customTextStyle,
            label = { Text(text = "Month", style = customTextStyleLabel) },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = AppTheme.colorScheme.primary, // Set cursor color
                focusedBorderColor = AppTheme.colorScheme.secondary, // Set focused border color
                unfocusedBorderColor = AppTheme.colorScheme.onSurface, // Set unfocused border color
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            maxLines = 1,
            singleLine = true
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = "/",
            style = customTextStyle,
            modifier = Modifier.padding(horizontal = 5.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        // Day EditText
        TextField(
            value = dayValue,
            onValueChange = onDayChanged,
            modifier = Modifier
                .width(80.dp)
                .height(IntrinsicSize.Min)
                .padding(start = 5.dp),
            textStyle = customTextStyle,
            label = { Text(text = "Day", style = customTextStyleLabel) },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = AppTheme.colorScheme.primary, // Set cursor color
                focusedBorderColor = AppTheme.colorScheme.secondary, // Set focused border color
                unfocusedBorderColor = AppTheme.colorScheme.onSurface, // Set unfocused border color
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            maxLines = 1,
            singleLine = true
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "/",
            style = customTextStyle,
            modifier = Modifier.padding(horizontal = 5.dp),
        )
        Spacer(modifier = Modifier.width(5.dp))
        // Year EditText
        TextField(
            value = yearValue,
            onValueChange = onYearChanged,
            modifier = Modifier
                .width(120.dp)
                .height(IntrinsicSize.Min)
                .padding(start = 2.dp),
            textStyle = customTextStyle,
            label = { Text(text = "Year", style = customTextStyleLabel) },
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = AppTheme.colorScheme.primary, // Set cursor color
                focusedBorderColor = AppTheme.colorScheme.secondary, // Set focused border color
                unfocusedBorderColor = AppTheme.colorScheme.onSurface, // Set unfocused border color
            ),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            maxLines = 1,
            singleLine = true
        )
    }
}