package com.threegroup.tobedated

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.threegroup.tobedated.callclass.calcAge
import com.threegroup.tobedated.callclass.checkBirthDate
import com.threegroup.tobedated.callclass.checkDay
import com.threegroup.tobedated.callclass.checkMonth
import com.threegroup.tobedated.callclass.checkYear
import com.threegroup.tobedated.callclass.getCurrentLocation
import com.threegroup.tobedated.composables.BioQuestion
import com.threegroup.tobedated.composables.BirthdateQuestion
import com.threegroup.tobedated.composables.BodyText
import com.threegroup.tobedated.composables.DialogWithImage
import com.threegroup.tobedated.composables.NameQuestion
import com.threegroup.tobedated.composables.PersonalityTest
import com.threegroup.tobedated.composables.PhotoQuestion
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.composables.RadioButtonGroup
import com.threegroup.tobedated.composables.SignUpFormat
import com.threegroup.tobedated.composables.SignUpNav
import com.threegroup.tobedated.composables.TitleText
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.ui.theme.AppTheme
import java.util.Calendar

class SignUpActivity : ComponentActivity() {
    private val indexArray = Array(5) { -1 }
    private val userInfoArray = Array(14) { "" }
    private var userLoginInfo: String = "" //This is the users phone number
    override fun onCreate(savedInstanceState: Bundle?) {
        userLoginInfo = intent.getStringExtra("userPhone").toString()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PolkaDotCanvas()
                SignUpNav(this@SignUpActivity, userInfoArray, indexArray)
            }
        }
    }

    fun switchBack() {
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goNextScreen() {
        val intent = Intent(this, DatingActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun storeData() {
        val data = UserModel(
            name = userInfoArray[0],
            birthday = userInfoArray[1],
            pronoun = userInfoArray[2],
            gender = userInfoArray[3],
            sexOrientation = userInfoArray[4],
            seeking = userInfoArray[5],
            sex = userInfoArray[6],
            testResultsMbti = userInfoArray[7],
            testResultTbd = userInfoArray[8],
            bio = userInfoArray[9],
            image1 = userInfoArray[10],
            image2 = userInfoArray[11],
            image3 = userInfoArray[12],
            image4 = userInfoArray[13],
            age = calcAge(userInfoArray[1].split("/")),
            location = getCurrentLocation(),
            status = "Active"
        )
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null && currentUser.phoneNumber != null) {
            FirebaseDatabase.getInstance().getReference("users")
                .child(currentUser.phoneNumber!!)
                .setValue(data)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(applicationContext, "Failed ${e.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(applicationContext, "User NOT authenticated or phone number is null", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun WelcomeScreen() {
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
    }
}


@Composable
fun NameScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var name by rememberSaveable { mutableStateOf(userInfo[0]) }
    updateButtonState(name)
    DisposableEffect(name) {
        onDispose {
            onAnswerChanged(name, 0)
        }
    }
    SignUpFormat(
        title = "Your Name",
        label = "This is what people will know you ass \n Just your first name is needed",
        enterField = {
            NameQuestion(
                input = name,
                onInputChanged = { input ->  name = input },
            )
        },
    )
}
@Composable
fun BirthScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var date by rememberSaveable { mutableStateOf(userInfo[1]) }
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
    updateButtonState(date)
    DisposableEffect(date) {
        onDispose {
            onAnswerChanged(date, 1)
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
}
@Composable
fun PronounScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var pronoun by rememberSaveable { mutableStateOf(userInfo[2]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[0]) }
    updateButtonState(pronoun)
    DisposableEffect(pronoun, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, 0)
            onAnswerChanged(pronoun, 2)
        }
    }
    SignUpFormat(
        title = "Pronouns",
        label = "How do you go by?",
        enterField = {
            val opts = listOf("He/Him", "She/Her", "They/Them", "Other: \nCan change later")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    pronoun = opts[selectedOptionIndex]
                },
                style = AppTheme.typography.titleMedium,
            )
        },
    )
}
@Composable
fun GenderScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var gender by rememberSaveable { mutableStateOf(userInfo[3]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[1]) }
    updateButtonState(gender)
    DisposableEffect(gender, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, 1)
            onAnswerChanged(gender, 3)
        }
    }
    SignUpFormat(
        title = "Your Gender",
        label = "What do you identify as",
        enterField = {
            val opts = listOf("Cis-Gender", "Transgender", "Non-binary", "Other")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    gender = opts[selectedOptionIndex]
                },
                style = AppTheme.typography.titleMedium
            ) },
    )
}
@Composable
fun SexOriScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var sexOri by rememberSaveable { mutableStateOf(userInfo[4]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[2]) }
    updateButtonState(sexOri)
    DisposableEffect(sexOri, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, 2)
            onAnswerChanged(sexOri, 4)
        }
    }
    SignUpFormat(
        title = "Sexual Orientation",
        label = "Who do you like?",
        enterField = {
            val opts = listOf("Asexual", "Bisexual", "HeteroSexual", "Pansexual", "Questioning", "Other")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    sexOri = opts[selectedOptionIndex]
                },
                style = AppTheme.typography.titleMedium
            )
        },
    )
}
@Composable
fun SearchScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var search by rememberSaveable { mutableStateOf(userInfo[5]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[3]) }
    updateButtonState(search)
    DisposableEffect(search, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, 3)
            onAnswerChanged(search, 5)
        }
    }
    SignUpFormat(
        title = "Searching For?",
        label = "Who are you looking to connect with?",
        enterField = {
            val opts = listOf("Male", "Female", "Everyone")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    search = opts[selectedOptionIndex]
                },
                style = AppTheme.typography.titleMedium
            )
        },
    )
}
@Composable
fun SexScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var sex by rememberSaveable { mutableStateOf(userInfo[6]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[4]) }
    updateButtonState(sex)
    DisposableEffect(sex, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, 4)
            onAnswerChanged(sex, 6)
        }
    }
    SignUpFormat(
        title = "Your sex",
        label = "What search category will you be in?",
        enterField = {
            val opts = listOf("Male", "Female", "Other")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    sex = opts[selectedOptionIndex]
                },
                style = AppTheme.typography.titleMedium
            )
        },
    )
}
@Composable
fun MbtiScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var mbti by rememberSaveable { mutableStateOf(userInfo[7]) }
    updateButtonState("mbti")//Don'tWork??
    val questions = listOf( "MBTI TEST QUESTION 1", "MBTI QUESTION 2", "MBTI 3")
    DisposableEffect(mbti) {
        onDispose {
            onAnswerChanged(mbti, 7)
        }
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
                            mbti = "MBTI Text answer"
                        },
                        question = quest
                    )
                }
            }
        },
    )
}
@Composable
fun OurTestScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var our by rememberSaveable { mutableStateOf(userInfo[8]) }
    updateButtonState("our")//Don't Work???
    val questions = listOf( "Do you like long walks on the beach?", "Do you like candles?", "Do you even?",
        "Do you like long walks on the beach?", "Do you like candles?", "Do you even?",
        "Do you like long walks on the beach?", "Do you like candles?", "Do you even?",
        "Do you like long walks on the beach?", "Do you like candles?", "Do you even?",)
    DisposableEffect(our) {
        onDispose {
            onAnswerChanged(our, 8)
        }
    }
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
                            our = "our Test Answer"
                        },
                        question = quest
                    )
                }
            }
        },
    )
}
@Composable
fun BioScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit, updateButtonState: (String) -> Unit) {
    var bio by rememberSaveable { mutableStateOf(userInfo[9]) }
    updateButtonState(bio) //Make it work so its only 15 characters allowed
    DisposableEffect(bio) {
        onDispose {
            onAnswerChanged(bio, 9)
        }
    }
    SignUpFormat(
        title = "Bio",
        label = "",
        enterField = {
            BioQuestion(
                input = bio,
                onInputChanged = { input  ->  bio = input
                },
            )
        },
    )
}
@Composable
fun PhotoScreen(userInfo: Array<String>, onAnswerChanged: (String, String, String, String) -> Unit, updateButtonState: (String) -> Unit) {
    var photo1 by rememberSaveable { mutableStateOf(userInfo[10]) }
    var photo2 by rememberSaveable { mutableStateOf(userInfo[11]) }
    var photo3 by rememberSaveable { mutableStateOf(userInfo[12]) }
    var photo4 by rememberSaveable { mutableStateOf(userInfo[13]) }
    updateButtonState(photo3)
    DisposableEffect(photo1, photo2, photo3, photo4) {
        onDispose {
            onAnswerChanged(photo1, photo2, photo3, photo4)
        }
    }
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
            photo1 = it.toString()} })
    val galleryLauncher2 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri2 = it
            photo2 = it.toString()} })
    val galleryLauncher3 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri3 = it
            photo3 = it.toString()} })
    val galleryLauncher4 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri4 = it
            photo4 = it.toString()} })

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
        )},
    )
}


enum class SignUp {
    WelcomeScreen,
    NameScreen,
    BirthScreen,
    PronounScreen,
    GenderScreen,
    SexOriScreen,
    SearchScreen,
    SexScreen,
    MbtiScreen,
    OurTestScreen,
    BioScreen,
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