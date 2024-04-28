package com.threegroup.tobedated._friends

import androidx.lifecycle.ViewModel
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FriendViewModel(private var repository: Repository) : ViewModel() {
    private val _signedInUser = MutableStateFlow<UserModel?>(null)
    private var signedInUser: StateFlow<UserModel?> = _signedInUser

    fun getUser(): UserModel {
        return signedInUser.value!!
    }
    fun setLoggedInUser() {
        signedInUser = MyApp.signedInUser
    }

}