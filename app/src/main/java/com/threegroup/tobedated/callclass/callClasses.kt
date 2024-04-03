package com.threegroup.tobedated.callclass

import java.util.Calendar
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


fun calcAge(birth: List<String>?): String{
    val month = (birth?.getOrNull(0)?.substring(0, 2))?.toInt()
    val day = birth?.getOrNull(1)?.substring(0, 2)?.toInt()
    val year = birth?.getOrNull(2)?.substring(0, 4)?.toInt()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonth = (Calendar.getInstance().get(Calendar.MONTH))+1
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val age = currentYear.minus(year!!)
    if(year == (currentYear - 18)){
        if (month != null) {
            if (day != null) {
                if (month > currentMonth || (month == currentMonth && day >= currentDay)) {
                    return (age - 1).toString()
                }
            }
        }
    }
    return age.toString()
    //return "calculatedAge"
}
fun calcDistance(potential:String, current:String):String{
    val distance: String
    if(potential != "error/" && current != "/"){
        val potentialParts = potential.split("/")
        val currentParts = current.split("/")
        val latitudePotential = potentialParts.first().toDouble()
        val longitudePotential = potentialParts.last().toDouble()
        val latitudeCurrent = currentParts.first().toDouble()
        val longitudeCurrent = currentParts.last().toDouble()
        val earthRadius = 3958.8 // Earth radius in miles

        val latDistance = Math.toRadians(latitudePotential - latitudeCurrent)
        val lonDistance = Math.toRadians(longitudePotential - longitudeCurrent)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(latitudeCurrent)) * cos(Math.toRadians(latitudePotential)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distanceInMiles = earthRadius * c

        distance = "%.0f miles".format(distanceInMiles)

    }else{
        distance = "x miles"
    }
    return distance
}
fun checkDay(month:Int, year:Int) :Int {
    val maxDays = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else -> 31
    }
    return maxDays
}
fun checkYear(year:Int, day:Int, month:Int):Int{
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonth = (Calendar.getInstance().get(Calendar.MONTH))+1
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    if(year == (currentYear - 18)){
        return if (month > currentMonth || (month == currentMonth && day >= currentDay)) {
            currentYear - 18
        }else{
            currentYear - 17
        }
    }
    return currentYear - 18
}
fun checkMonth(day:Int, year:Int):IntArray{
    if(day <= 28){
        return intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    }
    else if((year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))){
        return intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    }
    else if(day <= 30){
        return intArrayOf(1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    }
    else if(day <= 31){
        return intArrayOf(1, 3, 5, 7, 8, 10, 12)
    }
    return intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
}
fun checkBirthDate(year: Int, month: Int, day: Int): Boolean {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonth = (Calendar.getInstance().get(Calendar.MONTH))+1
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    if(year == (currentYear - 18)){
        if (month > currentMonth || (month == currentMonth && day >= currentDay)) {
            return false
        }
    }
    if (year > currentYear - 18 || year < 1950) return false
    val maxDays = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else -> return false
    }
    return day in 1..maxDays
}
fun formatPhone(phone: String, cCode:String): String {
    val digits = phone.filter { it.isDigit() }
    val formatted = buildString {
        if(cCode == "+1"){
            if (digits.isNotEmpty()) {
                append("(")
                append(digits.take(3))
                if (digits.length > 3) {
                    append(") ")
                    append(digits.substring(3, minOf(digits.length, 6)))
                }
                if (digits.length > 6) {
                    append("-")
                    append(digits.substring(6, minOf(digits.length, 10)))
                }
            }
        }else{
            append(digits.take(4))
            if (digits.length > 4) {
                append(" ")
                append(digits.substring(4))
            }
        }
    }
    return formatted
}
fun formatPhoneNumber(code: String, userPhoneNumber: String): String {
    var formattedPhoneNumber = userPhoneNumber.filter { it.isDigit() }
    formattedPhoneNumber = code + formattedPhoneNumber
    return formattedPhoneNumber
}