package com.example.apdhelper.utils

import com.example.apdhelper.data.featureMeans
import com.example.apdhelper.data.featureStds
import com.example.apdhelper.data.labelEncoders
import com.example.apdhelper.model.Question

fun mapAnswersToModelInput(
    answers: Map<String, Int>, numericAnswers: Map<String, Float>, questions: List<Question>
): FloatArray {
    val input = FloatArray(questions.size)
    for ((index, question) in questions.withIndex()) {
        val rawValue: Float = if (question.isNumeric) {
            numericAnswers[question.category] ?: featureMeans[question.category] ?: 0f
        } else {
            val selectedIndex = answers[question.category] ?: 0
            val option = question.options.getOrNull(selectedIndex) ?: ""
            labelEncoders[question.category]?.get(option) ?: 0f
        }
        val mean = featureMeans[question.category] ?: 0f
        val std = featureStds[question.category] ?: 1f
        input[index] = (rawValue - mean) / std
    }
    return input
}
