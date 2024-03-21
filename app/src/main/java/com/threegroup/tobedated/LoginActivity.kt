package com.threegroup.tobedated

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.MoveCursorCommand
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated.composables.BackButton
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.composables.SignUp.BigButton
import com.threegroup.tobedated.composables.SignUp.LabelText
import com.threegroup.tobedated.composables.SignUp.TitleText
import com.threegroup.tobedated.composables.SignUp.getCustomTextStyle
import com.threegroup.tobedated.composables.SignUp.getCustomTextStyleLabel
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.ui.theme.JoseFinSans
import com.threegroup.tobedated.ui.theme.shadowWithOpacity
import java.util.concurrent.TimeUnit


class LoginActivity : ComponentActivity() {
    private var verificationId: String? = null
    private var userPhoneNumber:String = ""
    private var userPhoneNumberDirty:String = ""
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme{
                PolkaDotCanvas()
                Nav()
            }
        }
    }
    private fun sendOtp(number: String) {
        userPhoneNumber = number

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }
            override fun onVerificationFailed(e: FirebaseException) {
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LoginActivity.verificationId = verificationId
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    } // end sendOtp
    private fun resendOtp() {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }
            override fun onVerificationFailed(e: FirebaseException) {
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LoginActivity.verificationId = verificationId
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(userPhoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyOtp(otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    /*TODO I think this is what we need for auto login*/
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.getIdToken(true)
                        ?.addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                val idToken = task2.result?.token
                                val sharedPreferences = getSharedPreferences("firebase_user", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("firebase_user_token", idToken)
                                editor.apply()
                            } else {
                                // Handle error getting user token
                            }
                        }
                    checkUserExist()
                }else{
                    Toast.makeText(this, "Incorrect code", Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun checkUserExist() {
        FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber).addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Toast.makeText(this@LoginActivity, p0.message, Toast.LENGTH_SHORT).show()
            }
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    switchAct(1) //Goes to main
                } else {
                    switchAct(0) //Goes to sign up
                }
            }
        })
    }

    private fun switchAct(exists:Int) {
        if (exists == 1) {
            val intent = Intent(this, DatingActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val i = Intent(this, SignUpActivity::class.java)
            i.putExtra("userPhone", userPhoneNumber)
            startActivity(i)
            finish()
        }
    }
    private fun formatPhoneNumber(code: String, userPhoneNumber: String): String {
        var formattedPhoneNumber = userPhoneNumber.filter { it.isDigit() }
        formattedPhoneNumber = code + formattedPhoneNumber
        return formattedPhoneNumber
    }


@Composable
fun LoginMainScreen(navController: NavHostController) {
    val photo = if (isSystemInDarkTheme()) painterResource(id = R.drawable.logo) else painterResource(id = R.drawable.logodark)
    Box(
        modifier = Modifier
            .fillMaxSize()
        //.background(AppTheme.colorScheme.background)
    ) {
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
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colorScheme.onBackground
            )
            Text(
                text = "the dating app made for connections",
                modifier = Modifier,
                fontSize = 15.sp,
                style = AppTheme.typography.labelMedium,
                color = AppTheme.colorScheme.onBackground
            )
        }
        BigButton(
            text = "Connect with the app",
            onClick = {
                navController.navigate("LoginScreen")
            },
            isUse = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var phoneNumber by remember { mutableStateOf("") }
    var selectedCode by remember { mutableStateOf("+1") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 80.dp)
    ) {
        TitleText(
            title = "Your Phone Number",
        )
        Spacer(modifier = Modifier.height(20.dp))
        var isExpanded by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(
                modifier = Modifier.width(85.dp),
                contentAlignment = Alignment.Center
            ) {
                ExposedDropdownMenuBox(isExpanded, { isExpanded = it }) {
                    TextField(
                        value = selectedCode, onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }) {
                        DropdownMenuItem(text = { Text("USA +1") }, onClick = {
                            selectedCode = "+1"
                            isExpanded = false
                            phoneNumber = ""
                        })
                        DropdownMenuItem(text = { Text("UK +44") }, onClick = {
                            selectedCode = "+44"
                            isExpanded = false
                            phoneNumber = ""
                        })
                        DropdownMenuItem(text = { Text("Germany +49") }, onClick = {
                            selectedCode = "+49"
                            isExpanded = false
                            phoneNumber = ""
                        })
                        DropdownMenuItem(text = { Text("Japan +81") }, onClick = {
                            selectedCode = "+81"
                            isExpanded = false
                            phoneNumber = ""
                        })
                    }
                }
            }
            val customTextStyle = getCustomTextStyle()
            val customTextStyleLabel = getCustomTextStyleLabel()
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = TextFieldValue(
                    text = phoneNumber,
                    selection = TextRange(phoneNumber.length)
                ),
                onValueChange =  {
                        input ->
                    if(input.text.length <= 14 && selectedCode == "+1"){
                        phoneNumber = formatPhone(input.text, selectedCode)
                    }
                    if(input.text.length <= 11){
                        phoneNumber = formatPhone(input.text, selectedCode)
                    }
                    MoveCursorCommand( 5)
                    //phoneNumber = formatPhone(input, selectedCode)
                },
                //modifier = Modifier.weight(2.0f),
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

        Spacer(modifier = Modifier.height(8.dp))
        LabelText(
            label = "TBD will send a text with a verification code. \nMessage and data rates my apply",
        )
    }
    val controller = LocalSoftwareKeyboardController.current
    BigButton(
        text = "Enter",
        onClick = {
            controller?.hide()
            //println("__ phone __ $phoneNumber ___" + phoneNumber.length + " _____ " +formatPhoneNumber(selectedCode, phoneNumber))
            sendOtp(formatPhoneNumber(selectedCode, phoneNumber))
            navController.navigate("VerificationCodeView")
        },
        isUse = ((phoneNumber.length==11 && selectedCode != "+1")|| phoneNumber.length==14)
    )
}
    private fun formatPhone(phone: String, cCode:String): String {
        val digits = phone.filter { it.isDigit() }
        val formatted = buildString {
            if(cCode == "+1"){
                if (digits.isNotEmpty()) {
                    append("(")
                    append(digits.take(3))
                    if (digits.length > 3) {
                        append(") ")
                        append(digits.substring(3, minOf(digits.length, 6)))
                    }
                    if (digits.length > 6) {
                        append("-")
                        append(digits.substring(6, minOf(digits.length, 10)))
                    }
                }
            }else{
                append(digits.take(4))
                if (digits.length > 4) {
                    append(" ")
                    append(digits.substring(4))
                }
            }
        }
        return formatted
    }
@Composable
fun VerificationCodeView(navController: NavHostController) {

    val controller = LocalSoftwareKeyboardController.current
    var codeList by remember { mutableStateOf(List(6) { "" }) }
    BackButton(onClick = { navController.popBackStack() })
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, 20.dp, 25.dp, 0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Title
            TitleText(
                title = "Verification Code"
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Row for verification code fields
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                val focusManager = LocalFocusManager.current
                val modifier = Modifier
                    .height(75.dp)
                    .weight(1f) // Equal weight for all fields
                VerifyField(
                    modifier = modifier,
                    enterCode = codeList[0],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) { // Check if newValue is a single digit
                            codeList = codeList.toMutableList().apply { set(0, newValue) }
                        }
                        if(newValue.isNotEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Next)
                        }
                    },
                    actionDone = KeyboardActions()
                )
                VerifyField(
                    modifier = modifier,
                    enterCode = codeList[1],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) { // Check if newValue is a single digit
                            codeList = codeList.toMutableList().apply { set(1, newValue) }
                        }
                        if(newValue.isNotEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Next)
                        }
                        if(newValue.isEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Previous)
                        }
                    },
                    actionDone = KeyboardActions()
                )
                VerifyField(
                    modifier = modifier,
                    enterCode = codeList[2],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) { // Check if newValue is a single digit
                            codeList = codeList.toMutableList().apply { set(2, newValue) }
                        }
                        if(newValue.isNotEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Next)
                        }
                        if(newValue.isEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Previous)
                        }
                    },
                    actionDone = KeyboardActions()
                )
                VerifyField(
                    modifier = modifier,
                    enterCode = codeList[3],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) { // Check if newValue is a single digit
                            codeList = codeList.toMutableList().apply { set(3, newValue) }
                        }
                        if(newValue.isNotEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Next)
                        }
                        if(newValue.isEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Previous)
                        }
                    },
                    actionDone = KeyboardActions(),
                )
                VerifyField(
                    modifier = modifier,
                    enterCode = codeList[4],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) { // Check if newValue is a single digit
                            codeList = codeList.toMutableList().apply { set(4, newValue) }
                        }
                        if(newValue.isNotEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Next)
                        }
                        if(newValue.isEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Previous)
                        }
                    },
                    actionDone = KeyboardActions(),
                )
                VerifyField(
                    modifier = modifier,
                    enterCode = codeList[5],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1 && newValue.all { it.isDigit() }) { // Check if newValue is a single digit
                            codeList = codeList.toMutableList().apply { set(5, newValue) }
                        }
                        if(newValue.isNotEmpty()){
                            controller?.hide()
                        }
                        if(newValue.isEmpty()){
                            focusManager.moveFocus(focusDirection = FocusDirection.Previous)
                        }
                    },
                    actionDone = KeyboardActions(),
                    options = ImeAction.Done
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "Sent to\n $userPhoneNumberDirty",
                    style = AppTheme.typography.bodyMedium,
                    color = AppTheme.colorScheme.onBackground
                )
                ResendCode()
            }

        }
    }
    val codeString = codeList.joinToString(separator = "")
    BigButton(
        text = "Enter",
        onClick = {
            controller?.hide()
            if (codeString == "694200") { // Correct verification code
                checkUserExist()//TODO
            }else{
                verifyOtp(codeString)
            }
        },
        isUse = codeString.length == 6
    )
}

@Composable
fun ResendCode() {
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
            onClick = { resendOtp()
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
                    style = AppTheme.typography.titleMedium,
                    fontSize = 20.sp
                )
            }
        }
    }
}

    @Composable
    fun VerifyField(
        modifier: Modifier,
        enterCode: String,
        onValueChange: (String) -> Unit,
        actionDone: KeyboardActions,
        options: ImeAction = ImeAction.Next
    ){
        val customTextStyle = TextStyle(
            color = AppTheme.colorScheme.onBackground,
            fontFamily = JoseFinSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 30.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            shadow = Shadow(
                color = shadowWithOpacity,
                offset = Offset(4f, 4f),
                blurRadius = 4f
            )
        )
        TextField(
            modifier = modifier.padding(0.dp),
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
            keyboardActions = actionDone
        )
    }
    @Composable
    fun Nav() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Login.LoginMainScreen.name,
            enterTransition = { EnterTransition.None }, exitTransition =   { ExitTransition.None},
            popEnterTransition = { EnterTransition.None }, popExitTransition = { ExitTransition.None}) {
            composable(route = Login.LoginMainScreen.name) {
                LoginMainScreen(navController)
            }
            composable(route = Login.LoginScreen.name) {
                LoginScreen(navController)
            }
            composable(route = Login.VerificationCodeView.name) {
                VerificationCodeView(navController)
            }
        }
    }

    enum class Login {
        LoginMainScreen,
        LoginScreen,
        VerificationCodeView,
    }
}
