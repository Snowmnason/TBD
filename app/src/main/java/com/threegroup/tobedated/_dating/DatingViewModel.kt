package com.threegroup.tobedated._dating

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.UserModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DatingViewModel(private var repository: Repository) : ViewModel() {
    val list = ArrayList<UserModel>()
    private val _signedInUser = MutableStateFlow<UserModel?>(null)
    private var signedInUser: StateFlow<UserModel?> = _signedInUser
    private var selectedUser: UserModel = UserModel() //The chat you open

    val potentialUserData: StateFlow<Pair<List<UserModel>, Int>> = repository.getPotentialUserData()
        .map { (userList, currentIndex) -> userList to currentIndex }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Pair(emptyList(), 0))

    fun getNextPotential(currentProfileIndex: Int): UserModel? {
        val potentialUsers = potentialUserData.value.first
        return potentialUsers.getOrNull(currentProfileIndex)
    }

    //Stuff for liking and passing
    fun likedCurrentPotential(currentProfileIndex: Int, currentPotential: UserModel): UserModel? {

        ///signedInUser.gotliked = currentPotential sample
        //TODO This returns a userModel....
        return getNextPotential(currentProfileIndex)
    }

    fun passedCurrentPotential(currentProfileIndex: Int, currentPotential: UserModel): UserModel? {

        return getNextPotential(currentProfileIndex)
    }

    fun reportedCurrentPotential(
        currentProfileIndex: Int,
        currentPotential: UserModel
    ): UserModel? {

        return getNextPotential(currentProfileIndex)
    }

    //Stuff for setting and getting matches
    fun getMatches(): List<UserModel> {
        return potentialUserData.value.first
    }


    //Stuff for chats
    fun setTalkedUser(userModel: UserModel) {
        selectedUser = userModel
    }

    fun getTalkedUser(): UserModel {
        return selectedUser
    }

    /**
     *  generates a unique chatId made from the UIDs of the sender and receiver
     */
    fun getChatId(senderId: String, receiverId: String): String {
        return if (senderId > receiverId) {
            senderId + receiverId
        } else receiverId + senderId
    }

    //Stuff for signed in user
    fun getUser(): UserModel {
        return signedInUser.value!!
    }

    fun updateUser(updatedUser: UserModel) {
        val userPhoneNumber = updatedUser.number
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber)
        databaseReference.setValue(updatedUser)
        _signedInUser.value = updatedUser
    }

    fun setLoggedInUser(
        userPhoneNumber: String,
        location: String
    ) {
        viewModelScope.launch(IO) {
            repository.setUserInfo(userPhoneNumber, location).collect { userInfo ->
                _signedInUser.value = userInfo
            }
        }
    }
    fun updateStatus(number: String, location:String) {
//        val databaseReference1 = TODO
//            FirebaseDatabase.getInstance().getReference("users").child(number).child("location")
//        databaseReference1.setValue(location)
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(number).child("status")
        databaseReference.setValue(System.currentTimeMillis())
    }
}