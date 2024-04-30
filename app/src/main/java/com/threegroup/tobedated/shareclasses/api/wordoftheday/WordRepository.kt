package com.threegroup.tobedated.shareclasses.api.wordoftheday

class WordRepository(private val wordnikService: WordNikService) {
    suspend fun getWordOfTheDay(date: String?): WordOfTheDayResponse? {
        return try {
            wordnikService.getWordOfTheDay(date, "YOUR_API_KEY")
        } catch (e: Exception) {
            null
        }
    }
}

