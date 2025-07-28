package com.example.apdhelper.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.apdhelper.data.getQuestions
import com.example.apdhelper.model.Question

class QuestionsViewModel : ViewModel() {
    val questions = getQuestions()
    var currentIndex by mutableStateOf(0)
    var answers = mutableStateMapOf<String, Int>()
    var numericAnswers = mutableStateMapOf<String, String>()

    val currentQuestion: Question
        get() = questions[currentIndex]

    fun reset() {
        currentIndex = 0
        answers.clear()
        numericAnswers.clear()
    }

    var slideDirection by mutableStateOf(1)

    fun nextQuestion() {
        if (currentIndex < questions.lastIndex) {
            slideDirection = 1
            currentIndex++
        }
    }

    fun previousQuestion() {
        if (currentIndex > 0) {
            slideDirection = -1
            currentIndex--
        }
    }

}
