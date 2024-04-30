package com.threegroup.tobedated.shareclasses.api


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.starOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DateFormat.getDateInstance
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale


class ApiViewModel(private val repository: Repository) : ViewModel() {
    private var wordOfDay = ""
    private var partOfSpeech = ""
    private var source = ""
    private var def = ""
    fun fetchWordOfTheDay() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelScope.launch(Dispatchers.IO) {
                val jsonObject = repository.getWord()
                // Parse the JSONObject and extract required values
                wordOfDay = jsonObject?.getString("word") ?: ""
                val definitionsArray = jsonObject?.getJSONArray("definitions")
                partOfSpeech = definitionsArray?.getJSONObject(0)?.getString("partOfSpeech") ?: ""
                source = definitionsArray?.getJSONObject(0)?.getString("source") ?: ""
                def = definitionsArray?.getJSONObject(0)?.getString("text") ?: ""
            }
        }
    }
    fun getWord(): String {
        wordOfDay.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        return if (this.wordOfDay != "") this.wordOfDay else "Word"
    }
    fun getPartOfSpeech():String{
        return  if(partOfSpeech != "") partOfSpeech else "part of speech"
    }
    fun getSource():String{
        return if(source != "") source else "source"
    }
    fun getDef():String{
        return if(def != "") def else "definition"
    }

    private var description = ""
    fun fetchHoroscope(sign: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val starSign = if(sign == "Ask me"){ starOptions.random() }else{sign.lowercase()}
            val jsonObject = repository.getHoroscope(starSign)
            // Parse the JSONObject and extract required values
            val horoscopeObject = jsonObject?.optJSONObject("data")
            val horoscopeData = horoscopeObject?.optString("horoscope_data", "")
            description = horoscopeData ?: ""
        }
    }

    fun getDate(): String {
        return getDateInstance().format(GregorianCalendar().time)
    }
    fun getTime(sign: String): String {
        val starSign = if(sign == "Ask me"){ starOptions.random() }else{sign.lowercase()}
        val dayOfWeek = GregorianCalendar().get(Calendar.DAY_OF_WEEK_IN_MONTH)
        val time = ((dayOfWeek + starSign.length + 13) % 24)
        return "$time:00"
    }

    fun getLuckyNumber(sign: String): String {
        val starSign = if(sign == "Ask me"){ starOptions.random() }else{sign.lowercase()}
        val dayOfWeek = GregorianCalendar().get(Calendar.DAY_OF_WEEK_IN_MONTH)
        val luckyNumber = ((dayOfWeek + starSign.length * 7) % 100)
        return luckyNumber.toString()
    }

    fun getMood(sign: String): String {
        val starSign = if(sign == "Ask me"){ starOptions.random() }else{sign.lowercase()}
        val dayOfWeek = GregorianCalendar().get(Calendar.DAY_OF_WEEK_IN_MONTH)
        val mood = ((dayOfWeek + starSign.length * 6) % 50)
        return moods[mood]
    }

    fun getDescription(): String {
        return if (description != "") description else "Description"
    }
private val moods = listOf("Joyful", "Melancholic", "Anxious", "Relaxed", "Excited", "Content", "Bored", "Amused",
    "Nostalgic", "Serene", "Frustrated", "Energetic", "Confused", "Hopeful", "Irritated", "Peaceful",
    "Enthusiastic", "Reflective", "Impatient", "Curious", "Motivated", "Overwhelmed", "Playful",
    "Disappointed", "Inspired", "Restless", "Grateful", "Insecure", "Anticipatory", "Optimistic", "Pensive",
    "Agitated", "Content", "Distracted", "Giddy", "Thoughtful", "Overjoyed", "Gloomy", "Tense", "Blissful",
    "Discouraged", "Relieved", "Wistful", "Uneasy", "Jubilant", "Despondent", "Hesitant", "Cheeky", "Listless",
    "Resilient",
)

}