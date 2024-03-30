package com.threegroup.tobedated.models

data class AgeRange(
    var min: Int = 0,
    var max: Int = 0
) {
    // Empty constructor required by Firebase
    constructor() : this(0, 0)
}

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


val tabs = listOf("Insights and Reflections", "Passions and Interests", "Curiosities and Imaginations")
val insightsANDReflections = listOf(
    "What's one misconception people often have about you?",
    "If you could go back and give your younger self one piece of advice, what would it be?",
    "What's a skill you've always wanted to develop but haven't had the chance to yet?",
    "Describe a moment that significantly changed your perspective on life.",
    "What's a topic you could talk about for hours without getting bored?",
    "What's the most important lesson you've learned from a past relationship?",
    "If you could rewrite one moment from your past, what would it be and why?,",
    "What's something you're currently struggling with, and how do you cope with it?",
    "Describe a time when you had to step out of your comfort zone and how it impacted you.",
    "What's something you wish more people understood about you?"
)
val passionsANDInterests = listOf("What's something you're deeply passionate about, but not many people know?",
    "Describe a project or hobby you've been meaning to start but haven't yet.",
    "If you could attend any event, past or present, what would it be?",
    "What's a cause or movement you strongly support, and why?",
    "Describe a book, movie, or piece of art that profoundly influenced you.",
    "What's a skill or talent you admire in others and wish you had?",
    "If you could spend a day with someone who inspires you, who would it be and why?",
    "What's a goal you're currently working towards, and what steps are you taking to achieve it?",
    "Describe a recent accomplishment you're proud of, big or small.",
    "If you could make one positive change in the world, what would it be and why?"
)
val curiositiesANDImaginations = listOf(
    "If you could witness any historical event, what would it be and why?",
    "Describe a place you've never been to but have always wanted to visit, and what draws you to it.",
    "What's a skill or hobby you've been curious to try but haven't yet?",
    "If you could live in any era other than the present, which one would it be and why?",
    "Describe a fictional world from a book or movie that you wish you could visit.",
    "If you could master any language overnight, which one would you choose and why?",
    "What's a technology or scientific advancement you're excited about for the future?",
    "If you could have dinner with any fictional character, who would it be and what would you talk about?",
    "Describe a dream you've had that left a lasting impression on you.",
    "If you could have a conversation with your future self, what would you ask?"
)