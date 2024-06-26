package com.threegroup.tobedated._login.composes

import android.os.CountDownTimer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.MainActivity
import com.threegroup.tobedated._login.LoginViewModel
import com.threegroup.tobedated._login.composables.ResendCode
import com.threegroup.tobedated._login.composables.VerifyField
import com.threegroup.tobedated._signUp.composables.BackButton
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.composables.getAddShadow
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun VerificationCodeView(navController: NavHostController, number: String, mainActivity: MainActivity, vmLogin:LoginViewModel, mainNav:NavHostController) {

    val controller = LocalSoftwareKeyboardController.current
    var codeList by remember { mutableStateOf(List(6) { "" }) }
    var runVerification  by remember { mutableStateOf(false) }
    var enable by remember{ mutableStateOf(true) }
    BackButton(onClick = { navController.popBackStack() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 20.dp, 10.dp, 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            // Title
            GenericTitleText(
                style = getAddShadow(style = AppTheme.typography.titleMedium, "med"),
                text = "Verification Code"
            )
            Spacer(modifier = Modifier.height(20.dp))
            // Row for verification code fields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                val focusManager = LocalFocusManager.current
                val modifier = Modifier
                    .height(65.dp)
                    .width(40.dp)
                    .weight(1f) // Equal weight for all fields
                    .padding(0.dp)
                for (i in codeList.indices) {
                    VerifyField(
                        modifier = modifier,
                        enterCode = codeList[i],
                        onValueChange = { newValue ->
                            if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                codeList = codeList.toMutableList().apply { set(i, newValue) }
                            }
                            if (newValue.isNotEmpty()) {
                                if (i < codeList.size - 1) {
                                    focusManager.moveFocus(FocusDirection.Next)
                                } else {
                                    controller?.hide()
                                }
                            } else if (i > 0) {
                                focusManager.moveFocus(FocusDirection.Previous)
                            }
                        },
                        options = if (i == codeList.size - 1) ImeAction.Done else ImeAction.Next
                    )
                }
            }
            var retryBtnState by remember { mutableStateOf(true) }
            var retryBtnText by remember { mutableStateOf("Resend Code") }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "Sent to\n $number",
                    style = getAddShadow(style = AppTheme.typography.bodyMedium, "body"),
                    color = AppTheme.colorScheme.onBackground
                )
                ResendCode(
                    mainActivity = mainActivity,
                    vmLogin = vmLogin,
                    retryBtnState = retryBtnState,
                    retryBtnText = retryBtnText,
                    onClick = {
                        vmLogin.resendOtp(mainActivity, navController)
                        var secondsLeft = 30
                        val countDownTimer = object : CountDownTimer(30000, 1000) { // 30 seconds with 1 second interval
                            override fun onTick(millisUntilFinished: Long) {
                                retryBtnState = false
                                retryBtnText = (secondsLeft--).toString() + "'s"
                            }
                            override fun onFinish() {
                                retryBtnState = true
                                enable = true
                                retryBtnText = "Resend Code"
                            }
                        }
                        countDownTimer.start()
                    },
                )
            }

        }
    }
    val codeString = codeList.joinToString(separator = "")

    BigButton(
        text = "Enter",
        onClick = {
            controller?.hide()
            vmLogin.verifyOtp(codeString, mainActivity, mainNav)
            runVerification = true
            enable = false
        },
        isUse = codeString.length == 6 && enable
    )
//    if(runVerification){
//        vmLogin.getFinished{exists, location, userPhoneNumber->
//            if(exists){
//                mainNav.navigate("Dating")
//            }else{
//                mainNav.navigate("SignUp/$location/$userPhoneNumber")
//            }
//        }
//    }
}