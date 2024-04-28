package com.threegroup.tobedated.shareclasses

import android.content.ContentResolver
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

suspend fun storeImageAttempt(uriString: String, contentResolver: ContentResolver, imageNumber: Int, userNumber: String): String {
    var downloadUrl = ""
    try {
        val storageRef = FirebaseStorage.getInstance().reference
        val filePath = getFileFromContentUri(Uri.parse(uriString), contentResolver) ?: return ""

        // Define the image path including the user's ID and image number
        val imagePath = "$userNumber/image$imageNumber"

        // Upload the image to Firebase Storage
        val imageRef = storageRef.child(imagePath)
        val file = Uri.fromFile(File(filePath))
        val inputStream = withContext(Dispatchers.IO) {
            FileInputStream(file.path)
        }
        val uploadTask = imageRef.putStream(inputStream).await()
        downloadUrl = imageRef.downloadUrl.await().toString()

        // Store the download URL in the Firebase Realtime Database under the user's ID and image number
        val databaseRef = FirebaseDatabase.getInstance().reference
        val userImagesRef = databaseRef.child("users").child(userNumber).child("image$imageNumber")
        userImagesRef.setValue(downloadUrl)

        // Delete the local image file after successful upload
        val localFile = File(filePath)
        if (localFile.exists()) {
            val deleted = localFile.delete()
            if (!deleted) {
                Log.e("storeImageAttempt", "Failed to delete local image file: $filePath")
            }
        }
    } catch (e: Exception) {
        Log.e("storeImageAttempt", "Error uploading image: ${e.message}")
    }
    return downloadUrl
}

//suspend fun deleteImage(userNumber: String, imageNumber: Int) {
//    try {
//        val storageRef = FirebaseStorage.getInstance().reference
//        val imagePath = "users/$userNumber/images/${userNumber}${imageNumber}ProfilePhoto"
//        val imageRef = storageRef.child(imagePath)
//        imageRef.delete().await()
//    } catch (e: Exception) {
//        Log.e("deleteImage", "Error deleting image: ${e.message}")
//    }
//}


fun getFileFromContentUri(contentUri: Uri, contentResolver: ContentResolver): String? {
    var filePath: String? = null
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    contentResolver.query(contentUri, projection, null, null, null)?.use { cursor ->
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        filePath = cursor.getString(columnIndex)
    }
    return filePath
}
fun stringToDate(dateString: String, format: String): Date {
    val formatter = DateTimeFormatter.ofPattern(format)
    val localDate = LocalDate.parse(dateString, formatter)
    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
}
fun calcAge(bdayString: String): Int{
    val format = "MM/dd/yyyy"
    val bdayDate = stringToDate(bdayString, format)
    val now = GregorianCalendar()
    now.time = Date()
    val birthdate = GregorianCalendar()
    birthdate.time = bdayDate
    return now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR)
}
fun calcDistance(potential:String, current:String):String{
    val distance: String
    if (current != "error/" && current != "/" && current.contains("/")){
        val potentialParts = potential.split("/")
        val currentParts = current.split("/")
        val latitudePotential = potentialParts.first().toDouble()
        val longitudePotential = potentialParts.last().toDouble()
        val latitudeCurrent = currentParts.first().toDouble()
        val longitudeCurrent = currentParts.last().toDouble()
        val earthRadius = 3958.8 // Earth radius in miles

        val latDistance = Math.toRadians(latitudePotential - latitudeCurrent)
        val lonDistance = Math.toRadians(longitudePotential - longitudeCurrent)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(latitudeCurrent)) * cos(Math.toRadians(latitudePotential)) *
                sin(lonDistance / 2) * sin(lonDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distanceInMiles = earthRadius * c

        distance = "%.0f miles".format(distanceInMiles)

    }else{
        distance = "x miles"
    }
    return distance
}
fun checkDay(month:Int, year:Int) :Int {
    val maxDays = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else -> 31
    }
    return maxDays
}
fun checkYear(year:Int, day:Int, month:Int):Int{
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonth = (Calendar.getInstance().get(Calendar.MONTH))+1
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    if(year == (currentYear - 18)){
        return if (month > currentMonth || (month == currentMonth && day >= currentDay)) {
            currentYear - 18
        }else{
            currentYear - 17
        }
    }
    return currentYear - 18
}
fun checkMonth(day:Int, year:Int):IntArray{
    if(day <= 28){
        return intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    }
    else if((year % 4 == 0 && (year % 100 != 0 || year % 400 == 0))){
        return intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    }
    else if(day <= 30){
        return intArrayOf(1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    }
    else if(day <= 31){
        return intArrayOf(1, 3, 5, 7, 8, 10, 12)
    }
    return intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
}
fun checkBirthDate(year: Int, month: Int, day: Int): Boolean {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val currentMonth = (Calendar.getInstance().get(Calendar.MONTH))+1
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    if(year == (currentYear - 18)){
        if (month > currentMonth || (month == currentMonth && day >= currentDay)) {
            return false
        }
    }
    if (year > currentYear - 18 || year < 1950) return false
    val maxDays = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        else -> return false
    }
    return day in 1..maxDays
}
fun formatPhone(phone: String, cCode:String): String {
    val digits = phone.filter { it.isDigit() }
    val formatted = buildString {
        if(cCode == "+1"){
            if (digits.isNotEmpty()) {
                append("(")
                append(digits.take(3))
                if (digits.length > 3) {
                    append(") ")
                    append(digits.substring(3, minOf(digits.length, 6)))
                }
                if (digits.length > 6) {
                    append("-")
                    append(digits.substring(6, minOf(digits.length, 10)))
                }
            }
        }else{
            append(digits.take(4))
            if (digits.length > 4) {
                append(" ")
                append(digits.substring(4))
            }
        }
    }
    return formatted
}
fun formatPhoneNumber(code: String, userPhoneNumber: String): String {
    var formattedPhoneNumber = userPhoneNumber.filter { it.isDigit() }
    formattedPhoneNumber = code + formattedPhoneNumber
    return formattedPhoneNumber
}

fun getChatId(senderId: String, receiverId: String): String {
    return if (senderId > receiverId) {
        senderId + receiverId
    } else receiverId + senderId
}