package com.threegroup.tobedated._login.composables

import android.os.CountDownTimer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.threegroup.tobedated.R
import com.threegroup.tobedated._login.LoginActivity
import com.threegroup.tobedated._signUp.composables.getCustomTextStyle
import com.threegroup.tobedated._signUp.composables.getCustomTextStyleLabel
import com.threegroup.tobedated.composeables.composables.baseAppTextTheme
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.theme.AppTheme
import com.threegroup.tobedated.theme.JoseFinSans

@Composable
fun LoginSplash(
    activity:String = "dating"
){
    val photo = if (isSystemInDarkTheme()) painterResource(
        id = when(activity){
            "dating" -> R.drawable.tbd_dark
            "friends" -> R.drawable.tbf_dark
            "causal" -> R.drawable.tbc_dark
            else -> R.drawable.tbd_dark
        }
    ) else painterResource(
        id = when(activity){
            "dating" -> R.drawable.tbd_light
            "friends" -> R.drawable.tbf_light
            "causal" -> R.drawable.tbc_light
            else -> R.drawable.tbd_light
        }
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier
            .size(250.dp, 100.dp)
        ){
            Image(
                painter = photo,
                contentDescription = "logo",
                contentScale = ContentScale.FillBounds)
        }
        Text(
            text = "To Be Dated",
            modifier = Modifier,
            fontSize = 24.sp,
            style = getAddShadow(style = AppTheme.typography.bodyMedium, "body"),
            color = AppTheme.colorScheme.onBackground
        )
        Text(
            text = "the dating app made for connections",
            modifier = Modifier,
            fontSize = 15.sp,
            style = getAddShadow(style = AppTheme.typography.labelMedium, "label"),
            color = AppTheme.colorScheme.onBackground
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    selectedCode: String,
    onCodeSelected: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    val countryCodes = listOf(
        "USA +1", "UK +44", "Canada +1", "Australia +61", "Germany +49", "France +33",
        "Italy +39", "Spain +34", "Japan +81", "Brazil +55"
    )
    Box(
        modifier = Modifier.width(85.dp),
        contentAlignment = Alignment.Center
    ) {
        ExposedDropdownMenuBox(isExpanded, { isExpanded = it }) {
            TextField(
                value = selectedCode,
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
                countryCodes.forEach { code ->
                    DropdownMenuItem(text = { Text(text = code, style = baseAppTextTheme()) }, onClick = {
                        val countryCodeWithoutPlus = code.substringAfter("+")
                        onCodeSelected(countryCodeWithoutPlus)
                        isExpanded = false
                        onPhoneNumberChange("")
                    })
                }
            }
        }
    }
}

@Composable
fun PhoneEnterField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,

){
    val customTextStyle = getCustomTextStyle()
    val customTextStyleLabel = getCustomTextStyleLabel()
    TextField(
        value = value,
        onValueChange =  onValueChange,
        placeholder = { Text(text = "Phone Number", style = customTextStyleLabel) },
        textStyle = customTextStyle,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = AppTheme.colorScheme.primary, // Set cursor color
            focusedBorderColor = AppTheme.colorScheme.secondary, // Set focused border color
            unfocusedBorderColor = AppTheme.colorScheme.onSurface, // Set unfocused border color
        ),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Phone
        )
    )
}
@Composable
fun VerifyField(
    modifier: Modifier,
    enterCode: String,
    onValueChange: (String) -> Unit,
    options: ImeAction = ImeAction.Next
){
    val customTextStyle = TextStyle(
        color = AppTheme.colorScheme.onBackground,
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 12.sp,
        letterSpacing = 0.5.sp,
        shadow = Shadow(
            color = AppTheme.colorScheme.primary.copy(alpha = 0.75f),
            offset = Offset(4f, 4f),
            blurRadius = 4f
        )
    )
    OutlinedTextField(
        modifier = modifier, // Add padding horizontally
        value = enterCode,
        textStyle = customTextStyle,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = AppTheme.colorScheme.primary, // Set cursor color
            focusedBorderColor = AppTheme.colorScheme.secondary, // Set focused border color
            unfocusedBorderColor = AppTheme.colorScheme.onSurface, // Set unfocused border color
        ),
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = options
        ),
        singleLine = true, // Ensure single line
        keyboardActions = KeyboardActions()//actionDone
    )
}
@Composable
fun ResendCode(
    loginActivity: LoginActivity
) {
    var retryBtnState by remember { mutableStateOf(true) }
    var retryBtnText by remember { mutableStateOf("Resend Code") }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp, 0.dp, 0.dp, 0.dp),
    ) {
        val gradient = Brush.linearGradient(
            0.6f to AppTheme.colorScheme.secondary,
            1.0f to AppTheme.colorScheme.primary,
        )
        Button(
            //modifier = modifier,
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            contentPadding = PaddingValues(),
            onClick = { loginActivity.resendOtp()
                var secondsLeft = 30
                val countDownTimer = object : CountDownTimer(30000, 1000) { // 30 seconds with 1 second interval
                    override fun onTick(millisUntilFinished: Long) {
                        retryBtnState = false
                        retryBtnText = (secondsLeft--).toString() + "'s"
                    }
                    override fun onFinish() {
                        retryBtnState = true
                        retryBtnText = "Resend Code"
                    }
                }
                countDownTimer.start()
            },
            enabled = retryBtnState,
        ) {
            Box(
                modifier = Modifier
                    .background(gradient)
                    //.then(modifier)
                    .height(55.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = retryBtnText,
                    style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
                    fontSize = 20.sp
                )
            }
        }
    }
}