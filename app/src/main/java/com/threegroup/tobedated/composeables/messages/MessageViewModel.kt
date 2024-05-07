package com.threegroup.tobedated.composeables.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.Match
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class MessageViewModel(private var repository: Repository) : ViewModel() {

    fun getChatData(chatId: String?, inOther:String ="") = chatId?.let {
        repository.getChatData(it, inOther)
    } ?: flow{
        Log.d("CHAT_TAG", "Error: chatId is null")
    }


    fun storeChatData(chatId: String, message: String, activity: String) {
        viewModelScope.launch(IO) {
            when(activity){
                "dating"->repository.storeChatData(chatId, message)
                "casual"->repository.storeChatData(chatId, message, "casual")
            }

        }
    }

    fun getCurrentUserSenderId(): String {
          return  repository.getCurrentUserSenderId()
    }

    private val _match = MutableStateFlow<Match?>(null)
    val match = _match.asStateFlow()

    // TODO figure out where in the code we call this function
    fun setMatch(match: Match) {
        _match.value = match
    }

}