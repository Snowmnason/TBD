package com.threegroup.tobedated._signUp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._login.LoginActivity
import com.threegroup.tobedated.composeables.composables.PolkaDotCanvas
import com.threegroup.tobedated.shareclasses.MyApp
import com.threegroup.tobedated.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SignUpActivity : ComponentActivity() {
    private lateinit var location:String
    override fun onCreate(savedInstanceState: Bundle?) {
        val number = intent.getStringExtra("userPhone").toString()
        location = intent.getStringExtra("location").toString()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(
                activity = "signup"
            ) {
                PolkaDotCanvas()
                SignUpNav(this@SignUpActivity, location, number)
            }
        }
    }

    fun switchBack() {
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        intent.putExtra("location", location)
        startActivity(intent)
        finish()
    }

    fun finishingUp(signUpVM: SignUpViewModel){
        lifecycleScope.launch {
            runBlocking {
                signUpVM.uploadImage(contentResolver)
                showToast()
                saveTokenToSharedPreferences(signUpVM.getUser().number)
                MyApp.x.setUserInfo(signUpVM.getUser().number, location).collect { userInfo ->
                    MyApp._signedInUser.value = userInfo
                }
                goNextScreen()
            }
        }
    }
    private fun goNextScreen() {
        val intent = Intent(this, DatingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast() {//message: String
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveTokenToSharedPreferences(token: String?) {
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_login", token)
        editor.apply()
    }
}


enum class SignUp {
    WelcomeScreen,
    NameScreen,//
    BirthScreen,//
    PronounScreen,//
    GenderScreen,//
    SexOriScreen,//
    HieghtScreen,//
    EthnicityScreen,//
    StarScreen,//
    SearchScreen,////
    SexScreen,////
    MbtiScreen,//
    OurTestScreen,////
    ChildrenScreen,//
    FamilyScreen,//
    EducationScreen,//
    ReligiousScreen,//
    PoliticsScreen,//
    RelationshipScreen,//
    IntentionsScreen,//
    MeetUpScreen,
    DrinkScreen,//
    SmokeScreen,//
    WeedScreen,//
    BioScreen,//
    PromptQuestionsScreen,
    PhotoScreen,
}
/* Cool multiSelect
val galleryLauncherMulti = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetMultipleContents(),
    onResult = { uriList ->
        imageUriList.value = uriList
    }
)
val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetMultipleContents(),
    onResult = { uriList ->
        imageUriList.value = uriList
    }
)
*/