class WordRepository(private val wordnikService: WordNikService) {
    suspend fun getWordOfTheDay(date: String): WordOfTheDayResponse? {
        return try {
            val response = wordnikService.getWordOfTheDay(date, "rd8wadogjxvq8mtpbsngll4mv6eokvk29vlx6rnlzkgof0475").execute()
            if (response.isSuccessful) {
                response.body()
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
