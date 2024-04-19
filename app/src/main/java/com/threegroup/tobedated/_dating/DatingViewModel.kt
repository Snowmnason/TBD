package com.threegroup.tobedated._dating

import androidx.compose.runtime.collectAsState
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
    var signedInUser: StateFlow<UserModel?> = _signedInUser
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
    ) { //TODO THIS IS THE FUCKING ERROR
        viewModelScope.launch(IO) {
            repository.setUserInfo(userPhoneNumber, location).collect { userInfo ->
                _signedInUser.value = userInfo
            }
        }
    }
}


//    fun getPotentialUserData(callback: () -> Unit) {
//        try {
//            FirebaseDatabase.getInstance().getReference("users")
//                .addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        if (snapshot.exists()) {
//                            val tempList =
//                                ArrayList<UserModel>() // Temporary list to store potential users
//                            for (data in snapshot.children) {
//                                val model = data.getValue(UserModel::class.java)
//                                if (model?.number != FirebaseAuth.getInstance().currentUser?.phoneNumber) {
//                                    model?.let {
//                                        if (!it.seeMe) {/
//                                            tempList.add(it)
//                                        }
//                                    }
//                                }
//                            }
//                            // Sort the list by status (currentTimeMillis)
//                            val sortedList = tempList.sortedByDescending { it.status }
//
//                            // Update the original list with the sorted list
//                            list.clear()
//                            list.addAll(sortedList)
//                        }
//                        // Invoke the callback to indicate that data retrieval is complete
//                        callback()
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        Log.d("MY_DEBUGGER", "ERROR")
//                        // Handle error
//                    }
//                })
//        } catch (e: Exception) {
//            Log.d("MY_DEBUGGER", "ERROR")
//            // Handle error
//        }
//    }

//    fun getPotentialUserData(): Flow<Pair<List<UserModel>, Int>> {
//        return repository.getPotentialUserData()
//    }
//
//    val potentialUserData: StateFlow<Pair<List<UserModel>, Int>> = repository.getPotentialUserData().map { userData ->
//        val userList: List<UserModel> = userData.first
//        val currentIndex: Int = userData.second
//        Pair(userList, currentIndex)
//    }.stateIn(viewModelScope, SharingStarted.Eagerly, Pair(emptyList(),0))
//
//    fun getNextPotential(currentProfileIndex: Int): UserModel? {///MUST RETURN USERMODEL
//        val potentialUsers = potentialUserData.value.first
//        return if (currentProfileIndex < potentialUsers.size) {
//            val nextPotential = potentialUsers[currentProfileIndex]
//            nextPotential
//        } else {
//            null
//        }
//    }