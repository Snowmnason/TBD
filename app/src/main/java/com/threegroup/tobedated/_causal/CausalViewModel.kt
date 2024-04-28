package com.threegroup.tobedated._causal

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.AdditionsIndex
import com.threegroup.tobedated.shareclasses.models.CasualAdditions
import com.threegroup.tobedated.shareclasses.models.UserModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CausalViewModel(private var repository: Repository) : ViewModel() {
    private val _signedInUser = MutableStateFlow<UserModel?>(null)
    private var signedInUser: StateFlow<UserModel?> = _signedInUser

    fun getUser(): UserModel {
        return signedInUser.value!!
    }
    fun setLoggedInUser() {
        signedInUser = MyApp.signedInUser

    }

    /**
     *  FOR causal additions
     */

    private var newUserAdditionsIndex = AdditionsIndex()
    private var additions = CasualAdditions()
    fun getUserIndex(): AdditionsIndex {
        return newUserAdditionsIndex
    }
    fun getAdditions():CasualAdditions{
        return additions
    }
    fun setAdditions(answer: String, value: String){
        when (answer) {
            "leaning" -> additions.leaning = value
            "lookingFor" -> additions.lookingFor = value
            "experience" -> additions.experience = value
            "location" -> additions.location = value
            "comm" -> additions.comm = value
            "sexHealth" -> additions.sexHealth = value
            "afterCare" -> additions.afterCare = value
            "bio" -> additions.casualBio = value
            "promptQ1" -> additions.promptQ1 = value
            "promptA1" -> additions.promptA1 = value
            "promptQ2" -> additions.promptQ2 = value
            "promptA2" -> additions.promptA2 = value
            "promptQ3" -> additions.promptQ3 = value
            "promptA3" -> additions.promptA3 = value
        }
    }
    fun setUserIndex(answer: String, value: Int) {
        when (answer) {
            "leaning" -> newUserAdditionsIndex.leaning = value
            "lookingFor" -> newUserAdditionsIndex.lookingFor = value
            "experience" -> newUserAdditionsIndex.experience = value
            "location" -> newUserAdditionsIndex.location = value
            "comm" -> newUserAdditionsIndex.comm = value
            "sexHealth" -> newUserAdditionsIndex.sexHealth = value
            "afterCare" -> newUserAdditionsIndex.afterCare = value
        }
    }
    fun storeDataC() {
        val number = signedInUser.value!!.number
        val  databaseReference = FirebaseDatabase.getInstance().getReference("users").child(number)
        databaseReference.child("casualAdditions").setValue(additions)
            .addOnSuccessListener {
                databaseReference.child("hasCasual").setValue(true)
                signedInUser.value!!.casualAdditions = additions
                signedInUser.value!!.hasCasual = true
            }
    }

}