package com.threegroup.tobedated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.Match
import com.threegroup.tobedated.shareclasses.models.MessageModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MessageViewModel(private var repository: Repository) : ViewModel() {

    private var _chatDataList = MutableStateFlow<List<MessageModel?>>(mutableListOf())
    val chatDataList = _chatDataList.asStateFlow()

    fun getChatData(chatId: String?) = repository.getChatData(chatId)

    fun storeChatData(chatId: String, message: String) {
        viewModelScope.launch(IO) {
            repository.storeChatData(chatId, message)
        }
    }

    fun displayChats() {
        viewModelScope.launch(IO) {
            repository.displayChats()
        }
    }

    suspend fun getCurrentUserSenderId(): String? {
          return  repository.getCurrentUserSenderId()
    }

    private val _match = MutableStateFlow<Match?>(null)
    val match = _match.asStateFlow()

    // TODO figure out where in the code we call this function
    fun setMatch(match: Match) {
        _match.value = match
    }

}