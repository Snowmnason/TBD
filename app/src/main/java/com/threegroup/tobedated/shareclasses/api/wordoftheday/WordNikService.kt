package com.threegroup.tobedated.shareclasses.api.wordoftheday

import retrofit2.http.GET
import retrofit2.http.Query

interface WordNikService {
    @GET("words.json/wordOfTheDay")
    suspend fun getWordOfTheDay(
        @Query("date") date: String?,
        @Query("api_key") apiKey: String
    ): WordOfTheDayResponse
}

data class WordOfTheDayResponse(
    val word: String,
    val definitions: List<Definition>
)

data class Definition(
    val text: String,
    val partOfSpeech: String
)

