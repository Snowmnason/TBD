package com.threegroup.tobedated.composeables.searching

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
import com.threegroup.tobedated.shareclasses.models.childrenList
import com.threegroup.tobedated.shareclasses.models.drinkList
import com.threegroup.tobedated.shareclasses.models.educationList
import com.threegroup.tobedated.shareclasses.models.familyPlansList
import com.threegroup.tobedated.shareclasses.models.genderList
import com.threegroup.tobedated.shareclasses.models.intentionsList
import com.threegroup.tobedated.shareclasses.models.mbtiList
import com.threegroup.tobedated.shareclasses.models.meetUpList
import com.threegroup.tobedated.shareclasses.models.politicalViewsList
import com.threegroup.tobedated.shareclasses.models.relationshipTypeList
import com.threegroup.tobedated.shareclasses.models.religionList
import com.threegroup.tobedated.shareclasses.models.sexualOriList
import com.threegroup.tobedated.shareclasses.models.smokeList
import com.threegroup.tobedated.shareclasses.models.weedList
import com.threegroup.tobedated.shareclasses.models.zodiacList
import com.threegroup.tobedated.theme.AppTheme

@Composable
fun ChangePreferenceScreen(
    nav: NavHostController,
    vmDating: DatingViewModel,
    title: String = "",
    index:Int
) {
    val currentUser = vmDating.getUser()
    //val currPref = currentUser.userPref
    val (opts, userSet) = when (title) {
        "Gender" -> genderList to currentUser.userPref.gender
        "Zodiac Sign" -> zodiacList to currentUser.userPref.zodiac
        "Sexual Orientation" -> sexualOriList to currentUser.userPref.sexualOri
        "Mbti" -> mbtiList to currentUser.userPref.mbti
        "Children" -> childrenList to currentUser.userPref.children
        "Family Plans" -> familyPlansList to currentUser.userPref.familyPlans
        "Education" -> educationList to currentUser.userPref.education
        "Religion" -> religionList to currentUser.userPref.religion
        "Political Views" -> politicalViewsList to currentUser.userPref.politicalViews
        "Relationship Type" -> relationshipTypeList to currentUser.userPref.relationshipType
        "Intentions" -> intentionsList to currentUser.userPref.intentions
        "Drink" -> drinkList to currentUser.userPref.drink
        "Smokes" -> smokeList to currentUser.userPref.smoke
        "Weed" -> weedList to currentUser.userPref.weed
        "Meeting Up" -> meetUpList to currentUser.userPref.meetUp
        else -> listOf("") to listOf("")
    }
    var userPrefList by remember { mutableStateOf(userSet) }

    val checkedItems = remember { mutableStateListOf<String>().apply { addAll(userPrefList) } }
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
                                if (!userPrefList.contains(option)) {
                                    userPrefList = if (option == "Doesn't Matter") {
                                        checkedItems.clear()
                                        checkedItems.add(option)
                                        listOf(option)
                                    } else {
                                        checkedItems.add(option)
                                        checkedItems.remove("Doesn't Matter")
                                        checkedItems.toList()
                                    }
                                } else {
                                    checkedItems.remove(option)
                                    userPrefList = if (checkedItems.isEmpty()) {
                                        checkedItems.add("Doesn't Matter")
                                        listOf("Doesn't Matter")
                                    } else {
                                        checkedItems.toList()
                                    }
                                }
                                val allOptionsSelected =
                                    checkedItems.containsAll(opts.filter { it != "Doesn't Matter" })
                                if (allOptionsSelected) {
                                    checkedItems.clear()
                                    checkedItems.add("Doesn't Matter")
                                    userPrefList = listOf("Doesn't Matter")
                                }
                                // Update currentPreference with checkedItems
                                userPrefList = checkedItems
                            }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GenericTitleText(text = option)
                        Checkbox(
                            checked = userPrefList.contains(option),
                            onCheckedChange = {
                                if (!userPrefList.contains(option)) {
                                    userPrefList = if (option == "Doesn't Matter") {
                                        checkedItems.clear()
                                        checkedItems.add(option)
                                        listOf(option)
                                    } else {
                                        checkedItems.add(option)
                                        checkedItems.remove("Doesn't Matter")
                                        checkedItems.toList()
                                    }
                                } else {
                                    checkedItems.remove(option)
                                    userPrefList = if (checkedItems.isEmpty()) {
                                        checkedItems.add("Doesn't Matter")
                                        listOf("Doesn't Matter")
                                    } else {
                                        checkedItems.toList()
                                    }
                                }
                                val allOptionsSelected =
                                    checkedItems.containsAll(opts.filter { it != "Doesn't Matter" })
                                if (allOptionsSelected) {
                                    checkedItems.clear()
                                    checkedItems.add("Doesn't Matter")
                                    userPrefList = listOf("Doesn't Matter")
                                }
                                // Update currentPreference with checkedItems
                                userPrefList = checkedItems
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
                        "Gender" -> currentUser.userPref.gender = userPrefList.sorted()
                        "Zodiac Sign" -> currentUser.userPref.zodiac = userPrefList.sorted()
                        "Sexual Orientation" -> currentUser.userPref.sexualOri =
                            userPrefList.sorted()

                        "Mbti" -> currentUser.userPref.mbti = userPrefList.sorted()
                        "Children" -> currentUser.userPref.children = userPrefList.sorted()
                        "Family Plans" -> currentUser.userPref.familyPlans = userPrefList.sorted()
                        "Education" -> currentUser.userPref.education = userPrefList.sorted()
                        "Religion" -> currentUser.userPref.religion = userPrefList.sorted()
                        "Political Views" -> currentUser.userPref.politicalViews =
                            userPrefList.sorted()

                        "Relationship Type" -> currentUser.userPref.relationshipType =
                            userPrefList.sorted()

                        "Intentions" -> currentUser.userPref.intentions = userPrefList.sorted()
                        "Drink" -> currentUser.userPref.drink = userPrefList.sorted()
                        "Smokes" -> currentUser.userPref.smoke = userPrefList.sorted()
                        "Weed" -> currentUser.userPref.weed = userPrefList.sorted()
                        "Meeting Up" -> currentUser.userPref.meetUp = userPrefList.sorted()
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
fun ChangePreferenceScreenC(
    nav: NavHostController,
    vmDating: CasualViewModel,
    title: String = "",
    index:Int
) {
    val currentUser = vmDating.getUser()
    //val currPref = currentUser.userPref
    val (opts, userSet) = when (title) {
        "Gender" -> genderList to currentUser.userPref.gender
        "Zodiac Sign" -> zodiacList to currentUser.userPref.zodiac
        "Sexual Orientation" -> sexualOriList to currentUser.userPref.sexualOri
        "Mbti" -> mbtiList to currentUser.userPref.mbti
        "Children" -> childrenList to currentUser.userPref.children
        "Family Plans" -> familyPlansList to currentUser.userPref.familyPlans
        "Education" -> educationList to currentUser.userPref.education
        "Religion" -> religionList to currentUser.userPref.religion
        "Political Views" -> politicalViewsList to currentUser.userPref.politicalViews
        "Relationship Type" -> relationshipTypeList to currentUser.userPref.relationshipType
        "Intentions" -> intentionsList to currentUser.userPref.intentions
        "Drink" -> drinkList to currentUser.userPref.drink
        "Smokes" -> smokeList to currentUser.userPref.smoke
        "Weed" -> weedList to currentUser.userPref.weed
        "Meeting Up" -> meetUpList to currentUser.userPref.meetUp
        else -> listOf("") to listOf("")
    }
    var userPrefList by remember { mutableStateOf(userSet) }

    val checkedItems = remember { mutableStateListOf<String>().apply { addAll(userPrefList) } }
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
                                if (!userPrefList.contains(option)) {
                                    userPrefList = if (option == "Doesn't Matter") {
                                        checkedItems.clear()
                                        checkedItems.add(option)
                                        listOf(option)
                                    } else {
                                        checkedItems.add(option)
                                        checkedItems.remove("Doesn't Matter")
                                        checkedItems.toList()
                                    }
                                } else {
                                    checkedItems.remove(option)
                                    userPrefList = if (checkedItems.isEmpty()) {
                                        checkedItems.add("Doesn't Matter")
                                        listOf("Doesn't Matter")
                                    } else {
                                        checkedItems.toList()
                                    }
                                }
                                val allOptionsSelected =
                                    checkedItems.containsAll(opts.filter { it != "Doesn't Matter" })
                                if (allOptionsSelected) {
                                    checkedItems.clear()
                                    checkedItems.add("Doesn't Matter")
                                    userPrefList = listOf("Doesn't Matter")
                                }
                                // Update currentPreference with checkedItems
                                userPrefList = checkedItems
                            }),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GenericTitleText(text = option)
                        Checkbox(
                            checked = userPrefList.contains(option),
                            onCheckedChange = {
                                if (!userPrefList.contains(option)) {
                                    userPrefList = if (option == "Doesn't Matter") {
                                        checkedItems.clear()
                                        checkedItems.add(option)
                                        listOf(option)
                                    } else {
                                        checkedItems.add(option)
                                        checkedItems.remove("Doesn't Matter")
                                        checkedItems.toList()
                                    }
                                } else {
                                    checkedItems.remove(option)
                                    userPrefList = if (checkedItems.isEmpty()) {
                                        checkedItems.add("Doesn't Matter")
                                        listOf("Doesn't Matter")
                                    } else {
                                        checkedItems.toList()
                                    }
                                }
                                val allOptionsSelected =
                                    checkedItems.containsAll(opts.filter { it != "Doesn't Matter" })
                                if (allOptionsSelected) {
                                    checkedItems.clear()
                                    checkedItems.add("Doesn't Matter")
                                    userPrefList = listOf("Doesn't Matter")
                                }
                                // Update currentPreference with checkedItems
                                userPrefList = checkedItems
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
                        "Gender" -> currentUser.userPref.gender = userPrefList.sorted()
                        "Zodiac Sign" -> currentUser.userPref.zodiac = userPrefList.sorted()
                        "Sexual Orientation" -> currentUser.userPref.sexualOri =
                            userPrefList.sorted()

                        "Mbti" -> currentUser.userPref.mbti = userPrefList.sorted()
                        "Children" -> currentUser.userPref.children = userPrefList.sorted()
                        "Family Plans" -> currentUser.userPref.familyPlans = userPrefList.sorted()
                        "Education" -> currentUser.userPref.education = userPrefList.sorted()
                        "Religion" -> currentUser.userPref.religion = userPrefList.sorted()
                        "Political Views" -> currentUser.userPref.politicalViews =
                            userPrefList.sorted()

                        "Relationship Type" -> currentUser.userPref.relationshipType =
                            userPrefList.sorted()

                        "Intentions" -> currentUser.userPref.intentions = userPrefList.sorted()
                        "Drink" -> currentUser.userPref.drink = userPrefList.sorted()
                        "Smokes" -> currentUser.userPref.smoke = userPrefList.sorted()
                        "Weed" -> currentUser.userPref.weed = userPrefList.sorted()
                        "Meeting Up" -> currentUser.userPref.meetUp = userPrefList.sorted()
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