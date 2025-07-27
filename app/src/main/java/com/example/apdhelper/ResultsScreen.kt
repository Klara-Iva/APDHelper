package com.example.apdhelper

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apdhelper.api.OpenAIService

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ResultsScreen(navController: NavController) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val scope = rememberCoroutineScope()
    val db = FirebaseFirestore.getInstance()

    var loading by remember { mutableStateOf(true) }
    var modelPrediction by remember { mutableStateOf<Float?>(null) }
    var recommendation by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (uid == null) {
            error = "User not logged in"
            loading = false
            return@LaunchedEffect
        }

        db.collection("users").document(uid).collection("tests").document("PD Answers").get()
            .addOnSuccessListener { doc ->
                val data = doc.data
                if (data != null && data.containsKey("ModelPrediction")) {
                    modelPrediction = (data["ModelPrediction"] as? Number)?.toFloat()
                    val answers = data.filterKeys { it != "ModelPrediction" && it != "timestamp" }
                    val prompt = buildPrompt(answers, modelPrediction!!)

                    scope.launch {
                        try {
                            val result = OpenAIService.getRecommendations(prompt)
                            recommendation = result

                            val insight = hashMapOf(
                                "recommendation" to result,
                                "prediction" to modelPrediction,
                                "timestamp" to com.google.firebase.Timestamp.now(),

                                )

                            db.collection("users").document(uid).collection("insights").add(insight)

                        } catch (e: Exception) {
                            error = "Failed to get response: ${e.localizedMessage}"
                        } finally {
                            loading = false
                        }
                    }
                } else {
                    error = "No prediction found."
                    loading = false
                }
            }.addOnFailureListener {
                error = "Error loading results: ${it.localizedMessage}"
                loading = false
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp), contentAlignment = Alignment.Center
    ) {
        when {
            loading -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }

            error != null -> {
                Text(text = error!!, color = Color.Red, fontSize = 16.sp)
            }

            else -> {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Your Personalized Insights 💡",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    modelPrediction?.let {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "📊 Risk Level",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${(it * 100).toInt()}% likelihood of panic disorder",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    if (recommendation != null) {
                        recommendation!!.split("\n\n").forEach { section ->
                            if (section.isNotBlank()) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = section.trim(),
                                            fontSize = 14.sp,
                                            lineHeight = 20.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { navController.navigate("home") },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Back to Home", color = Color.White)
                    }
                }
            }
        }
    }
}


fun buildPrompt(answers: Map<String, Any>, prediction: Float): String {
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
