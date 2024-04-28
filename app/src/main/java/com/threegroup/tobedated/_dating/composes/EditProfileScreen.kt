package com.threegroup.tobedated._dating.composes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._dating.DatingActivity
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.profiles.ChangeProfile
import com.threegroup.tobedated.composeables.profiles.EditProfile
import com.threegroup.tobedated.composeables.profiles.InsideProfileSettings
import com.threegroup.tobedated.composeables.profiles.LogOut
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.composables.OutLinedButton
import com.threegroup.tobedated.composeables.composables.SimpleBox

@Composable
fun EditProfileScreen(
    navController: NavHostController,
    dating: DatingActivity,
    vmDating: DatingViewModel
) {
    val currentUser = vmDating.getUser()
    var seen by remember { mutableStateOf(currentUser.seeMe) }
//currentUser.mbti,
    val userSettings = listOf(currentUser.ethnicity, currentUser.pronoun, currentUser.gender, currentUser.sexOrientation, currentUser.meetUp, currentUser.relationship, currentUser.intentions, currentUser.star,
        currentUser.children, currentUser.family, currentUser.drink, currentUser.smoke, currentUser.weed, currentUser.politics, currentUser.education, currentUser.religion,
    )

    val pref = listOf(
        "Ethnicity", "Pronoun", "Gender", "Sexual Orientation", "Meeting Up", "Relationship Type", "Intentions", "Zodiac Sign",
        "Children", "Family", "Drink", "Smokes", "Weed", "Political Views", "Education", "Religion"//"Mbti",
    )
    InsideProfileSettings(
        nav = navController,
        editProfile = {
            SimpleBox(
                whatsInsideTheBox = {
                    Row(
                        modifier = Modifier
                            .padding(15.dp, 0.dp, 15.dp, 0.dp)
                            .fillMaxWidth()
                            .clickable {
                                seen = !seen
                                currentUser.seeMe = seen
                                vmDating.updateUser(currentUser)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GenericTitleText(text = "Only be seen by people you like")
                        Checkbox(checked = seen,
                            onCheckedChange = {
                                seen = !seen
                                currentUser.seeMe = seen
                                vmDating.updateUser(currentUser)
                            })
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            for (i in pref.indices) {
                EditProfile(
                    title = pref[i],
                    navController = navController,
                    userSetting = userSettings[i],
                    clickable = true,
                    index = i
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
            LogOut(dating)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp, 0.dp)
            ) {
                OutLinedButton(
                    onClick = {/*TODO deactivate account*/ },
                    text = "Deactivate Account",
                    outLineColor = Color.Red
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutLinedButton(
                    onClick = { vmDating.deleteProfile(currentUser.number, dating) },
                    text = "Delete Account",
                    outLineColor = Color.Red,
                    textColor = Color.Red
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    )
}

@Composable
fun ChangeProfileScreen(
    navController: NavHostController,
    title: String,
    index: Int,
    vmDating: DatingViewModel
) {
    ChangeProfile(
        navController,
        title = title,
        vmDating = vmDating,
        index = index,
    )
}