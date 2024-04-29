package com.threegroup.tobedated.shareclasses

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated.RealtimeDBMatch
import com.threegroup.tobedated.RealtimeDBMatchProperties
import com.threegroup.tobedated.shareclasses.firebasedatasource.deleteChats
import com.threegroup.tobedated.shareclasses.firebasedatasource.deleteMatches
import com.threegroup.tobedated.shareclasses.firebasedatasource.deleteUserDataFromStorage
import com.threegroup.tobedated.shareclasses.firebasedatasource.deleteUserFromAuthentication
import com.threegroup.tobedated.shareclasses.firebasedatasource.removeLikeOrPassData
import com.threegroup.tobedated.shareclasses.firebasedatasource.setMatchedProperties
import com.threegroup.tobedated.shareclasses.firebasedatasource.setUserProperties
import com.threegroup.tobedated.shareclasses.models.Match
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.shareclasses.models.MessageModel
import com.threegroup.tobedated.shareclasses.models.UserModel
import com.threegroup.tobedated.shareclasses.models.UserSearchPreferenceModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FirebaseDataSource {

    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser?.phoneNumber
            ?: throw Exception("User not logged in")
    }

    fun getCurrentUserSenderId(): String {
        return FirebaseAuth.getInstance().currentUser?.phoneNumber
            ?: throw Exception("User not logged in")
    }

    /**
    Dating discovery related functions
     */
    fun getPotentialUserData(): Flow<Pair<List<MatchedUserModel>, Int>> = callbackFlow {
        val user = MyApp.signedInUser.value!!
        val userPref: UserSearchPreferenceModel = user.userPref

        val dbRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("users")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<MatchedUserModel>()
                for (data in snapshot.children) {
                    try {
                        //Potential User
                        val potential = data.getValue(MatchedUserModel::class.java)
                        potential.let {
                            if (it!!.number != FirebaseAuth.getInstance().currentUser?.phoneNumber) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    if (isProfileInteractedByUser(it.number, user.number)) {
                                        val userAge = calcAge(it.birthday)

                                        // Check if user's age is within the preferred age range
                                        val isAgeInRange =
                                            userAge in userPref.ageRange.min..userPref.ageRange.max

                                        // Check if user's location is within the preferred max distance
                                        val isLocationWithinDistance = calcDistance(
                                            it.location,
                                            user.location
                                        ).toInt() <= userPref.maxDistance
                                        //is Sex match
                                        var isSexMatch = true
                                        if (user.seeking != "Everyone") {
                                            isSexMatch = it.sex == user.seeking
                                        }


                                        if (!it.seeMe && isAgeInRange && isSexMatch && isLocationWithinDistance) {//
                                            list.add(it)
                                        }
                                    }
                                }
                            }
                        }
                        Log.d("USER_TAG", "Succeeded parsing UserModel")
                    } catch (e: Exception) {
                        Log.d("USER_TAG", "Error parsing UserModel", e)
                    }
                }
                val sortedList = list.sortedByDescending { it.status }
                // Emit both the list of potential users and the current profile index
                trySend(Pair(sortedList, 0)).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("USER_TAG", "Database error: ${error.message}")
            }
        }
        dbRef.addValueEventListener(valueEventListener)
        awaitClose {
            dbRef.removeEventListener(valueEventListener)
        }
    }


    private suspend fun isProfileMatchingPreferences(
        potentialUser: MatchedUserModel,

        ): Boolean {
        val user = MyApp.signedInUser.value!!
        val userPref: UserSearchPreferenceModel = user.userPref

        // Calculate user age

        // Check other preferences
        val isGenderMatch =
            userPref.gender.isEmpty() || userPref.gender.equals(potentialUser.gender)
        val isChildrenMatch =
            userPref.children.isEmpty() || (userPref.children.equals(potentialUser.children))
        val isEthnicityMatch =
            userPref.mbti.isEmpty() || userPref.mbti.equals(potentialUser.testResultsMbti)
        val isFamilyMatch =
            userPref.familyPlans.isEmpty() || userPref.familyPlans.equals(potentialUser.family)
        val isDrinkMatch =
            userPref.drink.isEmpty() || userPref.drink.equals(potentialUser.drink)
        val isEducationMatch =
            userPref.education.isEmpty() || userPref.education.equals(potentialUser.education)
        val isIntentionsMatch =
            userPref.intentions.isEmpty() || userPref.intentions.equals(potentialUser.intentions)
        val isMeetUpMatch =
            userPref.meetUp.isEmpty() || userPref.meetUp.equals(potentialUser.meetUp)
        val isPoliticsMatch =
            userPref.politicalViews.isEmpty() || userPref.politicalViews.equals(potentialUser.politics)
        val isRelationshipMatch =
            userPref.relationshipType.isEmpty() || userPref.relationshipType.equals(
                potentialUser.relationship
            )
        val isReligionMatch =
            userPref.religion.isEmpty() || userPref.religion.equals(potentialUser.religion)
        val isSexOriMatch =
            userPref.sexualOri.isEmpty() || userPref.sexualOri.equals(potentialUser.sexOrientation)
        val isSmokeMatch =
            userPref.smoke.isEmpty() || userPref.smoke.equals(potentialUser.smoke)
        val isWeedMatch = userPref.weed.isEmpty() || userPref.weed.equals(potentialUser.weed)

        // Combine all checks
        return isGenderMatch &&
                isChildrenMatch &&
                isEthnicityMatch &&
                isFamilyMatch &&
                isDrinkMatch &&
                isEducationMatch &&
                isIntentionsMatch &&
                isMeetUpMatch &&
                isPoliticsMatch &&
                isRelationshipMatch &&
                isReligionMatch &&
                isSexOriMatch &&
                isSmokeMatch &&
                isWeedMatch
    }


    private suspend fun isProfileInteractedByUser(
        potentialUser: String,
        currentUserId: String
    ): Boolean {
//        val likePassNodeRef = FirebaseDatabase.getInstance().getReference("likeorpass").child(currentUserId)
//        val snapshot = likePassNodeRef.get().await()
//
//        val likedSnapshot = snapshot.child("liked").child(potentialUser)
//        val passedSnapshot = snapshot.child("passed").child(potentialUser)
//        !(likedSnapshot.exists() || passedSnapshot.exists())
        return true
    }

    /**
    Likes and match related functions
     */

//    private suspend fun hasUserLikedBack(userId: String, likedUserId: String): Boolean {
//        val likedRef =
//            FirebaseDatabase.getInstance().getReference("users/$likedUserId/liked/$userId")
//        val likedSnapshot = likedRef.get().await()
//        return likedSnapshot.exists()
//    }

    private suspend fun hasUserLikedBack(userId: String, likedUserId: String): Boolean {
        val likeOrPassRef = FirebaseDatabase.getInstance().getReference("likeorpass")
        val likedNodeRef = likeOrPassRef.child(likedUserId).child("liked")

        val likedSnapshot = likedNodeRef.child(userId).get().await()
        return likedSnapshot.exists()
    }

    private fun getMatchId(userId1: String, userId2: String): String {
        return if (userId1 > userId2) {
            userId1 + userId2
        } else {
            userId2 + userId1
        }
    }


    // TODO (new function that creates a new path in database called "likeorpass" and stores like, pass, likedby, and passed by information)
    suspend fun likeOrPass(userId: String, likedUserId: String, isLike: Boolean): RealtimeDBMatch? {
        val database = FirebaseDatabase.getInstance()

        // Ensure "likeorpass" node exists
        val likeOrPassRef = database.getReference("likeorpass")
        // likeOrPassRef.keepSynced(true) // Keep the node synchronized for offline capabilities //TODO might need to comment out

        // Update user's liked or passed list
        val userLikeOrPassRef = likeOrPassRef.child(userId)
        val likedUserLikeOrPassRef = likeOrPassRef.child(likedUserId)
        if (isLike) {
            userLikeOrPassRef.child("liked").child(likedUserId).setValue(true)
            // Reference to likedby node with likedUserId as the key
            likedUserLikeOrPassRef.child("likedby").child(userId)
                .setValue(true) // Set the value as the current userId
        } else {
            userLikeOrPassRef.child("passed").child(likedUserId).setValue(true)
            // Reference to passedby node with likedUserId as the key
            likedUserLikeOrPassRef.child("passedby").child(userId)
                .setValue(true) // Set the value as the current userId
        }

        // Check if there's a match
        val hasUserLikedBack = hasUserLikedBack(userId, likedUserId)
        if (hasUserLikedBack) {
            val matchId = getMatchId(userId, likedUserId)

            // Create a new match in the database
            val matchRef = database.getReference("matches/$matchId")
            val matchData = RealtimeDBMatchProperties.toData(likedUserId, userId)
            matchRef.setValue(matchData)

            // Add the matched users to the match node
            matchRef.child(likedUserId).setValue(setMatch(likedUserId))
            matchRef.child(userId).setValue(setMatch(userId))

            // Retrieve the match data and return
            val matchSnapshot = matchRef.get().await()
            return matchSnapshot.getValue(RealtimeDBMatch::class.java)
        }

        return null
    }


    private suspend fun setMatch(userId: String): Match {//TODO This should be done somewhere else
        val userSnapshot = withContext(Dispatchers.IO) {
            FirebaseDatabase.getInstance().getReference("users").child(userId).get().await()
        }
        val userName = userSnapshot.child("name").getValue(String::class.java) ?: ""
        val userImage1 = userSnapshot.child("image1").getValue(String::class.java) ?: ""

        return Match(
            id = userId,
            userId = userId,
            userName = userName,
            userPicture = userImage1,
            //formattedDate = "", // Consider formatting the timestamp properly
            lastMessage = ""
        )
    }


    suspend fun getMatchesFlow(userId: String): Flow<List<RealtimeDBMatch>> = callbackFlow {
        val ref = FirebaseDatabase.getInstance().getReference("matches")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val matches = mutableListOf<RealtimeDBMatch>()
                for (data in snapshot.children) {
                    try {
                        val match = data.getValue(RealtimeDBMatch::class.java)
                        match?.let {
                            // Check if userId appears at the start or end of usersMatched field
                            if (it.usersMatched.contains(userId)) {
                                matches.add(it)
                            }
                        }
                        Log.d("MATCH_TAG", "Succeeded parsing RealtimeDBMatch")
                    } catch (e: Exception) {
                        Log.d("MATCH_TAG", "Error parsing RealtimeDBMatch", e)
                    }
                }
                trySend(matches).isSuccess // Emit the list of matches to the flow
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("MATCH_TAG", "Database error: ${error.message}")
            }
        }

        val query = ref.orderByChild(RealtimeDBMatchProperties.usersMatched)
        query.addListenerForSingleValueEvent(listener)

        awaitClose {
            query.removeEventListener(listener)
        }
    }

    /**
     * Functions related to blocking and reporting
     */
    suspend fun reportUser(reportedUserId: String, reportingUserId: String) {
        val database = FirebaseDatabase.getInstance()
        val reportsRef = database.getReference("reports")

        // Update report list with reported user's number as the key
        val userReportedRef = reportsRef.child(reportedUserId)

        // Add the reporting user as a value for field "reported by"
        userReportedRef.child("reportedby").push().setValue(reportingUserId)

        // Increment the report count
        userReportedRef.child("count").runTransaction(object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                var count = currentData.getValue(Int::class.java) ?: 0
                count++
                currentData.value = count
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    // Failure
                    println("Transaction failed.")

                } else {
                    // Success
                    println("Transaction successful.")
                    // Remove reported user from matches and chats
                    CoroutineScope(Dispatchers.IO).launch {
                        blockUser(reportedUserId, reportingUserId)
                    }
                }
            }
        })
    }

    suspend fun blockUser(blockedUserId: String, blockingUserId: String) {
        // Get database reference to "users" path
        val database = FirebaseDatabase.getInstance()
        val databaseRef = database.getReference("users")

        // Update users blocked list
        val userBlockedRef = databaseRef.child(blockingUserId).child("blocked").child(blockedUserId)
        userBlockedRef.setValue(true)

        // Remove blocked user from matches and chats
        deleteMatch(blockedUserId, blockingUserId)
    }

    /**
     * Function to get a single instance of a match
     */
    suspend fun getMatch(match: RealtimeDBMatch, currUser: String): Match {
        val userId = if (match.usersMatched[0] == currUser) match.usersMatched[1] else {
            match.usersMatched[0]
        }
        val userSnapshot = withContext(Dispatchers.IO) {
            FirebaseDatabase.getInstance().getReference("users").child(userId).get().await()
        }
        val userName = userSnapshot.child("name").getValue(String::class.java) ?: ""
        val userImage1 = userSnapshot.child("image1").getValue(String::class.java) ?: ""

        return match.timestamp.toString().let {
            Match(
                id = match.id,
                userId = userId,
                userName = userName,
                userPicture = userImage1,
                //formattedDate = it, // Consider formatting the timestamp properly
                lastMessage = getLastMessage(match.id)
            )
        }
    }

    /**
     * for unmatching
     */
    suspend fun deleteMatch(matchedUser: String, currUser: String) {
        try {
            val matchesRef = FirebaseDatabase.getInstance().getReference("matches")
            val matchSnapshot = matchesRef.get().await()

            val matchId = matchSnapshot.key ?: ""
            if (matchId.contains(matchedUser)) {
                matchSnapshot.ref.removeValue().await()
            }

            deleteChat(getChatId(matchedUser, currUser))
            Log.d("DeleteMatches", "Matches deleted successfully for user $matchedUser")
        } catch (e: Exception) {
            Log.e("DeleteMatches", "Error deleting matches: ${e.message}", e)
        }
    }

    private suspend fun deleteChat(chatId: String) {
        val chatsRef = FirebaseDatabase.getInstance()
            .getReference("chats").child(chatId)
        chatsRef.removeValue().await()
    }


//TODO check all code related to chats/messages for functionality
    /**
     * Function to get chats from database
     * takes the chat id
     * message related stuff
     */
    private suspend fun getLastMessage(chatId: String): String {
        return suspendCoroutine { continuation ->
            val chatsRef = FirebaseDatabase.getInstance().getReference("chats").child(chatId)
            chatsRef.orderByKey().limitToLast(1).get().addOnSuccessListener { dataSnapshot ->
                val lastChild = dataSnapshot.children.firstOrNull()
                val lastMessage = lastChild?.child("message")?.getValue(String::class.java) ?: ""
                continuation.resume(lastMessage)
            }.addOnFailureListener { exception ->
                continuation.resumeWithException(exception)
            }
        }
    }

    fun getChatData(chatId: String?): Flow<List<MessageModel>> = callbackFlow {
        val dbRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<MessageModel>()
                for (data in snapshot.children) {
                    try {
                        val messageModel = data.getValue(MessageModel::class.java)
                        messageModel?.let { list.add(it) }
                        Log.d("CHAT_TAG", "Succeeded parsing MessageModel")
                    } catch (e: Exception) {
                        Log.d("CHAT_TAG", "Error parsing MessageModel", e)
                    }
                }
                trySend(list).isSuccess // this should emit the list to the flow
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("CHAT_TAG", "Database error: ${error.message}")
            }
        }
        dbRef.addValueEventListener(valueEventListener)
        awaitClose {
            dbRef.removeEventListener(valueEventListener)
        }
    }

    /**
     * Function to store messages between users
     * takes the message as a parameter
     */

    fun storeChatData(chatId: String?, message: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.phoneNumber
            ?: throw Exception("User not logged in")
        val currentTime: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        val map = hashMapOf<String, String>()
        map["message"] = message
        map["senderId"] = senderId
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate

        val reference = FirebaseDatabase.getInstance().getReference("chats").child(chatId!!)

        reference.child(reference.push().key!!).setValue(map)
        /*
        .addOnCompleteListener {
        if (it.isSuccessful) {
            binding.yourMessage.text = null
            Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

         */
    }

    /**
     * Function to display the chats in the messages screen
     */
    fun displayChats() {
        val currentId = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        FirebaseDatabase.getInstance().getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = arrayListOf<String>()
                    val newList = arrayListOf<String>()

                    for (data in snapshot.children) {
                        if (data.key!!.contains(currentId!!)) {
                            list.add(data.key!!.replace(currentId, ""))
                            newList.add(data.key!!)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    /**
     * Deletes the user profile and all things connected to that profile
     */
    suspend fun deleteProfile(
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            val database = FirebaseDatabase.getInstance()
            //val storage = FirebaseStorage.getInstance()
            val userRef = database.getReference("/users/$userId")
            removeLikeOrPassData(database, userId)

            // Delete user's matches
            deleteMatches(database, userId)

            // Delete user's chats
            deleteChats(database, userId)

            // Delete user's data from Firebase Storage
            deleteUserDataFromStorage(userId)

            // Delete user from authentication list
            deleteUserFromAuthentication()
            userRef.removeValue()
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    onFailure(e)
                }
            Log.d("DeleteUserAndData", "User $userId and associated data deleted successfully")
        } catch (e: Exception) {
            Log.e("DeleteUserAndData", "Error deleting user data: ${e.message}", e)
        }
    }


    /**
     * Set info for the logged in user
     */

    suspend fun setUserInfo(number: String, location: String): Flow<UserModel?> = flow {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(number)

        val userDataMap = withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val eventListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        continuation.resume(snapshot.value as? Map<*, *>)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException())
                    }
                }
                databaseReference.addListenerForSingleValueEvent(eventListener)
            }
        }
        userDataMap?.let { map ->
            val user = setUserProperties(UserModel(), map, location)
            emit(user)
        }
        databaseReference.child("status").setValue(System.currentTimeMillis())
        databaseReference.child("location").setValue(location)
    }


    /**
     * Sets info for profiles that you see in seeking/your matches
     */

    suspend fun setMatchedInfo(number: String): Flow<MatchedUserModel?> = flow {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(number)

        val userDataMap = withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val eventListener = object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        continuation.resume(snapshot.value as? Map<*, *>)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException())
                    }
                }
                databaseReference.addListenerForSingleValueEvent(eventListener)
            }
        }
        userDataMap?.let { map ->
            val user = setMatchedProperties(MatchedUserModel(), map)
            emit(user)
        }
    }
}