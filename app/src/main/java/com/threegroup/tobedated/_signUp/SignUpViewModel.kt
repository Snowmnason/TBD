package com.threegroup.tobedated._signUp

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.threegroup.tobedated.MainActivity
import com.threegroup.tobedated.MyApp
import com.threegroup.tobedated.shareclasses.Repository
import com.threegroup.tobedated.shareclasses.models.PreferenceIndexModel
import com.threegroup.tobedated.shareclasses.models.UserModel
import com.threegroup.tobedated.shareclasses.models.ourTestQuestions
import com.threegroup.tobedated.shareclasses.storeImageAttempt
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignUpViewModel(private var repository: Repository) : ViewModel() {
    private var newUser = UserModel()
    private var newUserIndex = PreferenceIndexModel()
    private var answerListMBTI = Array(ourTestQuestions.size) { -1 }
    private var answerListOurTest = Array(ourTestQuestions.size) { -1 }

    fun getOurTest(): Array<Int> {
        return answerListOurTest
    }

    fun getMbti(): Array<Int> {
        return answerListMBTI
    }

    fun setMbti(index: Int, value: Int) {
        answerListMBTI[index] = value
    }

    fun setOurTest(index: Int, value: Int) {
        answerListOurTest[index] = value
    }

    fun setUser(answer: String, value: Any) {
        when (answer) {
            "name" -> newUser.name = value.toString()
            "birth" -> newUser.birthday = value.toString()
            "pronoun" -> newUser.pronoun = value.toString()
            "gender" -> newUser.gender = value.toString()
            "height" -> newUser.height = value.toString()
            "ethnicity" -> newUser.ethnicity = value.toString()
            "star" -> newUser.star = value.toString()
            "sexOrientation" -> newUser.sexOrientation = value.toString()
            "seeking" -> newUser.seeking = value.toString()
            "sex" -> newUser.sex = value.toString()
            "testResultsMbti" -> newUser.testResultsMbti = value.toString()
            "testResultTbd" -> newUser.testResultTbd = value as Int
            "children" -> newUser.children = value.toString()
            "family" -> newUser.family = value.toString()
            "education" -> newUser.education = value.toString()
            "religion" -> newUser.religion = value.toString()
            "politics" -> newUser.politics = value.toString()
            "relationship" -> newUser.relationship = value.toString()
            "intentions" -> newUser.intentions = value.toString()
            "drink" -> newUser.drink = value.toString()
            "smoke" -> newUser.smoke = value.toString()
            "weed" -> newUser.weed = value.toString()
            "meetUp" -> newUser.meetUp = value.toString()
            "promptQ1" -> newUser.promptQ1 = value.toString()
            "promptA1" -> newUser.promptA1 = value.toString()
            "promptQ2" -> newUser.promptQ2 = value.toString()
            "promptA2" -> newUser.promptA2 = value.toString()
            "promptQ3" -> newUser.promptQ3 = value.toString()
            "promptA3" -> newUser.promptA3 = value.toString()
            "bio" -> newUser.bio = value.toString()
            "image1" -> newUser.image1 = value.toString()
            "image2" -> newUser.image2 = value.toString()
            "image3" -> newUser.image3 = value.toString()
            "image4" -> newUser.image4 = value.toString()
            "location" -> newUser.location = value.toString()
            "number" -> newUser.number = value.toString()
        }
    }

    fun setUserIndex(answer: String, value: Int) {
        when (answer) {
            "pronoun" -> newUserIndex.pronoun = value
            "gender" -> newUserIndex.gender = value
            "ethnicity" -> newUserIndex.ethnicity = value
            "star" -> newUserIndex.star = value
            "sexOrientation" -> newUserIndex.sexOrientation = value
            "seeking" -> newUserIndex.seeking = value
            "sex" -> newUserIndex.sex = value
            "children" -> newUserIndex.children = value
            "family" -> newUserIndex.family = value
            "education" -> newUserIndex.education = value
            "religion" -> newUserIndex.religion = value
            "politics" -> newUserIndex.politics = value
            "relationship" -> newUserIndex.relationship = value
            "intentions" -> newUserIndex.intentions = value
            "drink" -> newUserIndex.drink = value
            "smoke" -> newUserIndex.smoke = value
            "weed" -> newUserIndex.weed = value
            "meetUp" -> newUserIndex.meetUp = value
            "promptQ1" -> newUserIndex.promptQ1 = value
            "promptA1" -> newUserIndex.promptA1 = value
            "promptQ2" -> newUserIndex.promptQ2 = value
            "promptA2" -> newUserIndex.promptA2 = value
            "promptQ3" -> newUserIndex.promptQ3 = value
            "promptA3" -> newUserIndex.promptA3 = value
        }
    }

    fun getUser(): UserModel {
        return newUser
    }

    fun getUserIndex(): PreferenceIndexModel {
        return newUserIndex
    }


    private fun storeData() {
        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).setValue(newUser)
            .addOnSuccessListener {
                newUser.number
            }
    }

    private suspend fun uploadImage(contentResolver: ContentResolver) {
        viewModelScope.launch {
            runBlocking {
                newUser.image1 = storeImageAttempt(newUser.image1, contentResolver, 1, newUser.number)
                newUser.image2 = storeImageAttempt(newUser.image2, contentResolver, 2, newUser.number)
                newUser.image3 = storeImageAttempt(newUser.image3, contentResolver, 3, newUser.number)
                newUser.image4 = storeImageAttempt(newUser.image4, contentResolver, 4, newUser.number)
                storeData()
            }
        }
    }
    fun finishingUp(signUpVM: SignUpViewModel, mainActivity: MainActivity, location:String, nav:NavHostController){
        viewModelScope.launch {
            runBlocking {
                signUpVM.uploadImage(mainActivity.contentResolver)
                //mainActivity.showToast()
                mainActivity.saveTokenToSharedPreferences(signUpVM.getUser().number)
                MyApp.x.setUserInfo(signUpVM.getUser().number, location).collect { userInfo ->
                    MyApp._signedInUser.value = userInfo
                }
                nav.navigate("Dating")
                //TODO BACKSTACK
            }
        }
    }

}