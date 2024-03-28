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

    private var signedInUser = repository.getUser()
    fun updateUserPreferences(updatedPreferences: UserSearchPreferenceModel) {
        signedInUser.userPref = updatedPreferences
    }
    fun updateUser(updatedUser: UserModel) {
        signedInUser = updatedUser
    }
    fun getUser(): UserModel {
        return signedInUser
    }
}