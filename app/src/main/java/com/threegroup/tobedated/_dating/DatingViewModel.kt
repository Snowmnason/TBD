package com.threegroup.tobedated._dating

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.Match
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.shareclasses.models.NewMatch
import com.threegroup.tobedated.shareclasses.models.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DatingViewModel(private var repository: Repository) : ViewModel() {
    private lateinit var signedInUser: StateFlow<UserModel?>
    /**
     *
     * This is for matches
     */
    private var _likedProfile = mutableStateOf(NewMatch())
    private val likedProfile: State<NewMatch> = _likedProfile

    private var _potentialUserData = MutableStateFlow(listOf<MatchedUserModel>())
    val potentialUserData = _potentialUserData.asStateFlow()

    fun fetchPotentialUserData() {
        viewModelScope.launch {
            repository.getPotentialUserData().collect { userData ->
                _potentialUserData.value = userData
            }
        }
    }
    fun likeCurrentProfile(currentUserId: String, currentProfile: MatchedUserModel): NewMatch {
        viewModelScope.launch(IO) {
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

    fun passCurrentProfile(currentUserId: String, currentProfile: MatchedUserModel) {
        viewModelScope.launch(IO) {
            repository.likeOrPass(currentUserId, currentProfile.number, false)
        }
    }
    fun suggestCurrentProfile(currentPotential:String, suggestion:String){
        repository.suggest(currentPotential, suggestion)
    }
    fun getSuggestion(currentUser: String, onComplete: (List<String>) -> Unit){
        repository.getSuggestion(currentUser, onComplete)
    }

    private var _matchList = MutableStateFlow(listOf<Match>())
    val matchList = _matchList.asStateFlow()
    // call this in the composable as val matchlist by viewModel.matchList.observeAsState()

    fun getMatchesFlow(userId: String) {
        viewModelScope.launch(IO) {
            repository.getMatchesFlow(userId).collect { matches ->
                val convertedMatches = matches.map {
                        match ->
                    repository.getMatch(match, userId)
                }
                _matchList.value = convertedMatches
            }
        }
    }
    fun getMatchSize():Int{
        return if(matchList.value.isEmpty()){
            0
        }else{
            matchList.value.size
        }
    }
    /**
     * This is for blocking
     */
    fun reportUser(reportedUserId: String, reportingUserId: String) {
        viewModelScope.launch(IO) {
            repository.reportUser(reportedUserId, reportingUserId)
        }
    }

    fun blockUser(blockedUserId: String, blockingUserId: String) {
        viewModelScope.launch(IO) {
            repository.blockUser(blockedUserId, blockingUserId)
        }
    }


    /**
     *
     * This is for Chats
     */
    private var _selectedUser = MutableStateFlow<MatchedUserModel?>(null)
    var selectedUser: StateFlow<MatchedUserModel?> = _selectedUser
    //Stuff for setting and getting matches
    fun setTalkedUser(number: String) {
        viewModelScope.launch(IO) {
            repository.setMatchInfo(number).collect { userInfo ->
                _selectedUser.value = userInfo
            }
        }
    }
    fun getTalkedUser(): MatchedUserModel {
        return selectedUser.value!!
    }
    fun deleteMatch(matchedUser:String, userId: String){
        viewModelScope.launch(IO) {
            repository.deleteMatch(matchedUser, userId)
        }
    }
        /**
         *  generates a unique chatId made from the UIDs of the sender and receiver
         */


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
    fun deleteProfile(number:String, datingActivity: DatingActivity) {
        viewModelScope.launch {
            repository.deleteProfile(number,
                onSuccess = {
                    datingActivity.clearUserToken()
                },
                onFailure = { exception ->
                    println(exception)
                }
            )
        }
    }
    /**
     * This is for someScreen
     */
    fun getLikes(userId: String, onComplete: (Int) -> Unit){
        repository.getLikes(userId, onComplete)
    }
    fun getPasses(userId: String, onComplete: (Int) -> Unit){
        repository.getPasses(userId, onComplete)
    }
    fun getLikedAndPassedby(userId: String, onComplete: (Int) -> Unit){
        repository.getLikedAndPassedby(userId, onComplete)
    }
}
//fun getCurrentUserId(): String {
//    return repository.getCurrentUserId()
//}