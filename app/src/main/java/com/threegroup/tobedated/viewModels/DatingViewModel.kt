package com.threegroup.tobedated.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.threegroup.tobedated.Repository
import com.threegroup.tobedated.models.AgeRange
import com.threegroup.tobedated.models.UserModel
import com.threegroup.tobedated.models.UserSearchPreferenceModel

class DatingViewModel(private var repository: Repository) : ViewModel() {
    val list = ArrayList<UserModel>()
    fun getPotentialUserData(callback: (ArrayList<UserModel>) -> Unit) {
        try {
            FirebaseDatabase.getInstance().getReference("users")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (data in snapshot.children) {
                                val model = data.getValue(UserModel::class.java)
                                if (model?.number != FirebaseAuth.getInstance().currentUser?.phoneNumber) {
                                    model?.let {
                                        list.add(it)
                                    }
                                }
                            }
                        }
                        // Invoke the callback with the fetched list
                        callback(list)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("MY_DEBUGGER", "ERROR")
                        // Handle error, maybe invoke the callback with an empty list
                        callback(ArrayList())
                    }
                })
        } catch (e: Exception) {
            Log.d("MY_DEBUGGER", "ERROR")
            // Handle error, maybe invoke the callback with an empty list
            callback(ArrayList())
        }
    }

    fun getNextPotential(currentProfileIndex:Int):UserModel{
        val user:UserModel = if (currentProfileIndex < list.size - 1) {
            list[currentProfileIndex]
        } else {
            UserModel()
        }
        return user
    }

    fun likedCurrentPotential(currentProfileIndex:Int, currentPotential:UserModel):UserModel{

        return getNextPotential(currentProfileIndex)
    }
    fun passedCurrentPotential(currentProfileIndex:Int,currentPotential:UserModel):UserModel{

        return getNextPotential(currentProfileIndex)
    }
    fun reportedCurrentPotential(currentProfileIndex:Int,currentPotential:UserModel):UserModel{

        return getNextPotential(currentProfileIndex)
    }
    //    private val _signedInUser = MutableStateFlow(UserModel())
//    val signedInUser: StateFlow<UserModel> = _signedInUser


    private var signedInUser:UserModel = UserModel() //= repository.getUser()

    fun getUser(): UserModel {
        return signedInUser
    }


    fun updateUser(updatedUser: UserModel) {
        val userPhoneNumber = updatedUser.number
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber)
        databaseReference.setValue(updatedUser)
        signedInUser = updatedUser
    }


    fun setLoggedInUser(userPhoneNumber: String, location:String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userPhoneNumber)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val userDataMap = snapshot.value as? Map<*, *>
                    if (userDataMap != null) {
                        signedInUser.name = userDataMap["name"] as? String ?: ""
                        signedInUser.birthday = userDataMap["birthday"] as? String ?: ""
                        signedInUser.pronoun = userDataMap["pronoun"] as? String ?: ""
                        signedInUser.gender = userDataMap["gender"] as? String ?: ""
                        signedInUser.height = userDataMap["height"] as? String ?: ""
                        signedInUser.ethnicity = userDataMap["ethnicity"] as? String ?: ""
                        signedInUser.star = userDataMap["star"] as? String ?: ""
                        signedInUser.sexOrientation = userDataMap["sexOrientation"] as? String ?: ""
                        signedInUser.seeking = userDataMap["seeking"] as? String ?: ""
                        signedInUser.sex = userDataMap["sex"] as? String ?: ""
                        signedInUser.testResultsMbti = userDataMap["testResultsMbti"] as? String ?: "Not Taken"
                        signedInUser.testResultTbd = userDataMap["testResultTbd"] as? Int ?: 10
                        signedInUser.children = userDataMap["children"] as? String ?: ""
                        signedInUser.family = userDataMap["family"] as? String ?: ""
                        signedInUser.education = userDataMap["education"] as? String ?: ""
                        signedInUser.religion = userDataMap["religion"] as? String ?: ""
                        signedInUser.politics = userDataMap["politics"] as? String ?: ""
                        signedInUser.relationship = userDataMap["relationship"] as? String ?: ""
                        signedInUser.intentions = userDataMap["intentions"] as? String ?: ""
                        signedInUser.drink = userDataMap["drink"] as? String ?: ""
                        signedInUser.smoke = userDataMap["smoke"] as? String ?: ""
                        signedInUser.weed = userDataMap["weed"] as? String ?: ""
                        signedInUser.promptQ1 = userDataMap["promptQ1"] as? String ?: ""
                        signedInUser.promptA1 = userDataMap["promptA1"] as? String ?: ""
                        signedInUser.promptQ2 = userDataMap["promptQ2"] as? String ?: ""
                        signedInUser.promptA2 = userDataMap["promptA2"] as? String ?: ""
                        signedInUser.promptQ3 = userDataMap["promptQ3"] as? String ?: ""
                        signedInUser.promptA3 = userDataMap["promptA3"] as? String ?: ""
                        signedInUser.bio = userDataMap["bio"] as? String ?: ""
                        signedInUser.image1 = userDataMap["image1"] as? String ?: ""
                        signedInUser.image2 = userDataMap["image2"] as? String ?: ""
                        signedInUser.image3 = userDataMap["image3"] as? String ?: ""
                        signedInUser.image4 = userDataMap["image4"] as? String ?: ""
                        signedInUser.location = if (location == "error/" || location == "/") {
                            // If location is 'error', retrieve location from Firebase
                            userDataMap["location"] as? String ?: ""
                        } else {
                            // Otherwise, use the provided location value
                            location
                        }
                        signedInUser.status = userDataMap["status"] as? String ?: ""
                        signedInUser.number = userDataMap["number"] as? String ?: ""
                        signedInUser.verified = userDataMap["verified"] as? Boolean ?: false
                        signedInUser.userPref = (userDataMap["userPref"] as? Map<*, *>)?.let { map ->
                            UserSearchPreferenceModel(
                                ageRange = map["ageRange"] as? AgeRange ?: AgeRange(18,35),
                                maxDistance = map["maxDistance"] as? Int ?: 25,
                                gender = (map["gender"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                zodiac = (map["zodiac"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                sexualOri = (map["sexualOri"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                mbti = (map["mbti"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                children = (map["children"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                familyPlans = (map["familyPlans"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                education = (map["education"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                religion = (map["religion"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                politicalViews = (map["politicalViews"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                relationshipType = (map["relationshipType"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                intentions = (map["intentions"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                drink = (map["drink"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                smoke = (map["smoke"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                                weed = (map["weed"] as? List<*>)?.filterIsInstance<String>() ?: listOf("Doesn't Matter"),
                            )
                        } ?: UserSearchPreferenceModel()
                    } else {
                        // Handle null user data
                    }
                } else {
                    // User not found in the database
                    // Handle accordingly
                }
            }
            override fun onCancelled(error: DatabaseError) {
                println("onCancelled triggered")
                // Handle error fetching user data
                println("Error: ${error.message}")
            }
        })
    }
}