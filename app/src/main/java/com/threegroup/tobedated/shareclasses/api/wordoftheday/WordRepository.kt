package com.threegroup.tobedated.shareclasses.api.wordoftheday

class WordRepository(private val wordnikService: WordNikService) {
    suspend fun getWordOfTheDay(date: String?): WordOfTheDayResponse? {
        return try {
            wordnikService.getWordOfTheDay(date, "rd8wadogjxvq8mtpbsngll4mv6eokvk29vlx6rnlzkgof0475")
        } catch (e: Exception) {
            null
        }
    }
}

