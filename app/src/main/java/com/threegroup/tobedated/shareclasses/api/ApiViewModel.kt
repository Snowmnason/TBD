package com.threegroup.tobedated.shareclasses.api


import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
    private var _wordOfDay = mutableStateOf("Word")
    private val wordOfDay:State<String> = _wordOfDay
    private var _partOfSpeech = mutableStateOf("part of speech")
    private val partOfSpeech:State<String> = _partOfSpeech
    private var _source = mutableStateOf("Source")
    private val source:State<String> = _source
    private var _def = mutableStateOf("definition")
    private val def:State<String> = _def
    fun fetchWordOfTheDay() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonObject = repository.getWord()
                // Parse the JSONObject and extract required values
                _wordOfDay.value = jsonObject?.optString("word", "Word") ?: "Word"
                val definitionsArray = jsonObject?.getJSONArray("definitions")
                if (definitionsArray != null && definitionsArray.length() > 0) {
                    _partOfSpeech.value = definitionsArray.getJSONObject(0).optString("partOfSpeech", "part of speech")
                    _source.value = definitionsArray.getJSONObject(0).optString("source", "source")
                    _def.value = definitionsArray.getJSONObject(0).optString("text", "definition")
                } else {
                    _partOfSpeech.value = "part of speech"
                    _source.value = "source"
                    _def.value = "definition"
                }
            } catch (e: JSONException) {
                // Handle JSONException
                Log.e("fetchWordOfTheDay", "JSONException: ${e.message}")
                // Set default values or handle the error accordingly
                _wordOfDay.value = "Word"
                _partOfSpeech.value = "part of speech"
                _source.value = "source"
                _def.value = "definition"
            } catch (e: Exception) {
                // Handle other exceptions
                Log.e("fetchWordOfTheDay", "Exception: ${e.message}")
            }
        }
    }
    fun getWord(): String {
        val word = wordOfDay.value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        return word
    }
    fun getPartOfSpeech():String{
        return  partOfSpeech.value
    }
    fun getSource():String{
        return source.value
    }
    fun getDef():String{
        return def.value
    }

    private var _description = mutableStateOf("Description")
    private val description:State<String> = _description
    private var star = "Ask me"


    fun fetchHoroscope(sign: String) {
        star = sign
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val starSign = if (sign == "Ask me") { starOptions.random() } else { sign.lowercase() }
                val jsonObject = repository.getHoroscope(starSign)
                try {
                    val horoscopeObject = jsonObject?.optJSONObject("data")
                    val horoscopeData = horoscopeObject?.optString("horoscope_data", "")
                    _description.value = horoscopeData ?: "Description"
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
    fun getStar():String{
        return star
    }
    fun getTime(): String {
        val starSign = if(star == "Ask me"){ starOptions.random() }else{star.lowercase()}
        val dayOfWeek = GregorianCalendar().get(Calendar.DAY_OF_WEEK_IN_MONTH)
        val time = ((dayOfWeek + starSign.length + 13) % 24)
        return "$time:00"
    }

    fun getLuckyNumber(): String {
        val starSign = if(star == "Ask me"){ starOptions.random() }else{star.lowercase()}
        val dayOfWeek = GregorianCalendar().get(Calendar.DAY_OF_WEEK_IN_MONTH)
        val luckyNumber = ((dayOfWeek + starSign.length * 7) % 100)
        return luckyNumber.toString()
    }

    fun getMood(): String {
        val starSign = if(star == "Ask me"){ starOptions.random() }else{star.lowercase()}
        val dayOfWeek = GregorianCalendar().get(Calendar.DAY_OF_WEEK_IN_MONTH)
        val mood = ((dayOfWeek + starSign.length * 6) % 50)
        return moods[mood]
    }

    fun getDescription(): String {
        return description.value
    }
private val moods = listOf("Joyful", "Melancholic", "Anxious", "Relaxed", "Excited", "Content", "Bored", "Amused",
    "Nostalgic", "Serene", "Frustrated", "Energetic", "Confused", "Hopeful", "Irritated", "Peaceful",
    "Enthusiastic", "Reflective", "Impatient", "Curious", "Motivated", "Overwhelmed", "Playful",
    "Disappointed", "Inspired", "Restless", "Grateful", "Insecure", "Anticipatory", "Optimistic", "Pensive",
    "Agitated", "Content", "Distracted", "Giddy", "Thoughtful", "Overjoyed", "Gloomy", "Tense", "Blissful",
    "Discouraged", "Relieved", "Wistful", "Uneasy", "Jubilant", "Despondent", "Hesitant", "Cheeky", "Listless",
    "Resilient",
)

    private var _poemTitle = mutableStateOf("Title")
     val poemTitle:State<String> = _poemTitle
    private var _poemAuthor = mutableStateOf("Author")
     val poemAuthor:State<String> = _poemAuthor
    private var _poem = mutableStateOf("poem")
    val poem:State<String> = _poem

    fun fetchPoem() {
        viewModelScope.launch {
            try {
                // Collect the flow
                repository.getPoem().collect { jsonArray ->
                    // Check if the JSONArray is not null and has at least one element
                    if (jsonArray.length() > 0) {
                        // Get the first element of the array
                        val jsonObject = jsonArray.getJSONObject(0)
                        // Parse the JSONObject and extract required values
                        _poemTitle.value = jsonObject.optString("title", "Title")
                        _poemAuthor.value = jsonObject.optString("author", "Author")
                        val linesArray = jsonObject.optJSONArray("lines")
                        // Join the lines array into a single string
                        _poem.value = buildString {
                            linesArray?.let { array ->
                                for (i in 0 until array.length()) {
                                    append(array.getString(i))
                                    if (i < array.length() - 1) {
                                        append("\n") // Add line break except for the last line
                                    }
                                }
                            }
                        }
                        //println(_poem)
                    } else {
                        _poemTitle.value = "Title"
                        _poemAuthor.value = "Author"
                        _poem.value = "Poem"
                    }
                }
            } catch (e: Exception) {
                Log.e("fetchWordOfTheDay", "Exception: ${e.message}")
            }
        }
    }
}