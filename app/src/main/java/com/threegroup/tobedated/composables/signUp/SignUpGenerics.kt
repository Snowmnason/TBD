package com.threegroup.tobedated.composables.signUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.threegroup.tobedated.composables.baseAppTextTheme
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.ui.theme.JoseFinSans
import com.threegroup.tobedated.ui.theme.shadowWithOpacity

@Composable
fun getCustomTextStyle(): TextStyle {
    return TextStyle(
        color = AppTheme.colorScheme.onBackground,
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        shadow = Shadow(
            color = shadowWithOpacity,
            offset = Offset(4f, 4f),
            blurRadius = 4f
        )
    )
}
@Composable
fun getCustomButtonStyle(): TextStyle {
    return TextStyle(
        color = AppTheme.colorScheme.onBackground,
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        shadow = Shadow(
            color = shadowWithOpacity,
            offset = Offset(4f, 4f),
            blurRadius = 4f
        )
    )
}

@Composable
fun getCustomTextStyleLabel(
    opacity:Float = .5F,
    fontSize: TextUnit = 26.sp,

    ): TextStyle {
    val colorWithOpacity = AppTheme.colorScheme.onBackground.copy(alpha = opacity)
    return TextStyle(
        color = colorWithOpacity,
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        fontSize = fontSize,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp,
        shadow = Shadow(
            color = shadowWithOpacity,
            offset = Offset(4f, 4f),
            blurRadius = 4f
        )
    )
}

@Composable
fun BigButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit, // onClick function with no return value
    isUse: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, 50.dp)
    ) {
        val gradient = if (isUse) {
            Brush.linearGradient(
                0.6f to AppTheme.colorScheme.secondary,
                1.0f to AppTheme.colorScheme.primary,
            )
        } else {
            Brush.horizontalGradient(
                0.0f to Color(0xCC7d758d),
                0.5f to Color(0x997d758d),
                1.0f to Color(0xCC7d758d),
            )
        }
        Button(
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            contentPadding = PaddingValues(),
            onClick = { onClick() }, // Call the provided onClick function
            enabled = isUse,
        ) {
            Box(
                modifier = Modifier
                    .background(gradient)
                    .then(modifier)
                    .height(55.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = text,
                    style = AppTheme.typography.titleMedium,
                    fontSize = 25.sp
                )
            }
        }
    }
}

@Composable
fun SignUpFormat(
    title: String,
    label: String,
    enterField: @Composable () -> Unit = {},
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 65.dp, 0.dp, 145.dp)
    ) {
        Surface(
            modifier = Modifier
                .padding(25.dp, 0.dp, 25.dp, 0.dp),
            color = AppTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp)
        ) {
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(0) }
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .verticalScroll(state)
                    .fillMaxSize()
            ) {
                // Title
                TitleText(title = title)
                Spacer(modifier = Modifier.height(5.dp))
                enterField()
                //Fun Label
                Spacer(modifier = Modifier.height(12.dp))
                LabelText(
                    label = label,
                )
            }
        }
    }
}

//TO ALLOW TWO QUESTIONS ON ONE PAGE
@Composable
fun SignUpFormatDouble(
    title1: String,
    label1: String,
    enterField1: @Composable () -> Unit = {},
    title2: String,
    label2: String,
    enterField2: @Composable () -> Unit = {},
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 65.dp, 0.dp, 145.dp)
    ) {
        Surface(
            modifier = Modifier
                .padding(25.dp, 0.dp, 25.dp, 0.dp),
            color = AppTheme.colorScheme.surface,
            shape = RoundedCornerShape(10.dp)
        ) {
            val state = rememberScrollState()
            LaunchedEffect(Unit) { state.animateScrollTo(0) }
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .verticalScroll(state)
                    .fillMaxSize()
            ) {
                // Title
                TitleText(title = title1)
                Spacer(modifier = Modifier.height(5.dp))
                enterField1()
                //Fun Label
                Spacer(modifier = Modifier.height(12.dp))
                LabelText(label = label1,)
                Spacer(modifier = Modifier.height(12.dp))
                TitleText(title = title2)
                Spacer(modifier = Modifier.height(5.dp))
                enterField2()
                //Fun Label
                Spacer(modifier = Modifier.height(12.dp))
                LabelText(label = label2,)
            }
        }
    }
}
@Composable
fun TitleText(
    title:String
){
    Text(
        text = title,
        style = AppTheme.typography.titleMedium,
        color = AppTheme.colorScheme.onBackground,
    )
}
@Composable
fun BodyText(
    label:String
){
    Text(
        text = label,
        style = AppTheme.typography.bodyMedium,
        color = AppTheme.colorScheme.onBackground
    )
}
@Composable
fun LabelText(
    label: String,
    color: Color = AppTheme.colorScheme.onBackground
){
    Text(
        text = label,
        style = AppTheme.typography.labelMedium,
        color = color
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownAlertBox(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    selectedMBTI:String,
    onMBTISelect: (String) -> Unit,

){
    val opts = listOf("INTJ", "INTP", "ENTJ", "ENTP", "ENFP",
        "ENFJ", "INFP", "INFJ",
        "ESFJ", "ESTJ", "ISFJ", "ISTJ",
        "ISTP", "ISFP", "ESTP", "ESFP", "Not Taken")
    AlertDialog(
        containerColor = AppTheme.colorScheme.surface,
        title = {
            Text(text = "Add your Results", style = AppTheme.typography.titleLarge, color = AppTheme.colorScheme.onSurface)
        },
        text = {
            Column {
                var isExpanded by remember { mutableStateOf(false) }
                Text(text = "If you have taken the test before, you can add you results here, or just click later to take in the future", style = AppTheme.typography.body, color = AppTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(isExpanded, { isExpanded = it }) {
                    TextField(
                        value = selectedMBTI,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(),
                        colors =  TextFieldDefaults.colors(
                            focusedContainerColor = AppTheme.colorScheme.secondary,
                            unfocusedContainerColor = AppTheme.colorScheme.secondary
                        ),
                        textStyle = baseAppTextTheme(),
                    )
                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false },
                            modifier = Modifier.background(AppTheme.colorScheme.secondary).height(250.dp)
                        ) {
                            opts.forEach{ result ->
                                DropdownMenuItem(
                                    text = { Text(text = result, style = baseAppTextTheme()) },
                                    onClick = {
                                    onMBTISelect(result)
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
                Text(text = "Later", color = AppTheme.colorScheme.secondary)
            }
        }
    )
}