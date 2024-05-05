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
    private var firebaseDataSource: FirebaseDataSource
) {
    fun getCurrentUserSenderId(): String {
        return firebaseDataSource.getCurrentUserSenderId()
    }
    suspend fun likeOrPass(userId: String, likedUserId: String, isLike: Boolean): RealtimeDBMatch? {
        return firebaseDataSource.likeOrPass(userId, likedUserId, isLike)
    }
    suspend fun markMatchAsViewed(matchId: String, userId: String) {
        firebaseDataSource.markMatchAsViewed(matchId, userId)
    }
    suspend fun getMatchesFlow(userId: String): Flow<List<RealtimeDBMatch>> {
        return firebaseDataSource.getMatchesFlow(userId)
    }
    suspend fun getPotentialUserData(): Flow<List<MatchedUserModel>> {
        return firebaseDataSource.getPotentialUserData()
    }

    fun getChatData(chatId: String?): Flow<List<MessageModel>> =
        firebaseDataSource.getChatData(chatId).map { list ->
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

    fun storeChatData(chatId: String, message: String) {
        return firebaseDataSource.storeChatData(chatId, message)
    }
    suspend fun openChat(chatId: String) {
        firebaseDataSource.openChat(chatId)
    }

    fun displayChats() {
        return firebaseDataSource.displayChats()
    }

    suspend fun setUserInfo(number: String, location: String): Flow<UserModel?> {
        return firebaseDataSource.setUserInfo(number, location)
    }
    suspend fun getMatch(match: RealtimeDBMatch, userId: String): Match{
        return firebaseDataSource.getMatch(match, userId)
    }
    suspend fun setMatchInfo(number: String):Flow<MatchedUserModel?>{
        return firebaseDataSource.setMatchedInfo(number)
    }
    suspend fun deleteProfile(number: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        firebaseDataSource.deleteProfile(number, onSuccess, onFailure)
    }
    suspend fun reportUser(reportedUserId: String, reportingUserId: String) {
        firebaseDataSource.reportUser(reportedUserId,reportingUserId)
    }
    suspend fun blockUser(blockedUserId: String, blockingUserId: String){
        firebaseDataSource.blockUser(blockedUserId, blockingUserId)
    }
    suspend fun deleteMatch(matchedUser:String, userId:String){
        firebaseDataSource.deleteMatch(matchedUser, userId)
    }
    suspend fun getWord(): JSONObject?{
        return firebaseDataSource.getWord()
    }
    suspend fun getHoroscope(sign:String): JSONObject?{
        return firebaseDataSource.getHoroscope(sign)
    }
    fun getLikes(userId: String, onComplete: (Int) -> Unit) {
        firebaseDataSource.getLikes(userId, onComplete)
    }

    fun getPasses(userId: String, onComplete: (Int) -> Unit) {
        firebaseDataSource.getPasses(userId, onComplete)
    }
    fun getLikedAndPassedby(userId: String, onComplete: (Int) -> Unit){
        firebaseDataSource.getLikedAndPassedby(userId, onComplete)
    }
    fun getPoem():  Flow<JSONArray>{
        return firebaseDataSource.getPoem()
    }
    fun suggest(currentPotential:String, suggestion:String){
        firebaseDataSource.suggest(currentPotential, suggestion)
    }
    fun getSuggestion(currentUser: String, onComplete: (List<String>) -> Unit){
        firebaseDataSource.getSuggestions(currentUser, onComplete)
    }
    fun updateNotificationCounts(callback: (totalNotificationCount: Int) -> Unit) {
        var totalNotificationCount = 0
        var countUpdated = 0

        updateNewMatchesCount { newMatchesCount ->
            totalNotificationCount += newMatchesCount
            countUpdated++
            checkCountsAndUpdate(totalNotificationCount, countUpdated, callback)
        }

        updateNewChatsCount { newChatsCount ->
            totalNotificationCount += newChatsCount
            countUpdated++
            checkCountsAndUpdate(totalNotificationCount, countUpdated, callback)
        }
    }
    private fun updateNewMatchesCount(callback: NotificationCountCallback) {
        firebaseDataSource.updateNewMatchesCount(callback)
    }
    private fun updateNewChatsCount(callback: NotificationCountCallback) {
        firebaseDataSource.updateNewChatsCount(callback)
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
