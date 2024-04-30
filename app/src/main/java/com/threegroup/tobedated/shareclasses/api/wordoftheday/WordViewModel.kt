package com.threegroup.tobedated.shareclasses.api.wordoftheday


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class WordViewModel(private val wordRepository: WordRepository) : ViewModel() {
    val wordOfDay = MutableLiveData<String>()
    val partOfSpeech = MutableLiveData<String>()
    val source = MutableLiveData<String>()
    val def = MutableLiveData<String>()

    fun fetchWordOfTheDay() {
        viewModelScope.launch {
            wordRepository.getWordOfTheDay("YOUR_DATE").let { response ->
                response?.let {
                    wordOfDay.postValue(it.word)
                    partOfSpeech.postValue(it.definitions.firstOrNull()?.partOfSpeech)
                    source.postValue("WordNik")
                    def.postValue(it.definitions.firstOrNull()?.text)
                }
            }
        }
    }
}