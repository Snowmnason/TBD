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
import androidx.compose.runtime.remember
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
import com.threegroup.tobedated.composables.HeightQuestion
import com.threegroup.tobedated.composables.NameQuestion
import com.threegroup.tobedated.composables.PersonalityTest
import com.threegroup.tobedated.composables.PhotoQuestion
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.composables.RadioButtonGroup
import com.threegroup.tobedated.composables.SignUpFormat
import com.threegroup.tobedated.composables.SignUpNav
import com.threegroup.tobedated.composables.TitleText
import com.threegroup.tobedated.composables.getCustomButtonStyle
import com.threegroup.tobedated.composables.rememberPickerState
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.ui.theme.AppTheme
import java.util.Calendar

class SignUpActivity : ComponentActivity() {
    private val indexArray = Array(17) { -1 }
    private val userInfoArray = Array(27) { "" }
    private var userLoginInfo: String = "" //This is the users phone number
    override fun onCreate(savedInstanceState: Bundle?) {
        userLoginInfo = intent.getStringExtra("userPhone").toString()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PolkaDotCanvas()
                SignUpNav(this@SignUpActivity, userInfoArray, indexArray)
                //HeightTest()
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
            hieght = userInfoArray [4],
            ethnicity = userInfoArray[5],
            star = userInfoArray[6],
            sexOrientation = userInfoArray[7],
            seeking = userInfoArray[8],
            sex = userInfoArray[9],
            testResultsMbti = userInfoArray[10],
            testResultTbd = userInfoArray[11],
            children = userInfoArray[12],
            family = userInfoArray[13],
            education = userInfoArray[14],
            religious = userInfoArray[15],
            politics = userInfoArray[16],
            relationship = userInfoArray[17],
            intentions = userInfoArray[18],
            drink = userInfoArray[19],
            smoke = userInfoArray[20],
            weed = userInfoArray[21],
            bio = userInfoArray[22],
            image1 = userInfoArray[23],
            image2 = userInfoArray[24],
            image3 = userInfoArray[25],
            image4 = userInfoArray[26],
            age = calcAge(userInfoArray[1].split("/")),
            location = getCurrentLocation(),
            status = "Active",
            number = userLoginInfo,
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
    val questionNumber = 0
    var name by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    updateButtonState(name)
    DisposableEffect(name) {
        onDispose {
            onAnswerChanged(name, questionNumber)
        }
    }
    SignUpFormat(
        title = "Your Name",
        label = "This is what people will know you ass \nJust your first name is needed",
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
    val questionNumber = 1
    var date by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
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
            onAnswerChanged(date, questionNumber)
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
    val questionNumber = 2
    val radioQuestionNumber = 0
    var pronoun by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(pronoun)
    DisposableEffect(pronoun, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(pronoun, questionNumber)
        }
    }
    SignUpFormat(
        title = "Pronouns",
        label = "How do you go by?",
        enterField = {
            val opts = listOf("He/Him", "She/Her", "They/Them", "Ze/Zir" ,"Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    pronoun = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle(),
            )
        },
    )
}
@Composable
fun GenderScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 3
    val radioQuestionNumber = 1
    var gender by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(gender)
    DisposableEffect(gender, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(gender, questionNumber)
        }
    }
    SignUpFormat(
        title = "Your Gender",
        label = "What do you identify as",
        enterField = {
            val opts = listOf("Cis-Gender", "Transgender", "Non-binary")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    gender = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            ) },
    )
}
@Composable
fun HeightScreen(userInfo: Array<String>,  onAnswerChanged: (String, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 4
    var height by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    updateButtonState(height)
    DisposableEffect(height) {
        onDispose {
            onAnswerChanged(height, questionNumber)
        }
    }
    val valuesPickerState = rememberPickerState()
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
}
@Composable
fun EthnicityScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 5
    val radioQuestionNumber = 2
    var ethnicity by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(ethnicity)
    DisposableEffect(ethnicity, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(ethnicity, questionNumber)
        }
    }
    SignUpFormat(
        title = "Ethnicity",
        label = "We curious on where you are from!",
        enterField = {
            val opts = listOf("Black/African Descent", "East Asian", "Hispanic/Latino",
                "Middle Eastern",  "Native American", "Pacific Islander", "South Asian",  "Southeast Asian",  "White/Caucasian",  "Other",  "Ask me",)
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    ethnicity = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun StarScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 6
    val radioQuestionNumber = 3
    var star by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(star)
    DisposableEffect(star, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(star, questionNumber)
        }
    }
    SignUpFormat(
        title = "Whats your sign?",
        label = "Do the stars say we are in favor?",
        enterField = {
            val opts = listOf("Aries", "Taurus", "Gemini",
                "Cancer",  "Leo", "Virgo", "Libra",  "Scorpius",  "Sagittarius",  "Capricornus", "Aquarius", "Pisces", "Ask me",)
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    star = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun SexOriScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 7
    val radioQuestionNumber = 4
    var sexOri by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(sexOri)
    DisposableEffect(sexOri, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(sexOri, questionNumber)
        }
    }
    SignUpFormat(
        title = "Sexual Orientation",
        label = "Who do you like?",
        enterField = {
            val opts = listOf("Androsexual", "Asexual", "Bisexual", "Demisexuality", "Gynesexual", "HeteroSexual", "Pansexual", "Queer", "Questioning", "Ask Me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    sexOri = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun SearchScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 8
    val radioQuestionNumber = 5
    var search by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(search)
    DisposableEffect(search, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(search, questionNumber)
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
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun SexScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 9
    val radioQuestionNumber = 6
    var sex by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(sex)
    DisposableEffect(sex, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(sex, questionNumber)
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
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun MbtiScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 10
    var mbti by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    updateButtonState("mbti")//Don'tWork??
    val questions = listOf( "MBTI TEST QUESTION 1", "MBTI QUESTION 2", "MBTI 3", "MBTI TEST QUESTION 4", "MBTI QUESTION 5", "MBTI 6")
    DisposableEffect(mbti) {
        onDispose {
            onAnswerChanged(mbti, questionNumber)
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
    val questionNumber = 11
    var our by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    updateButtonState("our")//Don't Work???
    DisposableEffect(our) {
        onDispose {
            onAnswerChanged(our, questionNumber)
        }
    }
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
fun ChildrenScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 12
    val radioQuestionNumber = 7
    var children by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(children)
    DisposableEffect(children, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(children, questionNumber)
        }
    }
    SignUpFormat(
        title = "Do you have children?",
        label = "Do you have someone else we should think about too?",
        enterField = {
            val opts = listOf("Don't have children", "Have children", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    children = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun FamilyScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 13
    val radioQuestionNumber = 8
    var family by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(family)
    DisposableEffect(family, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(family, questionNumber)
        }
    }
    SignUpFormat(
        title = "Do you want Children?",
        label = "What are your plans for your family?",
        enterField = {
            val opts = listOf("Don't want children", "Want children", "Open to children", "Not sure yet", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    family = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun EducationScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 14
    val radioQuestionNumber = 9
    var education by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(education)
    DisposableEffect(education, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(education, questionNumber)
        }
    }
    SignUpFormat(
        title = "Education Level",
        label = "Your heart is always smarter than your brain",
        enterField = {
            val opts = listOf("High School", "Undergrad", "Postgrad", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    education = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun ReligiousScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 15
    val radioQuestionNumber = 10
    var religious by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(religious)
    DisposableEffect(religious, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(religious, questionNumber)
        }
    }
    SignUpFormat(
        title = "Religion",
        label = "We believe in you, but what do you believe in?",
        enterField = {
            val opts = listOf("Agnostic", "Atheist", "Buddhist", "Catholic", "Christian", "Hindu", "Jewish", "Muslim", "Sikh", "Spiritual", "Other", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    religious = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun PoliticsScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 16
    val radioQuestionNumber = 11
    var politics by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(politics)
    DisposableEffect(politics, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(politics, questionNumber)
        }
    }
    SignUpFormat(
        title = "Politics",
        label = "We vote for you to find a meaningful connection",
        enterField = {
            val opts = listOf("Liberal", "Moderate", "Conservative", "Not Political", "Other", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    politics = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun RelationshipScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 17
    val radioQuestionNumber = 12
    var relationship by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(relationship)
    DisposableEffect(relationship, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(relationship, questionNumber)
        }
    }
    SignUpFormat(
        title = "Relationship type",
        label = "There is always enough to go around",
        enterField = {
            val opts = listOf("Monogamy", "Non-monogamy", "Finding it", "Other", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    relationship = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun IntentionsScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 18
    val radioQuestionNumber = 13
    var intention by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(intention)
    DisposableEffect(intention, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(intention, questionNumber)
        }
    }
    SignUpFormat(
        title = "Dating Intentions",
        label = "What are you looking for?",
        enterField = {
            val opts = listOf("Life Partner", "Long-term", "Long open to short", "Short open to long", "Short-term", "Figuring it out","Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    intention = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun DrinkScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 19
    val radioQuestionNumber = 14
    var drink by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(drink)
    DisposableEffect(drink, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(drink, questionNumber)
        }
    }
    SignUpFormat(
        title = "Do you drink?",
        label = "How do you like to party?",
        enterField = {
            val opts = listOf("Yes", "Sometimes", "No", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    drink = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun SmokeScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 20
    val radioQuestionNumber = 15
    var smoke by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(smoke)
    DisposableEffect(smoke, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(smoke, questionNumber)
        }
    }
    SignUpFormat(
        title = "Do you Smoke?",
        label = "How do you like to relax?",
        enterField = {
            val opts = listOf("Yes", "Sometimes", "No", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    smoke = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}
@Composable
fun WeedScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 21
    val radioQuestionNumber = 16
    var weed by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(index[radioQuestionNumber]) }
    updateButtonState(weed)
    DisposableEffect(weed, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, radioQuestionNumber)
            onAnswerChanged(weed, questionNumber)
        }
    }
    SignUpFormat(
        title = "Do you Smoke, Marijuana?",
        label = "How do you like to relax or party?",
        enterField = {
            val opts = listOf("Yes", "Sometimes", "No", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    weed = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
}

@Composable
fun BioScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit, updateButtonState: (String) -> Unit) {
    val questionNumber = 22
    var bio by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    updateButtonState(bio) //Make it work so its only 15 characters allowed
    DisposableEffect(bio) {
        onDispose {
            onAnswerChanged(bio, questionNumber)
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
    val questionNumber = 23
    var photo1 by rememberSaveable { mutableStateOf(userInfo[questionNumber]) }
    var photo2 by rememberSaveable { mutableStateOf(userInfo[questionNumber+1]) }
    var photo3 by rememberSaveable { mutableStateOf(userInfo[questionNumber+2]) }
    var photo4 by rememberSaveable { mutableStateOf(userInfo[questionNumber+3]) }
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
    HieghtScreen,
    EthnicityScreen,
    StarScreen,
    SearchScreen,
    SexScreen,
    MbtiScreen,
    OurTestScreen,
    ChildrenScreen,
    FamilyScreen,
    EducationScreen,
    ReligiousScreen,
    PoliticsScreen,
    RelationshipScreen,
    IntentionsScreen,
    DrinkScreen,
    SmokeScreen,
    WeedScreen,
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