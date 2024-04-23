package com.threegroup.tobedated._dating

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.threegroup.tobedated.shareclasses.MyApp
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.Match
import com.threegroup.tobedated.shareclasses.models.NewMatch
import com.threegroup.tobedated.shareclasses.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatingViewModel(private var repository: Repository) : ViewModel() {
    private lateinit var signedInUser: StateFlow<UserModel?>
    private var selectedUser: UserModel = UserModel() //The chat you open


    /**
     *
     * This is for matches
     */
    val potentialUserData: StateFlow<Pair<List<UserModel>, Int>> = repository.getPotentialUserData()
        .map { (userList, currentIndex) -> userList to currentIndex }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Pair(emptyList(), 0))

    fun getNextPotential(currentProfileIndex: Int): UserModel? {
        val potentialUsers = potentialUserData.value.first
        return potentialUsers.getOrNull(currentProfileIndex)
    }
    private var _likedProfile = mutableStateOf(NewMatch())
    val likedProfile: State<NewMatch> = _likedProfile

    // TODO not 100% sure on this one--wrote it kinda fast
    fun likeCurrentProfile(currentUserId: String, currentProfile: UserModel): NewMatch? {
        viewModelScope.launch(Dispatchers.IO) {
            val deferredResult = async {
                repository.likeOrPass(currentUserId, currentProfile.number, true)?.let { model ->
                    NewMatch( // can use NewMatch to display the match splash screen
                        model.id,
                        currentProfile.number,
                        currentProfile.name,
                        listOf(
                            currentProfile.image1,
                            currentProfile.image2,
                            currentProfile.image3,
                            currentProfile.image4
                        )
                    )
                }
            }
            val queryResult = deferredResult.await()
            withContext(Dispatchers.Main) { // TODO with context might be an issue
                if (queryResult != null) {
                    _likedProfile.value = queryResult
                }
            }
        }
        return likedProfile.value
    }

    fun passCurrentProfile(currentUserId: String, currentProfile: UserModel) {
        viewModelScope.launch(IO) {
            repository.likeOrPass(currentUserId, currentProfile.number, false)
        }
    }

    private var _matchList = mutableStateOf(listOf<Match>())
    val matchList: State<List<Match>> = _matchList
    // call this in the composable as val matchlist by viewModel.matchList.observeAsState()

    fun getMatchesFlow(userId: String) {
        var temp = listOf<Match>()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getMatchesFlow(userId).collect { matches ->
                matches.forEach { match ->
                    temp = listOf(repository.getMatch(match)!!)

                    println(temp)
                }
            }
            _matchList.value = temp
        }
    }
    fun getCurrentUserId(): String {
        return repository.getCurrentUserId()
    }

    /**
     *
     * This is for Chats
     */
    //Stuff for setting and getting matches
    fun getMatches(): List<Match> {
        getMatchesFlow(signedInUser.value!!.number)
        return matchList.value
    }
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

    /**
     *
     * This is for Profiles
     */
    fun getUser(): UserModel {
        return signedInUser.value!!
    }

    fun updateUser(updatedUser: UserModel) {
        val userPhoneNumber = updatedUser.number
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber)
        databaseReference.setValue(updatedUser)
        MyApp._signedInUser.value = updatedUser
    }

    fun setLoggedInUser() {
        signedInUser = MyApp.signedInUser
    }
}