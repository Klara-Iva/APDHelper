package com.example.apdhelper

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.content.res.AssetFileDescriptor
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color


data class Question(
    val label: String,
    val category: String,
    val tag: String,
    val isNumeric: Boolean = false,
    val options: List<String> = emptyList(),
    val displayOptions: List<String> = options
)

val labelEncoders = mapOf(
    "Gender" to mapOf("Female" to 0f, "Male" to 1f),
    "Family History" to mapOf("No" to 0f, "Yes" to 1f),
    "Personal History" to mapOf("No" to 0f, "Yes" to 1f),
    "Current Stressors" to mapOf("High" to 0f, "Low" to 1f, "Moderate" to 2f),
    "Symptoms" to mapOf(
        "Chest pain" to 0f,
        "Dizziness" to 1f,
        "Fear of losing control" to 2f,
        "Panic attacks" to 3f,
        "Shortness of breath" to 4f
    ),
    "Severity" to mapOf("Mild" to 0f, "Moderate" to 1f, "Severe" to 2f),
    "Impact on Life" to mapOf("Mild" to 0f, "Moderate" to 1f, "Significant" to 2f),
    "Demographics" to mapOf("Rural" to 0f, "Urban" to 1f),
    "Medical History" to mapOf(
        "Asthma" to 0f, "Diabetes" to 1f, "Heart disease" to 2f, "Unknown" to 3f
    ),
    "Psychiatric History" to mapOf(
        "Anxiety disorder" to 0f,
        "Bipolar disorder" to 1f,
        "Depressive disorder" to 2f,
        "Unknown" to 3f
    ),
    "Substance Use" to mapOf("Alcohol" to 0f, "Drugs" to 1f, "Unknown" to 2f),
    "Coping Mechanisms" to mapOf(
        "Exercise" to 0f, "Meditation" to 1f, "Seeking therapy" to 2f, "Socializing" to 3f
    ),
    "Social Support" to mapOf("High" to 0f, "Low" to 1f, "Moderate" to 2f),
    "Lifestyle Factors" to mapOf("Diet" to 0f, "Exercise" to 1f, "Sleep quality" to 2f)
)

val featureMeans = mapOf(
    "Age" to 41.4543f,
    "Gender" to 0.50052f,
    "Family History" to 0.50042f,
    "Personal History" to 0.4979f,
    "Current Stressors" to 0.99853f,
    "Symptoms" to 1.99758f,
    "Severity" to 1.00145f,
    "Impact on Life" to 0.99763f,
    "Demographics" to 0.49913f,
    "Medical History" to 1.50579f,
    "Psychiatric History" to 1.49813f,
    "Substance Use" to 0.99929f,
    "Coping Mechanisms" to 1.50478f,
    "Social Support" to 0.999f,
    "Lifestyle Factors" to 0.99741f
)

val featureStds = mapOf(
    "Age" to 13.83913478f,
    "Gender" to 0.49999973f,
    "Family History" to 0.49999982f,
    "Personal History" to 0.49999559f,
    "Current Stressors" to 0.81603176f,
    "Symptoms" to 1.41169903f,
    "Severity" to 0.81777008f,
    "Impact on Life" to 0.81649518f,
    "Demographics" to 0.49999924f,
    "Medical History" to 1.11741956f,
    "Psychiatric History" to 1.11754933f,
    "Substance Use" to 0.81742859f,
    "Coping Mechanisms" to 1.1183636f,
    "Social Support" to 0.81734876f,
    "Lifestyle Factors" to 0.81487624f
)

fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
    val fileDescriptor: AssetFileDescriptor = context.assets.openFd(modelName)
    val inputStream = fileDescriptor.createInputStream()
    val fileChannel = inputStream.channel
    val startOffset = fileDescriptor.startOffset
    val declaredLength = fileDescriptor.declaredLength
    return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
}

fun runModel(context: Context, inputData: FloatArray): Float {
    Interpreter(loadModelFile(context, "model.tflite")).use { interpreter ->
        val output = Array(1) { FloatArray(1) }
        interpreter.run(inputData, output)
        return output[0][0]
    }
}

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


fun getQuestions(): List<Question> = listOf(

    Question("How old are you? 👶", "Age", "Age", isNumeric = true), Question(
        "How do you identify? 🌈",
        "Gender",
        "Gender",
        options = listOf("Female", "Male"),
        displayOptions = listOf("I’m a woman 👧", "I’m a man 👦")
    ), Question(
        "Any family history of anxiety? 🏟️",
        "Family History",
        "Family History",
        options = listOf("No", "Yes"),
        displayOptions = listOf("Nope, none that I know 😊", "Yes, someone in my family")
    ), Question(
        "Have you experienced anxiety before? 😓",
        "Personal History",
        "Personal History",
        options = listOf("No", "Yes"),
        displayOptions = listOf("Never really", "Yes, I have")
    ), Question(
        "How intense is your current stress? 🌧️",
        "Current Stressors",
        "Current Stressors",
        options = listOf("High", "Low", "Moderate"),
        displayOptions = listOf("Very intense 😬", "Pretty calm 🌼", "Somewhere in between")
    ), Question(
        "Which of these symptoms have you noticed lately? 🤔",
        "Symptoms",
        "Symptoms",
        options = listOf(
            "Chest pain",
            "Dizziness",
            "Fear of losing control",
            "Panic attacks",
            "Shortness of breath"
        ),
        displayOptions = listOf(
            "Tight chest", "Feeling dizzy", "Fear of losing it", "Panic episodes", "Hard to breathe"
        )
    ), Question(
        "How would you rate the severity of your symptoms? 🌌",
        "Severity",
        "Severity",
        options = listOf("Mild", "Moderate", "Severe"),
        displayOptions = listOf("Mild 😐", "Moderate 😕", "Severe 😫")
    ), Question(
        "How much do your symptoms affect your daily life? 📚",
        "Impact on Life",
        "Impact on Life",
        options = listOf("Mild", "Moderate", "Significant"),
        displayOptions = listOf("Barely noticeable", "Manageable", "Quite a lot")
    ), Question(
        "Where do you live? 🌆",
        "Demographics",
        "Demographics",
        options = listOf("Rural", "Urban"),
        displayOptions = listOf("Countryside 🌿", "City life 🌇")
    ), Question(
        "Any history of physical health conditions? 🏥",
        "Medical History",
        "Medical History",
        options = listOf("Asthma", "Diabetes", "Heart disease", "Unknown"),
        displayOptions = listOf("Asthma", "Diabetes", "Heart condition", "Not sure")
    ), Question(
        "Have you been diagnosed with any mental health conditions? 🤦",
        "Psychiatric History",
        "Psychiatric History",
        options = listOf("Anxiety disorder", "Bipolar disorder", "Depressive disorder", "Unknown"),
        displayOptions = listOf("Anxiety", "Bipolar", "Depression", "Not diagnosed")
    ), Question(
        "Do you use any substances? 🍻",
        "Substance Use",
        "Substance Use",
        options = listOf("Alcohol", "Drugs", "Unknown"),
        displayOptions = listOf("Alcohol", "Other drugs", "Neither")
    ), Question(
        "How do you usually cope with stress? 🧘",
        "Coping Mechanisms",
        "Coping Mechanisms",
        options = listOf("Exercise", "Meditation", "Seeking therapy", "Socializing"),
        displayOptions = listOf(
            "I workout 🏋", "I meditate 😴", "I talk to a therapist 💬", "I hang out with friends 🌞"
        )
    ), Question(
        "How would you rate your social support? 👬",
        "Social Support",
        "Social Support",
        options = listOf("Low", "Moderate", "High"),
        displayOptions = listOf("Not really", "Somewhat", "Very supportive 💕")
    ), Question(
        "What’s your current lifestyle like? 🥗",
        "Lifestyle Factors",
        "Lifestyle Factors",
        options = listOf("Diet", "Exercise", "Sleep quality"),
        displayOptions = listOf("Focused on eating well", "I stay active", "Sleep matters to me")
    )
)


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Questions(navController: NavController) {
    val questions = getQuestions()
    val answers = remember { mutableStateMapOf<String, Int>() }
    val numericAnswers = remember { mutableStateMapOf<String, String>() }
    val currentIndex = remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val showConfetti = remember { mutableStateOf(false) }

    val total = questions.size
    val currentQuestion = questions[currentIndex.value]

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(
                        alpha = 0.92f
                    )
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LinearProgressIndicator(
                        progress = (currentIndex.value + 1) / total.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Step ${currentIndex.value + 1} of $total",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AnimatedContent(targetState = currentQuestion, transitionSpec = {
                        slideInHorizontally { fullWidth -> fullWidth } + fadeIn() with slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
                    }) { question ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = question.label,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            if (question.isNumeric) {
                                OutlinedTextField(value = numericAnswers[question.category] ?: "",
                                    onValueChange = {
                                        if (it.all { char -> char.isDigit() }) {
                                            numericAnswers[question.category] = it
                                        }
                                    },
                                    label = { Text("Enter your age") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                        keyboardController?.hide()
                                        if (!numericAnswers[question.category].isNullOrEmpty()) {
                                            if (currentIndex.value < total - 1) {
                                                currentIndex.value++
                                            }
                                        }
                                    }),
                                    modifier = Modifier.fillMaxWidth())
                            } else {
                                question.displayOptions.forEachIndexed { index, display ->
                                    Button(
                                        onClick = {
                                            answers[question.category] = index

                                            if (currentIndex.value < total - 1) {
                                                currentIndex.value++
                                            } else {
                                                val age = numericAnswers["Age"]?.toFloatOrNull()
                                                    ?: featureMeans["Age"] ?: 41.45f
                                                val input = mapAnswersToModelInput(
                                                    answers, mapOf("Age" to age), questions
                                                )
                                                val prediction = runModel(context, input)
                                                val uid =
                                                    FirebaseAuth.getInstance().currentUser?.uid
                                                        ?: return@Button
                                                val db = FirebaseFirestore.getInstance()
                                                val data = mutableMapOf<String, Any>()

                                                questions.forEach {
                                                    if (it.isNumeric) {
                                                        data[it.tag] = age
                                                    } else {
                                                        val selected =
                                                            answers[it.category] ?: return@forEach
                                                        data[it.tag] = it.options[selected]
                                                    }
                                                }
                                                data["ModelPrediction"] = prediction
                                                data["timestamp"] =
                                                    com.google.firebase.Timestamp.now()

                                                db.collection("users").document(uid)
                                                    .collection("tests")
                                                    .document("PanicDisorderTests")
                                                    .collection("entries").add(data)
                                                    .addOnSuccessListener {
                                                        navController.navigate("results")
                                                    }

                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 6.dp),
                                        shape = RoundedCornerShape(18.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(
                                                alpha = 0.9f
                                            )
                                        )
                                    ) {
                                        Text(display, fontSize = 15.sp)
                                    }
                                }

                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    if (currentIndex.value > 0) {
                        TextButton(onClick = { currentIndex.value-- }) {
                            Text("Back", color = MaterialTheme.colorScheme.primary)
                        }
                    } else {
                        Spacer(modifier = Modifier)
                    }
                }
            }
        }
    }
}