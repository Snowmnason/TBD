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
                println("Database error: ${error.message}")
            }
        }

        dbRef.addValueEventListener(valueEventListener)

        awaitClose {
            // Remove the listeners in reverse order
            dbRef.removeEventListener(valueEventListener)
            likePassNodeRef.removeEventListener(likePassListener)
        }
    }.flowOn(Dispatchers.IO)

    private fun passBlocked(
        dbReference: DatabaseReference,
        userId: String,
        potentialUser: String
    ): Boolean {
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
        if (user.location == "error/") {
            user.location = "37.4220936/-122.083922"
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

        return isAgeInRange && isLocationWithinDistance && isSexMatch
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
     * Functions related to blocking and reporting and suggesting
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

    fun suggest(currentPotential: String, suggestion: String) {
        val database = FirebaseDatabase.getInstance().getReference("users").child(currentPotential)
            .child("suggestion")
        database.setValue(suggestion)
    }

    fun getSuggestions(currentUser: String, onComplete: (List<String>) -> Unit) {
        val database = FirebaseDatabase.getInstance().getReference("users").child(currentUser)
            .child("suggestion")

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
            val matchId2 = matchesRef.child(getMatchId(matchedUser, currUser))
            matchId2.removeValue().await()

            deleteChat(getChatId(matchedUser, currUser))
            val databaseReference =
                FirebaseDatabase.getInstance().getReference("users").child(currUser)
                    .child("hasThree")
            databaseReference.setValue(false)
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
    fun getLikes(userId: String, onComplete: (Int) -> Unit) {
        val db = FirebaseDatabase.getInstance()
        val likePassNodeRef = db.getReference("likeorpass").child(userId).child("liked")

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

    fun getPasses(userId: String, onComplete: (Int) -> Unit) {
        val db = FirebaseDatabase.getInstance()
        val likePassNodeRef = db.getReference("likeorpass").child(userId).child("passed")

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

    fun getLikedAndPassedby(userId: String, onComplete: (Int) -> Unit) {
        val db = FirebaseDatabase.getInstance()
        val likeNodeRef = db.getReference("likeorpass").child(userId).child("likedby")
        val passNodeRef = db.getReference("likeorpass").child(userId).child("passedby")

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
}