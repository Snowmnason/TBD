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
        val dbRef: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("users")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<MatchedUserModel>()
                for (data in snapshot.children) {
                    try {
                        val userModel = data.getValue(MatchedUserModel::class.java)

                        if (userModel?.number != FirebaseAuth.getInstance().currentUser?.phoneNumber) {
                            userModel?.let {
                                if (!it.seeMe) {
                                    list.add(it)
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
        return if (userId1 < userId2) {
            "$userId1-$userId2"
        } else {
            "$userId2-$userId1"
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
                        deleteMatches(database, reportedUserId)
                        deleteChats(database, reportedUserId)
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
        deleteMatches(database, blockedUserId)
        deleteChats(database, blockedUserId)
    }

    /**
     * Function to get a single instance of a match
     */
    suspend fun getMatch(match: RealtimeDBMatch): Match? {
        val userId = match.usersMatched.firstOrNull {
            it != (FirebaseAuth.getInstance().currentUser?.uid
                ?: throw Exception("User not logged in"))
        } ?: return null
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
                lastMessage = match.lastMessage
            )
        }
//        val user = MyApp.signedInUser.value!!
//        val matchId = getMatchId(userId, user.name)
//        val userSnapshot = withContext(Dispatchers.IO) {
//            FirebaseDatabase.getInstance().getReference("matches/$matchId/$userId").get().await()
//        }
//        //val matchRef =  FirebaseDatabase.getInstance().getReference("matches/$matchId/$userId")
//        val userName = userSnapshot.child("userName").getValue(String::class.java) ?: ""
//        val userAge = userSnapshot.child("age").getValue(Int::class.java) ?: 18
//        val userImage1 = userSnapshot.child("userPicture").getValue(String::class.java) ?: ""
//

    }



    /*
        Message related functions
     */
//TODO check all code related to chats/messages for functionality
    /**
     * Function to get chats from database
     * takes the chat id
     */
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
    suspend fun deleteProfile(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
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
///*
//   Log in and authentication functions
// */
///**
// * Function for user to sign in by phone number and verification code
// */
//suspend fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
//    FirebaseAuth.getInstance().signInWithCredential(credential)
//}
//
///**
// * Function to check if a user exists in the database
// * takes a phone number as a parameter
// */
//suspend fun checkUserExist(number: String) {
//    FirebaseDatabase.getInstance().getReference("users").child("+1$number")
//}
//
///*
//  Sign up functions
//*/
///**
// * Function to store users data after making an account
// * takes an UserModel type object which will contain all the user's input and adds it to database
// */
//suspend fun storeUserData(data: UserModel) {
//    FirebaseDatabase.getInstance().getReference("users")
//        .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
//        .setValue(data)
//}
//
///*
//      User data related functions
// */
//
//private fun getCurrentFirebaseUser(): FirebaseUser? {
//    return FirebaseAuth.getInstance().currentUser
//}
/**
 * Function to pull the list of users (not including current user)
 * meant to be used for dating discovery
 */
//suspend fun getUserData(): ArrayList<UserModel>? { //TODO look into converting from DataSnapshot to Flow and StateFLow
//    var list: ArrayList<UserModel>? = null
//    try {
//        FirebaseDatabase.getInstance().getReference("users")
//            .addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    Log.d("MYTAG", "onDataChange: ${snapshot.toString()}")
//
//                    if (snapshot.exists()) {
//                        list = arrayListOf()
//                        for (data in snapshot.children) {
//                            val model = data.getValue(UserModel::class.java)
//
//                            if (model!!.number != FirebaseAuth.getInstance().currentUser!!.phoneNumber) { // TODO check this; not sure if it works
//                                list!!.add(model)
//                            }
//
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    Log.d("MY_DEBUGGER", Log.ERROR.toString())
//                }
//            })
//    } catch (e: Exception) {
//        Log.d("MY_DEBUGGER", Log.ERROR.toString())
//    }
//    return list
//}
//
///**
// * Function to update user's profile
// * currently takes a UserModel as a parameter but we may want to change this to the specific variables being updated
// */
//suspend fun updateUserData(userUpdates: UserModel) {
//    //TODO check if this properly updates the changed values the user put in and does not create a duplicate user (resulting in an error most likely) or anything else strange
//    FirebaseDatabase.getInstance().getReference("users")
//        .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
//        .setValue(userUpdates)
//}

// TODO (old function that stores like and pass info inside User's path)

//   suspend fun likeOrPass(userId: String, likedUserId: String, isLike: Boolean): RealtimeDBMatch? {
//        val database = FirebaseDatabase.getInstance().getReference("users/$userId")
//
//        // Update user's liked or passed list
//        if (isLike) {
//            database.child("liked").push().setValue(likedUserId)
//            // Mark the liked user as existing in the liked list
//            database.child("liked/$likedUserId").setValue(true)
//        } else {
//            database.child("passed").push().setValue(likedUserId)
//        }
//        // Check if there's a match
//        val hasUserLikedBack = hasUserLikedBack(userId, likedUserId)
//        if (hasUserLikedBack) {
//            val matchId = getMatchId(userId, likedUserId)
//
//            // Create a new match in the database
//            val matchRef = FirebaseDatabase.getInstance().getReference("matches/$matchId")
//            val matchData = RealtimeDBMatchProperties.toData(likedUserId, userId)
//            matchRef.setValue(matchData)
//            /**
//             * //THIS IS MY ADDITION
//             */
//            val matchSet = matchRef.child(likedUserId)
//            matchSet.setValue(setMatch(likedUserId))
//            val userSet = matchRef.child(userId)
//            userSet.setValue(setMatch(userId))
//            /**
//             * TO HERE I dunno if this is needed
//             */
//            // Retrieve the match data and return
//            val matchSnapshot = matchRef.get().await()
//            return matchSnapshot.getValue(RealtimeDBMatch::class.java)
//        }
//        return null
//    }