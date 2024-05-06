package com.threegroup.tobedated._login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import com.threegroup.tobedated.MainActivity
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.formatPhoneNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit


class LoginViewModel(private var repository: Repository) : ViewModel() {
    private var verificationId: String? = null
    private var userPhoneNumber: String = ""
    private val auth = FirebaseAuth.getInstance()
    private lateinit var location:String

    fun setLocation(loc:String){
        location = loc
    }
    fun sendOtp(code:String, number: String, mainActivity:MainActivity, nav:NavHostController) {
        userPhoneNumber = formatPhoneNumber(code, number)
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential, mainActivity, nav)
            }

            override fun onVerificationFailed(e: FirebaseException) {
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LoginViewModel.verificationId = verificationId
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(userPhoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(mainActivity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    } // end sendOtp

    fun verifyOtp(otp: String, mainActivity:MainActivity, nav:NavHostController) {
        try {
            if (verificationId != null) { //TODO small bug here, might not be a problem in the future ask dom idk
                val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
                signInWithPhoneAuthCredential(credential, mainActivity, nav)
            } else {
                // Handle case where verificationId is null
                mainActivity.showToast("Verification Code expired")
            }
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // Handle invalid credentials exception
            mainActivity.showToast("Incorrect verification code")
        }
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, mainActivity:MainActivity, nav:NavHostController) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(mainActivity) { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.getIdToken(true)
                        ?.addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                checkUserExist(mainActivity, nav)
                            } else {
                                // Handle error getting user token
                            }
                        }
                } else {
                    mainActivity.showToast("Incorrect code")
                }
            }
    }

    private fun checkUserExist(mainActivity: MainActivity, nav:NavHostController) {
        FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        switchAct(1, mainActivity, nav) //Goes to main
                    } else {
                        switchAct(0, mainActivity, nav) //Goes to sign up
                    }
                }
            })
    }
    fun resendOtp(mainActivity:MainActivity, nav:NavHostController) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential, mainActivity, nav)
            }

            override fun onVerificationFailed(e: FirebaseException) {
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                this@LoginViewModel.verificationId = verificationId
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(userPhoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(mainActivity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun switchAct(exists: Int, mainActivity: MainActivity, nav:NavHostController) {
        if (exists == 1) {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    MyApp.x.setUserInfo(userPhoneNumber, location).collect { userInfo ->
                        MyApp._signedInUser.value = userInfo
                    }
                    mainActivity.saveTokenToSharedPreferences(userPhoneNumber)
                    nav.navigate("Dating"){popUpToRoute}
                }
            }
        } else {
            nav.navigate("SignUp/$location/$userPhoneNumber"){popUpToRoute}

        }
    }
}