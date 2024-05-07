package com.threegroup.tobedated.shareclasses

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.threegroup.tobedated.RealtimeDBMatch
import com.threegroup.tobedated.shareclasses.models.Match
import com.threegroup.tobedated.shareclasses.models.MatchedUserModel
import com.threegroup.tobedated.shareclasses.models.MessageModel
import com.threegroup.tobedated.shareclasses.models.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

class Repository(
    private var firebaseDataSource: FirebaseDataSource,
) {
    fun getCurrentUserSenderId(): String {
        return firebaseDataSource.getCurrentUserSenderId()
    }
    suspend fun likeOrPass(userId: String, likedUserId: String, isLike: Boolean, inOther:String=""): RealtimeDBMatch? {
        return firebaseDataSource.likeOrPass(userId, likedUserId, isLike, inOther)
    }
    suspend fun markMatchAsViewed(matchId: String, userId: String, inOther:String="") {
        firebaseDataSource.markMatchAsViewed(matchId, userId, inOther)
    }
    suspend fun getMatchesFlow(userId: String, inOther:String=""): Flow<List<RealtimeDBMatch>> {
        return firebaseDataSource.getMatchesFlow(userId, inOther)
    }

    suspend fun getPotentialUserData(): Flow<List<MatchedUserModel>> {
        return firebaseDataSource.getPotentialUserData()
    }
    suspend fun getPotentialUserDataC(): Flow<List<MatchedUserModel>> {
        return firebaseDataSource.getPotentialUserDataC()
    }

    fun getChatData(chatId: String?, inOther:String=""): Flow<List<MessageModel>> =
        firebaseDataSource.getChatData(chatId, inOther).map { list ->
            val currUser = FirebaseAuth.getInstance().currentUser?.phoneNumber.orEmpty() // null check
            list.mapNotNull { messageModel ->
                try {
                    MessageModel(
                        senderId = messageModel.senderId,
                        message = messageModel.message,
                        currentTime = messageModel.currentTime,
                        currentDate = messageModel.currentDate,
                    )
                } catch (e: Exception) {
                    null
                }
            }
        }


    fun storeChatData(chatId: String, message: String, inOther:String="") {
        return firebaseDataSource.storeChatData(chatId, message, inOther)
    }
    suspend fun openChat(chatId: String, inOther:String="") {
        firebaseDataSource.openChat(chatId, inOther)
    }

    fun checkRead(chatId: String, inOther:String="", callback: (Boolean) ->Unit) {
        firebaseDataSource.checkRead(chatId, inOther, callback)
    }

    suspend fun setUserInfo(number: String, location: String): Flow<UserModel?> {
        return firebaseDataSource.setUserInfo(number, location)
    }
    suspend fun getMatch(match: RealtimeDBMatch, userId: String, inOther:String=""): Match{
        return firebaseDataSource.getMatch(match, userId, inOther)
    }
    suspend fun setMatchInfo(number: String):Flow<MatchedUserModel?>{
        return firebaseDataSource.setMatchedInfo(number)
    }

    suspend fun deleteProfile(number: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit, inOther:String="") {
        firebaseDataSource.deleteProfile(number, onSuccess, onFailure, inOther)
    }
    suspend fun reportUser(reportedUserId: String, reportingUserId: String, inOther:String="") {
        firebaseDataSource.reportUser(reportedUserId,reportingUserId, inOther)
    }
    suspend fun blockUser(blockedUserId: String, blockingUserId: String, inOther:String=""){
        firebaseDataSource.blockUser(blockedUserId, blockingUserId, inOther)
    }
    suspend fun deleteMatch(matchedUser:String, userId:String, inOther:String=""){
        firebaseDataSource.deleteMatch(matchedUser, userId, inOther)
    }
    suspend fun getWord(): JSONObject?{
        return firebaseDataSource.getWord()
    }
    suspend fun getHoroscope(sign:String): JSONObject?{
        return firebaseDataSource.getHoroscope(sign)
    }
    fun getLikes(userId: String, onComplete: (Int) -> Unit, inOther:String="") {
        firebaseDataSource.getLikes(userId, onComplete, inOther)
    }

    fun getPasses(userId: String, onComplete: (Int) -> Unit, inOther:String="") {
        firebaseDataSource.getPasses(userId, onComplete, inOther)
    }
    fun getLikedAndPassedby(userId: String, onComplete: (Int) -> Unit, inOther:String=""){
        firebaseDataSource.getLikedAndPassedby(userId, onComplete, inOther)
    }
    fun getPoem():  Flow<JSONArray>{
        return firebaseDataSource.getPoem()
    }
    fun suggest(currentPotential:String, suggestion:String, inOther:String=""){
        firebaseDataSource.suggest(currentPotential, suggestion, inOther)
    }
    fun getSuggestion(currentUser: String, onComplete: (List<String>) -> Unit, inOther:String=""){
        firebaseDataSource.getSuggestions(currentUser, onComplete, inOther)
    }
    fun updateNotificationCounts(callback: (totalNotificationCount: Int) -> Unit, inOther:String="") {
        var totalNotificationCount = 0
        var countUpdated = 0

        updateNewMatchesCount( { newMatchesCount ->
            totalNotificationCount += newMatchesCount
            countUpdated++
            checkCountsAndUpdate(totalNotificationCount, countUpdated, callback)
        }, inOther)

        updateNewChatsCount( { newChatsCount ->
            totalNotificationCount += newChatsCount
            countUpdated++
            checkCountsAndUpdate(totalNotificationCount, countUpdated, callback)
        }, inOther)
    }
    private fun updateNewMatchesCount(callback: (totalNotificationCount: Int) -> Unit, inOther: String) {
        firebaseDataSource.updateNewMatchesCount(callback, inOther)
    }
    private fun updateNewChatsCount(callback: (totalNotificationCount: Int) -> Unit, inOther: String) {
        firebaseDataSource.updateNewChatsCount(callback, inOther)
    }

    // Function to check if all counts have been updated and then call the callback
    private fun checkCountsAndUpdate(
        totalNotificationCount: Int,
        countUpdated: Int,
        callback: (totalNotificationCount: Int) -> Unit
    ) {
        // If both counts have been updated, call the callback
        if (countUpdated == 2) {
            callback(totalNotificationCount)
            Log.d("UPDATE_TAG", "Total notification count = $totalNotificationCount")
        }
    }
}
