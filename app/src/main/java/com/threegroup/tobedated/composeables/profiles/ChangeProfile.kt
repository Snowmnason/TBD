package com.threegroup.tobedated.composeables.profiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.threegroup.tobedated._casual.CasualViewModel
import com.threegroup.tobedated._dating.DatingViewModel
import com.threegroup.tobedated.composeables.composables.GenericTitleText
import com.threegroup.tobedated.composeables.searching.ChangePreferenceTopBar
import com.threegroup.tobedated.shareclasses.models.afterCareOptions
import com.threegroup.tobedated.shareclasses.models.childrenOptions
import com.threegroup.tobedated.shareclasses.models.commOptions
import com.threegroup.tobedated.shareclasses.models.drinkOptions
import com.threegroup.tobedated.shareclasses.models.educationOptions
import com.threegroup.tobedated.shareclasses.models.ethnicityOptions
import com.threegroup.tobedated.shareclasses.models.experienceOptions
import com.threegroup.tobedated.shareclasses.models.familyOptions
import com.threegroup.tobedated.shareclasses.models.genderOptions
import com.threegroup.tobedated.shareclasses.models.intentionsOptions
import com.threegroup.tobedated.shareclasses.models.leaningOptions
import com.threegroup.tobedated.shareclasses.models.lookingForOptions
import com.threegroup.tobedated.shareclasses.models.meetUpOptions
import com.threegroup.tobedated.shareclasses.models.politicsOptions
import com.threegroup.tobedated.shareclasses.models.pronounOptions
import com.threegroup.tobedated.shareclasses.models.relationshipOptions
import com.threegroup.tobedated.shareclasses.models.religionOptions
import com.threegroup.tobedated.shareclasses.models.sexHealthOptions
import com.threegroup.tobedated.shareclasses.models.sexOrientationOptions
import com.threegroup.tobedated.shareclasses.models.smokeOptions
import com.threegroup.tobedated.shareclasses.models.starOptions
import com.threegroup.tobedated.shareclasses.models.weedOptions
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun ChangeProfile(
    nav: NavHostController,
    vmDating: DatingViewModel,
    title: String = "",
    index:Int
) {
    val currentUser = vmDating.getUser()
    val (opts, userSet) = when (title) {
        "Ethnicity" -> ethnicityOptions to currentUser.ethnicity
        "Pronoun" -> pronounOptions to currentUser.pronoun
        "Gender" -> genderOptions to currentUser.gender
        "Sexual Orientation" -> sexOrientationOptions to currentUser.sexOrientation
        "Meeting Up" -> meetUpOptions to currentUser.meetUp
        "Relationship Type" -> relationshipOptions to currentUser.relationship
        "Intentions" -> intentionsOptions to currentUser.intentions
        "Zodiac Sign" -> starOptions to currentUser.star
        "Children" -> childrenOptions to currentUser.children
        "Family" -> familyOptions to currentUser.family
        "Drink" -> drinkOptions to currentUser.drink
        "Smokes" -> smokeOptions to currentUser.smoke
        "Weed" -> weedOptions to currentUser.weed
        "Political Views" -> politicsOptions to currentUser.politics
        "Education" -> educationOptions to currentUser.education
        "Religion" -> religionOptions to currentUser.religion
        else -> listOf("") to ""
    }
    var userSettings by remember { mutableStateOf(userSet) }
    val checkedItems = remember { mutableStateListOf<String>().apply { add(userSettings) } }
    ChangePreferenceTopBar(
        nav = nav,
        title = title,
        changeSettings = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                opts.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                            .clickable(onClick = {
                                if (!userSettings.contains(option)) {
                                    checkedItems.clear()
                                    checkedItems.add(option)
                                    userSettings = option
                                }
                            }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GenericTitleText(text = option)
                        Checkbox(
                            checked = userSettings.contains(option),
                            onCheckedChange = {
                                if (!userSettings.contains(option)) {
                                    checkedItems.clear()
                                    checkedItems.add(option)
                                    userSettings = option
                                }
                            },
                        )
                    }
                }
            }
        },
        save = {
            Button(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                modifier = Modifier.offset(y = 5.dp),
                onClick = {
                    nav.popBackStack()
                    when (title) {
                        "Ethnicity" -> currentUser.ethnicity = userSettings
                        "Pronoun" -> currentUser.pronoun = userSettings
                        "Gender" -> currentUser.gender = userSettings
                        "Sexual Orientation" -> currentUser.sexOrientation = userSettings
                        "Meeting Up" -> currentUser.meetUp = userSettings
                        "Relationship Type" -> currentUser.relationship = userSettings
                        "Intentions" -> currentUser.intentions = userSettings
                        "Zodiac Sign" -> currentUser.star = userSettings
                        "Children" -> currentUser.children = userSettings
                        "Family" -> currentUser.family = userSettings
                        "Drink" -> currentUser.drink = userSettings
                        "Smokes" -> currentUser.smoke = userSettings
                        "Weed" -> currentUser.weed = userSettings
                        "Political Views" -> currentUser.politics = userSettings
                        "Education" -> currentUser.education = userSettings
                        "Religion" -> currentUser.religion = userSettings
                    }
                    vmDating.updateUser(currentUser)
                    checkedItems.clear()
                }
            ) {
                Text(
                    text = "Confirm",
                    style = AppTheme.typography.titleSmall,
                    color = Color(0xFF93C47D)
                )
            }
        }
    )
}
@Composable
fun ChangeProfileC(
    nav: NavHostController,
    vmCasual: CasualViewModel,
    title: String = "",
    index:Int
) {
    val currentUser = vmCasual.getUser()
    val (opts, userSet) = when (title) {
        "Ethnicity"             -> ethnicityOptions to currentUser.ethnicity
        "Pronoun"               -> pronounOptions to currentUser.pronoun
        "Gender"                -> genderOptions to currentUser.gender
        "Sexual Orientation"    -> sexOrientationOptions to currentUser.sexOrientation
        "Leaning"               -> leaningOptions to currentUser.casualAdditions.leaning
        "Looking For"           -> lookingForOptions to currentUser.casualAdditions.lookingFor
        "Experience"            -> experienceOptions to currentUser.casualAdditions.experience
        "Communication"         -> commOptions to currentUser.casualAdditions.experience
        "Sex Health"            -> sexHealthOptions to currentUser.casualAdditions.sexHealth
        "Aftercare"             -> afterCareOptions to currentUser.casualAdditions.afterCare
        else -> listOf("") to ""
    }

    var userSettings by remember { mutableStateOf(userSet) }
    val checkedItems = remember { mutableStateListOf<String>().apply { add(userSettings) } }
    ChangePreferenceTopBar(
        nav = nav,
        title = title,
        changeSettings = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                opts.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 8.dp)
                            .clickable(onClick = {
                                if (!userSettings.contains(option)) {
                                    checkedItems.clear()
                                    checkedItems.add(option)
                                    userSettings = option
                                }
                            }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GenericTitleText(text = option)
                        Checkbox(
                            checked = userSettings.contains(option),
                            onCheckedChange = {
                                if (!userSettings.contains(option)) {
                                    checkedItems.clear()
                                    checkedItems.add(option)
                                    userSettings = option
                                }
                            },
                        )
                    }
                }
            }
        },
        save = {
            Button(
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Transparent
                ),
                modifier = Modifier.offset(y = 5.dp),
                onClick = {
                    nav.popBackStack()
                    when (title) {
                        "Ethnicity"             -> currentUser.ethnicity = userSettings
                        "Pronoun"               -> currentUser.pronoun = userSettings
                        "Gender"                -> currentUser.gender = userSettings
                        "Sexual Orientation"    -> currentUser.sexOrientation = userSettings
                        "Leaning"               -> currentUser.casualAdditions.leaning = userSettings
                        "Looking For"           -> currentUser.casualAdditions.lookingFor = userSettings
                        "Experience"            -> currentUser.casualAdditions.experience = userSettings
                        "Communication"         -> currentUser.casualAdditions.comm = userSettings
                        "Sex Health"            -> currentUser.casualAdditions.sexHealth = userSettings
                        "Aftercare"             -> currentUser.casualAdditions.afterCare = userSettings
                    }
                    vmCasual.updateUser(currentUser)
                    checkedItems.clear()
                }
            ) {
                Text(
                    text = "Confirm",
                    style = AppTheme.typography.titleSmall,
                    color = Color(0xFF93C47D)
                )
            }
        }
    )
}