package com.threegroup.tobedated._signUp.composables


import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.threegroup.tobedated.R
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated.shareclasses.composables.BasicPicker
import com.threegroup.tobedated.shareclasses.composables.GenericBodyText
import com.threegroup.tobedated.shareclasses.composables.GenericLabelText
import com.threegroup.tobedated.shareclasses.composables.PickerState
import com.threegroup.tobedated.shareclasses.composables.PlainTextButton
import com.threegroup.tobedated.shareclasses.composables.baseAppTextTheme
import com.threegroup.tobedated.shareclasses.composables.getAddShadow
import com.threegroup.tobedated.shareclasses.models.curiositiesANDImaginations
import com.threegroup.tobedated.shareclasses.models.insightsANDReflections
import com.threegroup.tobedated.shareclasses.models.passionsANDInterests
import com.threegroup.tobedated.shareclasses.models.tabs
import com.threegroup.tobedated.shareclasses.theme.AppTheme

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

            GenericLabelText(
                style = getAddShadow(style = AppTheme.typography.labelMedium, "label"),
                text = "Tell us about yourself",
            )
            // Character count
            GenericLabelText(
                style = getAddShadow(style = AppTheme.typography.labelMedium, "label"),
                text = "$remainingChars/$maxLength",
                color = if (remainingChars < 0) Color.Red else AppTheme.colorScheme.onBackground
            )
        }
    }
}
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
            .heightIn(50.dp, 260.dp)
            .padding(0.dp, 8.dp, 0.dp, 0.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, Color(0xFFB39DB7), shape = RoundedCornerShape(8.dp))
                .padding(2.dp, 6.dp, 2.dp, 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.padding(horizontal = 10.dp, vertical = 4.dp)){
                GenericBodyText(style = getAddShadow(style = AppTheme.typography.bodyMedium, "body"), text = question, )
            }
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
    isEnabled2:Boolean = true,
    isEnabled3:Boolean = true,
    isEnabled4:Boolean = true,
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

@Composable
fun PromptQuestions(nav:NavController, signUpVM: SignUpViewModel, questionNumber:Int){
    var tabIndex by remember { mutableIntStateOf(0) }
    val state = ScrollState(0)
    val stateCol = rememberScrollState()
    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            modifier = Modifier
                .fillMaxWidth()
                .scrollable(state, orientation = Orientation.Horizontal),
            selectedTabIndex = tabIndex,
            contentColor = AppTheme.colorScheme.secondary,
            containerColor = Color(0xFFB39DB7)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(text = title, style = AppTheme.typography.bodySmall) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        var questionsToUse=  insightsANDReflections
        when (tabIndex) {
            0 -> questionsToUse = insightsANDReflections
            1 -> questionsToUse = passionsANDInterests
            2 -> questionsToUse = curiositiesANDImaginations
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp, vertical = 15.dp).verticalScroll(stateCol)
            //.scrollable(state, orientation = Orientation.Vertical)
        ) {
            questionsToUse.forEach { quest ->
                if(quest == signUpVM.getUser().promptQ1 || quest == signUpVM.getUser().promptQ2 || quest == signUpVM.getUser().promptQ3){
                    PlainTextButton(
                        question = quest,
                        onClick  = { },
                        enabled = false
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }else{
                    PlainTextButton(
                        question = quest,
                        onClick  = { nav.popBackStack()
                            when(questionNumber){
                                1-> signUpVM.setUser("promptQ1", quest)
                                2-> signUpVM.setUser("promptQ2", quest)
                                3-> signUpVM.setUser("promptQ3", quest)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MBTIDropDown(
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
                        modifier = Modifier
                            .background(AppTheme.colorScheme.secondary)
                            .height(250.dp)
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



































