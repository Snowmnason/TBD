package com.threegroup.tobedated.models

data class UserModel(
    val name : String? = "", // Your name?                          0\\
    val birthday : String? = "", // use to calculate age later      1\\
    val pronoun : String? = "", // How do you go by?                2\\
    val gender : String? = "", // identify as?                      3\\
    val hieght: String? = "",//                                     4\\
    val ethnicity: String? = "",//                                  5\\
    val star: String? = "",//                                       6\\
    val sexOrientation :String? = "", // sexual preference          7\\
    val seeking : String? = "", // looking for?                     8
    val sex : String? = "", // What search category will you be in? 9
    val testResultsMbti : String? = "", // MBTI test                10\\
    val testResultTbd : String? = "", // our test                   11
    val children: String? = "",//                                   12\\
    val family: String? = "",//                                     13\\
    val education: String? = "",//                                  14\\
    val religious: String? = "",//                                  15\\
    val politics: String? = "",//                                   16\\
    val relationship: String? = "",//                               17\\
    val intentions: String? = "",//                                 18\\
    val exerise: String? = "",//                                    19\\
    val drink: String? = "",//                                      20\\
    val smoke: String? = "",//                                      21\\
    val weed: String? = "",//                                       22\\
    val promptQ1: String? = "",//                                   23\\
    val promptA1: String? = "",//                                   24\\
    val promptQ2: String? = "",//                                   25\\
    val promptA2: String? = "",//                                   26\\
    val promptQ3: String? = "",//                                   27\\
    val promptA3: String? = "",//                                   28\\
    val bio : String? = "", // Tell us about yourself               29\\
    val image1 : String? = "", // 1st image                         30\\
    val image2 : String? = "", // 2nd image                         31\\
    val image3 : String? = "", // 3rd image                         32\\
    val image4 : String? = "", // 4th image                         33\\
    val age : String? = "", // need to calculate from birthday      34
    val location : String? = "", // use GPS location                35
    val status : String? = "", // online/active status could remove
    val number:String = ""
)