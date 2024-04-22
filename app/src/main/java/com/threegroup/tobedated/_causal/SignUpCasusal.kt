package com.threegroup.tobedated._causal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.threegroup.tobedated._signUp.composables.BioQuestion
import com.threegroup.tobedated._signUp.composables.PromptAnswer
import com.threegroup.tobedated._signUp.composables.SignUpFormat
import com.threegroup.tobedated._signUp.composables.getCustomButtonStyle
import com.threegroup.tobedated.shareclasses.composables.AlertDialogBox
import com.threegroup.tobedated.shareclasses.composables.GenTextOnlyButton
import com.threegroup.tobedated.shareclasses.composables.GenericBodyText
import com.threegroup.tobedated.shareclasses.composables.GenericLabelText
import com.threegroup.tobedated.shareclasses.composables.GenericTitleText
import com.threegroup.tobedated.shareclasses.composables.RadioButtonGroup
import com.threegroup.tobedated.shareclasses.composables.getAddShadow
import com.threegroup.tobedated.shareclasses.models.afterCareOptions
import com.threegroup.tobedated.shareclasses.models.commOptions
import com.threegroup.tobedated.shareclasses.models.experienceOptions
import com.threegroup.tobedated.shareclasses.models.leaningOptions
import com.threegroup.tobedated.shareclasses.models.locationOptions
import com.threegroup.tobedated.shareclasses.models.lookingForOptions
import com.threegroup.tobedated.shareclasses.models.sexHealthOptions
import com.threegroup.tobedated.shareclasses.theme.AppTheme

@Composable
fun welcomeScreenC():Boolean {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        Spacer(modifier = Modifier.height(55.dp))
        GenericTitleText(style = getAddShadow(style = AppTheme.typography.titleMedium, "med"), text = "Welcome to: To Be Casual")
        Spacer(modifier = Modifier.height(55.dp))
        GenericTitleText(style = getAddShadow(style = AppTheme.typography.titleMedium, "med"), text = "Be Kind")
        Spacer(modifier = Modifier.height(5.dp))
        GenericBodyText(style = getAddShadow(style = AppTheme.typography.bodyMedium, "bod"), text = "Treat everyone with respect!")
        Spacer(modifier = Modifier.height(25.dp))
        GenericTitleText(style = getAddShadow(style = AppTheme.typography.titleMedium, "med"), text = "Be Safe")
        Spacer(modifier = Modifier.height(5.dp))
        GenericBodyText(style = getAddShadow(style = AppTheme.typography.bodyMedium, "bod"), text = "Don't jump into things too quick")
        Spacer(modifier = Modifier.height(25.dp))
        GenericTitleText(style = getAddShadow(style = AppTheme.typography.titleMedium, "med"), text = "Be Casual")
        Spacer(modifier = Modifier.height(5.dp))
        GenericBodyText(style = getAddShadow(style = AppTheme.typography.bodyMedium, "bod"), text = "Have fun an be yourself")
    }

    return true
}
@Composable
fun leaningScreen(causalVM: CausalViewModel): Boolean {
    var leaning by rememberSaveable { mutableStateOf(causalVM.getAdditions().leaning) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(causalVM.getUserIndex().leaning) }

    SignUpFormat(
        title = "What Role do you play",
        label = "Peanut Butter always looks for jelly",
        enterField = {
            RadioButtonGroup(
                options = leaningOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    leaning = leaningOptions[selectedOptionIndex]
                    causalVM.setAdditions("leaning", leaning)
                    causalVM.setUserIndex("leaning", newIndex)
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return leaning.isNotEmpty()
}
@Composable
fun lookingForScreen(causalVM: CausalViewModel): Boolean {
    var lookingFor by rememberSaveable { mutableStateOf(causalVM.getAdditions().lookingFor) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(causalVM.getUserIndex().lookingFor) }

    SignUpFormat(
        title = "What are you looking for?",
        label = "Be open with your intentions",
        enterField = {
            RadioButtonGroup(
                options = lookingForOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    lookingFor = lookingForOptions[selectedOptionIndex]
                    causalVM.setAdditions("lookingFor", lookingFor)
                    causalVM.setUserIndex("lookingFor", newIndex)
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return lookingFor.isNotEmpty()
}
@Composable
fun experienceScreen(causalVM: CausalViewModel): Boolean {
    var experience by rememberSaveable { mutableStateOf(causalVM.getAdditions().experience) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(causalVM.getUserIndex().experience) }

    SignUpFormat(
        title = "Your experience",
        label = "How do you go by?",
        enterField = {
            RadioButtonGroup(
                options = experienceOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    experience = experienceOptions[selectedOptionIndex]
                    causalVM.setAdditions("experience", experience)
                    causalVM.setUserIndex("experience", newIndex)
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return experience.isNotEmpty()
}
@Composable
fun locationScreen(causalVM: CausalViewModel): Boolean {
    var location by rememberSaveable { mutableStateOf(causalVM.getAdditions().location) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(causalVM.getUserIndex().location) }

    SignUpFormat(
        title = "Where can you meet?",
        label = "Where should the connection take place",
        enterField = {
            RadioButtonGroup(
                options = locationOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    location = locationOptions[selectedOptionIndex]
                    causalVM.setAdditions("location", location)
                    causalVM.setUserIndex("location", newIndex)
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return location.isNotEmpty()
}
@Composable
fun commScreen(causalVM: CausalViewModel): Boolean {
    var comm by rememberSaveable { mutableStateOf(causalVM.getAdditions().comm) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(causalVM.getUserIndex().comm) }

    SignUpFormat(
        title = "How do you talk?",
        label = "Lets save you the trouble of telling them",
        enterField = {
            RadioButtonGroup(
                options = commOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    comm = commOptions[selectedOptionIndex]
                    causalVM.setAdditions("comm", comm)
                    causalVM.setUserIndex("comm", newIndex)
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return comm.isNotEmpty()
}
@Composable
fun sexHealthScreen(causalVM: CausalViewModel): Boolean {
    var sexHealth by rememberSaveable { mutableStateOf(causalVM.getAdditions().sexHealth) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(causalVM.getUserIndex().sexHealth) }

    SignUpFormat(
        title = "Your Sexual Health",
        label = "Its always best to practice safely",
        enterField = {
            RadioButtonGroup(
                options = sexHealthOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    sexHealth = sexHealthOptions[selectedOptionIndex]
                    causalVM.setAdditions("sexHealth", sexHealth)
                    causalVM.setUserIndex("sexHealth", newIndex)
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return sexHealth.isNotEmpty()
}
@Composable
fun afterCareScreen(causalVM: CausalViewModel): Boolean {
    var afterCare by rememberSaveable { mutableStateOf(causalVM.getAdditions().afterCare) }
    var selectedOptionIndex by rememberSaveable { mutableIntStateOf(causalVM.getUserIndex().afterCare) }

    SignUpFormat(
        title = "Your After Care?",
        label = "We typically chat on TBC",
        enterField = {
            RadioButtonGroup(
                options = afterCareOptions,
                selectedIndex = selectedOptionIndex,
                onSelectionChange = { newIndex -> selectedOptionIndex = newIndex
                    afterCare = afterCareOptions[selectedOptionIndex]
                    causalVM.setAdditions("afterCare", afterCare)
                    causalVM.setUserIndex("afterCare", newIndex)
                },
                style = getCustomButtonStyle(),
            )
        },
    )
    return afterCare.isNotEmpty()
}

@Composable
fun promptQuestionsScreenC(nav: NavController, causalVM: CausalViewModel):Boolean{
    var promptA1 by rememberSaveable { mutableStateOf(causalVM.getAdditions().promptA1) }
    var promptA2 by rememberSaveable { mutableStateOf(causalVM.getAdditions().promptA2) }
    var promptA3 by rememberSaveable { mutableStateOf(causalVM.getAdditions().promptA3) }
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
        if(isEnable1){ question1 = causalVM.getAdditions().promptQ1}
        if(isEnable2){ question2 =causalVM.getAdditions().promptQ2}
        if(isEnable3){ question3 =causalVM.getAdditions().promptQ3}
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
                    causalVM.setAdditions("promptA1", input)
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
                    causalVM.setAdditions("promptA2", input)
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
                    causalVM.setAdditions("promptA3", input)
                    isAnswered3 = promptA3.length <= 200
                },
            )
        },
    )
    return (isAnswered1 && isAnswered2 && isAnswered3)
}

@Composable
fun newBioScreen(causalVM: CausalViewModel, onNavigate: () -> Unit): Boolean{
    var isSkip by rememberSaveable { mutableStateOf(false) }
    var bio by rememberSaveable { mutableStateOf(causalVM.getAdditions().casualBio) }
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
        title = "Bio for your causal profile?",
        label = "",
        enterField = {
            BioQuestion(
                input = bio,
                onInputChanged = { input  ->  bio = input
                    causalVM.setAdditions("bio", input)
                },
            )
        },
    )
    if(isSkip){
        AlertDialogBox(
            onDismissRequest = { isSkip = false },
            onConfirmation = { onNavigate() },
            dialogTitle = "Keep your bio?",
            dialogText = "if you confirm skip you will you TBD bio!"
        )
    }
    return (bio.length in 16..499)
}



enum class CasualSign {
    WelcomeScreenC,
    LeaningScreen,
    LookingForScreen,
    ExperienceScreen,
    LocationScreen,
    SexHealthScreen,
    CommScreen,
    AfterCareScreen,
    NewBioScreen,
    PromptQuestionsScreenC,
}