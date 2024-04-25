package com.threegroup.tobedated.shareclasses.api.wordoftheday

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WordnikService {
    @GET("word.json/{word}/definitions")
    fun getDefinitions(
        @Path("word") word: String,
        @Query("api_key") apiKey: String
    ): Call<List<Definition>>
}

data class Definition(
    val text: String
    // Add other fields as necessary
)