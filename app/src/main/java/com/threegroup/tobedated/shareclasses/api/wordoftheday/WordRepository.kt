package com.threegroup.tobedated.shareclasses.api.wordoftheday

class WordRepository(private val wordnikService: WordNikService) {
    fun getWordOfTheDay(): WordOfTheDayResponse? {
        return try {
            val response = wordnikService.getWordOfTheDay(null, "rd8wadogjxvq8mtpbsngll4mv6eokvk29vlx6rnlzkgof0475").execute()
            if (response.isSuccessful) {
                response.body()
            } else null
        } catch (e: Exception) {
            null
        }
    }
}

