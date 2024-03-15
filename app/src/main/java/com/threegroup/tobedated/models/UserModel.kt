package com.threegroup.tobedated.models

data class UserModel(
    val name : String = "", // Your name
    val birthday : String = "", // use to calculate age later
    val pronoun : String = "", // How do you go by
    val gender : String = "", // identify as
    val height: String = "",//
    val ethnicity: String = "",//
    val star: String = "",//
    val sexOrientation :String = "", // sexual preference
    var seeking : String = "", // looking for
    val sex : String = "", // What search category will you be
    val testResultsMbti : String = "", // MBTI test
    val testResultTbd : String = "", // our test
    val children: String = "",//
    val family: String = "",//
    val education: String = "",//
    val religion: String = "",//
    val politics: String = "",//
    val relationship: String = "",//
    val intentions: String = "",//
    val drink: String = "",//
    val smoke: String = "",//
    val weed: String = "",//
    val promptQ1: String = "",//
    val promptA1: String = "",//
    val promptQ2: String = "",//
    val promptA2: String = "",//
    val promptQ3: String = "",//
    val promptA3: String = "",//
    val bio : String = "", // Tell us about yourself
    val image1 : String = "", // 1st image
    val image2 : String = "", // 2nd image
    val image3 : String = "", // 3rd image
    val image4 : String = "", // 4th image
    val location : String = "", // use GPS location
    val status : String = "", // online/active status could remove
    val number:String = "",//
    val verified:String = ""//
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

data class UserSearchPreferenceModel(
    var ageRange: AgeRange = AgeRange(18, 45),
    var maxDistance: Int = 25,
    var gender: List<String> = listOf("Doesn't Matter"),
    var zodiac: List<String> = listOf("Doesn't Matter"),
    var sexualOri: List<String> = listOf("Doesn't Matter"),
    var mbti: List<String> = listOf("Doesn't Matter"),
    var children: List<String> = listOf("Doesn't Matter"),
    var familyPlans: List<String> = listOf("Doesn't Matter"),
    var education: List<String> = listOf("Doesn't Matter"),
    var religion: List<String> = listOf("Doesn't Matter"),
    var politicalViews: List<String> = listOf("Doesn't Matter"),
    var intentions: List<String> = listOf("Doesn't Matter"),
    var drink: List<String> = listOf("Doesn't Matter"),
    var smoke: List<String> = listOf("Doesn't Matter"),
    var weed: List<String> = listOf("Doesn't Matter"),
)