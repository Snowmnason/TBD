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