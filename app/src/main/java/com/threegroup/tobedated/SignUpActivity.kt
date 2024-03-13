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
import com.threegroup.tobedated.callclass.checkBirthDate
import com.threegroup.tobedated.callclass.checkDay
import com.threegroup.tobedated.callclass.checkMonth
import com.threegroup.tobedated.callclass.checkYear
import com.threegroup.tobedated.callclass.getCurrentLocation
import com.threegroup.tobedated.composables.DialogWithImage
import com.threegroup.tobedated.composables.PolkaDotCanvas
import com.threegroup.tobedated.composables.RadioButtonGroup
import com.threegroup.tobedated.composables.SignUp.BioQuestion
import com.threegroup.tobedated.composables.SignUp.BirthdateQuestion
import com.threegroup.tobedated.composables.SignUp.BodyText
import com.threegroup.tobedated.composables.SignUp.HeightQuestion
import com.threegroup.tobedated.composables.SignUp.NameQuestion
import com.threegroup.tobedated.composables.SignUp.PersonalityTest
import com.threegroup.tobedated.composables.SignUp.PhotoQuestion
import com.threegroup.tobedated.composables.SignUp.SignUpFormat
import com.threegroup.tobedated.composables.SignUp.TitleText
import com.threegroup.tobedated.composables.SignUp.getCustomButtonStyle
import com.threegroup.tobedated.composables.SignUpNav
import com.threegroup.tobedated.composables.rememberPickerState
import com.threegroup.tobedated.models.NewUserModel
import com.threegroup.tobedated.models.PreferenceIndexModel
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.ui.theme.AppTheme
import java.util.Calendar

var newUser = NewUserModel()
var newUserIndex = PreferenceIndexModel()
class SignUpActivity : ComponentActivity() {
    private var userLoginInfo: String = "" //This is the users phone number
    override fun onCreate(savedInstanceState: Bundle?) {
        newUser.number = intent.getStringExtra("userPhone").toString()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PolkaDotCanvas()
                SignUpNav(this@SignUpActivity)
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
            name = newUser.name,
            birthday = newUser.birthday,
            pronoun = newUser.pronoun,
            gender = newUser.gender,
            height = newUser.height,
            ethnicity = newUser.ethnicity,
            star = newUser.star,
            sexOrientation = newUser.sexOrientation,
            seeking = newUser.seeking,
            sex = newUser.sex,
            testResultsMbti = newUser.testResultsMbti,
            testResultTbd = newUser.testResultTbd,
            children = newUser.children,
            family = newUser.family,
            education = newUser.education,
            religion = newUser.religion,
            politics = newUser.politics,
            relationship = newUser.relationship,
            intentions = newUser.intentions,
            drink = newUser.drink,
            smoke = newUser.smoke,
            weed = newUser.weed,
            promptQ1 = "not Picked",
            promptA1 = "not answered",
            promptQ2 = "not Picked",
            promptA2 = "not answered",
            promptQ3 = "not Picked",
            promptA3 = "not answered",
            bio = newUser.bio,
            image1 = newUser.image1,
            image2 = newUser.image2,
            image3 = newUser.image3,
            image4 = newUser.image4,
            location = getCurrentLocation(),
            status = "Active",
            number = userLoginInfo,
        )
        //TODO, URI does not store data. we need to take the URI and turn it into an image
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
//    fun uploadImage(imageUri: String, onComplete: (String) -> Unit) {
//
//    }
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
            val opts = listOf("He/Him", "She/Her", "They/Them", "Ze/Zir" ,"Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    pronoun = opts[selectedOptionIndex]
                    newUser.pronoun = opts[selectedOptionIndex]
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
            val opts = listOf("Cis-Gender", "Transgender", "Non-binary")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    gender = opts[selectedOptionIndex]
                    newUser.gender = opts[selectedOptionIndex]
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
            val opts = listOf("Black/African Descent", "East Asian", "Hispanic/Latino",
                "Middle Eastern",  "Native American", "Pacific Islander", "South Asian",  "Southeast Asian",  "White/Caucasian",  "Other",  "Ask me",)
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    ethnicity = opts[selectedOptionIndex]
                    newUser.ethnicity = opts[selectedOptionIndex]
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
            val opts = listOf("Aries", "Taurus", "Gemini",
                "Cancer",  "Leo", "Virgo", "Libra",  "Scorpio",  "Sagittarius",  "Capricorn", "Aquarius", "Pisces", "Ask me",)
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    star = opts[selectedOptionIndex]
                    newUser.star = opts[selectedOptionIndex]
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
            val opts = listOf("Androsexual", "Asexual", "Bisexual", "Demisexuality", "Gynesexual", "HeteroSexual", "Pansexual", "Queer", "Questioning", "Ask Me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    sexOri = opts[selectedOptionIndex]
                    newUser.sexOrientation = opts[selectedOptionIndex]
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
            val opts = listOf("Male", "Female", "Everyone")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    search = opts[selectedOptionIndex]
                    newUser.seeking = opts[selectedOptionIndex]
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
            val opts = listOf("Male", "Female", "Other")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    sex = opts[selectedOptionIndex]
                    newUser.sex = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return sex.isNotEmpty()
}
@Composable
fun mbtiScreen():Boolean{
    var mbti by rememberSaveable { mutableStateOf(newUser.testResultsMbti) }
    val questions = listOf( "MBTI TEST QUESTION 1", "MBTI QUESTION 2", "MBTI 3", "MBTI TEST QUESTION 4", "MBTI QUESTION 5", "MBTI 6")
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
                            newUser.testResultsMbti = "ENTP-T"
                        },
                        question = quest
                    )
                }
            }
        },
    )
    return true//mbti.isNotEmpty()
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
                            our = "our Test Answer"
                            newUser.testResultTbd = "0.241"
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
fun childrenScreen():Boolean{
    var children by rememberSaveable { mutableStateOf(newUser.children) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(newUserIndex.children) }
    SignUpFormat(
        title = "Do you have children?",
        label = "Do you have someone else we should think about too?",
        enterField = {
            val opts = listOf("Don't have", "Have children", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    children = opts[selectedOptionIndex]
                    newUser.children = opts[selectedOptionIndex]
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
            val opts = listOf("Don't want", "Want children", "Open to children", "Not sure yet", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    family = opts[selectedOptionIndex]
                    newUser.family = opts[selectedOptionIndex]
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
            val opts = listOf("High School", "Undergrad", "Postgrad", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    education = opts[selectedOptionIndex]
                    newUser.education = opts[selectedOptionIndex]
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
            val opts = listOf("Agnostic", "Atheist", "Buddhist", "Catholic", "Christian", "Hindu", "Jewish", "Muslim", "Sikh", "Spiritual", "Other", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    religious = opts[selectedOptionIndex]
                    newUser.religion = opts[selectedOptionIndex]
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
            val opts = listOf("Liberal", "Moderate", "Conservative", "Not Political", "Other", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    politics = opts[selectedOptionIndex]
                    newUser.politics = opts[selectedOptionIndex]
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
            val opts = listOf("Monogamy", "Non-monogamy", "Finding it", "Other", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    relationship = opts[selectedOptionIndex]
                    newUser.relationship = opts[selectedOptionIndex]
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
            val opts = listOf("Life Partner", "Long-term", "Long open to short", "Short open to long", "Short-term", "Figuring it out","Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    intention = opts[selectedOptionIndex]
                    newUser.intentions = opts[selectedOptionIndex]
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
            val opts = listOf("Yes", "Sometimes", "No", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    drink = opts[selectedOptionIndex]
                    newUser.drink = opts[selectedOptionIndex]
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
            val opts = listOf("Yes", "Sometimes", "No", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    smoke = opts[selectedOptionIndex]
                    newUser.smoke = opts[selectedOptionIndex]
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
            val opts = listOf("Yes", "Sometimes", "No", "Ask me")
            RadioButtonGroup(
                options = opts,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    weed = opts[selectedOptionIndex]
                    newUser.weed = opts[selectedOptionIndex]
                },
                style = getCustomButtonStyle()
            )
        },
    )
    return weed.isNotEmpty()
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
    DrinkScreen,//
    SmokeScreen,//
    WeedScreen,//
    BioScreen,//
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