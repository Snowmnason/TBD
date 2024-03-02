package com.threegroup.tobedated

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.threegroup.tobedated.callclass.CalculateProf
import com.threegroup.tobedated.composables.BioQuestion
import com.threegroup.tobedated.composables.BirthdateQuestion
import com.threegroup.tobedated.composables.BodyText
import com.threegroup.tobedated.composables.DialogWithImage
import com.threegroup.tobedated.composables.NameQuestion
import com.threegroup.tobedated.composables.PersonalityTest
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.composables.RadioButtonGroup
import com.threegroup.tobedated.composables.SignUpFormat
import com.threegroup.tobedated.composables.SignUpNav
import com.threegroup.tobedated.composables.TitleText
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.ui.theme.AppTheme

class SignUpActivity : ComponentActivity() {
    private val indexArray = Array(5) { -1 }
    private val userInfoArray = Array(14) { "" }
    private var userLoginInfo: String = ""
    private val calcu = CalculateProf()
    override fun onCreate(savedInstanceState: Bundle?) {
        userLoginInfo = intent.getStringExtra("userPhone").toString()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PolkaDotCanvas()
                SignUpNav(this@SignUpActivity, userInfoArray, indexArray)
                //TODO Change Radio Button Colors, hard to read, dunno how yet...maybe add bored
            }
        }
    }

    fun switchBack() {
        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun goNextScreen() {//TODO
//        val Intent = Intent(this, ProfileActivity::class.java)
//        startActivity(Intent)
//        finish()
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
            age = calcu.calcAge(userInfoArray[1].split("/")),
            location = calcu.getCurrentLocation(),
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
        Spacer(modifier = Modifier.height(25.dp))
        TitleText(title = "Be Kind")
        BodyText(label = "Treat everyone with respect!")
        TitleText(title = "Be Safe")
        BodyText(label = "Don't jump into things too quick")
    }
}

@Composable
fun NameScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit) {
    var name by remember { mutableStateOf(userInfo[0]) }
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
                onInputChanged = { input ->  name = input},
            )
        },
    )
}
@Composable
fun BirthScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit) {
    var date by remember { mutableStateOf(userInfo[1]) }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
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
            onMonthChanged = { input -> month = input
                date = "$month/"
            },
            onDayChanged = { input -> day = input
                date = "$day/"},
            onYearChanged = { input -> year = input
                date = year
            }
            //TODO add Text Listeners or whatever
        ) },
    )
}
@Composable
fun PronounScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit) {
    var pronoun by remember { mutableStateOf(userInfo[2]) }
    var selectedOptionIndex by remember { mutableIntStateOf(index[0]) }
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
                    pronoun = opts[selectedOptionIndex]},
                style = AppTheme.typography.titleMedium,
            )
        },
    )
}
@Composable
fun GenderScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit) {
    var gender by remember { mutableStateOf(userInfo[3]) }
    var selectedOptionIndex by remember { mutableIntStateOf(index[1]) }
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
                    gender = opts[selectedOptionIndex]},
                style = AppTheme.typography.titleMedium
            ) },
    )
}
@Composable
fun SexOriScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit) {
    var sexOri by remember { mutableStateOf(userInfo[4]) }
    var selectedOptionIndex by remember { mutableIntStateOf(index[2]) }
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
                    sexOri = opts[selectedOptionIndex]},
                style = AppTheme.typography.titleMedium
            )
        },
    )
}
@Composable
fun SearchScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit) {
    var search by remember { mutableStateOf(userInfo[5]) }
    var selectedOptionIndex by remember { mutableIntStateOf(index[3]) }
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
                    search = opts[selectedOptionIndex]},
                style = AppTheme.typography.titleMedium
            )
        },
    )
}
@Composable
fun SexScreen(userInfo: Array<String>, index:Array<Int>, onAnswerChanged: (String, Int) -> Unit, onIndexChange: (Int, Int) -> Unit) {
    var sex by remember { mutableStateOf(userInfo[6]) }
    var selectedOptionIndex by remember { mutableIntStateOf(index[4]) }
    DisposableEffect(sex, selectedOptionIndex) {
        onDispose {
            onIndexChange(selectedOptionIndex, 4)
            onAnswerChanged(sex, 6)
        }
    }
    SignUpFormat(
        title = "Your sex",
        label = "What search category will you be in?\nYes please", //TODO
        enterField = {
            val opts = listOf("Male", "Female", "Other")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    sex = opts[selectedOptionIndex]},
                style = AppTheme.typography.titleMedium
            )
        },
    )
}
@Composable
fun MbtiScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit) {
    var mbti by remember { mutableStateOf(userInfo[7]) }

    val questions = listOf( "MBTI TEST QUESTION 1", "MBTI QUESTION 2", "MBTI 3")
    DisposableEffect(mbti) {
        onDispose {
            onAnswerChanged(mbti, 7)
        }
    }
    mbti = ""
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
                    var selectedOptionIndex by remember { mutableIntStateOf(-1) } //This is their Answer
                    PersonalityTest(
                        //TODO some how get value from this shit, I am thinking don't return the values return the result. so math
                        selectedIndex = selectedOptionIndex,
                        onSelectionChange = { newIndex ->
                            selectedOptionIndex = newIndex
                            mbti = newIndex.toString()
                        },
                        question = quest
                    )
                }
            }
        },
    )
}
@Composable
fun OurTestScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit) {
    var our by remember { mutableStateOf(userInfo[8]) }

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
                    var selectedOptionIndex by remember { mutableIntStateOf(-1) } //This is their Answer
                    PersonalityTest(
                        //TODO some how get value from this shit, I am thinking don't return the values return the result. so math
                        selectedIndex = selectedOptionIndex,
                        onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                            our = newIndex.toString()},
                        question = quest
                    )
                }
            }
        },
    )
}
@Composable
fun BioScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit) {
    var bio by remember { mutableStateOf(userInfo[9]) }
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
                onInputChanged = { input  ->  bio = input },
            )
        },
    )
}
@Composable
fun PhotoScreen(userInfo: Array<String>, onAnswerChanged: (String, Int) -> Unit) {
//    var photo1 by remember { mutableStateOf(userInfo[10]) }
//    var photo2 by remember { mutableStateOf(userInfo[11]) }
//    var photo3 by remember { mutableStateOf(userInfo[12]) }
//    var photo4 by remember { mutableStateOf(userInfo[13]) }
    var showFinalDialog by remember { mutableStateOf(true) }
    if (showFinalDialog) {
        DialogWithImage(
            onDismissRequest = { showFinalDialog = false },
            imageDescription = "first photo",
            body = "To make everyone look and feel more equal\nWe have a strict first photo policy!\nPlease Select a photo that matches the one above\nThe second third and forth express yourself\nBut we also shy against group photos",
            painter = painterResource(id = R.drawable.firstphoto),
        )
    }
    SignUpFormat(
        title = "Add some photos!",
        label = "Show yourself off!",
        enterField = {  },
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


