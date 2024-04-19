package com.threegroup.tobedated._signUp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.threegroup.tobedated.R
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._login.LoginActivity
import com.threegroup.tobedated._signUp.composables.BioQuestion
import com.threegroup.tobedated._signUp.composables.BirthdateQuestion
import com.threegroup.tobedated._signUp.composables.HeightQuestion
import com.threegroup.tobedated._signUp.composables.MBTIDropDown
import com.threegroup.tobedated._signUp.composables.NameQuestion
import com.threegroup.tobedated._signUp.composables.PersonalityTest
import com.threegroup.tobedated._signUp.composables.PhotoQuestion
import com.threegroup.tobedated._signUp.composables.PromptAnswer
import com.threegroup.tobedated._signUp.composables.SignUpFormat
import com.threegroup.tobedated._signUp.composables.SignUpFormatLong
import com.threegroup.tobedated._signUp.composables.getCustomButtonStyle
import com.threegroup.tobedated.shareclasses.checkBirthDate
import com.threegroup.tobedated.shareclasses.checkDay
import com.threegroup.tobedated.shareclasses.checkMonth
import com.threegroup.tobedated.shareclasses.checkYear
import com.threegroup.tobedated.shareclasses.composables.DialogWithImage
import com.threegroup.tobedated.shareclasses.composables.GenTextOnlyButton
import com.threegroup.tobedated.shareclasses.composables.GenericBodyText
import com.threegroup.tobedated.shareclasses.composables.GenericLabelText
import com.threegroup.tobedated.shareclasses.composables.GenericTitleText
import com.threegroup.tobedated.shareclasses.composables.PolkaDotCanvas
import com.threegroup.tobedated.shareclasses.composables.RadioButtonGroup
import com.threegroup.tobedated.shareclasses.composables.rememberPickerState
import com.threegroup.tobedated.shareclasses.models.childrenOptions
import com.threegroup.tobedated.shareclasses.models.drinkOptions
import com.threegroup.tobedated.shareclasses.models.educationOptions
import com.threegroup.tobedated.shareclasses.models.ethnicityOptions
import com.threegroup.tobedated.shareclasses.models.familyOptions
import com.threegroup.tobedated.shareclasses.models.genderOptions
import com.threegroup.tobedated.shareclasses.models.intentionsOptions
import com.threegroup.tobedated.shareclasses.models.mbtiQuestion
import com.threegroup.tobedated.shareclasses.models.meetUpOptions
import com.threegroup.tobedated.shareclasses.models.ourTestQuestions
import com.threegroup.tobedated.shareclasses.models.politicsOptions
import com.threegroup.tobedated.shareclasses.models.pronounOptions
import com.threegroup.tobedated.shareclasses.models.relationshipOptions
import com.threegroup.tobedated.shareclasses.models.religionOptions
import com.threegroup.tobedated.shareclasses.models.seekingOptions
import com.threegroup.tobedated.shareclasses.models.sexOptions
import com.threegroup.tobedated.shareclasses.models.sexOrientationOptions
import com.threegroup.tobedated.shareclasses.models.smokeOptions
import com.threegroup.tobedated.shareclasses.models.starOptions
import com.threegroup.tobedated.shareclasses.models.weedOptions
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Calendar

class SignUpActivity : ComponentActivity() {
    private lateinit var location:String
    override fun onCreate(savedInstanceState: Bundle?) {
        val number = intent.getStringExtra("userPhone").toString()
        location = intent.getStringExtra("location").toString()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
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
                goNextScreen(signUpVM.getUser().number)
            }
        }
    }
    private fun goNextScreen(userToken: String?) {
        val intent = Intent(this, DatingActivity::class.java)
        intent.putExtra("token", userToken)
        intent.putExtra("location", location)
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


@Composable
fun welcomeScreen():Boolean {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Spacer(modifier = Modifier.height(55.dp))
        GenericTitleText(style = AppTheme.typography.titleMedium, text = "Welcome to: To Be Dated")
        Spacer(modifier = Modifier.height(55.dp))
        GenericTitleText(style = AppTheme.typography.titleMedium, text = "Be Kind")
            Spacer(modifier = Modifier.height(5.dp))
        GenericBodyText(style = AppTheme.typography.bodyMedium, text = "Treat everyone with respect!")
            Spacer(modifier = Modifier.height(25.dp))
        GenericTitleText(style = AppTheme.typography.titleMedium, text = "Be Safe")
            Spacer(modifier = Modifier.height(5.dp))
        GenericBodyText(style = AppTheme.typography.bodyMedium, text = "Don't jump into things too quick")
        Spacer(modifier = Modifier.height(25.dp))
        GenericTitleText(style = AppTheme.typography.titleMedium, text = "Be Dated")
            Spacer(modifier = Modifier.height(5.dp))
        GenericBodyText(style = AppTheme.typography.bodyMedium, text = "Have fun an be yourself")
    }
    return true
}


@Composable
fun nameScreen(signUpVM:SignUpViewModel):Boolean {
    var name by rememberSaveable { mutableStateOf(signUpVM.getUser().name) }
    SignUpFormat(
        title = "Your Name",
        label = "This is what people will know you ass \nJust your first name is needed",
        enterField = {
            NameQuestion(
                input = name,
                onInputChanged = { input ->  name = input
                    signUpVM.setUser("name", input)},
            )
        },
    )
    return name.isNotEmpty()
}
@Composable
fun birthScreen(signUpVM:SignUpViewModel): Boolean {
    var date by rememberSaveable { mutableStateOf(signUpVM.getUser().birthday) }
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
            signUpVM.setUser("birth", date)
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
fun pronounScreen(signUpVM:SignUpViewModel): Boolean {
    var pronoun by rememberSaveable { mutableStateOf(signUpVM.getUser().pronoun) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().pronoun) }

    SignUpFormat(
        title = "Pronouns",
        label = "How do you go by?",
        enterField = {
            RadioButtonGroup(
                options = pronounOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    pronoun = pronounOptions[selectedOptionIndex]
                    signUpVM.setUser("pronoun", pronoun)
                    signUpVM.setUserIndex("pronoun", newIndex)
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return pronoun.isNotEmpty()
}
@Composable
fun genderScreen(signUpVM:SignUpViewModel):Boolean {
    var gender by rememberSaveable { mutableStateOf(signUpVM.getUser().gender) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().gender) }
    SignUpFormat(
        title = "Your Gender",
        label = "What do you identify as",
        enterField = {
            RadioButtonGroup(
                options = genderOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    gender = genderOptions[selectedOptionIndex]
                    signUpVM.setUser("gender", gender)
                    signUpVM.setUserIndex("gender", newIndex)
                },
                style = getCustomButtonStyle()
            ) },
    )
    return gender.isNotEmpty()
}
@Composable
fun heightScreen(signUpVM:SignUpViewModel):Boolean {
    var height by rememberSaveable { mutableStateOf(signUpVM.getUser().height) }
    val valuesPickerState = rememberPickerState()
    DisposableEffect(height) {
        onDispose {
            signUpVM.setUser("height", height)
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
fun ethnicityScreen(signUpVM: SignUpViewModel): Boolean {
    var ethnicity by rememberSaveable { mutableStateOf(signUpVM.getUser().ethnicity) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().ethnicity) }

    SignUpFormat(
        title = "Ethnicity",
        label = "We're curious about where you are from!",
        enterField = {
            RadioButtonGroup(
                options = ethnicityOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    ethnicity = ethnicityOptions[selectedOptionIndex]
                    signUpVM.setUser("ethnicity", ethnicity)
                    signUpVM.setUserIndex("ethnicity", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return ethnicity.isNotEmpty()
}

@Composable
fun starScreen(signUpVM: SignUpViewModel): Boolean {
    var star by rememberSaveable { mutableStateOf(signUpVM.getUser().star) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().star) }

    SignUpFormat(
        title = "What's your sign?",
        label = "Do the stars say we are in favor?",
        enterField = {
            RadioButtonGroup(
                options = starOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    star = starOptions[selectedOptionIndex]
                    signUpVM.setUser("star", star)
                    signUpVM.setUserIndex("star", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return star.isNotEmpty()
}

@Composable
fun sexOriScreen(signUpVM: SignUpViewModel): Boolean {
    var sexOri by rememberSaveable { mutableStateOf(signUpVM.getUser().sexOrientation) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().sexOrientation) }

    SignUpFormat(
        title = "Sexual Orientation",
        label = "Who do you like?",
        enterField = {
            RadioButtonGroup(
                options = sexOrientationOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    sexOri = sexOrientationOptions[selectedOptionIndex]
                    signUpVM.setUser("sexOrientation", sexOri)
                    signUpVM.setUserIndex("sexOrientation", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return sexOri.isNotEmpty()
}

@Composable
fun searchScreen(signUpVM: SignUpViewModel): Boolean {
    var search by rememberSaveable { mutableStateOf(signUpVM.getUser().seeking) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().seeking) }

    SignUpFormat(
        title = "Searching For?",
        label = "Who are you looking to connect with?",
        enterField = {
            RadioButtonGroup(
                options = seekingOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    search = seekingOptions[selectedOptionIndex]
                    signUpVM.setUser("seeking", search)
                    signUpVM.setUserIndex("seeking", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return search.isNotEmpty()
}

@Composable
fun sexScreen(signUpVM: SignUpViewModel): Boolean {
    var sex by rememberSaveable { mutableStateOf(signUpVM.getUser().sex) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().sex) }

    SignUpFormat(
        title = "Your Sex",
        label = "What search category will you be in?",
        enterField = {
            RadioButtonGroup(
                options = sexOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    sex = sexOptions[selectedOptionIndex]
                    signUpVM.setUser("sex", sex)
                    signUpVM.setUserIndex("sex", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return sex.isNotEmpty()
}

@Composable
fun mbtiScreen(signUpVM: SignUpViewModel, onNavigate: () -> Unit):Boolean{
    var mbti by rememberSaveable { mutableStateOf(signUpVM.getUser().testResultsMbti) }
    var isSkip by rememberSaveable { mutableStateOf(false) }

    val answersList: MutableList<Int> = signUpVM.getMbti().toMutableList()
    var results by remember { mutableIntStateOf(-1)    }


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
    SignUpFormatLong(
        title = "MBTI",
        label = "What does your personality say about you?",
        enterField = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                mbtiQuestion.forEachIndexed { index, quest ->
                    var selectedIndex by remember { mutableIntStateOf(-1) }
                    selectedIndex = if(answersList[index] != -1){answersList[index]} else{ -1}
                    PersonalityTest(
                        //TODO some how get value from this shit, I am thinking don't return the values return the result. so math
                        selectedIndex = selectedIndex,
                        onSelectionChange = { newIndex ->
                            selectedIndex = newIndex
                            // Save the index and the new value in answersList
                            answersList[index] = selectedIndex
                            signUpVM.setMbti(index, selectedIndex)
                            if(!answersList.contains(-1)){
                                results = 0
                                answersList.forEach{ va ->
                                    results += va
                                }
                                //mbti = "Not Taken"
                                mbti = when (results) {
                                    0 -> "INTJ"
                                    1 -> "INTP"
                                    2 -> "ENTJ"
                                    3 -> "ENTP"
                                    4 -> "ENFP"
                                    5 -> "ENFJ"
                                    6 -> "INFP"
                                    7 -> "INFJ"
                                    8 -> "ESFJ"
                                    9 -> "ESTJ"
                                    10 -> "ISFJ"
                                    11 -> "ISTJ"
                                    12 -> "ISTP"
                                    13 -> "ISFP"
                                    14 -> "ESTP"
                                    15 -> "ESFP"
                                    else -> "Not Taken"
                                }
                                signUpVM.setUser("testResultsMbti", mbti)
                            }
                        },
                        question = quest
                    )
                }
            }
        },
    )

    if(isSkip){
        var selectedResult by rememberSaveable { mutableStateOf("Not Taken") }
        MBTIDropDown(
            onConfirmation = { isSkip = false
                //newUser.testResultsMbti = selectedResult
                signUpVM.setUser("testResultsMbti", selectedResult)
                onNavigate()},
            onDismissRequest = { isSkip = false
                //newUser.testResultsMbti = "Not Taken"
                signUpVM.setUser("testResultsMbti", "Not Taken")
                onNavigate()},
            selectedMBTI = selectedResult,
            onMBTISelect = { newResult -> selectedResult = newResult }
        )
    }

    return mbti.isNotEmpty()//TODO
}
@Composable
fun ourTestScreen(signUpVM: SignUpViewModel):Boolean{
    val answersList: MutableList<Int> = signUpVM.getOurTest().toMutableList()
    var results by remember { mutableIntStateOf((signUpVM.getUser().testResultTbd))    }

    SignUpFormatLong(
        title = "Our Test",
        label = "We just want to know you a little more",
        enterField = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .verticalScroll(rememberScrollState())
            ){
                ourTestQuestions.forEachIndexed { index, quest ->
                    var selectedIndex by remember { mutableIntStateOf(-1) }
                    selectedIndex = if(answersList[index] != -1){answersList[index]} else{ -1}
                    PersonalityTest(
                        //TODO some how get value from this shit, I am thinking don't return the values return the result. so math
                        selectedIndex = selectedIndex,
                        onSelectionChange = { newIndex ->
                            selectedIndex = newIndex
                            // Save the index and the new value in answersList
                            answersList[index] = newIndex
                            signUpVM.setOurTest(index, newIndex)
                            if(!answersList.contains(-1)){
                                results = 0
                                answersList.forEach{ va ->
                                    results += va
                                }
                                //newUser.testResultTbd = results
                                signUpVM.setUser("testResultTbd", results)
                            }
                        },
                        question = quest
                    )
                }
            }
        },
    )
    return true //results != -1//TODO
}
@Composable
fun meetUpScreen(signUpVM: SignUpViewModel): Boolean {
    var metUp by rememberSaveable { mutableStateOf(signUpVM.getUser().meetUp) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().meetUp) }

    SignUpFormat(
        title = "How Comfortable Are You Meeting Up?",
        label = "When's the first date?",
        enterField = {
            RadioButtonGroup(
                options = meetUpOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    metUp = meetUpOptions[selectedOptionIndex]
                    signUpVM.setUser("meetUp", metUp)
                    signUpVM.setUserIndex("meetUp", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return metUp.isNotEmpty()
}

@Composable
fun childrenScreen(signUpVM: SignUpViewModel): Boolean {
    var children by rememberSaveable { mutableStateOf(signUpVM.getUser().children) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().children) }

    SignUpFormat(
        title = "Do You Have Children?",
        label = "Do you have someone else we should think about too?",
        enterField = {
            RadioButtonGroup(
                options = childrenOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    children = childrenOptions[selectedOptionIndex]
                    signUpVM.setUser("children", children)
                    signUpVM.setUserIndex("children", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return children.isNotEmpty()
}

@Composable
fun familyScreen(signUpVM: SignUpViewModel): Boolean {
    var family by rememberSaveable { mutableStateOf(signUpVM.getUser().family) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().family) }

    SignUpFormat(
        title = "Do You Want Children?",
        label = "What are your plans for your family?",
        enterField = {
            RadioButtonGroup(
                options = familyOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    family = familyOptions[selectedOptionIndex]
                    signUpVM.setUser("family", family)
                    signUpVM.setUserIndex("family", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return family.isNotEmpty()
}

@Composable
fun educationScreen(signUpVM: SignUpViewModel): Boolean {
    var education by rememberSaveable { mutableStateOf(signUpVM.getUser().education) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().education) }

    SignUpFormat(
        title = "Education Level",
        label = "Your heart is always smarter than your brain",
        enterField = {
            RadioButtonGroup(
                options = educationOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    education = educationOptions[selectedOptionIndex]
                    signUpVM.setUser("education", education)
                    signUpVM.setUserIndex("education", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return education.isNotEmpty()
}

@Composable
fun religiousScreen(signUpVM: SignUpViewModel): Boolean {
    var religious by rememberSaveable { mutableStateOf(signUpVM.getUser().religion) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().religion) }

    SignUpFormat(
        title = "Religion",
        label = "We believe in you, but what do you believe in?",
        enterField = {
            RadioButtonGroup(
                options = religionOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    religious = religionOptions[selectedOptionIndex]
                    signUpVM.setUser("religion", religious)
                    signUpVM.setUserIndex("religion", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return religious.isNotEmpty()
}

@Composable
fun politicsScreen(signUpVM: SignUpViewModel): Boolean {
    var politics by rememberSaveable { mutableStateOf(signUpVM.getUser().politics) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().politics) }

    SignUpFormat(
        title = "Politics",
        label = "We vote for you to find a meaningful connection",
        enterField = {
            RadioButtonGroup(
                options = politicsOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    politics = politicsOptions[selectedOptionIndex]
                    signUpVM.setUser("politics", politics)
                    signUpVM.setUserIndex("politics", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return politics.isNotEmpty()
}

@Composable
fun relationshipScreen(signUpVM: SignUpViewModel): Boolean {
    var relationship by rememberSaveable { mutableStateOf(signUpVM.getUser().relationship) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().relationship) }

    SignUpFormat(
        title = "Relationship Type",
        label = "There is always enough to go around",
        enterField = {
            RadioButtonGroup(
                options = relationshipOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    relationship = relationshipOptions[selectedOptionIndex]
                    signUpVM.setUser("relationship", relationship)
                    signUpVM.setUserIndex("relationship", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return relationship.isNotEmpty()
}

@Composable
fun intentionsScreen(signUpVM: SignUpViewModel): Boolean {
    var intention by rememberSaveable { mutableStateOf(signUpVM.getUser().intentions) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().intentions) }

    SignUpFormat(
        title = "Dating Intentions",
        label = "What are you looking for?",
        enterField = {
            RadioButtonGroup(
                options = intentionsOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    intention = intentionsOptions[selectedOptionIndex]
                    signUpVM.setUser("intentions", intention)
                    signUpVM.setUserIndex("intentions", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return intention.isNotEmpty()
}

@Composable
fun drinkScreen(signUpVM: SignUpViewModel): Boolean {
    var drink by rememberSaveable { mutableStateOf(signUpVM.getUser().drink) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().drink) }

    SignUpFormat(
        title = "Do You Drink?",
        label = "How do you like to party?",
        enterField = {
            RadioButtonGroup(
                options = drinkOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    drink = drinkOptions[selectedOptionIndex]
                    signUpVM.setUser("drink", drink)
                    signUpVM.setUserIndex("drink", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return drink.isNotEmpty()
}

@Composable
fun smokeScreen(signUpVM: SignUpViewModel): Boolean {
    var smoke by rememberSaveable { mutableStateOf(signUpVM.getUser().smoke) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().smoke) }

    SignUpFormat(
        title = "Do You Smoke?",
        label = "How do you like to relax?",
        enterField = {
            RadioButtonGroup(
                options = smokeOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    smoke = smokeOptions[selectedOptionIndex]
                    signUpVM.setUser("smoke", smoke)
                    signUpVM.setUserIndex("smoke", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return smoke.isNotEmpty()
}

@Composable
fun weedScreen(signUpVM: SignUpViewModel): Boolean {
    var weed by rememberSaveable { mutableStateOf(signUpVM.getUser().weed) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(signUpVM.getUserIndex().weed) }

    SignUpFormat(
        title = "Do You Smoke Marijuana?",
        label = "How do you like to relax or party?",
        enterField = {
            RadioButtonGroup(
                options = weedOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex ->
                    selectedOptionIndex = newIndex
                    weed = weedOptions[selectedOptionIndex]
                    signUpVM.setUser("weed", weed)
                    signUpVM.setUserIndex("weed", newIndex)
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return weed.isNotEmpty()
}


@Composable
fun promptQuestionsScreen(nav:NavController, signUpVM: SignUpViewModel):Boolean{
    var promptA1 by rememberSaveable { mutableStateOf(signUpVM.getUser().promptA1) }
    var promptA2 by rememberSaveable { mutableStateOf(signUpVM.getUser().promptA2) }
    var promptA3 by rememberSaveable { mutableStateOf(signUpVM.getUser().promptA3) }
    var isEnable1 by rememberSaveable { mutableStateOf(false) }
    var isEnable2 by rememberSaveable { mutableStateOf(false) }
    var isEnable3 by rememberSaveable { mutableStateOf(false) }
    var isAnswered1 by rememberSaveable { mutableStateOf(false) }
    var isAnswered2 by rememberSaveable { mutableStateOf(false) }
    var isAnswered3 by rememberSaveable { mutableStateOf(false) }
    var question1 by rememberSaveable { mutableStateOf("Question 1") }
    var question2 by rememberSaveable { mutableStateOf("Question 2") }
    var question3 by rememberSaveable { mutableStateOf("Question 3") }
    LaunchedEffect(Unit, question1, question2, question3) {
         if(isEnable1){ question1 = signUpVM.getUser().promptQ1}
         if(isEnable2){ question2 =signUpVM.getUser().promptQ2}
         if(isEnable3){ question3 =signUpVM.getUser().promptQ3}
    }
    SignUpFormat(
        title = "Some Ice breakers!",
        label = "Don't be shy, the ice will melt anyway!",
        enterField = {

            OutlinedButton(
                onClick = {
                    nav.navigate("PromptQuestions/1")
                    isEnable1 = true
                })
            {
                GenericLabelText(text = question1)
            }
            PromptAnswer(
                isEnables = isEnable1,
                input = promptA1,
                onInputChanged = { input  ->  promptA1 = input
                    signUpVM.setUser("promptA1", input)
                    isAnswered1 = promptA1.length <= 200
                },
            )
                Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    nav.navigate("PromptQuestions/2")
                    isEnable2 = true
                })
            {

                GenericLabelText(text = question2)
            }
            PromptAnswer(
                isEnables = isEnable2,
                input = promptA2,
                onInputChanged = { input  ->  promptA2 = input
                    signUpVM.setUser("promptA2", input)
                    isAnswered2 = promptA2.length <= 200
                },
            )
                Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    nav.navigate("PromptQuestions/3")
                    isEnable3 = true
                })
            {
                GenericLabelText(text = question3)
            }
            PromptAnswer(
                isEnables = isEnable3,
                input = promptA3,
                onInputChanged = { input  ->  promptA3 = input
                    signUpVM.setUser("promptA3", input)
                    isAnswered3 = promptA3.length <= 200
                },
            )
        },
    )
    return (isAnswered1 && isAnswered2 && isAnswered3)
}

@Composable
fun bioScreen(signUpVM: SignUpViewModel):Boolean{
    var bio by rememberSaveable { mutableStateOf(signUpVM.getUser().bio) }
    SignUpFormat(
        title = "Bio",
        label = "",
        enterField = {
            BioQuestion(
                input = bio,
                onInputChanged = { input  ->  bio = input
                    signUpVM.setUser("bio", input)
                },
            )
        },
    )
    return (bio.length in 16..499)
}

@Composable
fun photoScreen(signUpVM: SignUpViewModel):Boolean{
    var photo1 by rememberSaveable { mutableStateOf(signUpVM.getUser().image1) }
    var photo2 by rememberSaveable { mutableStateOf(signUpVM.getUser().image2) }
    var photo3 by rememberSaveable { mutableStateOf(signUpVM.getUser().image3) }
    var photo4 by rememberSaveable { mutableStateOf(signUpVM.getUser().image4) }
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
            signUpVM.setUser("image1", photo1) } })
    val galleryLauncher2 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri2 = it
            photo2 = it.toString()
            signUpVM.setUser("image2", photo2) } })
    val galleryLauncher3 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri3 = it
            photo3 = it.toString()
            signUpVM.setUser("image3", photo3) } })
    val galleryLauncher4 = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> uri?.let { imageUri4 = it
            photo4 = it.toString()
            signUpVM.setUser("image4", photo4) } })

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