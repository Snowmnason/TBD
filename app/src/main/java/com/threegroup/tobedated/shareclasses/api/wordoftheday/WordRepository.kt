package com.threegroup.tobedated.shareclasses.api.wordoftheday

class WordRepository(private val wordnikService: WordnikService) {
    suspend fun getDefinitions(word: String): List<Definition>? {
        return try {
            val response = wordnikService.getDefinitions(word, "rd8wadogjxvq8mtpbsngll4mv6eokvk29vlx6rnlzkgof0475").execute()
            if (response.isSuccessful) {
                response.body()
            } else null
        } catch (e: Exception) {
            null
        }
    }
}