package com.threegroup.tobedated.activities

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.threegroup.tobedated.R
import com.threegroup.tobedated.callclass.checkBirthDate
import com.threegroup.tobedated.callclass.checkDay
import com.threegroup.tobedated.callclass.checkMonth
import com.threegroup.tobedated.callclass.checkYear
import com.threegroup.tobedated.composables.DialogWithImage
import com.threegroup.tobedated.composables.GenTextOnlyButton
import com.threegroup.tobedated.composables.GenericLabelText
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.composables.RadioButtonGroup
import com.threegroup.tobedated.composables.SignUpNav
import com.threegroup.tobedated.composables.rememberPickerState
import com.threegroup.tobedated.composables.signUp.BioQuestion
import com.threegroup.tobedated.composables.signUp.BirthdateQuestion
import com.threegroup.tobedated.composables.signUp.BodyText
import com.threegroup.tobedated.composables.signUp.DropdownAlertBox
import com.threegroup.tobedated.composables.signUp.HeightQuestion
import com.threegroup.tobedated.composables.signUp.NameQuestion
import com.threegroup.tobedated.composables.signUp.PersonalityTest
import com.threegroup.tobedated.composables.signUp.PhotoQuestion
import com.threegroup.tobedated.composables.signUp.PromptAnswer
import com.threegroup.tobedated.composables.signUp.SignUpFormat
import com.threegroup.tobedated.composables.signUp.TitleText
import com.threegroup.tobedated.composables.signUp.getCustomButtonStyle
import com.threegroup.tobedated.models.PreferenceIndexModel
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.childrenOptions
import com.threegroup.tobedated.models.drinkOptions
import com.threegroup.tobedated.models.educationOptions
import com.threegroup.tobedated.models.ethnicityOptions
import com.threegroup.tobedated.models.familyOptions
import com.threegroup.tobedated.models.genderOptions
import com.threegroup.tobedated.models.intentionsOptions
import com.threegroup.tobedated.models.meetUpOptions
import com.threegroup.tobedated.models.politicsOptions
import com.threegroup.tobedated.models.pronounOptions
import com.threegroup.tobedated.models.relationshipOptions
import com.threegroup.tobedated.models.religionOptions
import com.threegroup.tobedated.models.seekingOptions
import com.threegroup.tobedated.models.sexOptions
import com.threegroup.tobedated.models.sexOrientationOptions
import com.threegroup.tobedated.models.smokeOptions
import com.threegroup.tobedated.models.starOptions
import com.threegroup.tobedated.models.weedOptions
import com.threegroup.tobedated.ui.theme.AppTheme
import com.threegroup.tobedated.viewModels.SignUpViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.util.Calendar

var newUser = UserModel()
var newUserIndex = PreferenceIndexModel()
class SignUpActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getLastLocation()
            } else {
                // Handle permission denied
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        newUser.number = intent.getStringExtra("userPhone").toString()
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Request location permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
        } else {
            newUser.location = getLastLocation()
        }
        setContent {
            AppTheme {
                PolkaDotCanvas()
                SignUpNav(this@SignUpActivity)
                //HeightTest()
            }
        }
    }
    private fun requestLocationPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun getLastLocation():String {
        var latitude = ""
        var longitude = ""
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return "error"
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                // Handle the retrieved location
                if (location != null) {
                    // Got last known location
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                } else {
                    // Last location is null
                    // Handle this situation
                    latitude = "error"
                    longitude = ""
                }
            }
            .addOnFailureListener { e ->
                // Handle failure to retrieve location

            }
        return "$latitude/$longitude"
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 100
    }


    fun switchBack() {
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun finishingUp(signUpVM: SignUpViewModel){
        lifecycleScope.launch {
            newUser.promptQ1 = signUpVM.getQuestion1()
            newUser.promptQ2 = signUpVM.getQuestion2()
            newUser.promptQ3 = signUpVM.getQuestion3()
            uploadImage()
            val userToken = storeData()
            goNextScreen(userToken)

        }
    }
    private fun goNextScreen(userToken: String?) {

        val intent = Intent(this, DatingActivity::class.java)
        intent.putExtra("token", userToken)
        startActivity(intent)
        finish()
//        val viewModelDating =  DatingViewModel(MyApp.x)
//        lifecycleScope.launch {
//            viewModelDating.setUser(userToken!!)
//
//        }
    }

    private suspend fun storeData(): String? {
        val deferredToken = CompletableDeferred<String?>()

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null && currentUser.phoneNumber != null) {
            FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.phoneNumber!!)
                .setValue(newUser)
                .addOnSuccessListener {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.getIdToken(true)
                        ?.addOnCompleteListener { task2 ->
                            if (task2.isSuccessful) {
                                //val idToken = task2.result?.token
                                saveTokenToSharedPreferences(currentUser.phoneNumber)
                                // Resolve the deferred with the token value
                                deferredToken.complete(currentUser.phoneNumber)
                            } else {
                                // Handle error getting user token
                                deferredToken.complete(null)
                            }
                        }
                    showToast("Success!")
                }
                .addOnFailureListener { e ->
                    showToast("Failed ${e.message}")
                    deferredToken.complete(null)
                }
        } else {
            showToast("User NOT authenticated or phone number is null")
            deferredToken.complete(null)
        }

        // Wait for the token to be resolved and return it
        return deferredToken.await()
    }
    private suspend fun storeImageAttempt(uriString: String, contentResolver: ContentResolver, imageNumber:Int, imageName:String): String {
        var downloadUrl = ""
        try {
            val storageRef = FirebaseStorage.getInstance().reference
            val databaseRef = FirebaseDatabase.getInstance().reference
            val filePath = getFileFromContentUri(Uri.parse(uriString), contentResolver) ?: return ""

            // Create a reference to the image in Firebase Storage
            val imageRef = storageRef.child("images/$imageName${imageNumber}ProfilePhoto")

            // Upload the image file
            val file = Uri.fromFile(File(filePath))
            val inputStream = withContext(Dispatchers.IO) {
                FileInputStream(file.path)
            }

            val uploadTask = imageRef.putStream(inputStream).await()
            downloadUrl = imageRef.downloadUrl.await().toString()

            // Once you have the download URL, store it in the database
            databaseRef.child("images").push().setValue(downloadUrl)

            // Delete the local image file after successful upload
            val localFile = File(filePath)
            if (localFile.exists()) {
                val deleted = localFile.delete()
                if (!deleted) {
                    Log.e("storeImageAttempt", "Failed to delete local image file: $filePath")
                }
            }
        } catch (e: Exception) {
            // Handle the exception gracefully
            Log.e("storeImageAttempt", "Error uploading image: ${e.message}")
        }
        return downloadUrl
    }

    // Function to get the file path from a content URI
    private fun getFileFromContentUri(contentUri: Uri, contentResolver: ContentResolver): String? {
        var filePath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(contentUri, projection, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            filePath = cursor.getString(columnIndex)
        }
        return filePath
    }
    // Modified uploadImage function
    private suspend fun uploadImage() {
        newUser.image1 = storeImageAttempt(newUser.image1, contentResolver, 1, newUser.number)
        newUser.image2 = storeImageAttempt(newUser.image2, contentResolver, 2, newUser.number)
        newUser.image3 = storeImageAttempt(newUser.image3, contentResolver, 3, newUser.number)
        newUser.image4 = storeImageAttempt(newUser.image4, contentResolver, 4, newUser.number)
    }
    private fun showToast(message: String) {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveTokenToSharedPreferences(token: String?) {
        val sharedPreferences = getSharedPreferences("firebase_user", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("firebase_user_token", token)
        editor.apply()
    }
}


@Composable
fun welcomeScreen():Boolean {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Spacer(modifier = Modifier.height(55.dp))
        TitleText(title = "Welcome to: To Be Dated")
        Spacer(modifier = Modifier.height(55.dp))
        TitleText(title = "Be Kind")
            Spacer(modifier = Modifier.height(5.dp))
        BodyText(label = "Treat everyone with respect!")
            Spacer(modifier = Modifier.height(25.dp))
        TitleText(title = "Be Safe")
            Spacer(modifier = Modifier.height(5.dp))
        BodyText(label = "Don't jump into things too quick")
        Spacer(modifier = Modifier.height(25.dp))
        TitleText(title = "Be Dated")
            Spacer(modifier = Modifier.height(5.dp))
        BodyText(label = "Have fun an be yourself")
    }
    return true
}


@Composable
fun nameScreen():Boolean {
    var name by rememberSaveable { mutableStateOf(newUser.name) }
    SignUpFormat(
        title = "Your Name",
        label = "This is what people will know you ass \nJust your first name is needed",
        enterField = {
            NameQuestion(
                input = name,
                onInputChanged = { input ->  name = input
                                 newUser.name = input},
            )
        },
    )
    return name.isNotEmpty()
}
@Composable
fun birthScreen(): Boolean {
    var date by rememberSaveable { mutableStateOf(newUser.birthday) }
    val dateComponents = date.split("/")
    var month by rememberSaveable { mutableStateOf(dateComponents.getOrNull(0) ?: "") }
    var day by rememberSaveable { mutableStateOf(dateComponents.getOrNull(1) ?: "") }
    var year by rememberSaveable { mutableStateOf(dateComponents.getOrNull(2) ?: "") }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var monthMax by rememberSaveable { mutableStateOf(intArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)) }
    var dayMax by rememberSaveable { mutableIntStateOf(31) }
    val yearMin = 1940
    var yearMax by rememberSaveable { mutableIntStateOf(currentYear - 17) }
    val focusManager = LocalFocusManager.current
    DisposableEffect(date) {
        onDispose {
            newUser.birthday = date
        }
    }

    SignUpFormat(
        title = "Birthday",
        label = "Let us celebrate together!",
        enterField = { BirthdateQuestion(
            monthValue = month,
            dayValue= day,
            yearValue = year,
            onMonthChanged = { input -> //month = input
                val inValue = input.toIntOrNull()
                val checkDay = if(day.isNotEmpty()) {day.toInt()} else 5
                val checkYear = if(year.isNotEmpty()) {year.toInt()} else 1999
                monthMax = checkMonth(checkDay, checkYear)
                month = when {
                    input.length < month.length -> input
                    month.isEmpty() && inValue in 0..1 -> input
                    month.length == 1 && inValue!! in monthMax -> input
                    else -> month
                }
                if(month.length == 2){
                    focusManager.moveFocus(focusDirection = FocusDirection.Next)
                }
                if(month.isNotEmpty() && day.isNotEmpty() && year.isNotEmpty()){
                    date = if(checkBirthDate(year.toInt(),month.toInt(),day.toInt())) {
                        "$month/$day/$year"
                    }else{
                        ""
                    }
                }
            },
            onDayChanged = { input -> //day = input
                val inValue = input.toIntOrNull()
                val checkMonth = if(month.isNotEmpty()) {month.toInt()} else 1
                val checkYear = if(year.isNotEmpty()) {year.toInt()} else 1999
                dayMax = checkDay(checkMonth, checkYear)
                day = when {
                    input.length < day.length -> input
                    day.isEmpty() && inValue in 0..(dayMax/10) -> input
                    day.length == 1 && inValue in 1..(dayMax) -> input
                    else -> day
                }
                if(day.length == 2){
                    focusManager.moveFocus(focusDirection = FocusDirection.Next)
                }
                if(day.isEmpty()){
                    focusManager.moveFocus(focusDirection = FocusDirection.Previous)
                }
                if(month.isNotEmpty() && day.isNotEmpty() && year.isNotEmpty()){
                    date = if(checkBirthDate(year.toInt(),month.toInt(),day.toInt())) {
                        "$month/$day/$year"
                    }else{
                        ""
                    }
                }
            },
            onYearChanged = { input -> //year = input
                val inValue = input.toIntOrNull()
                val checkMonth = if(month.isNotEmpty()) {month.toInt()} else 1
                val checkYear = if(year.isNotEmpty()) {year.toInt()} else 1999
                val checkDay = if(day.isNotEmpty()) {day.toInt()} else 5
                yearMax = checkYear(checkYear, checkMonth, checkDay)
                year = when {
                    input.length < year.length -> input
                    year.isEmpty() && inValue in (yearMin/1000)..(yearMax/1000) -> input
                    year.length == 1 && inValue in (yearMin/100)..(yearMax/100) -> input
                    year.length == 2 && inValue in (yearMin/10)..(yearMax/10) -> input
                    year.length == 3 && inValue in (yearMin/1)..(yearMax/1) -> input
                    else -> year
                }
                if(year.isEmpty()){
                    focusManager.moveFocus(focusDirection = FocusDirection.Previous)
                }
                if(month.isNotEmpty() && day.isNotEmpty() && year.isNotEmpty()){
                    date = if(checkBirthDate(year.toInt(),month.toInt(),day.toInt())) {
                        "$month/$day/$year"
                    }else{
                        ""
                    }
                }
            }
        ) },
    )
    return date.isNotEmpty()
}
@Composable
fun pronounScreen(): Boolean {
    var pronoun by rememberSaveable { mutableStateOf(newUser.pronoun) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.pronoun) }

    SignUpFormat(
        title = "Pronouns",
        label = "How do you go by?",
        enterField = {
            RadioButtonGroup(
                options = pronounOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    pronoun = pronounOptions[selectedOptionIndex]
                    newUser.pronoun = pronounOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return pronoun.isNotEmpty()
}
@Composable
fun genderScreen():Boolean {
    var gender by rememberSaveable { mutableStateOf(newUser.gender) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.gender) }
    SignUpFormat(
        title = "Your Gender",
        label = "What do you identify as",
        enterField = {
            RadioButtonGroup(
                options = genderOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    gender = genderOptions[selectedOptionIndex]
                    newUser.gender = genderOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            ) },
    )
    return gender.isNotEmpty()
}
@Composable
fun heightScreen():Boolean {
    var height by rememberSaveable { mutableStateOf(newUser.height) }
    val valuesPickerState = rememberPickerState()
    DisposableEffect(height) {
        onDispose {
            newUser.height = height
        }
    }

    height = valuesPickerState.selectedItem
    val feet = remember {
        val heights = mutableListOf<String>()
        heights.add(" ")
        heights.add(" ")
        for (feet in 4..6) {
            for (inches in 0..11) {
                heights.add("$feet'$inches\"")
            }
        }
        heights
    }
    val cms = listOf(" ", " ") + (122..214).map { "$it cm" }
    val cm = remember { cms }
    SignUpFormat(
        title = "How tall are you?",
        label = "Did you know your arms length is as long as your hieght?\nWe just want to know how big your hug is!",
        enterField = {
            HeightQuestion(
                feet = feet,
                cm = cm,
                pickedState = valuesPickerState
            )
        })
    return height.isNotEmpty()
}
@Composable
fun ethnicityScreen():Boolean {
    var ethnicity by rememberSaveable { mutableStateOf(newUser.ethnicity) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.ethnicity) }
    SignUpFormat(
        title = "Ethnicity",
        label = "We curious on where you are from!",
        enterField = {
            RadioButtonGroup(
                options = ethnicityOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    ethnicity = ethnicityOptions[selectedOptionIndex]
                    newUser.ethnicity = ethnicityOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return ethnicity.isNotEmpty()
}
@Composable
fun starScreen():Boolean{
    var star by rememberSaveable { mutableStateOf(newUser.star) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.star) }

    SignUpFormat(
        title = "Whats your sign?",
        label = "Do the stars say we are in favor?",
        enterField = {
            RadioButtonGroup(
                options = starOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    star = starOptions[selectedOptionIndex]
                    newUser.star = starOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return star.isNotEmpty()
}
@Composable
fun sexOriScreen():Boolean{
    var sexOri by rememberSaveable { mutableStateOf(newUser.sexOrientation) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.sexOrientation) }
    SignUpFormat(
        title = "Sexual Orientation",
        label = "Who do you like?",
        enterField = {
            RadioButtonGroup(
                options = sexOrientationOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    sexOri = sexOrientationOptions[selectedOptionIndex]
                    newUser.sexOrientation = sexOrientationOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return sexOri.isNotEmpty()
}
@Composable
fun searchScreen():Boolean{
    var search by rememberSaveable { mutableStateOf(newUser.seeking) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.seeking) }
    SignUpFormat(
        title = "Searching For?",
        label = "Who are you looking to connect with?",
        enterField = {
            RadioButtonGroup(
                options = seekingOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    search = seekingOptions[selectedOptionIndex]
                    newUser.seeking = seekingOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return search.isNotEmpty()
}
@Composable
fun sexScreen():Boolean{
    var sex by rememberSaveable { mutableStateOf(newUser.sex) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.sex) }
    SignUpFormat(
        title = "Your sex",
        label = "What search category will you be in?",
        enterField = {
            RadioButtonGroup(
                options = sexOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    sex = sexOptions[selectedOptionIndex]
                    newUser.sex = sexOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return sex.isNotEmpty()
}
@Composable
fun mbtiScreen(onNavigate: () -> Unit):Boolean{
    var mbti by rememberSaveable { mutableStateOf(newUser.testResultsMbti) }
    val questions = listOf( "MBTI TEST QUESTION 1", "MBTI QUESTION 2", "MBTI 3", "MBTI TEST QUESTION 4", "MBTI QUESTION 5", "MBTI 6")
    var isSkip by rememberSaveable { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.End
    ){
        GenTextOnlyButton(
            onClick = { isSkip = true },
            text = "Skip",
            color = Color.Gray
        )
    }
    SignUpFormat(
        title = "MBTI",
        label = "What does your personality say about you?",
        enterField = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                questions.forEach { quest ->
                    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(-1) } //This is their Answer
                    PersonalityTest(
                        //TODO some how get value from this shit, I am thinking don't return the values return the result. so math
                        selectedIndex = selectedOptionIndex,
                        onSelectionChange = { newIndex ->
                            selectedOptionIndex = newIndex
                            mbti = "Not Taken"
                            newUser.testResultsMbti = "Not Taken"
                        },
                        question = quest
                    )
                }
            }
        },
    )

    if(isSkip){
        var selectedResult by rememberSaveable { mutableStateOf("Not Taken") }
        DropdownAlertBox(
            onConfirmation = { isSkip = false
                newUser.testResultsMbti = selectedResult
                onNavigate()},
            onDismissRequest = { isSkip = false
                newUser.testResultsMbti = "Not Taken"
                onNavigate()},
            selectedMBTI = selectedResult,
            onMBTISelect = { newResult -> selectedResult = newResult }
        )
    }

    return mbti.isNotEmpty()//TODO
}
@Composable
fun ourTestScreen():Boolean{
    var our by rememberSaveable { mutableStateOf(newUser.testResultTbd) }
    val questions = listOf( "Do you like long walks on the beach?", "Do you like candles?", "Do you even?",
        "Do you like long walks on the beach?", "Do you like candles?", "Do you even?",
        "Do you like long walks on the beach?", "Do you like candles?", "Do you even?",
        "Do you like long walks on the beach?", "Do you like candles?", "Do you even?",)
    SignUpFormat(
        title = "Our Test",
        label = "We just want to know you a little more",
        enterField = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .verticalScroll(rememberScrollState())
            ){
                questions.forEach { quest ->
                    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(-1) } //This is their Answer
                    PersonalityTest(
                        //TODO some how get value from this shit, I am thinking don't return the values return the result. so math
                        selectedIndex = selectedOptionIndex,
                        onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                            our = 12
                            newUser.testResultTbd = 12
                        },
                        question = quest
                    )
                }
            }
        },
    )
    return true //our.isNotEmpty()
}
@Composable
fun meetUpScreen():Boolean{
    var metUp by rememberSaveable { mutableStateOf(newUser.meetUp) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.meetUp) }
    SignUpFormat(
        title = "How comfortable are you meeting up?",
        label = "Whens the first date?",
        enterField = {
            RadioButtonGroup(
                options = meetUpOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    metUp = meetUpOptions[selectedOptionIndex]
                    newUser.meetUp = meetUpOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return metUp.isNotEmpty()
}
@Composable
fun childrenScreen():Boolean{
    var children by rememberSaveable { mutableStateOf(newUser.children) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.children) }
    SignUpFormat(
        title = "Do you have children?",
        label = "Do you have someone else we should think about too?",
        enterField = {
            RadioButtonGroup(
                options = childrenOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    children = childrenOptions[selectedOptionIndex]
                    newUser.children = childrenOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return children.isNotEmpty()
}
@Composable
fun familyScreen():Boolean{
    var family by rememberSaveable { mutableStateOf(newUser.family) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.family) }
    SignUpFormat(
        title = "Do you want Children?",
        label = "What are your plans for your family?",
        enterField = {
            RadioButtonGroup(
                options = familyOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    family = familyOptions[selectedOptionIndex]
                    newUser.family = familyOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return family.isNotEmpty()
}
@Composable
fun educationScreen():Boolean{
    var education by rememberSaveable { mutableStateOf(newUser.education) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.education) }
    SignUpFormat(
        title = "Education Level",
        label = "Your heart is always smarter than your brain",
        enterField = {
            RadioButtonGroup(
                options = educationOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    education = educationOptions[selectedOptionIndex]
                    newUser.education = educationOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return education.isNotEmpty()
}
@Composable
fun religiousScreen():Boolean{
    var religious by rememberSaveable { mutableStateOf(newUser.religion) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.religion) }
    SignUpFormat(
        title = "Religion",
        label = "We believe in you, but what do you believe in?",
        enterField = {
            RadioButtonGroup(
                options = religionOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    religious = religionOptions[selectedOptionIndex]
                    newUser.religion = religionOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return religious.isNotEmpty()
}
@Composable
fun politicsScreen():Boolean{
    var politics by rememberSaveable { mutableStateOf(newUser.politics) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.politics) }
    SignUpFormat(
        title = "Politics",
        label = "We vote for you to find a meaningful connection",
        enterField = {
            RadioButtonGroup(
                options = politicsOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    politics = politicsOptions[selectedOptionIndex]
                    newUser.politics = politicsOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return politics.isNotEmpty()
}
@Composable
fun relationshipScreen():Boolean{
    var relationship by rememberSaveable { mutableStateOf(newUser.relationship) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.relationship) }
    SignUpFormat(
        title = "Relationship type",
        label = "There is always enough to go around",
        enterField = {
            RadioButtonGroup(
                options = relationshipOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    relationship = relationshipOptions[selectedOptionIndex]
                    newUser.relationship = relationshipOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return relationship.isNotEmpty()
}
@Composable
fun intentionsScreen():Boolean{
    var intention by rememberSaveable { mutableStateOf(newUser.intentions) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.intentions) }
    SignUpFormat(
        title = "Dating Intentions",
        label = "What are you looking for?",
        enterField = {
            RadioButtonGroup(
                options = intentionsOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    intention = intentionsOptions[selectedOptionIndex]
                    newUser.intentions = intentionsOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return intention.isNotEmpty()
}
@Composable
fun drinkScreen():Boolean{
    var drink by rememberSaveable { mutableStateOf(newUser.drink) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.drink) }
    SignUpFormat(
        title = "Do you drink?",
        label = "How do you like to party?",
        enterField = {
            RadioButtonGroup(
                options = drinkOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    drink = drinkOptions[selectedOptionIndex]
                    newUser.drink = drinkOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return drink.isNotEmpty()
}
@Composable
fun smokeScreen():Boolean{
    var smoke by rememberSaveable { mutableStateOf(newUser.smoke) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.smoke) }
    SignUpFormat(
        title = "Do you Smoke?",
        label = "How do you like to relax?",
        enterField = {
            RadioButtonGroup(
                options = smokeOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    smoke = smokeOptions[selectedOptionIndex]
                    newUser.smoke = smokeOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return smoke.isNotEmpty()
}
@Composable
fun weedScreen():Boolean{
    var weed by rememberSaveable { mutableStateOf(newUser.weed) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.weed) }
    SignUpFormat(
        title = "Do you Smoke, Marijuana?",
        label = "How do you like to relax or party?",
        enterField = {
            RadioButtonGroup(
                options = weedOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    weed = weedOptions[selectedOptionIndex]
                    newUser.weed = weedOptions[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return weed.isNotEmpty()
}

@Composable
fun promptQuestionsScreen(nav:NavController, signUpVM:SignUpViewModel):Boolean{
    var promptQ1 by rememberSaveable { mutableStateOf(newUser.promptQ1) }
    var promptA1 by rememberSaveable { mutableStateOf(newUser.promptA1) }
    var promptQ2 by rememberSaveable { mutableStateOf(newUser.promptQ2) }
    var promptA2 by rememberSaveable { mutableStateOf(newUser.promptA2) }
    var promptQ3 by rememberSaveable { mutableStateOf(newUser.promptQ3) }
    var promptA3 by rememberSaveable { mutableStateOf(newUser.promptA3) }
    var isEnable1 by rememberSaveable { mutableStateOf(false) }
    var isEnable2 by rememberSaveable { mutableStateOf(false) }
    var isEnable3 by rememberSaveable { mutableStateOf(false) }
    promptQ1 = signUpVM.getQuestion1()
    promptQ2 = signUpVM.getQuestion2()
    promptQ3 = signUpVM.getQuestion3()
    SignUpFormat(
        title = "Some Ice breakers!",
        label = "Don't be shy, the ice will melt anyway!",
        enterField = {

            OutlinedButton(
                onClick = {
                    nav.navigate("PromptQuestions/1")
                    newUser.promptQ1 = promptQ1
                    isEnable1 = true
                })
            {
                GenericLabelText(text = promptQ1)
            }
            PromptAnswer(
                isEnables = isEnable1,
                input = promptA1,
                onInputChanged = { input  ->  promptA1 = input
                    newUser.promptA1 = input
                },
            )
                Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    nav.navigate("PromptQuestions/2")
                    newUser.promptQ2 = promptQ2
                    isEnable2 = true
                })
            {
                GenericLabelText(text = promptQ2)
            }
            PromptAnswer(
                isEnables = isEnable2,
                input = promptA2,
                onInputChanged = { input  ->  promptA2 = input
                    newUser.promptA2 = input
                },
            )
                Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    nav.navigate("PromptQuestions/3")
                    newUser.promptQ3 = promptQ3
                    isEnable3 = true
                })
            {
                GenericLabelText(text = promptQ3)
            }
            PromptAnswer(
                isEnables = isEnable3,
                input = promptA3,
                onInputChanged = { input  ->  promptA3 = input
                    newUser.promptA3 = input
                },
            )
        },
    )
    return (promptA1.isNotEmpty() && promptA2.isNotEmpty() && promptA3.isNotEmpty())
}

@Composable
fun bioScreen():Boolean{
    var bio by rememberSaveable { mutableStateOf(newUser.bio) }
    SignUpFormat(
        title = "Bio",
        label = "",
        enterField = {
            BioQuestion(
                input = bio,
                onInputChanged = { input  ->  bio = input
                    newUser.bio = input
                },
            )
        },
    )
    return (bio.length in 16..499)
}

@Composable
fun photoScreen():Boolean{
    var photo1 by rememberSaveable { mutableStateOf(newUser.image1) }
    var photo2 by rememberSaveable { mutableStateOf(newUser.image2) }
    var photo3 by rememberSaveable { mutableStateOf(newUser.image3) }
    var photo4 by rememberSaveable { mutableStateOf(newUser.image4) }
    var showFinalDialog by rememberSaveable { mutableStateOf(true) }
    if (showFinalDialog) {
        DialogWithImage(
            onDismissRequest = { showFinalDialog = false },
            imageDescription = "first photo",
            body = "To make everyone look and feel more equal\nWe have a strict first photo policy!\nPlease Select a photo that matches the one above\nThe second third and forth express yourself\nBut we also shy against group photos",
            painter = painterResource(id = R.drawable.firstphoto),
        )
    }
    val nullPhoto = painterResource(id = R.drawable.photoholder)
    var imageUri1 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri2 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri3 by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageUri4 by rememberSaveable { mutableStateOf<Uri?>(null) }

    val enables by rememberSaveable { mutableStateOf(BooleanArray(3) { false }) }
    val galleryLauncher1 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri1 = it
            photo1 = it.toString()
            newUser.image1 = it.toString()} })
    val galleryLauncher2 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri2 = it
            photo2 = it.toString()
            newUser.image2 = it.toString()} })
    val galleryLauncher3 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri3 = it
            photo3 = it.toString()
            newUser.image3 = it.toString()} })
    val galleryLauncher4 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri4 = it
            photo4 = it.toString()
            newUser.image4 = it.toString()} })

    SignUpFormat(
        title = "Add some photos!",
        label = "Show yourself off!",
        enterField = { PhotoQuestion(
            photo1 = if(imageUri1 != null){rememberAsyncImagePainter(model = imageUri1)}else{nullPhoto},
            photo2 = if(imageUri2 != null){rememberAsyncImagePainter(model = imageUri2)}else{nullPhoto},
            photo3 = if(imageUri3 != null){rememberAsyncImagePainter(model = imageUri3)}else{nullPhoto},
            photo4 = if(imageUri4 != null){rememberAsyncImagePainter(model = imageUri4)}else{nullPhoto},
            onClick1= { galleryLauncher1.launch("image/*")
                enables[0] = true
            },
            onClick2= { galleryLauncher2.launch("image/*")
                enables[1] = true
            },
            onClick3= { galleryLauncher3.launch("image/*")
                enables[2] = true
            },
            onClick4= { galleryLauncher4.launch("image/*")
            },
            isEnabled2= enables[0],
            isEnabled3= enables[1],
            isEnabled4= enables[2],
        )
        },
    )
    return (photo1.isNotEmpty() && photo2.isNotEmpty() && photo3.isNotEmpty())
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