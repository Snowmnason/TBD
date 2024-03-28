package com.threegroup.tobedated.models

data class AgeRange(
    var min: Int,
    val max: Int
)

val genderList = listOf("Cis-Gender", "Transgender", "Non-binary", "Doesn't Matter")
val zodiacList = listOf("Aries", "Taurus", "Gemini", "Cancer",  "Leo", "Virgo", "Libra",  "Scorpio",  "Sagittarius",  "Capricorn", "Aquarius", "Pisces", "Doesn't Matter")
val sexualOriList = listOf("Androsexual", "Asexual", "Bisexual", "Demisexuality", "Gynesexual", "HeteroSexual", "Pansexual", "Queer", "Questioning", "Doesn't Matter")
val mbtiList = listOf(  "INTJ", "INTP", "ENTJ", "ENTP", "ENFP", "ENFJ", "INFP", "INFJ", "ESFJ", "ESTJ", "ISFJ", "ISTJ", "ISTP", "ISFP", "ESTP", "ESFP", "Doesn't Matter")
val childrenList = listOf("Don't have", "Have children", "Doesn't Matter")
val familyPlansList = listOf("Don't want", "Want children", "Open to children", "Not sure yet", "Doesn't Matter")
val educationList = listOf("High School", "Undergrad", "Postgrad", "Doesn't Matter")
val religionList = listOf("Agnostic", "Atheist", "Buddhist", "Catholic", "Christian", "Hindu", "Jewish", "Muslim", "Sikh", "Spiritual", "Other", "Doesn't Matter")
val politicalViewsList = listOf("Liberal", "Moderate", "Conservative", "Not Political", "Doesn't Matter")
val relationshipTypeList = listOf("Monogamy", "Non-monogamy", "Finding it", "Doesn't Matter")
val intentionsList = listOf("Life Partner", "Long-term", "Long open to short", "Short open to long", "Short-term", "Doesn't Matter")
val drinkList = listOf("Yes", "Sometimes", "No", "Doesn't Matter")
val smokeList = listOf("Yes", "Sometimes", "No", "Doesn't Matter")
val weedList = listOf("Yes", "Sometimes", "No", "Doesn't Matter")

val pronounOptions = listOf("He/Him", "She/Her", "They/Them", "Ze/Zir" ,"Ask me")
val genderOptions = listOf("Cis-Gender", "Transgender", "Non-binary")
val ethnicityOptions = listOf("Black/African Descent", "East Asian", "Hispanic/Latino", "Middle Eastern",  "Native American", "Pacific Islander", "South Asian",  "Southeast Asian",  "White/Caucasian",  "Other",  "Ask me",)
val starOptions = listOf("Aries", "Taurus", "Gemini", "Cancer",  "Leo", "Virgo", "Libra",  "Scorpio",  "Sagittarius",  "Capricorn", "Aquarius", "Pisces", "Ask me",)
val sexOrientationOptions = listOf("Androsexual", "Asexual", "Bisexual", "Demisexuality", "Gynesexual", "HeteroSexual", "Pansexual", "Queer", "Questioning", "Ask Me")
val seekingOptions = listOf("Male", "Female", "Everyone")
val sexOptions = listOf("Male", "Female", "Other")
val childrenOptions = listOf("Don't have", "Have children", "Ask me")
val familyOptions = listOf("Don't want", "Want children", "Open to children", "Not sure yet", "Ask me")
val educationOptions = listOf("High School", "Undergrad", "Postgrad", "Ask me")
val religionOptions =  listOf("Agnostic", "Atheist", "Buddhist", "Catholic", "Christian", "Hindu", "Jewish", "Muslim", "Sikh", "Spiritual", "Other", "Ask me")
val politicsOptions = listOf("Liberal", "Moderate", "Conservative", "Not Political", "Other", "Ask me")
val relationshipOptions = listOf("Monogamy", "Non-monogamy", "Finding it", "Other", "Ask me")
val intentionsOptions = listOf("Life Partner", "Long-term", "Long open to short", "Short open to long", "Short-term", "Figuring it out","Ask me")
val drinkOptions = listOf("Yes", "Sometimes", "No", "Ask me")
val smokeOptions = listOf("Yes", "Sometimes", "No", "Ask me")
val weedOptions = listOf("Yes", "Sometimes", "No", "Ask me")