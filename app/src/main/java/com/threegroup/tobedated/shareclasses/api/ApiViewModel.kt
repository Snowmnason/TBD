package com.threegroup.tobedated.shareclasses.api


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.starOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
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
        try{
            viewModelScope.launch(Dispatchers.IO) {
                val jsonObject = repository.getWord()
                // Parse the JSONObject and extract required values
                wordOfDay = jsonObject?.optString("word", "Word") ?: "Word"
                val definitionsArray = jsonObject?.getJSONArray("definitions")
                partOfSpeech = definitionsArray?.getJSONObject(0)?.getString("partOfSpeech") ?: "part of speech"
                source = definitionsArray?.getJSONObject(0)?.getString("source") ?: "source"
                def = definitionsArray?.getJSONObject(0)?.getString("text") ?: "definition"
            }
        }catch (e: JSONException) {
            // Handle JSONException
            Log.e("fetchWordOfTheDay", "JSONException: ${e.message}")
            // Set default values or handle the error accordingly
            wordOfDay = "Word"
            partOfSpeech = "part of speech"
            source = "source"
            def = "definition"
        } catch (e: Exception) {
            // Handle other exceptions
            Log.e("fetchWordOfTheDay", "Exception: ${e.message}")
        }
    }
    fun getWord(): String {
        wordOfDay.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        return wordOfDay
    }
    fun getPartOfSpeech():String{
        return  partOfSpeech
    }
    fun getSource():String{
        return source
    }
    fun getDef():String{
        return def
    }

    private var description = "description"
    fun fetchHoroscope(sign: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val starSign = if (sign == "Ask me") { starOptions.random() } else { sign.lowercase() }
                val jsonObject = repository.getHoroscope(starSign)
                try {
                    val horoscopeObject = jsonObject?.optJSONObject("data")
                    val horoscopeData = horoscopeObject?.optString("horoscope_data", "")
                    description = horoscopeData ?: "Description"
                } catch (e: JSONException) {
                    Log.e("fetchHoroscope", "JSONException: ${e.message}")
                }
            }
        } catch (e: Exception) {
            Log.e("fetchHoroscope", "Exception: ${e.message}")
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
        return description
    }
private val moods = listOf("Joyful", "Melancholic", "Anxious", "Relaxed", "Excited", "Content", "Bored", "Amused",
    "Nostalgic", "Serene", "Frustrated", "Energetic", "Confused", "Hopeful", "Irritated", "Peaceful",
    "Enthusiastic", "Reflective", "Impatient", "Curious", "Motivated", "Overwhelmed", "Playful",
    "Disappointed", "Inspired", "Restless", "Grateful", "Insecure", "Anticipatory", "Optimistic", "Pensive",
    "Agitated", "Content", "Distracted", "Giddy", "Thoughtful", "Overjoyed", "Gloomy", "Tense", "Blissful",
    "Discouraged", "Relieved", "Wistful", "Uneasy", "Jubilant", "Despondent", "Hesitant", "Cheeky", "Listless",
    "Resilient",
)

    private var poemTitle = "title"
    private var poemAuthor = "author"
    private var poem = "poem"
    fun fetchPoem() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val jsonArray = repository.getPoem()
                // Check if the JSONArray is not null and has at least one element
                if (jsonArray != null && jsonArray.length() > 0) {
                    // Get the first element of the array
                    val jsonObject = jsonArray.getJSONObject(0)
                    // Parse the JSONObject and extract required values
                    poemTitle = jsonObject.optString("title", "Title")
                    poemAuthor = jsonObject.optString("author", "Author")
                    val linesArray = jsonObject.optJSONArray("lines")
                    // Join the lines array into a single string
                    poem = buildString {
                        linesArray?.let { array ->
                            for (i in 0 until array.length()) {
                                append(array.getString(i))
                                if (i < array.length() - 1) {
                                    append("\n") // Add line break except for the last line
                                }
                            }
                        }
                    }
                } else {
                    poemTitle = "Title"
                    poemAuthor = "Author"
                    poem = "Poem"
                }
                }
        }
        catch (e: Exception) {
            Log.e("fetchWordOfTheDay", "Exception: ${e.message}")
        }
    }
    fun getPoemTitle(): String {
        return poemTitle
    }
    fun getPoemAuthor(): String {
        return poemAuthor
    }
    fun getPoem(): String {
        return poem
    }


}