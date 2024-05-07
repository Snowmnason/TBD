package com.threegroup.tobedated.shareclasses.firebasedatasource

import androidx.core.text.isDigitsOnly
import com.threegroup.tobedated.shareclasses.models.AgeRange
import com.threegroup.tobedated.shareclasses.models.CasualAdditions
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.shareclasses.models.UserModel
import com.threegroup.tobedated.shareclasses.models.UserSearchPreferenceModel

fun setUserProperties(
    user: UserModel,
    userDataMap: Map<*, *>,
    location: String,
): UserModel {
    return user.apply {
        name = userDataMap["name"] as? String ?: ""
        birthday = userDataMap["birthday"] as? String ?: ""
        pronoun = userDataMap["pronoun"] as? String ?: ""
        user.gender = userDataMap["gender"] as? String ?: ""
        user.height = userDataMap["height"] as? String ?: ""
        user.ethnicity = userDataMap["ethnicity"] as? String ?: ""
        user.star = userDataMap["star"] as? String ?: ""
        user.sexOrientation = userDataMap["sexOrientation"] as? String ?: ""
        user.seeking = userDataMap["seeking"] as? String ?: ""
        user.sex = userDataMap["sex"] as? String ?: ""
        user.testResultsMbti = userDataMap["testResultsMbti"] as? String ?: "Not Taken"
        user.testResultTbd = (userDataMap["testResultTbd"] as? Long)?.toInt() ?: 18
        user.children = userDataMap["children"] as? String ?: ""
        user.family = userDataMap["family"] as? String ?: ""
        user.education = userDataMap["education"] as? String ?: ""
        user.religion = userDataMap["religion"] as? String ?: ""
        user.politics = userDataMap["politics"] as? String ?: ""
        user.relationship = userDataMap["relationship"] as? String ?: ""
        user.intentions = userDataMap["intentions"] as? String ?: ""
        user.drink = userDataMap["drink"] as? String ?: ""
        user.smoke = userDataMap["smoke"] as? String ?: ""
        user.weed = userDataMap["weed"] as? String ?: ""
        user.meetUp = userDataMap["meetUp"] as? String ?: ""
        user.promptQ1 = userDataMap["promptQ1"] as? String ?: ""
        user.promptA1 = userDataMap["promptA1"] as? String ?: ""
        user.promptQ2 = userDataMap["promptQ2"] as? String ?: ""
        user.promptA2 = userDataMap["promptA2"] as? String ?: ""
        user.promptQ3 = userDataMap["promptQ3"] as? String ?: ""
        user.promptA3 = userDataMap["promptA3"] as? String ?: ""
        user.bio = userDataMap["bio"] as? String ?: ""
        user.image1 = userDataMap["image1"] as? String ?: ""
        user.image2 = userDataMap["image2"] as? String ?: ""
        user.image3 = userDataMap["image3"] as? String ?: ""
        user.image4 = userDataMap["image4"] as? String ?: ""
        val loc = location.split("/")
        val latitudePotential = loc.first()
        val longitudePotential = loc.last()
        this.location = if (latitudePotential.isDigitsOnly() && longitudePotential.isDigitsOnly()) {
            location
        } else {
            userDataMap["location"] as? String ?: ""
        }
        status = System.currentTimeMillis()
        number = userDataMap["number"] as? String ?: ""
        verified = userDataMap["verified"] as? Boolean ?: false
        seeMe = userDataMap["Seen"] as? Boolean ?: false
        userPref = (userDataMap["userPref"] as? Map<*, *>)?.let { map ->
            getUserSearchPreference(map)
        } ?: UserSearchPreferenceModel()
        hasCasual = userDataMap["hasCasual"] as? Boolean ?: false
        hasFriends = userDataMap["hasFriends"] as? Boolean ?: false
        hasThree = userDataMap["hasThree"] as? Boolean ?: false
        hasThreeCasual = userDataMap["hasThree"] as? Boolean ?: false
        casualAdditions = (userDataMap["casualAdditions"] as? Map<*, *>)?.let { map ->
            getCasualAdditions(map)
        } ?: CasualAdditions()
    }
}

    private fun getUserSearchPreference(map: Map<*, *> ): UserSearchPreferenceModel {
        val ageRangeData = map["ageRange"]
        val ageRange: AgeRange = if (ageRangeData is Map<*, *>) {
            val min = (ageRangeData["min"] as Long).toInt()
            val max = (ageRangeData["max"] as Long).toInt()
            AgeRange(min, max)
        } else {
            AgeRange(18, 35)
        }
        val maxDistanceData = map["maxDistance"]
        val maxDistance: Int = if (maxDistanceData is Int) {
            maxDistanceData
        } else {
            val maxDistanceLong = maxDistanceData as? Long
            maxDistanceLong?.toInt() ?: 25
        }
        return UserSearchPreferenceModel(
        ageRange = ageRange,
        maxDistance = maxDistance,
        gender = (map["gender"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        zodiac = (map["zodiac"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        sexualOri = (map["sexualOri"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        mbti = (map["mbti"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        children = (map["children"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        familyPlans = (map["familyPlans"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        education = (map["education"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        meetUp = (map["meetUp"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        religion = (map["religion"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        politicalViews = (map["politicalViews"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        relationshipType = (map["relationshipType"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        intentions = (map["intentions"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        drink = (map["drink"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        smoke = (map["smoke"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        weed = (map["weed"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
            //Casual addons
        leaning = (map["leaning"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        lookingFor = (map["lookingFor"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        experience = (map["experience"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        location = (map["location"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        comm = (map["comm"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        sexHealth = (map["sexHealth"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        afterCare = (map["afterCare"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
        )
}

private fun getCasualAdditions(map: Map<*, *>): CasualAdditions {
    return CasualAdditions(
        leaning = map["leaning"] as? String ?: "",
        lookingFor = map["lookingFor"] as? String ?: "",
        experience = map["experience"] as? String ?: "",
        location = map["location"] as? String ?: "",
        comm = map["comm"] as? String ?: "",
        sexHealth = map["sexHealth"] as? String ?: "",
        afterCare = map["afterCare"] as? String ?: "",
        promptQ1 = map["promptQ1"] as? String ?: "",
        promptA1 = map["promptA1"] as? String ?: "",
        promptQ2 = map["promptQ2"] as? String ?: "",
        promptA2 = map["promptA2"] as? String ?: "",
        promptQ3 = map["promptQ3"] as? String ?: "",
        promptA3 = map["promptA3"] as? String ?: "",
    )
}

/**
 * Matched User
 */
fun setMatchedProperties(
    user: MatchedUserModel,
    userDataMap: Map<*, *>,
): MatchedUserModel {
    return user.apply {
        name = userDataMap["name"] as? String ?: ""
        birthday = userDataMap["birthday"] as? String ?: ""
        pronoun = userDataMap["pronoun"] as? String ?: ""
        user.gender = userDataMap["gender"] as? String ?: ""
        user.height = userDataMap["height"] as? String ?: ""
        user.ethnicity = userDataMap["ethnicity"] as? String ?: ""
        user.star = userDataMap["star"] as? String ?: ""
        user.sexOrientation = userDataMap["sexOrientation"] as? String ?: ""
        user.sex = userDataMap["sex"] as? String ?: ""
        user.testResultsMbti = userDataMap["testResultsMbti"] as? String ?: "Not Taken"
        user.children = userDataMap["children"] as? String ?: ""
        user.family = userDataMap["family"] as? String ?: ""
        user.education = userDataMap["education"] as? String ?: ""
        user.religion = userDataMap["religion"] as? String ?: ""
        user.politics = userDataMap["politics"] as? String ?: ""
        user.relationship = userDataMap["relationship"] as? String ?: ""
        user.intentions = userDataMap["intentions"] as? String ?: ""
        user.drink = userDataMap["drink"] as? String ?: ""
        user.smoke = userDataMap["smoke"] as? String ?: ""
        user.weed = userDataMap["weed"] as? String ?: ""
        user.meetUp = userDataMap["meetUp"] as? String ?: ""
        user.promptQ1 = userDataMap["promptQ1"] as? String ?: ""
        user.promptA1 = userDataMap["promptA1"] as? String ?: ""
        user.promptQ2 = userDataMap["promptQ2"] as? String ?: ""
        user.promptA2 = userDataMap["promptA2"] as? String ?: ""
        user.promptQ3 = userDataMap["promptQ3"] as? String ?: ""
        user.promptA3 = userDataMap["promptA3"] as? String ?: ""
        user.bio = userDataMap["bio"] as? String ?: ""
        user.image1 = userDataMap["image1"] as? String ?: ""
        user.image2 = userDataMap["image2"] as? String ?: ""
        user.image3 = userDataMap["image3"] as? String ?: ""
        user.image4 = userDataMap["image4"] as? String ?: ""
        user.location = userDataMap["location"] as? String ?: ""
        user.status = userDataMap["status"] as? Long ?: 0
        user.number = userDataMap["number"] as? String ?: ""
        user.hasThree = userDataMap["hasThree"] as? Boolean ?: false
        user.verified = userDataMap["verified"] as? Boolean ?: false
        user.seeMe = userDataMap["Seen"] as? Boolean ?: false

        user.hasCasual = userDataMap["hasCasual"] as? Boolean ?: false
        user.hasThreeCasual = userDataMap["hasThree"] as? Boolean ?: false
        user.casualAdditions = (userDataMap["casualAdditions"] as? Map<*, *>)?.let { map ->
            getCasualAdditions(map)
        } ?: CasualAdditions()
    }
}