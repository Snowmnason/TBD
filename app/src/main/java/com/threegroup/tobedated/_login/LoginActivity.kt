package com.threegroup.tobedated._login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
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
import com.threegroup.tobedated._signUp.SignUpActivity
import com.threegroup.tobedated.composeables.composables.PolkaDotCanvas
import com.threegroup.tobedated.shareclasses.MyApp
import com.threegroup.tobedated.shareclasses.formatPhoneNumber
import com.threegroup.tobedated.theme.AppTheme
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


enum class Login {
    LoginMainScreen,
    LoginScreen,
}

