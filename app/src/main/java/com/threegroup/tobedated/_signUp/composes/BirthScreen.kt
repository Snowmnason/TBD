package com.threegroup.tobedated._signUp.composes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import com.threegroup.tobedated._signUp.SignUpViewModel
import com.threegroup.tobedated._signUp.composables.BirthdateQuestion
import com.threegroup.tobedated._signUp.composables.SignUpFormat
import com.threegroup.tobedated.shareclasses.checkBirthDate
import com.threegroup.tobedated.shareclasses.checkDay
import com.threegroup.tobedated.shareclasses.checkMonth
import com.threegroup.tobedated.shareclasses.checkYear
import java.util.Calendar

@Composable
fun birthScreen(signUpVM: SignUpViewModel): Boolean {
    var date by rememberSaveable { mutableStateOf(signUpVM.getUser().birthday) }
    val dateComponents = date.split("/")
    var month by rememberSaveable { mutableStateOf(dateComponents.getOrNull(0) ?: "") }
    var day by rememberSaveable { mutableStateOf(dateComponents.getOrNull(1) ?: "") }
    var year by rememberSaveable { mutableStateOf(dateComponents.getOrNull(2) ?: "") }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    var monthMax by rememberSaveable {
        mutableStateOf(
            intArrayOf(
                0,
                1,
                2,
                3,
                4,
                5,
                6,
                7,
                8,
                9,
                10,
                11,
                12
            )
        )
    }
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
        enterField = {
            BirthdateQuestion(
                monthValue = month,
                dayValue = day,
                yearValue = year,
                onMonthChanged = { input -> //month = input
                    val inValue = input.toIntOrNull()
                    val checkDay = if (day.isNotEmpty()) {
                        day.toInt()
                    } else 5
                    val checkYear = if (year.isNotEmpty()) {
                        year.toInt()
                    } else 1999
                    monthMax = checkMonth(checkDay, checkYear)
                    month = when {
                        input.length < month.length -> input
                        month.isEmpty() && inValue in 0..1 -> input
                        month.length == 1 && inValue!! in monthMax -> input
                        else -> month
                    }
                    if (month.length == 2) {
                        focusManager.moveFocus(focusDirection = FocusDirection.Next)
                    }
                    if (month.isNotEmpty() && day.isNotEmpty() && year.isNotEmpty()) {
                        date = if (checkBirthDate(year.toInt(), month.toInt(), day.toInt())) {
                            "$month/$day/$year"
                        } else {
                            ""
                        }
                    }
                },
                onDayChanged = { input -> //day = input
                    val inValue = input.toIntOrNull()
                    val checkMonth = if (month.isNotEmpty()) {
                        month.toInt()
                    } else 1
                    val checkYear = if (year.isNotEmpty()) {
                        year.toInt()
                    } else 1999
                    dayMax = checkDay(checkMonth, checkYear)
                    day = when {
                        input.length < day.length -> input
                        day.isEmpty() && inValue in 0..(dayMax / 10) -> input
                        day.length == 1 && inValue in 1..(dayMax) -> input
                        else -> day
                    }
                    if (day.length == 2) {
                        focusManager.moveFocus(focusDirection = FocusDirection.Next)
                    }
                    if (day.isEmpty()) {
                        focusManager.moveFocus(focusDirection = FocusDirection.Previous)
                    }
                    if (month.isNotEmpty() && day.isNotEmpty() && year.isNotEmpty()) {
                        date = if (checkBirthDate(year.toInt(), month.toInt(), day.toInt())) {
                            "$month/$day/$year"
                        } else {
                            ""
                        }
                    }
                },
                onYearChanged = { input -> //year = input
                    val inValue = input.toIntOrNull()
                    val checkMonth = if (month.isNotEmpty()) {
                        month.toInt()
                    } else 1
                    val checkYear = if (year.isNotEmpty()) {
                        year.toInt()
                    } else 1999
                    val checkDay = if (day.isNotEmpty()) {
                        day.toInt()
                    } else 5
                    yearMax = checkYear(checkYear, checkMonth, checkDay)
                    year = when {
                        input.length < year.length -> input
                        year.isEmpty() && inValue in (yearMin / 1000)..(yearMax / 1000) -> input
                        year.length == 1 && inValue in (yearMin / 100)..(yearMax / 100) -> input
                        year.length == 2 && inValue in (yearMin / 10)..(yearMax / 10) -> input
                        year.length == 3 && inValue in (yearMin / 1)..(yearMax / 1) -> input
                        else -> year
                    }
                    if (year.isEmpty()) {
                        focusManager.moveFocus(focusDirection = FocusDirection.Previous)
                    }
                    if (month.isNotEmpty() && day.isNotEmpty() && year.isNotEmpty()) {
                        date = if (checkBirthDate(year.toInt(), month.toInt(), day.toInt())) {
                            "$month/$day/$year"
                        } else {
                            ""
                        }
                    }
                }
            )
        },
    )
    return date.isNotEmpty()
}