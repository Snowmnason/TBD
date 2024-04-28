package com.threegroup.tobedated._login.composes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.MoveCursorCommand
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._login.LoginActivity
import com.threegroup.tobedated._login.composables.DropDown
import com.threegroup.tobedated._login.composables.PhoneEnterField
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated.composeables.composables.GenericLabelText
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.shareclasses.formatPhone
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun LoginScreen(navController: NavHostController, loginActivity: LoginActivity) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var selectedCode by rememberSaveable { mutableStateOf("+1") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 80.dp)
    ) {
        GenericTitleText(
            style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
            text = "Your Phone Number"
        )
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            DropDown(
                selectedCode = selectedCode,
                onCodeSelected = { newCode -> selectedCode = newCode },
                phoneNumber = phoneNumber,
                onPhoneNumberChange = { newNumber -> phoneNumber = newNumber }
            )
            Spacer(modifier = Modifier.width(8.dp))
            PhoneEnterField(
                value = TextFieldValue(
                    text = phoneNumber,
                    selection = TextRange(phoneNumber.length)
                ),
                onValueChange = { input ->
                    if (input.text.length <= 14 && selectedCode == "+1") {
                        phoneNumber = formatPhone(input.text, selectedCode)
                    }
                    if (input.text.length <= 11) {
                        phoneNumber = formatPhone(input.text, selectedCode)
                    }
                    MoveCursorCommand(5)
                    //phoneNumber = formatPhone(input, selectedCode)
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        GenericLabelText(
            style = getAddShadow(style = AppTheme.typography.labelMedium, "label"),
            text = "TBD will send a text with a verification code. \nMessage and data rates my apply",
        )
    }
    val controller = LocalSoftwareKeyboardController.current
    BigButton(
        text = "Enter",
        onClick = {
            controller?.hide()
            //println("__ phone __ $phoneNumber ___" + phoneNumber.length + " _____ " +formatPhoneNumber(selectedCode, phoneNumber))
            loginActivity.sendOtp(selectedCode, phoneNumber)
            navController.navigate("VerificationCodeView/$phoneNumber")
        },
        isUse = ((phoneNumber.length == 11 && selectedCode != "+1") || phoneNumber.length == 14)
    )
}