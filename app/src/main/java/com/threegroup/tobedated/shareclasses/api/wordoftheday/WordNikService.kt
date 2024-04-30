package com.threegroup.tobedated.shareclasses.api.wordoftheday

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WordNikService {
    @GET("words.json/wordOfTheDay")
    fun getWordOfTheDay(
        @Query("date") date: String?, // Optional: Fetch word of the day for a specific date
        @Query("api_key") apiKey: String
    ): Call<WordOfTheDayResponse>
}

data class WordOfTheDayResponse(
    val word: String,
    @SerializedName("definitions")
    val definitions: List<Definition>
)

data class Definition(
    val text: String,
    @SerializedName("partOfSpeech")
    val partOfSpeech: String
)
