package com.threegroup.tobedated.shareclasses.api.wordoftheday

import WordRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class WordViewModel(private val wordRepository: WordRepository) : ViewModel() {
    val wordOfDay = MutableLiveData<String>()
    val partOfSpeech = MutableLiveData<String>()
    val source = MutableLiveData<String>()
    val def = MutableLiveData<String>()

    fun fetchWordOfTheDay() {
        viewModelScope.launch {
            val response = wordRepository.getWordOfTheDay("YOUR_API_KEY")
            if (response != null) {
                wordOfDay.postValue(response.word)
                partOfSpeech.postValue(response.definitions.firstOrNull()?.partOfSpeech)
                source.postValue("WordNik") // Assuming source is static or needs to be derived
                def.postValue(response.definitions.firstOrNull()?.text)
            }
        }
    }
}
