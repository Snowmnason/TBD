package com.threegroup.tobedated._signUp.composables.questions

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.threegroup.tobedated.composeables.composables.GenericBodyText
import com.threegroup.tobedated.composeables.composables.GenericLabelText
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun PersonalityTest(
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    question: String,
) {
    val opts = listOf("", "", "", "", "", "", "")
    val agreeColors: RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = Color(0xFF86FE6E),
        unselectedColor = Color(0xFF86FE6E)
    )
    val naturalColors: RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = Color.Gray,
        unselectedColor = Color.Gray
    )
    val disagreeColors: RadioButtonColors = RadioButtonDefaults.colors(
        selectedColor = Color(0xFFFE6E86),
        unselectedColor = Color(0xFFFE6E86)
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(50.dp, 260.dp)
            .padding(0.dp, 8.dp, 0.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFFB39DB7), shape = RoundedCornerShape(8.dp))
                .padding(2.dp, 6.dp, 2.dp, 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.padding(horizontal = 10.dp, vertical = 4.dp)) {
                GenericBodyText(
                    style = getAddShadow(
                        style = AppTheme.typography.bodyMedium,
                        "body"
                    ), text = question,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp, 30.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                opts.forEachIndexed { index, text ->
                    val scaleFactor: Float = when (index) {
                        0, 6 -> 2.0F
                        1, 5 -> 1.5F
                        2, 4 -> 1.0F
                        3 -> 1.0F
                        else -> 0F
                    }
                    val color: RadioButtonColors = when (index) {
                        0, 1, 2 -> disagreeColors
                        3 -> naturalColors
                        4, 5, 6 -> agreeColors
                        else -> naturalColors
                    }
                    Column(
                        modifier = Modifier
                            .clickable { onSelectionChange(index) }
                            .padding(horizontal = 0.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        RadioButton(
                            selected = index == selectedIndex,
                            onClick = null, // onClick handled by Column
                            modifier = Modifier.scale(scaleFactor),
                            colors = color
                        )
                        Text(
                            text = text,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 5.dp, 25.dp, 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                GenericLabelText(
                    text = "Disagree",
                )
                GenericLabelText(
                    text = "Agree",
                )
            }
        }
    }
}