package com.threegroup.tobedated.shareclasses.models

data class MatchedUserModel(
    var name            : String = "",
    var birthday        : String = "",
    var seeMe           : Boolean = false,
    var pronoun         : String = "",
    var gender          : String = "",
    var height          : String = "",
    var ethnicity       : String = "",
    var star            : String = "",
    var sexOrientation  :String = "",
    var sex             : String = "",
    var testResultsMbti : String = "Not Taken",
    var children        : String = "",
    var family          : String = "",
    var education       : String = "",
    var religion        : String = "",
    var politics        : String = "",
    var relationship    : String = "",
    var intentions      : String = "",
    var drink           : String = "",
    var smoke           : String = "",
    var weed            : String = "",
    var meetUp          : String = "",
    var promptQ1        : String = "",
    var promptA1        : String = "",
    var promptQ2        : String = "",
    var promptA2        : String = "",
    var promptQ3        : String = "",
    var promptA3        : String = "",
    var bio             : String = "",
    var location        : String ="",
    var image1          : String = "",
    var image2          : String = "",
    var image3          : String = "",
    var image4          : String = "",
    var number          : String = "",
    var hasThree        : Boolean = false,
    var status          : Long = 0,
    var verified        : Boolean = false,

    var hasThreeCasual  : Boolean = false,
    var hasFriends      : Boolean = false,
    var casualAdditions : CasualAdditions = CasualAdditions(),
    var hasCasual       : Boolean = false,
    var seeking         : String = "",
    var testResultTbd   : Long = 10,
    var userPref        : UserSearchPreferenceModel = UserSearchPreferenceModel(),

//    var blocked           :String = "",
//    var suggestion      :String = "",

)