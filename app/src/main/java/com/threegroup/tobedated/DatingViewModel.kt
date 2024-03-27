package com.threegroup.tobedated

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.UserSearchPreferenceModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class DatingViewModel(private var repository: Repository) : ViewModel() {
    fun getUserData(): ArrayList<UserModel> {
        var userList: ArrayList<UserModel> = arrayListOf()
        viewModelScope.launch(IO) {
//           userList = repository.getUserData()!!
//
//            for (user in repository.getUserData()!!.toMutableList()) {
//                println(user)
//            }
        }
        return userList
    }

    private var miaModel = repository.getUser()
    fun updateUserPreferences(updatedPreferences: UserSearchPreferenceModel) {
        miaModel.userPref = updatedPreferences
    }
    fun getUser(): UserModel {
        return miaModel
    }
}