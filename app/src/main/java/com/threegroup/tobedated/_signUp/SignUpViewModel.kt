package com.threegroup.tobedated._signUp

import androidx.lifecycle.ViewModel
import com.threegroup.tobedated.shareclasses.Repository

class SignUpViewModel(private var repository: Repository) : ViewModel() {
     private var question1:String = "Question 1"
    private var question2:String = "Question 2"
    private var question3:String = "Question 3"
    fun setQuestion1(question:String){
        question1 = question
    }
    fun setQuestion2(question:String){
        question2 = question
    }
    fun setQuestion3(question:String){
        question3 = question
    }
    fun getQuestion1():String{
        return question1
    }
    fun getQuestion2():String{
        return question2
    }
    fun getQuestion3():String{
        return question3
    }
}

/*
    private var newUser = UserModel()
    private var newUserIndex = PreferenceIndexModel()

    fun setUser(answer:String, value:Any){
        when(answer){
            "name" -> newUser.name = value.toString()
            "birth" -> newUser.birthday = value.toString()
            "pronoun" -> newUser.pronoun = value.toString()
            "gender" -> newUser.gender         = value.toString()
            "height" -> newUser.height         = value.toString()
            "ethnicity" -> newUser.ethnicity      = value.toString()
            "star" -> newUser.star           = value.toString()
            "sexOrientation" -> newUser.sexOrientation = value.toString()
            "seeking" -> newUser.seeking        = value.toString()
            "sex" -> newUser.sex            = value.toString()
            "testResultsMbti" -> newUser.testResultsMbti= value.toString()
            "testResultTbd" -> newUser.testResultTbd  = value as Int
            "children" -> newUser.children       = value.toString()
            "family" -> newUser.family         = value.toString()
            "education" -> newUser.education      = value.toString()
            "religion" -> newUser.religion       = value.toString()
            "politics" -> newUser.politics       = value.toString()
            "relationship" -> newUser.relationship   = value.toString()
            "intentions" -> newUser.intentions     = value.toString()
            "drink" -> newUser.drink          = value.toString()
            "smoke" -> newUser.smoke          = value.toString()
            "weed" -> newUser.weed           = value.toString()
            "meetUp" -> newUser.meetUp         = value.toString()
            "promptQ1" -> newUser.promptQ1       = value.toString()
            "promptA1" -> newUser.promptA1       = value.toString()
            "promptQ2" -> newUser.promptQ2       = value.toString()
            "promptA2" -> newUser.promptA2       = value.toString()
            "promptQ3" -> newUser.promptQ3       = value.toString()
            "promptA3" -> newUser.promptA3       = value.toString()
            "bio" -> newUser.bio            = value.toString()
            "image1" -> newUser.image1         = value.toString()
            "image2" -> newUser.image2         = value.toString()
            "image3" -> newUser.image3         = value.toString()
            "image4" -> newUser.image4         = value.toString()
            "location" -> newUser.location       = value.toString()
            "number" -> newUser.number         = value.toString()
        }
    }
    fun getUser():UserModel{
        return newUser
    }
    fun setUserIndex(userIndex:PreferenceIndexModel){
        newUserIndex = userIndex
    }
    fun setUserIndex():PreferenceIndexModel{
        return newUserIndex
    }
 */