package com.threegroup.tobedated.models

data class UserModel(
    var name : String = "", // Your name
    var birthday : String = "", // use to calculate age later
    var pronoun : String = "", // How do you go by
    var gender : String = "", // identify as
    var height: String = "",//
    var ethnicity: String = "",//
    var star: String = "",//
    var sexOrientation :String = "", // sexual preference
    var seeking : String = "", // looking for
    var sex : String = "", // What search category will you be
    var testResultsMbti : String = "", // MBTI test
    var testResultTbd : String = "", // our test
    var children: String = "",//
    var family: String = "",//
    var education: String = "",//
    var religion: String = "",//
    var politics: String = "",//
    var relationship: String = "",//
    var intentions: String = "",//
    var drink: String = "",//
    var smoke: String = "",//
    var weed: String = "",//
    var promptQ1: String = "not Picked",
    var promptA1: String = "not answered",
    var promptQ2: String = "not Picked",
    var promptA2: String = "not answered",
    var promptQ3: String = "not Picked",
    var promptA3: String = "not answered",
    var bio : String = "", // Tell us about yourself
    var image1 : String = "", // 1st image
    var image2 : String = "", // 2nd image
    var image3 : String = "", // 3rd image
    var image4 : String = "", // 4th image
    var location : String = "", // use GPS location
    var status : String = "", // online/active status could remove
    var number:String = "",//
    var verified:Boolean = false,
    var userPref:UserSearchPreferenceModel = UserSearchPreferenceModel()
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
    var relationshipType: List<String> = listOf("Doesn't Matter"),
    var intentions: List<String> = listOf("Doesn't Matter"),
    var drink: List<String> = listOf("Doesn't Matter"),
    var smoke: List<String> = listOf("Doesn't Matter"),
    var weed: List<String> = listOf("Doesn't Matter"),
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

