package com.threegroup.tobedated._signUp.composables.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated.composeables.composables.GenericLabelText
import com.threegroup.tobedated.composeables.composables.baseAppTextTheme
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun PromptAnswer(
    input: String,
    onInputChanged: (String) -> Unit,
    isEnables:Boolean,
    height:Int = 80
) {
    val maxLength = 200
    val remainingChars = maxLength - input.length
    val customTextStyle = baseAppTextTheme()
    Column(modifier = Modifier.fillMaxWidth()) {

        // MultiAutoCompleteTextView
        TextField(
            enabled = isEnables,
            value = input,
            onValueChange = onInputChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
                .height(height.dp),
            textStyle = customTextStyle,
            maxLines = 4,
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
            // Character count
            GenericLabelText(
                style = getAddShadow(style = AppTheme.typography.labelMedium, "label"),
                text = "$remainingChars/$maxLength",
                color = if (remainingChars < 0) Color.Red else AppTheme.colorScheme.onBackground
            )
        }
    }
}