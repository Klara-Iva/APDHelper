package com.example.apdhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apdhelper.api.OpenAIService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultsViewModel : ViewModel() {

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _modelPrediction = MutableStateFlow<Float?>(null)
    val modelPrediction = _modelPrediction.asStateFlow()

    private val _recommendation = MutableStateFlow<String?>(null)
    val recommendation = _recommendation.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        fetchResults()
    }

    private fun fetchResults() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            _error.value = "User not logged in"
            _loading.value = false
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(uid).collection("tests").document("PD Answers").get()
            .addOnSuccessListener { doc ->
                val data = doc.data
                if (data != null && data.containsKey("ModelPrediction")) {
                    val prediction = (data["ModelPrediction"] as? Number)?.toFloat()
                    _modelPrediction.value = prediction
                    val answers = data.filterKeys { it != "ModelPrediction" && it != "timestamp" }
                    val prompt = buildPrompt(answers, prediction!!)

                    viewModelScope.launch {
                        try {
                            val result = OpenAIService.getRecommendations(prompt)
                            _recommendation.value = result

                            val insight = hashMapOf(
                                "recommendation" to result,
                                "prediction" to prediction,
                                "timestamp" to Timestamp.now()
                            )

                            db.collection("users").document(uid).collection("insights").add(insight)
                        } catch (e: Exception) {
                            _error.value = "Failed to get response: ${e.localizedMessage}"
                        } finally {
                            _loading.value = false
                        }
                    }
                } else {
                    _error.value = "No prediction found."
                    _loading.value = false
                }
            }.addOnFailureListener {
                _error.value = "Error loading results: ${it.localizedMessage}"
                _loading.value = false
            }
    }

    private fun buildPrompt(answers: Map<String, Any>, prediction: Float): String {
        val answerList = answers.entries.joinToString("\n") { (key, value) -> "$key: $value" }
        return """
            This user completed a panic disorder screening test and got a prediction score of ${(prediction * 100).toInt()}%.

            Based on the following input:
            $answerList

            Provide a personalized, friendly report with:
            - A brief explanation of the result
            - Encouraging message
            - Practical self-care strategies for panic disorder
            - When to consider professional help

            Structure the output in short sections separated by new lines, you must use emojis or headers to guide the user visually. Do not bold anything.
        """.trimIndent()
    }
}
