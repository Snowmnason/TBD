package com.threegroup.tobedated.shareclasses

import android.content.ContentValues.TAG
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
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
class FirebaseDataSource {

    private fun getCurrentUserId(): String {
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
    fun getPotentialUserData(): Flow<List<MatchedUserModel>> = callbackFlow {
        val user = MyApp.signedInUser.value!!
        val db = FirebaseDatabase.getInstance()
        val dbRef: DatabaseReference = db.getReference("users")
        val likePassNodeRef = db.getReference("likeorpass").child(user.number)

        val likePassListener = object : ValueEventListener {
            override fun onDataChange(likePassSnapshot: DataSnapshot) {
                val query = dbRef.orderByChild("status").limitToLast(10)
                val usersListener = object : ValueEventListener {
                    override fun onDataChange(usersSnapshot: DataSnapshot) {
                        val list = mutableListOf<MatchedUserModel>()
                        usersSnapshot.children.forEach { userSnapshot ->
                            try {
                                val potential = userSnapshot.getValue(MatchedUserModel::class.java)
                                potential?.let {
                                    if (it.number != FirebaseAuth.getInstance().currentUser?.phoneNumber
                                        && passBlocked(dbRef, user.number, it.number)
                                        && passSeeMe(it, likePassSnapshot)
                                        && isProfileInteractedByUser(it.number, likePassSnapshot)
                                        && passBasicPreferences(user, it)
                                        && !passMatch(it)
                                        && passPremiumPref(user, it)
                                    ) {
                                        list.add(it)
                                    }
                                }
                            } catch (e: Exception) {
                                Log.d("Searching","Error parsing UserModel: $e")
                            }
                        }
                        val sortedList = list.sortedByDescending { it.status }
                        trySend(sortedList).isSuccess
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.d("Searching","Database error: ${error.message}")
                    }
                }
                query.addValueEventListener(usersListener)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Searching","Database error: ${error.message}")
            }
        }

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                likePassNodeRef.addValueEventListener(likePassListener)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("USER_TAG", "Database error: ${error.message}")
            }
        }
        dbRef.addValueEventListener(valueEventListener)
        awaitClose {
            dbRef.removeEventListener(valueEventListener)
            likePassNodeRef.removeEventListener(likePassListener)
        }
    }.flowOn(Dispatchers.IO)

    private fun passBlocked(dbReference: DatabaseReference, userId: String, potentialUser: String): Boolean {
        //val block = dbReference.child(userId).child("blocked").child(potentialUser)

        //val exists = true

        return true
    }

    private fun passSeeMe(potentialUser: MatchedUserModel, snapshot: DataSnapshot): Boolean {
        return if (!potentialUser.seeMe) {
            true
        } else {
            val likedSnapshot = snapshot.child("likedby").child(potentialUser.number)
            likedSnapshot.exists()
        }
    }

    private fun passMatch(potentialUser: MatchedUserModel): Boolean {

        return potentialUser.hasThree
    }

    private fun isProfileInteractedByUser(potentialUser: String, snapshot: DataSnapshot): Boolean {
        val likedSnapshot = snapshot.child("liked").child(potentialUser)
        val passedSnapshot = snapshot.child("passed").child(potentialUser)

        return !(likedSnapshot.exists() || passedSnapshot.exists())
    }

    /**
     * This function checks basic preferences, sex, age, distance
     */
    private fun passBasicPreferences(user: UserModel, potentialUser: MatchedUserModel): Boolean {
        //TODO this is a temporary fix for the location error issue
        if (user.location == "error/" || user.location == "null") {
            user.location = "40.709278/-73.348686"
        }
        val userPref = user.userPref
        val userAge = calcAge(potentialUser.birthday)
        val isAgeInRange = userAge in userPref.ageRange.min..userPref.ageRange.max
        val isSexMatch = (user.seeking == "Everyone") || (potentialUser.sex == user.seeking)
        val potentialLocation = potentialUser.location
        val userLocation = user.location
        val maxDistance = userPref.maxDistance
        val distance = calcDistance(potentialLocation, userLocation).toInt()
        val isLocationWithinDistance = distance <= maxDistance

        return isAgeInRange && isSexMatch && isLocationWithinDistance
    }

    /**
     * This checks the "premium" features
     */
    private fun passPremiumPref(user: UserModel, potentialUser: MatchedUserModel): Boolean {
        val userPref: UserSearchPreferenceModel = user.userPref
        val preferences = listOf(
            userPref.gender to potentialUser.gender,
            userPref.children to potentialUser.children,
            userPref.zodiac to potentialUser.star,
            userPref.mbti to potentialUser.testResultsMbti,
            userPref.familyPlans to potentialUser.family,
            userPref.drink to potentialUser.drink,
            userPref.education to potentialUser.education,
            userPref.intentions to potentialUser.intentions,
            userPref.meetUp to potentialUser.meetUp,
            userPref.politicalViews to potentialUser.politics,
            userPref.relationshipType to potentialUser.relationship,
            userPref.religion to potentialUser.religion,
            userPref.sexualOri to potentialUser.sexOrientation,
            userPref.smoke to potentialUser.smoke,
            userPref.weed to potentialUser.weed
        )

        return preferences.all { (userPrefList, userValue) ->
            userPrefList[0] == "Doesn't Matter" || userPrefList.contains(userValue)
        }
    }

    /**
    Likes and match related functions
     */
    private suspend fun hasUserLikedBack(userId: String, likedUserId: String, inOther:String= ""): Boolean { //= ""
        val likeOrPassRef = FirebaseDatabase.getInstance().getReference("likeorpass$inOther")
        val likedNodeRef = likeOrPassRef.child(likedUserId).child("liked")

        val likedSnapshot = likedNodeRef.child(userId).get().await()
        return likedSnapshot.exists()
    }

    // TODO (new function that creates a new path in database called "likeorpass" and stores like, pass, likedby, and passed by information)
    suspend fun likeOrPass(userId: String, likedUserId: String, isLike: Boolean, inOther:String): RealtimeDBMatch? {
        val database = FirebaseDatabase.getInstance()

        // Ensure "likeorpass" node exists
        val likeOrPassRef = database.getReference("likeorpass$inOther")
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
        val hasUserLikedBack = hasUserLikedBack(userId, likedUserId, inOther)
        if (hasUserLikedBack) {
            val matchId = getMatchId(userId, likedUserId)

            // Create a new match in the database
            val matchRef = database.getReference("matches$inOther/$matchId")
            val matchData = RealtimeDBMatchProperties.toData(likedUserId, userId)
            matchRef.setValue(matchData)

            // Add the matched users to the match node
            matchRef.child(likedUserId).setValue(setMatch(likedUserId))
            matchRef.child(userId).setValue(setMatch(userId))

            // Set isNewMatch status for both users
            matchRef.child(likedUserId).child("isNewMatch").setValue(true)
            matchRef.child(userId).child("isNewMatch").setValue(true)

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

        val newMatch = Match(
            id = userId,
            userId = userId,
            userName = userName,
            userPicture = userImage1,
            //formattedDate = "", // Consider formatting the timestamp properly
            lastMessage = ""
        )
        //println("New Match is $newMatch")
        return newMatch
    }

    suspend fun markMatchAsViewed(matchId: String, userId: String, inOther: String) {
        val matchRef = FirebaseDatabase.getInstance().getReference("matches$inOther/$matchId")
        matchRef.child(userId).child("isNewMatch").setValue(false)
    }

    suspend fun getMatchesFlow(userId: String, inOther: String): Flow<List<RealtimeDBMatch>> = callbackFlow {
        val ref = FirebaseDatabase.getInstance().getReference("matches$inOther")
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
     * Functions related to blocking and reporting and suggesting
     */
    suspend fun reportUser(reportedUserId: String, reportingUserId: String, inOther: String) {
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
                        blockUser(reportedUserId, reportingUserId, inOther)
                    }
                }
            }
        })
    }

    suspend fun blockUser(blockedUserId: String, blockingUserId: String, inOther: String) {
        // Get database reference to "users" path
        val database = FirebaseDatabase.getInstance()
        val databaseRef = database.getReference("users")

        // Update users blocked list
        val userBlockedRef = databaseRef.child(blockingUserId).child("blocked").child(blockedUserId)
        userBlockedRef.setValue(true)

        // Remove blocked user from matches and chats
        deleteMatch(blockedUserId, blockingUserId, inOther)
    }

    fun suggest(currentPotential: String, suggestion: String, inOther: String) {
        val database = FirebaseDatabase.getInstance().getReference("users").child(currentPotential)
            .child("suggestion$inOther")
        database.setValue(suggestion)
    }

    fun getSuggestions(currentUser: String, onComplete: (List<String>) -> Unit, inOther: String) {
        val database = FirebaseDatabase.getInstance().getReference("users").child(currentUser)
            .child("suggestion$inOther")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val suggestions = mutableListOf<String>()
                for (childSnapshot in dataSnapshot.children) {
                    val suggestion = childSnapshot.getValue(String::class.java)
                    suggestion?.let {
                        suggestions.add(it)
                    }
                }
                // Call the onComplete callback with the list of suggestions
                onComplete(suggestions)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                Log.e(TAG, "Error fetching suggestions: ${databaseError.message}")
            }
        }

        database.addListenerForSingleValueEvent(valueEventListener)
    }

    /**
     * Function to get a single instance of a match
     */
    suspend fun getMatch(match: RealtimeDBMatch, currUser: String, inOther: String): Match {
        val userId = if (match.usersMatched[0] == currUser) match.usersMatched[1] else {
            match.usersMatched[0]
        }
        val userSnapshot = withContext(Dispatchers.IO) {
            FirebaseDatabase.getInstance().getReference("users").child(userId).get().await()
        }
        val userName = userSnapshot.child("name").getValue(String::class.java) ?: ""
        val userImage1 = userSnapshot.child("image1").getValue(String::class.java) ?: ""
        val retrievedMatch = match.timestamp.toString().let {
            Match(
                id = match.id,
                userId = userId,
                userName = userName,
                userPicture = userImage1,
                //formattedDate = it, // Consider formatting the timestamp properly
                lastMessage = getLastMessage(
                    getMatchId(
                        match.usersMatched[0],
                        match.usersMatched[1]
                    ), inOther
                )
            )
        }
        //println(retrievedMatch)
        return retrievedMatch
    }

    /**
     * for unmatching
     */
    suspend fun deleteMatch(matchedUser: String, currUser: String, inOther: String) {
        try {
            val matchesRef = FirebaseDatabase.getInstance().getReference("matches$inOther")
            val matchId2 = matchesRef.child(getMatchId(matchedUser, currUser))
            matchId2.removeValue().await()

            deleteChat(getChatId(matchedUser, currUser), inOther)
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("users").child(currUser)
                    .child("hasThree")
            databaseReference.setValue(false)
            Log.d("DeleteMatches", "Matches deleted successfully for user $matchedUser")
        } catch (e: Exception) {
            Log.e("DeleteMatches", "Error deleting matches: ${e.message}", e)
        }
    }

    private suspend fun deleteChat(chatId: String, inOther: String) {
        val chatsRef = FirebaseDatabase.getInstance()
            .getReference("chats$inOther").child(chatId)
        chatsRef.removeValue().await()
    }


//TODO check all code related to chats/messages for functionality
    /**
     * Function to get chats from database
     * takes the chat id
     * message related stuff
     */
    private suspend fun getLastMessage(chatId: String, inOther: String): String {
        return suspendCoroutine { continuation ->
            val chatsRef = FirebaseDatabase.getInstance().getReference("chats$inOther").child(chatId)
            chatsRef.orderByKey().limitToLast(1).get().addOnSuccessListener { dataSnapshot ->
                val lastChild = dataSnapshot.children.firstOrNull()
                val lastMessage = lastChild?.child("message")?.getValue(String::class.java) ?: ""
                continuation.resume(lastMessage)
            }.addOnFailureListener { exception ->
                println("oops failure")
                continuation.resumeWithException(exception)
            }
        }
    }

    fun getChatData(chatId: String?, inOther:String): Flow<List<MessageModel>> = callbackFlow {
        val dbRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("chats$inOther").child(chatId!!)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<MessageModel>()
                for (data in snapshot.children) {
                    if (data.key == "notifications") {
                        continue
                    }
                    try {
                        val messageModel = data.getValue(MessageModel::class.java)
                        messageModel?.let { list.add(it) }
                        //Log.d("CHAT_TAG", "Succeeded parsing MessageModel")
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

    fun storeChatData(chatId: String?, message: String, inOther: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.phoneNumber
            ?: throw Exception("User not logged in")
        val currentTime: String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

        val map = hashMapOf<String, Any>()
        map["message"] = message
        map["senderId"] = senderId
        map["currentTime"] = currentTime
        map["currentDate"] = currentDate
        map["read"] = false // If read by receiving user change to true

        val reference = FirebaseDatabase.getInstance().getReference("chats$inOther").child(chatId!!)

        reference.child(reference.push().key!!).setValue(map).addOnSuccessListener {
            incrementNotificationCount(chatId, senderId, inOther)
        }
            .addOnFailureListener { exception ->
                Log.e("Chat", "Error sending message: ${exception.message}", exception)
            }
    }

    private fun incrementNotificationCount(chatId: String?, senderId: String?, inOther: String) {
        // Determine the receiverId and senderId based on the chatId
        val senderIdFromChatId: String
        val receiverIdFromChatId: String
        if (senderId == chatId?.substring(0, senderId!!.length)) {
            senderIdFromChatId = chatId!!.substring(0, senderId!!.length)
            receiverIdFromChatId = chatId.substring(senderId.length)
        } else {
            senderIdFromChatId = chatId!!.substring(chatId.length - senderId!!.length)
            receiverIdFromChatId = chatId.substring(0, chatId.length - senderId.length)
        }

        // Determine the receiverId
        val receiverId =
            if (senderId == senderIdFromChatId) receiverIdFromChatId else senderIdFromChatId

        // Check if the senderId matches the receiverId
        if (senderId != receiverId) {
            // Increment the count in the database for the receiver
            val notificationRef =
                FirebaseDatabase.getInstance().getReference("chats$inOther").child(chatId)
                    .child("notifications").child(receiverId ?: "")

            notificationRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentCount = snapshot.getValue(Int::class.java) ?: 0
                    // Increment the count
                    notificationRef.setValue(currentCount + 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Log.e(
                        "Chat",
                        "Error incrementing notification count: ${error.message}",
                        error.toException()
                    )
                }
            })
        }
    }


    private suspend fun decrementNotificationCount(chatId: String?, inOther: String) {
        // Decrement the count in the database
        val userId = getCurrentUserId()
        val notificationRef = FirebaseDatabase.getInstance().getReference("chats$inOther").child(chatId!!)
            .child("notifications").child(userId)

        try {
            val currentCountSnapshot = notificationRef.get().await()
            val currentCount = currentCountSnapshot.getValue(Int::class.java) ?: 0
            if (currentCount > 0) {
                // Get the current unread messages count using a Flow
                val unreadMessagesCount = getUnreadMessagesCount(chatId, userId, inOther)
                unreadMessagesCount.collect { count ->
                    // Decrement the count by the number of unread messages
                    notificationRef.setValue(currentCount - count)
                }
            }
        } catch (e: Exception) {
            // Handle error
            Log.e("Chat", "Error decrementing notification count: ${e.message}", e)
        }
    }

    private fun getUnreadMessagesCount(chatId: String?, userId: String, inOther: String): Flow<Int> = flow {
        val chatRef = FirebaseDatabase.getInstance().getReference("chats$inOther").child(chatId!!)
        var unreadMessagesCount = 0

        try {
            val snapshot = chatRef.get().await()
            snapshot.children.forEach { messageSnapshot ->
                val senderId = messageSnapshot.child("senderId").getValue(String::class.java)
                val read = messageSnapshot.child("read").getValue(Boolean::class.java) ?: false
                if (senderId != null && senderId != userId && !read) {
                    unreadMessagesCount++
                }
            }
            println("Unread message count success: $unreadMessagesCount")
            emit(unreadMessagesCount)
        } catch (e: Exception) {
            println("Unread message count error: $e")
            emit(0) // Return 0 in case of error
        }
    }

    suspend fun openChat(chatId: String, inOther: String) {
        // Mark the chat as read or seen
        markChatAsRead(chatId, inOther)

        // Decrement the notification count
        decrementNotificationCount(chatId, inOther)
    }
    fun checkRead(chatId: String, inOther: String, callback: (Boolean)->Unit) {
            val chatsRef = FirebaseDatabase.getInstance().getReference("chats$inOther").child(chatId)
            chatsRef.orderByKey().limitToLast(1).get().addOnSuccessListener { dataSnapshot ->
                val lastChild = dataSnapshot.children.firstOrNull()
                val isRead = lastChild?.child("read")?.getValue(Boolean::class.java) ?: false
                callback(isRead)
            }.addOnFailureListener { exception ->
                Log.e("Read", exception.toString())
        }
    }
    private fun markChatAsRead(chatId: String, inOther: String) {
        val currentUser = getCurrentUserId()
        val chatRef = FirebaseDatabase.getInstance().getReference("chats$inOther").child(chatId)

        // Marking the chat as read for the current user
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { messageSnapshot ->
                    if (messageSnapshot.key != "notifications") {
                        val senderId =
                            messageSnapshot.child("senderId").getValue(String::class.java)
                        val read =
                            messageSnapshot.child("read").getValue(Boolean::class.java) ?: false

                        // Mark the message as read for the current user if it was sent by another user and is unread
                        if (senderId != currentUser && !read) {
                            messageSnapshot.child("read").ref.setValue(true)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Log.e("Chat", "Error marking chat as read: ${error.message}", error.toException())
            }
        })
    }


    /**
     * Deletes the user profile and all things connected to that profile
     */
    suspend fun deleteProfile(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit, inOther: String) {
        try {
            val database = FirebaseDatabase.getInstance()
            //val storage = FirebaseStorage.getInstance()
            val userRef = database.getReference("/users/$userId")

            removeLikeOrPassData(database, userId, inOther)

            // Delete user's matches
            deleteMatches(database, userId, inOther)

            // Delete user's chats
            deleteChats(database, userId, inOther)
            when(inOther){
                "casual" -> userRef.child("hasCasual").setValue(false)
                else ->{
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
                }
            }
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

    /**
     * API calls
     */
    suspend fun getWord(): JSONObject? {
        val apiKey = "rd8wadogjxvq8mtpbsngll4mv6eokvk29vlx6rnlzkgof0475"
        return withContext(Dispatchers.IO) {
            val url = "https://api.wordnik.com/v4/words.json/wordOfTheDay?api_key=$apiKey"
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            responseBody?.let { JSONObject(it) }
        }
    }

    suspend fun getHoroscope(sign: String): JSONObject? {
        return try {
            withContext(Dispatchers.IO) {
                val url =
                    "https://horoscope-app-api.vercel.app/api/v1/get-horoscope/daily?sign=$sign&day=TODAY"

                // Increase timeout values to handle potential timeouts
                val client = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // Adjust timeout as needed
                    .readTimeout(30, TimeUnit.SECONDS) // Adjust timeout as needed
                    .build()

                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                responseBody?.let { JSONObject(it) }
            }
        } catch (e: Exception) {
            // Handle exceptions gracefully
            Log.e("getHoroscope", "Exception: ${e.message}")
            null // Return null in case of failure
        }
    }

    fun getPoem(): Flow<JSONArray> {
        return flow {
            val url = "https://poetrydb.org/random"
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()
            responseBody?.let {
                val jsonArray = JSONArray(it)
                emit(jsonArray) // Emit the JSON array when API call completes
            }
        }.flowOn(Dispatchers.IO) // Perform API call on IO dispatcher
    }

    /**
     * SomeScreen Calls
     */
    fun getLikes(userId: String, onComplete: (Int) -> Unit, inOther: String) {
        val db = FirebaseDatabase.getInstance()
        val likePassNodeRef = db.getReference("likeorpass$inOther").child(userId).child("liked")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount.toInt()
                onComplete(count)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                onComplete(0) // Return 0 in case of error
            }
        }

        likePassNodeRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun getPasses(userId: String, onComplete: (Int) -> Unit, inOther: String) {
        val db = FirebaseDatabase.getInstance()
        val likePassNodeRef = db.getReference("likeorpass$inOther").child(userId).child("passed")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.childrenCount.toInt()
                onComplete(count)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                onComplete(0) // Return 0 in case of error
            }
        }

        likePassNodeRef.addListenerForSingleValueEvent(valueEventListener)
    }

    fun getLikedAndPassedby(userId: String, onComplete: (Int) -> Unit, inOther: String) {
        val db = FirebaseDatabase.getInstance().getReference("likeorpass$inOther")
        val likeNodeRef = db.child(userId).child("likedby")
        val passNodeRef = db.child(userId).child("passedby")

        var likedCount = 0
        var passedCount = 0

        val likedListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                likedCount = snapshot.childrenCount.toInt()
                calculateTotal(likedCount, passedCount, onComplete)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                calculateTotal(0, 0, onComplete)
            }
        }

        val passedListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                passedCount = snapshot.childrenCount.toInt()
                calculateTotal(likedCount, passedCount, onComplete)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                calculateTotal(0, 0, onComplete)
            }
        }

        likeNodeRef.addListenerForSingleValueEvent(likedListener)
        passNodeRef.addListenerForSingleValueEvent(passedListener)
    }

    fun calculateTotal(likedCount: Int, passedCount: Int, onComplete: (Int) -> Unit) {
        val total = likedCount + passedCount
        onComplete(total)
    }

    /**
     * Functions for notifications
     */
    fun updateNewMatchesCount(callback: (totalNotificationCount: Int) -> Unit, inOther: String) {
        val userId = getCurrentUserId()
        val database = FirebaseDatabase.getInstance()
        val userMatchesRef = database.getReference("matches$inOther")

        val matchesListener = object : ValueEventListener {
            override fun onDataChange(matchesSnapshot: DataSnapshot) {
                var newMatchesCount = 0
                matchesSnapshot.children.forEach { matchSnapshot ->
                    val matchId = matchSnapshot.key
                    if (matchId != null && matchId.contains(userId)) {
                        val isNewMatch = matchSnapshot.child(userId).child("isNewMatch")
                            .getValue(Boolean::class.java)
                        if (isNewMatch == true) {
                            newMatchesCount++
                            Log.d("UPDATE_TAG", "New matches count = $newMatchesCount")
                        }
                    }
                }
                callback(newMatchesCount)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UPDATE_TAG", "Error in getting new matches count: ${error.message}")
            }
        }

        userMatchesRef.addValueEventListener(matchesListener)
    }

    fun updateNewChatsCount(callback: (totalNotificationCount: Int) -> Unit, inOther: String) {
        val userId = getCurrentUserId()
        val database = FirebaseDatabase.getInstance()
        val userChatsRef = database.getReference("chats$inOther")

        val chatsListener = object : ValueEventListener {
            override fun onDataChange(chatsSnapshot: DataSnapshot) {
                var newChatsCount = 0
                chatsSnapshot.children.forEach { chatSnapshot ->
                    val chatId = chatSnapshot.key
                    if (chatId != null && chatId.contains(userId)) {
                        val notificationSnapshot = chatSnapshot.child("notifications").child(userId)
                        val notificationsCountLong = notificationSnapshot.getValue(Long::class.java)
                        val notificationsCount = notificationsCountLong?.toInt() ?: 0
                        newChatsCount += notificationsCount
                    }
                }
                callback(newChatsCount)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("UPDATE_TAG", "Error in getting unread chats count: ${error.message}")
            }
        }
        userChatsRef.addValueEventListener(chatsListener)
    }

    /**
     *FOR CASUAL
     *
     */
    fun getPotentialUserDataC(): Flow<List<MatchedUserModel>> = callbackFlow {
        val user = MyApp.signedInUser.value!!
        val db = FirebaseDatabase.getInstance()
        val dbRef: DatabaseReference = db.getReference("users")
        val likePassNodeRef = db.getReference("likeorpasscasual").child(user.number)

        val likePassListener = object : ValueEventListener {
            override fun onDataChange(likePassSnapshot: DataSnapshot) {
                val query = dbRef.orderByChild("status").limitToLast(10)
                val usersListener = object : ValueEventListener {
                    override fun onDataChange(usersSnapshot: DataSnapshot) {
                        val list = mutableListOf<MatchedUserModel>()
                        usersSnapshot.children.forEach { userSnapshot ->
                            try {
                                val potential = userSnapshot.getValue(MatchedUserModel::class.java)
                                potential?.let {
                                    if (it.number != FirebaseAuth.getInstance().currentUser?.phoneNumber
                                        && passBlocked(dbRef, user.number, it.number)//KEEP
                                        && passSeeMe(it, likePassSnapshot)//KEEP
                                        && isProfileInteractedByUserC(it.number, likePassSnapshot)//KEEP
                                        && passBasicPreferences(user, it)//Keep
                                        && !passMatchC(it)//KEEP
                                        && passPremiumPrefC(user, it)//KEEP
                                        && it.hasCasual
                                    ) {
                                        list.add(it)
                                    }
                                }
                            } catch (e: Exception) {
                                println("Error parsing UserModel: $e")
                            }
                        }
                        val sortedList = list.sortedByDescending { it.status }
                        trySend(sortedList).isSuccess
                    }

                    override fun onCancelled(error: DatabaseError) {
                        println("Database error: ${error.message}")
                    }
                }
                query.addValueEventListener(usersListener)
            }

            override fun onCancelled(error: DatabaseError) {
                println("Database error: ${error.message}")
            }
        }

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                likePassNodeRef.addValueEventListener(likePassListener)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("USER_TAG", "Database error: ${error.message}")
            }
        }
        dbRef.addValueEventListener(valueEventListener)
        awaitClose {
            dbRef.removeEventListener(valueEventListener)
            likePassNodeRef.removeEventListener(likePassListener)
        }
    }.flowOn(Dispatchers.IO)
    private fun isProfileInteractedByUserC(potentialUser: String, snapshot: DataSnapshot): Boolean {
        val likedSnapshot = snapshot.child("liked").child(potentialUser)
        val passedSnapshot = snapshot.child("passed").child(potentialUser)

        return !(likedSnapshot.exists() || passedSnapshot.exists())
    }
    private fun passMatchC(potentialUser: MatchedUserModel): Boolean {

        return potentialUser.hasThreeCasual
    }
    private fun passPremiumPrefC(user: UserModel, potentialUser: MatchedUserModel): Boolean {
        val userPref: UserSearchPreferenceModel = user.userPref
        val preferences = listOf(
            userPref.gender to potentialUser.gender,
            userPref.meetUp to potentialUser.meetUp,
            userPref.sexualOri to potentialUser.sexOrientation,
            userPref.leaning to potentialUser.casualAdditions.leaning,
            userPref.lookingFor to potentialUser.casualAdditions.lookingFor,
            userPref.experience to potentialUser.casualAdditions.experience,
            userPref.location to potentialUser.casualAdditions.location,
            userPref.comm to potentialUser.casualAdditions.comm,
            userPref.sexHealth to potentialUser.casualAdditions.sexHealth,
            userPref.afterCare to potentialUser.casualAdditions.afterCare,
        )

        return preferences.all { (userPrefList, userValue) ->
            userPrefList[0] == "Doesn't Matter" || userPrefList.contains(userValue)
        }
    }

}

