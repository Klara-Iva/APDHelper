package com.example.apdhelper.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apdhelper.ml.runModel
import com.example.apdhelper.utils.mapAnswersToModelInput
import com.example.apdhelper.viewmodel.QuestionsViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuestionsScreen(navController: NavController, vm: QuestionsViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val total = vm.questions.size
    val currentQuestion = vm.currentQuestion

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart
        ) {
            TextButton(onClick = {
                vm.reset()
                navController.navigate("questionnaire") {
                    popUpTo("questions") { inclusive = true }
                }
            }) {
                Text("Cancel Test", color = MaterialTheme.colorScheme.error)
            }
        }
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
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.92f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = (vm.currentIndex + 1) / total.toFloat(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Step ${vm.currentIndex + 1} of $total",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AnimatedContent(targetState = currentQuestion, transitionSpec = {
                        slideInHorizontally { fullWidth -> vm.slideDirection * fullWidth } + fadeIn() with slideOutHorizontally { fullWidth -> -vm.slideDirection * fullWidth } + fadeOut()
                    }) { question ->

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = question.label,
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 20.dp)
                            )

                            if (question.isNumeric) {
                                OutlinedTextField(
                                    value = vm.numericAnswers[question.category] ?: "",
                                    onValueChange = {
                                        if (it.all { char -> char.isDigit() }) {
                                            vm.numericAnswers[question.category] = it
                                        }
                                    },
                                    label = { Text("Enter your age") },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(onDone = {
                                        keyboardController?.hide()
                                        if (!vm.numericAnswers[question.category].isNullOrEmpty()) {
                                            vm.nextQuestion()
                                        }
                                    }),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        if (!vm.numericAnswers[question.category].isNullOrEmpty()) {
                                            vm.nextQuestion()
                                        }
                                    },
                                    shape = RoundedCornerShape(20.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Next")
                                }
                            } else {
                                question.displayOptions.forEachIndexed { index, display ->
                                    Button(
                                        onClick = {
                                            vm.answers[question.category] = index
                                            if (vm.currentIndex < total - 1) {
                                                vm.nextQuestion()
                                            } else {
                                                val age = vm.numericAnswers["Age"]?.toFloatOrNull()
                                                    ?: 41.45f
                                                val input = mapAnswersToModelInput(
                                                    vm.answers, mapOf("Age" to age), vm.questions
                                                )
                                                val prediction = runModel(context, input)
                                                saveResult(vm, age, prediction)
                                                navController.navigate("results")
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

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (vm.currentIndex > 0) {
                            TextButton(onClick = { vm.previousQuestion() }) {
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
}


fun saveResult(vm: QuestionsViewModel, age: Float, prediction: Float) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()
    val data = mutableMapOf<String, Any>()

    vm.questions.forEach {
        if (it.isNumeric) data[it.tag] = age
        else data[it.tag] = it.options[vm.answers[it.category] ?: 0]
    }

    data["ModelPrediction"] = prediction
    data["timestamp"] = Timestamp.now()

    db.collection("users").document(uid).collection("tests").document("PanicDisorderTests")
        .collection("entries").add(data)
}
