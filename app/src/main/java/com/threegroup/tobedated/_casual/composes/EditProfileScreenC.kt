package com.threegroup.tobedated._casual.composes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated.MainActivity
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated._signUp.composables.BigButton
import com.threegroup.tobedated.composeables.composables.AlertDialogBox
import com.threegroup.tobedated.composeables.composables.AlertDialogBoxWTextField
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.composables.OutLinedButton
import com.threegroup.tobedated.composeables.composables.SimpleBox
import com.threegroup.tobedated.composeables.profiles.ChangeProfileC
import com.threegroup.tobedated.composeables.profiles.EditProfileC

@Composable
fun EditProfileScreenC(navController: NavHostController, main: MainActivity, vmCasual: CasualViewModel, mainNav: NavHostController){
    val currentUser = vmCasual.getUser()
    var showDelete by rememberSaveable { mutableStateOf(false) }
    var showBlock by rememberSaveable { mutableStateOf(false) }
    var seen by remember { mutableStateOf(currentUser.seeMe) }
//currentUser.mbti,
    val userSettings = listOf(
        currentUser.casualAdditions.leaning,
        currentUser.ethnicity,
        currentUser.pronoun,
        currentUser.gender,
        currentUser.sexOrientation,
        currentUser.meetUp,
        currentUser.casualAdditions.lookingFor,
        currentUser.intentions,
        currentUser.casualAdditions.experience,
        currentUser.casualAdditions.location,
        currentUser.casualAdditions.comm,
        currentUser.casualAdditions.sexHealth,
        currentUser.casualAdditions.afterCare,
    )

    val pref = listOf(
        "Leaning",
        "Ethnicity",
        "Pronoun",
        "Gender",
        "Sexual Orientation",
        "Meeting Up",
        "Looking For",
        "Intentions",
        "Experience",
        "Communication",
        "Sex Health",
        "Aftercare"
    )
    val state = rememberScrollState(0)
    Column(
        Modifier
            .verticalScroll(state)
            .fillMaxSize()
            .padding(15.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SimpleBox(
            whatsInsideTheBox = {
                Row(
                    modifier = Modifier
                        .padding(15.dp, 0.dp, 15.dp, 0.dp)
                        .fillMaxWidth()
                        .clickable {
                            seen = !seen
                            currentUser.seeMe = seen
                            vmCasual.updateUser(currentUser)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GenericTitleText(text = "Only be seen by people you like")
                    Checkbox(checked = seen,
                        onCheckedChange = {
                            seen = !seen
                            currentUser.seeMe = seen
                            vmCasual.updateUser(currentUser)
                        })
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
        for (i in pref.indices) {
            EditProfileC(
                title = pref[i],
                navController = navController,
                userSetting = userSettings[i],
                clickable = true,
                index = i
            )
            Spacer(modifier = Modifier.height(14.dp))
        }
        BigButton(
            text = "Log Out",
            onClick = {
                vmCasual.goToLogin(main, mainNav)
            }, isUse = true
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp, 0.dp)
        ) {
            OutLinedButton(
                onClick = { showBlock = true },
                text = "Block Numbers",
                outLineColor = Color.Red
            )
            Spacer(modifier = Modifier.height(8.dp))



            OutLinedButton(
                onClick = { showDelete = true },
                text = "Delete Account",
                outLineColor = Color.Red,
                textColor = Color.Red
            )
            Spacer(modifier = Modifier.height(25.dp))
        }
    }
    if (showDelete) {
        AlertDialogBox(
            dialogTitle = "Are you sure?",
            onDismissRequest = { showDelete = false },
            dialogText = "if you confirm your casual account and everything connected will be deleted",
            onConfirmation = {
                vmCasual.deleteProfile(currentUser.number, main, mainNav)

            }
        )
    }
    if (showBlock) {
        var number by remember { mutableStateOf("") }
        AlertDialogBoxWTextField(
            dialogTitle = "Who do you want to block",
            onDismissRequest = { showBlock = false },
            dialogText = "Blocking someone prevents them seeing, this is not reversible",
            onConfirmation = {
                vmCasual.blockUser(number, currentUser.number)
                showBlock = false
            },
            number = number,
            numberChange = {input -> number = input
//                if (input.length <= 14 && currentUser.number.startsWith("+1")) {
//                    number = formatPhone(input, "+1")
//                }
//                if (input.length <= 11) {
//                    number = formatPhone(input, currentUser.number.substring(0, 3))
//                }
//                MoveCursorCommand(5)
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Phone
            )
        )
    }
}

@Composable
fun ChangeProfileScreenC(navController: NavHostController, title:String, index:Int, vmCasual: CasualViewModel){
    ChangeProfileC(
        navController,
        title = title,
        vmCasual = vmCasual,
        index = index,
    )
}