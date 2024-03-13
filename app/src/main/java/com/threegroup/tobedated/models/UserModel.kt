package com.threegroup.tobedated.models

data class UserModel(
    val name : String = "", // Your name                          0\\
    val birthday : String = "", // use to calculate age later      1\\
    val pronoun : String = "", // How do you go by                2\\
    val gender : String = "", // identify as                      3\\
    val height: String = "",//                                     4\\
    val ethnicity: String = "",//                                  5\\
    val star: String = "",//                                       6\\
    val sexOrientation :String = "", // sexual preference          7\\
    val seeking : String = "", // looking for                     8
    val sex : String = "", // What search category will you be in 9
    val testResultsMbti : String = "", // MBTI test                10\\
    val testResultTbd : String = "", // our test                   11
    val children: String = "",//                                   12\\
    val family: String = "",//                                     13\\
    val education: String = "",//                                  14\\
    val religion: String = "",//                                  15\\
    val politics: String = "",//                                   16\\
    val relationship: String = "",//                               17\\
    val intentions: String = "",//                                 18\\
    val drink: String = "",//                                      19\\
    val smoke: String = "",//                                      20\\
    val weed: String = "",//                                       21\\
    val promptQ1: String = "",//                                   22\\
    val promptA1: String = "",//                                   23\\
    val promptQ2: String = "",//                                   24\\
    val promptA2: String = "",//                                   25\\
    val promptQ3: String = "",//                                   26\\
    val promptA3: String = "",//                                   27\\
    val bio : String = "", // Tell us about yourself               28\\
    val image1 : String = "", // 1st image                         29\\
    val image2 : String = "", // 2nd image                         30\\
    val image3 : String = "", // 3rd image                         31\\
    val image4 : String = "", // 4th image                         32\\
    val location : String = "", // use GPS location                34
    val status : String = "", // online/active status could remove 35
    val number:String = "",//                                       36
    val verified:String = ""//                                      37
)

data class NewUserModel(
    var name: String = "",
    var birthday: String = "",
    var pronoun: String = "",
    var gender: String = "",
    var height: String = "",
    var ethnicity: String = "",
    var star: String = "",
    var sexOrientation: String = "",
    var seeking: String = "",
    var sex: String = "",
    var testResultsMbti: String = "",
    var testResultTbd: String = "",
    var children: String = "",
    var family: String = "",
    var education: String = "",
    var religion: String = "",
    var politics: String = "",
    var relationship: String = "",
    var intentions: String = "",
    var drink: String = "",
    var smoke: String = "",
    var weed: String = "",
    var promptQ1: String = "",
    var promptA1: String = "",
    var promptQ2: String = "",
    var promptA2: String = "",
    var promptQ3: String = "",
    var promptA3: String = "",
    var bio: String = "",
    var image1: String = "",
    var image2: String = "",
    var image3: String = "",
    var image4: String = "",
    var location: String = "",
    var number: String = "",
)

data class PreferenceIndexModel(
    var pronoun : Int = -1,
    var gender : Int = -1,
    var hieght : Int = -1,
    var ethnicity : Int = -1,
    var star: Int = -1,
    var sexOrientation :Int = -1,
    var seeking : Int = -1,
    var sex : Int = -1,
    var children : Int = -1,
    var family : Int = -1,
    var education : Int = -1,
    var religion : Int = -1,
    var politics : Int = -1,
    var relationship : Int = -1,
    var intentions : Int = -1,
    var drink : Int = -1,
    var smoke : Int = -1,
    var weed : Int = -1,
    var promptQ1 : Int = -1,
    var promptA1 : Int = -1,
    var promptQ2 : Int = -1,
    var promptA2 : Int = -1,
    var promptQ3 : Int = -1,
    var promptA3 : Int = -1,
)