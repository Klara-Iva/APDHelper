package com.example.apdhelper

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apdhelper.ui.theme.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddNewNoteScreen(navController: NavController) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid
    val db = FirebaseFirestore.getInstance()

    val date = remember {
        SimpleDateFormat("dd. MM. yyyy. HH:mm:ss", Locale.getDefault()).format(Date())
    }

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf(5f) }
    var anxiety by remember { mutableStateOf(5f) }
    var triggers by remember { mutableStateOf(setOf<String>()) }
    var tags by remember { mutableStateOf(setOf<String>()) }

    var triggerInput by remember { mutableStateOf("") }
    var tagInput by remember { mutableStateOf("") }

    val commonTriggers = listOf(
        "Work stress",
        "Social situations",
        "Health concerns",
        "Financial worry",
        "Relationship issues",
        "Sleep problems",
        "Crowds",
        "Public speaking",
        "Decision making",
        "Change/uncertainty"
    )

    val suggestedTags = listOf(
        "Panic attack",
        "Breakthrough",
        "Gratitude",
        "Coping strategy",
        "Therapy session",
        "Medication",
        "Exercise",
        "Meditation",
        "Sleep",
        "Daily reflection"
    )

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("New Note", color = MaterialTheme.colorScheme.onPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            actions = {
                TextButton(onClick = {
                    if (uid != null) {
                        val note = Note(
                            title = title,
                            content = content,
                            date = date,
                            moodLevel = mood.toInt(),
                            anxietyLevel = anxiety.toInt(),
                            triggers = triggers.toList(),
                            tags = tags.toList()
                        )
                        db.collection("users").document(uid).collection("notes").add(note)
                            .addOnSuccessListener { navController.popBackStack() }
                    }
                }) {
                    Text("Save", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp)
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            val formattedDate = remember {
                SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    )
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📅 $formattedDate",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
            }

            RoundedSection {
                Text("What's on your mind? ☁️", color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Give your note a title...", fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }

            RoundedSection {
                Text("Share your thoughts 📝", color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    shape = RoundedCornerShape(12.dp),
                    placeholder = {
                        Text(
                            "Write about your feelings, experiences, or anything that comes to mind. This is your safe space...",
                            fontSize = 12.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            }

            RoundedSection {
                Text(
                    "🙂 Overall Mood: ${mood.toInt()}/10", color = MaterialTheme.colorScheme.primary
                )
                Slider(
                    value = mood,
                    onValueChange = { mood = it },
                    valueRange = 1f..10f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            RoundedSection {
                Text(
                    "😵 Anxiety Level: ${anxiety.toInt()}/10",
                    color = MaterialTheme.colorScheme.primary
                )
                Slider(
                    value = anxiety,
                    onValueChange = { anxiety = it },
                    valueRange = 1f..10f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            RoundedSection {
                Text("⚡ What triggered these feelings?", color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))

                Row {
                    OutlinedTextField(value = triggerInput,
                        onValueChange = { triggerInput = it },
                        placeholder = { Text("Add a custom trigger...", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val input = triggerInput.trim()
                        if (input.isNotEmpty() && input !in triggers) {
                            triggers = triggers + input
                            triggerInput = ""
                        }
                    }) {
                        Text("Add")
                    }
                }

                Spacer(Modifier.height(12.dp))

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    commonTriggers.forEach { trigger ->
                        val isSelected = trigger in triggers
                        AssistChip(onClick = {
                            triggers = if (isSelected) triggers - trigger else triggers + trigger
                        },
                            label = { Text(trigger) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.2f
                                ) else Color.Transparent,
                                labelColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }
            }

            RoundedSection {
                Text("🏷 Add tags to organize your notes", color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))

                Row {
                    OutlinedTextField(value = tagInput,
                        onValueChange = { tagInput = it },
                        placeholder = { Text("Add a custom tag...", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val input = tagInput.trim()
                        if (input.isNotEmpty() && input !in tags) {
                            tags = tags + input
                            tagInput = ""
                        }
                    }) {
                        Text("Add")
                    }
                }

                Spacer(Modifier.height(12.dp))

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    suggestedTags.forEach { tag ->
                        val isSelected = tag in tags
                        AssistChip(onClick = {
                            tags = if (isSelected) tags - tag else tags + tag
                        }, label = { Text(tag) }, colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.2f
                            ) else Color.Transparent,
                            labelColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        ), border = BorderStroke(
                            width = 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                        )
                    }
                }
            }

            RoundedSection {
                Text(
                    "💜 Remember, every feeling is valid. You're being so brave by taking time to reflect and care for yourself.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun RoundedSection(content: @Composable ColumnScope.() -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = themedGradientBrush(), shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        content = content
    )
}
