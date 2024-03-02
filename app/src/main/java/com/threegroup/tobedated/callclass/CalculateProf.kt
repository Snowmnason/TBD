package com.threegroup.tobedated.callclass

import java.util.Calendar

class CalculateProf {

    fun calcAge(birth: List<String>?): String{
        val month = (birth?.getOrNull(0)?.substring(0, 2))?.toInt()
        val day = birth?.getOrNull(1)?.substring(0, 2)?.toInt()
        val year = birth?.getOrNull(2)?.substring(0, 4)?.toInt()
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val currentMonth = (Calendar.getInstance().get(Calendar.MONTH))+1
        val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)


        val age = year?.minus(currentYear)
        if(year == (currentYear - 18)){
            if (month != null) {
                if (day != null) {
                    if (month > currentMonth || (month == currentMonth && day >= currentDay)) {
                        if (age != null) {
                            //return "calculatedAgeSubtractedOne"
                            return (age - 1).toString()
                        }
                    }
                }
            }
        }
        return age.toString()
        //return "calculatedAge"
    }

    fun getCurrentLocation(): String {
        return "userLocation"
    }


}