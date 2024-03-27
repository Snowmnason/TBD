package com.threegroup.tobedated

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threegroup.tobedated.models.UserModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DatingViewModel(private var repository: Repository) : ViewModel() {
    fun getUserData(): ArrayList<UserModel> {
        var userList: ArrayList<UserModel> = arrayListOf()
        runBlocking(IO) {
            viewModelScope.launch(IO) {
                userList = repository.getUserData()!!

                for (user in repository.getUserData()!!.toMutableList()) {
                    println(user)
                }
            }
        }
        return userList
    }
    @Composable
    fun getUser() :UserModel{
        return repository.getUser()
    }
}