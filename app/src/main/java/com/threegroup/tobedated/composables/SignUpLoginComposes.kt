package com.threegroup.tobedated.composables


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.threegroup.tobedated.R
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
fun getCustomTextStyleLabel(
    opacity:Float = .5F,
    fontSize:TextUnit = 26.sp,

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
            .padding(0.dp, 65.dp, 0.dp, 65.dp)
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

@Composable
fun NameQuestion( //FOR SIGN UP
    input: String,
    onInputChanged: (String) -> Unit,
){
    val customTextStyle = getCustomTextStyle()
    val customTextStyleLabel = getCustomTextStyleLabel()
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        value = input,
        onValueChange = onInputChanged,
        placeholder = { Text(text = "First Name", style = customTextStyleLabel) },
        textStyle = customTextStyle,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = AppTheme.colorScheme.primary, // Set cursor color
            focusedBorderColor = AppTheme.colorScheme.secondary, // Set focused border color
            unfocusedBorderColor = AppTheme.colorScheme.onSurface, // Set unfocused border color
        ),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
        )
    )
}

@Composable
fun BioQuestion(
    input: String,
    onInputChanged: (String) -> Unit
) {
    val maxLength = 500
    val remainingChars = maxLength - input.length
    val customTextStyle = getCustomTextStyle()
    val customTextStyleLabel = getCustomTextStyleLabel( fontSize = 10.sp, opacity = 1F)
    Column(modifier = Modifier.fillMaxWidth()) {

        // MultiAutoCompleteTextView
        TextField(
            value = input,
            onValueChange = onInputChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
                .height(200.dp),
            textStyle = customTextStyle,
            maxLines = 10,
            label = { Text(text = "Bio", style = customTextStyleLabel) },
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

            LabelText(
                label = "Tell us about yourself",
            )
            // Character count
            LabelText(
                label = "$remainingChars/$maxLength",
                color = if (remainingChars < 0) Color.Red else AppTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun PersonalityTest(
    selectedIndex: Int,
    onSelectionChange: (Int) -> Unit,
    question: String,
) {
    val opts = listOf("", "", "", "", "", "", "")
    val agreeColors:RadioButtonColors = RadioButtonDefaults.colors(
            selectedColor = Color(0xFF86FE6E),
            unselectedColor =  Color(0xFF86FE6E))
    val naturalColors:RadioButtonColors =RadioButtonDefaults.colors(
            selectedColor = Color.Gray,
            unselectedColor = Color.Gray)
    val disagreeColors:RadioButtonColors = RadioButtonDefaults.colors(
            selectedColor = Color(0xFFFE6E86),
            unselectedColor = Color(0xFFFE6E86))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(50.dp, 150.dp)
            .padding(0.dp, 8.dp, 0.dp, 0.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFFB39DB7), shape = RoundedCornerShape(8.dp))
                .padding(2.dp, 6.dp, 2.dp, 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BodyText(label = question)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp, 0.dp, 30.dp, 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                opts.forEachIndexed { index, text ->
                    val scaleFactor:Float = when (index) {
                        0, 6 -> 2.0F
                        1, 5 -> 1.5F
                        2, 4 -> 1.0F
                        3 -> 1.0F
                        else -> 0F
                    }
                    val color:RadioButtonColors = when (index) {
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
            ){
                LabelText(
                    label = "Disagree",
                )
                LabelText(
                    label = "Agree",
                )
            }
        }
    }
}
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
        val customTextStyleLabel = getCustomTextStyleLabel( fontSize = 10.sp, opacity = 1F)

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
                .padding(start = 10.dp),
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
@Composable
fun PhotoQuestion(
    photo1: Painter = painterResource(id = R.drawable.photoholder),
    photo2: Painter = painterResource(id = R.drawable.photoholder),
    photo3: Painter = painterResource(id = R.drawable.photoholder),
    photo4: Painter = painterResource(id = R.drawable.photoholder),
    onClick1: () -> Unit,
    onClick2: () -> Unit,
    onClick3: () -> Unit,
    onClick4: () -> Unit,
    isEnabled2:Boolean,
    isEnabled3:Boolean,
    isEnabled4:Boolean,
) {
    Box(
        modifier = Modifier
            .size(525.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PFPPhotoButton(photo1, onClick1)
                Spacer(modifier = Modifier.height(2.dp))
                PFPPhotoButton(photo2, onClick2, isEnabled2)
            }
            Spacer(modifier = Modifier.width(2.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PFPPhotoButton(photo3, onClick3, isEnabled3)
                Spacer(modifier = Modifier.height(2.dp))
                PFPPhotoButton(photo4, onClick4, isEnabled4)
            }
        }
    }
}

@Composable
fun PFPPhotoButton(
    photo: Painter,
    onClick: () -> Unit,
    isEnabled:Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(9f / 16f)
            .padding(0.dp)
            .fillMaxSize(),
        shape = RectangleShape,
        colors= ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor  = Color.Transparent,
            disabledContainerColor = Color.Transparent,

        ),
        contentPadding = PaddingValues(2.dp),
        enabled = isEnabled,
        content = {
            Image(
                painter = photo,
                contentDescription = "Your profile photo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    )
}
@Composable
fun HeightQuestion(
    feet:List<String>,
    cm:List<String>,
    pickedState:PickerState
) {
    Column {
        var checked by remember { mutableStateOf(false) }
        Box(Modifier.height(275.dp)) {
            if (!checked) {
                BasicPicker(
                    values = feet,
                    valuesPickerState = pickedState,
                    style = AppTheme.typography.titleMedium,
                    start = feet.size / 2,
                    visible = 5
                )
            } else {
                BasicPicker(
                    values = cm,
                    valuesPickerState = pickedState,
                    style = AppTheme.typography.titleMedium,
                    start = cm.size / 2,
                    visible = 5
                )
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Ft",
                style = AppTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = "Cm",
                style = AppTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}
