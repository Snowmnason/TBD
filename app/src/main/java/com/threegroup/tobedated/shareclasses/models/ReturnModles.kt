package com.threegroup.tobedated.shareclasses.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.threegroup.tobedated.R
import com.threegroup.tobedated.shareclasses.theme.AppTheme
import com.threegroup.tobedated.shareclasses.theme.JoseFinSans

data class AgeRange(
    var min: Int = 0,
    var max: Int = 0
) {
    // Empty constructor required by Firebase
    constructor() : this(0, 0)
}



val pronounOptions = listOf("He/Him", "She/Her", "They/Them", "Ask me")
val seekingOptions = listOf("Male", "Female", "Everyone")
val sexOptions = listOf("Male", "Female", "Other")
val genderOptions = listOf("Cis-Gender", "Transgender", "Non-binary")
val ethnicityOptions = listOf("Black/African Descent", "East Asian", "Hispanic/Latino", "Middle Eastern", "Native American", "Pacific Islander", "South Asian", "Southeast Asian", "White/Caucasian", "Other", "Ask me",)
val starOptions = listOf("Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces", "Ask me",)
val sexOrientationOptions = listOf("Androsexual", "Asexual", "Bisexual", "Demisexuality", "Gynesexual", "Heterosexual", "Pansexual", "Queer", "Questioning", "Ask Me")
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
val meetUpOptions = listOf("Right Away", "Talk first", "Take it slow", "Ask me")

//Causal options
val leaningOptions = listOf("Dominant", "Submissive", "Switch", "Vanilla", "Ask me")
val lookingForOptions = listOf("ONS", "FWB", "Casual", "Ask me")
val experienceOptions = listOf("Novice", "Intermediate", "Experienced", "Ask me")
//sexOrientationOptions
//ethnicityOptions
//genderOptions
//sexOptions
//seekingOptions
//pronounOptions
//meetUpOptions
//intentionsOptions
val locationOptions = listOf("Host", "Travel", "Both", "Ask me")
val commOptions = listOf("Direct and explicit", "Flirty and playful", "Open and honest", "Ask me")
val sexHealthOptions = listOf("STI-Free", "Regularly Tested", "On Birth Control", "Vasectomy/Tubal Ligation", "Ask me",)
val afterCareOptions = listOf("Cuddling", "Talking", "Snacking", "Quick Departure", "Ask me",)

//Dating prompt questions
val tabs = listOf("Insights and Reflections", "Passions and Interests", "Curiosities and Imaginations")
val insightsANDReflections = listOf(
    "What's one misconception people often have about you?",
    "If you could go back and give your younger self one piece of advice, what would it be?",
    "What's a skill you've always wanted to develop but haven't had the chance to yet?",
    "Describe a moment that significantly changed your perspective on life.",
    "What's a topic you could talk about for hours without getting bored?",
    "What's the most important lesson you've learned from a past relationship?",
    "If you could rewrite one moment from your past, what would it be and why?",
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

//Casual prompt questions
val tabsCasual = listOf("Preferences and Desires", "Limits and Boundaries", "Expectations and Communication")
val preferencesAndDesires = listOf(
    "Do you prefer to take charge and lead in the bedroom, or do you enjoy letting your partner take control?",
    "Are you open to experimenting with new fantasies and kinks, or do you prefer to stick to familiar activities?",
    "How important is physical attraction to you in a sexual encounter?",
    "Do you enjoy engaging in foreplay and building anticipation, or do you prefer to get straight to the main event?",
    "Are you comfortable discussing your sexual boundaries and preferences with your partner beforehand?",
    "Do you prefer a slow and sensual pace during sex, or do you enjoy a more intense and passionate experience?",
    "How important is mutual pleasure and satisfaction to you in a sexual encounter?",
    "Are you interested in exploring different locations or settings for sexual activities, or do you prefer privacy and familiarity?",
    "Do you have any specific fantasies or role-playing scenarios you'd like to explore with a partner?",
    "How important is communication and feedback during sex to ensure both partners' needs are met?",
)
val limitsANDBoundaries = listOf(
"What are your hard limits or non-negotiable boundaries when it comes to sexual activities?",
"Are you comfortable discussing and respecting your partner's boundaries during a sexual encounter?",
"How important is consent and mutual agreement before engaging in any sexual activity?",
"Do you prefer to establish safe words or signals to communicate discomfort or the need to stop during sex?",
"Are you open to discussing your sexual health status and practices with potential partners?",
"How do you handle situations where a partner wants to try something you're not comfortable with?",
"Are you comfortable using protection and practicing safe sex with new partners?",
"Do you have any specific preferences or requirements regarding sexual hygiene and cleanliness?",
"How do you navigate discussions about safer sex practices and STI testing with potential partners?",
"Are you open to discussing past sexual experiences or history with new partners?",
)
val expectationsANDCommunication = listOf(
"How do you prefer to communicate your sexual desires and needs with a partner?",
"Are you comfortable discussing fantasies or desires that may be considered unconventional or taboo?",
"How do you handle conversations about sexual performance and satisfaction with a partner?",
"Are you open to giving and receiving feedback during sex to enhance the experience for both partners?",
"How important is open and honest communication about sexual preferences and expectations before meeting up?",
"Are you comfortable discussing any concerns or anxieties you may have about a sexual encounter with your partner?",
"How do you navigate discussions about sexual history and experiences with potential partners?",
"Are you open to discussing boundaries and expectations regarding the nature of the sexual encounter (e.g., one-time hookup vs. ongoing arrangement)?",
"How do you handle situations where there's a mismatch in sexual desires or expectations between you and your partner?",
"How do you prioritize mutual respect and consideration for your partner's feelings and boundaries during a sexual encounter?",
)


val ourTestQuestions = listOf(
    "I believe communication is key in a relationship.",
    "I prefer to take things slow when getting to know someone new.",
    "I think it's important for partners to have their own separate interests and hobbies.",
    "I value quality time spent together over extravagant gestures or gifts.",
    "I believe in giving my partner space and independence within the relationship.",
    "I feel it's important for partners to have similar long-term goals and values.",
    "I prefer to express affection through actions rather than words.",
    "I enjoy trying new activities and experiences with my partner.",
    "I believe in resolving conflicts through open and respectful communication.",
    "I think it's important for partners to support each other's personal growth and development.",
)
val mbtiQuestion = listOf(
    "When making decisions, do you prefer to rely on logic and analysis, or do you trust your gut feelings and instincts?",
    "In social situations, are you more inclined to engage in lively discussions and debates, or do you prefer to observe and listen?",
    "Do you feel energized by spending time with a large group of people, or do you find yourself needing alone time to recharge?",
    "When approaching tasks, do you prefer to follow a structured plan and stick to a set schedule, or do you enjoy adapting and improvising as you go?",
    "Are you more comfortable making decisions based on established traditions and norms, or do you prefer to explore new ideas and possibilities?",
    "When working on a team project, do you typically take on a leadership role and direct others, or do you prefer to collaborate and share responsibilities?",
    "In unfamiliar situations, do you tend to rely on past experiences and knowledge, or do you enjoy experimenting and discovering new approaches?",
    "When engaging in conversations, do you prefer to focus on practical matters and concrete details, or do you enjoy exploring theoretical concepts and abstract ideas?",
    "Are you more inclined to trust your own judgment and instincts when faced with uncertainty, or do you prefer to seek advice and opinions from others?",
    "When it comes to completing tasks, do you prefer to work methodically and systematically, or do you thrive on spontaneity and flexibility?",
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
val meetUpList = listOf("Right Away", "Talk first", "Take it slow", "Doesn't Matter")

@Composable
fun getStarSymbol(star:String):ImageVector{
    val zodiacToLetterMap = mapOf(
        "Aries" to ImageVector.vectorResource(id = R.drawable.aries), "Taurus" to ImageVector.vectorResource(id = R.drawable.taurus),
        "Capricorn" to ImageVector.vectorResource(id = R.drawable.capricorn), "Aquarius" to ImageVector.vectorResource(id = R.drawable.aquarius),
        "Gemini" to ImageVector.vectorResource(id = R.drawable.gemini), "Pisces" to ImageVector.vectorResource(id = R.drawable.pisces),
        "Cancer" to ImageVector.vectorResource(id = R.drawable.cancer), "Leo" to ImageVector.vectorResource(id = R.drawable.leo),
        "Virgo" to ImageVector.vectorResource(id = R.drawable.virgo), "Libra" to ImageVector.vectorResource(id = R.drawable.libra),
        "Scorpio" to ImageVector.vectorResource(id = R.drawable.scorpio), "Sagittarius" to ImageVector.vectorResource(id = R.drawable.sagittarius),
        "Ask me" to ImageVector.vectorResource(id = R.drawable.other)
    )
    return zodiacToLetterMap[star]!!
}
@Composable
fun getSmallerTextStyle(
    color:Color
): TextStyle {
    return TextStyle(
        fontFamily = JoseFinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp,
        color = color
    )
}
/*
A=Aries         Orange          0xFFf07019
B=Taurus        Brown           0xFF874b2f
C=Capricorn     Yellow          0xFFecab2b
D=Aquarius      Purple          0xFF9a68bf
E=Gemini        Light Green     0xFF6ca169
F=Pisces        Blue            0xFF0e4caf
G=Cancer        Grey            0xFF5c5463
H=Leo           Red             0xFFb9361a
I=Virgo         Dark Green      0xFF345c42
J=Libra         light Blue      0xFF366b8d
K=Scorpio       Blue            0xFF0a434c
L=Sagittarius   Pink            0xFFa0467c

INTJ INTP ENTJ ENTP 834e69
ENFP ENFJ INFP INFJ 617c44
ESFJ ESTJ ISFJ ISTJ 176363
ISTP ISFP ESTP ESFP 71531e

*/
@Composable
fun getMBTIColor(result:String):Color{
    return if (result != "Not Taken") {
        val parts = result.split("-")
        val mainType = parts.first()
        mbtiColors[mbtiColorMap[mainType]!!]
    } else {
        AppTheme.colorScheme.onBackground
    }
}
val starColors = listOf(
    Color(0xFFf07019), Color(0xFF874b2f), Color(0xFFecab2b),
    Color(0xFF9a68bf), Color(0xFF6ca169), Color(0xFF0e4caf),
    Color(0xFF5c5463), Color(0xFFb9361a), Color(0xFF345c42),
    Color(0xFF366b8d), Color(0xFF0a434c), Color(0xFFa0467c), Color.Gray
)
val starColorMap = mapOf(
    "Aries" to 0, "Taurus" to 1, "Capricorn" to 2, "Aquarius" to 3, "Gemini" to 4, "Pisces" to 5,
    "Cancer" to 6, "Leo" to 7, "Virgo" to 8, "Libra" to 9, "Scorpio" to 10, "Sagittarius" to 11, "Ask me" to 12
)
val mbtiColors = listOf(
    Color(0xFF834e69), Color(0xFF617c44), Color(0xFF176363), Color(0xFFD1A00C)
)
val mbtiColorMap = mapOf(
    "INTJ" to 0, "INTP" to 0, "ENTJ" to 0, "ENTP" to 0,
    "ENFP" to 1, "ENFJ" to 1, "INFP" to 1, "INFJ" to 1,
    "ESFJ" to 2, "ESTJ" to 2, "ISFJ" to 2, "ISTJ" to 2,
    "ISTP" to 3, "ISFP" to 3, "ESTP" to 3, "ESFP" to 3
)

