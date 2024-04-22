package com.threegroup.tobedated._login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.MoveCursorCommand
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._login.composables.DropDown
import com.threegroup.tobedated._login.composables.LoginSplash
import com.threegroup.tobedated._login.composables.PhoneEnterField
import com.threegroup.tobedated._login.composables.ResendCode
import com.threegroup.tobedated._login.composables.VerifyField
import com.threegroup.tobedated._signUp.SignUpActivity
import com.threegroup.tobedated._signUp.composables.BackButton
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated.shareclasses.MyApp
import com.threegroup.tobedated.shareclasses.composables.GenericLabelText
import com.threegroup.tobedated.shareclasses.composables.GenericTitleText
import com.threegroup.tobedated.shareclasses.composables.PolkaDotCanvas
import com.threegroup.tobedated.shareclasses.composables.getAddShadow
import com.threegroup.tobedated.shareclasses.formatPhone
import com.threegroup.tobedated.shareclasses.formatPhoneNumber
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class LoginActivity : ComponentActivity() {
    private var verificationId: String? = null
    private var userPhoneNumber: String = ""
    private val auth = FirebaseAuth.getInstance()
    private lateinit var location:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        location = intent.getStringExtra("location").toString()
        setContent {
            AppTheme(
                activity = "login"
            ) {
                PolkaDotCanvas()
                LoginNav(this@LoginActivity)
            }
        }
    }
    fun sendOtp(code:String, number: String) {
        userPhoneNumber = formatPhoneNumber(code, number)
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
    } // end sendOtp

    fun verifyOtp(otp: String) {
        try {
            if (verificationId != null) { //TODO small bug here, might not be a problem in the future ask dom idk
                val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
                signInWithPhoneAuthCredential(credential)
            } else {
                // Handle case where verificationId is null
                showToast("Verification Code expired")
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // Handle invalid credentials exception
            showToast("Incorrect verification code")
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.getIdToken(true)
                        ?.addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                checkUserExist()
                            } else {
                                // Handle error getting user token
                            }
                        }
                } else {
                    showToast("Incorrect code")
                }
            }
    }

    private fun checkUserExist() {
        FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        switchAct(1) //Goes to main
                    } else {
                        switchAct(0) //Goes to sign up
                    }
                }
            })
    }
    fun resendOtp() {
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

    private fun switchAct(exists: Int) {
        if (exists == 1) {
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    MyApp.x.setUserInfo(userPhoneNumber, location).collect { userInfo ->
                        MyApp._signedInUser.value = userInfo
                    }
                    saveTokenToSharedPreferences(userPhoneNumber)

                }
            }
            val intent = Intent(this, DatingActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val i = Intent(this, SignUpActivity::class.java)
            i.putExtra("userPhone", userPhoneNumber)
            i.putExtra("location", location)
            startActivity(i)
            finish()
        }
    }
    private fun saveTokenToSharedPreferences(token: String?) {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_login", token)
        editor.apply()
    }
    private fun showToast(message: String, ) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}


@Composable
fun LoginMainScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
        //.background(AppTheme.colorScheme.background)
    ) {
        LoginSplash()
        BigButton(
            text = "Connect with the app",
            onClick = {
                navController.navigate("LoginScreen")
            },
            isUse = true
        )
    }
}
@Composable
fun LoginScreen(navController: NavHostController, loginActivity: LoginActivity) {
    var phoneNumber by rememberSaveable { mutableStateOf("") }
    var selectedCode by rememberSaveable { mutableStateOf("+1") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 80.dp)
    ) {
        GenericTitleText(style = getAddShadow(style = AppTheme.typography.titleMedium, "med"), text =  "Your Phone Number")
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
                onValueChange = {
                        input ->
                    if(input.text.length <= 14 && selectedCode == "+1"){
                        phoneNumber = formatPhone(input.text, selectedCode)
                    }
                    if(input.text.length <= 11){
                        phoneNumber = formatPhone(input.text, selectedCode)
                    }
                    MoveCursorCommand( 5)
                    //phoneNumber = formatPhone(input, selectedCode)
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        GenericLabelText(style=  getAddShadow(style = AppTheme.typography.labelMedium, "label"),text = "TBD will send a text with a verification code. \nMessage and data rates my apply",)
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
        isUse = ((phoneNumber.length==11 && selectedCode != "+1")|| phoneNumber.length==14)
    )
}

@Composable
fun VerificationCodeView(navController: NavHostController, number: String, loginActivity: LoginActivity) {

    val controller = LocalSoftwareKeyboardController.current
    var codeList by remember { mutableStateOf(List(6) { "" }) }
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
            GenericTitleText(style = getAddShadow(style = AppTheme.typography.titleMedium, "med"), text =  "Verification Code")
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
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Text(
                    text = "Sent to\n $number",
                    style = getAddShadow(style = AppTheme.typography.bodyMedium, "body"),
                    color = AppTheme.colorScheme.onBackground
                )
                ResendCode( loginActivity)
            }

        }
    }
    val codeString = codeList.joinToString(separator = "")
    BigButton(
        text = "Enter",
        onClick = {
            controller?.hide()
            loginActivity.verifyOtp(codeString)
        },
        isUse = codeString.length == 6
    )
}

enum class Login {
    LoginMainScreen,
    LoginScreen,
}

