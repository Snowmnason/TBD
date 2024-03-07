package com.threegroup.tobedated.models

data class UserModel(
    val name : String? = "", // Your name?
    val birthday : String? = "", // use to calculate age later
    val pronoun : String? = "", // How do you go by?
    val gender : String? = "", // identify as?
    val hieght: String? = "",
    val ethnicity: String? = "",
    val star: String? = "",
    val sexOrientation :String? = "", // sexual preference
    val seeking : String? = "", // looking for?
    val sex : String? = "", // What search category will you be in?
    val testResultsMbti : String? = "", // MBTI test
    val testResultTbd : String? = "", // our test
    val bio : String? = "", // Tell us about yourself
    val image1 : String? = "", // 1st image
    val image2 : String? = "", // 2nd image
    val image3 : String? = "", // 3rd image
    val image4 : String? = "", // 4th image
    val age : String? = "", // need to calculate from birthday
    val location : String? = "", // use GPS location
    val status : String? = "", // online/active status could remove
    val number:String = ""
)