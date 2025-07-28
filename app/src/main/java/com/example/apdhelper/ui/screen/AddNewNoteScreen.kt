package com.example.apdhelper.ui.screen.notes

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apdhelper.ui.theme.themedGradientBrush
import com.example.apdhelper.viewmodel.AddNewNoteViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddNewNoteScreen(navController: NavController) {
    val viewModel: AddNewNoteViewModel = viewModel()
    val context = LocalContext.current

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

                    viewModel.saveNote(onSuccess = { navController.popBackStack() },
                        onError = { message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        })

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
                    text = "📅 ${viewModel.formattedDate}",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                )
            }

            RoundedSection {
                Text("What's on your mind? ☁️", color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(value = viewModel.title,
                    onValueChange = { viewModel.title = it },
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
                    value = viewModel.content,
                    onValueChange = { viewModel.content = it },
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
                    "🙂 Overall Mood: ${viewModel.mood.toInt()}/10",
                    color = MaterialTheme.colorScheme.primary
                )
                Slider(
                    value = viewModel.mood,
                    onValueChange = { viewModel.mood = it },
                    valueRange = 1f..10f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            RoundedSection {
                Text(
                    "😵 Anxiety Level: ${viewModel.anxiety.toInt()}/10",
                    color = MaterialTheme.colorScheme.primary
                )
                Slider(
                    value = viewModel.anxiety,
                    onValueChange = { viewModel.anxiety = it },
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
                    OutlinedTextField(
                        value = viewModel.triggerInput,
                        onValueChange = { viewModel.triggerInput = it },
                        placeholder = { Text("Add a custom trigger...", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { viewModel.addTrigger() }) {
                        Text("Add")
                    }
                }

                Spacer(Modifier.height(12.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    viewModel.commonTriggers.forEach { trigger ->
                        val isSelected = trigger in viewModel.triggers
                        AssistChip(onClick = { viewModel.toggleTrigger(trigger) },
                            label = { Text(trigger) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(
                                    0.2f
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
                    OutlinedTextField(value = viewModel.tagInput,
                        onValueChange = { viewModel.tagInput = it },
                        placeholder = { Text("Add a custom tag...", fontSize = 12.sp) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = { viewModel.addTag() }) {
                        Text("Add")
                    }
                }

                Spacer(Modifier.height(12.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    viewModel.suggestedTags.forEach { tag ->
                        val isSelected = tag in viewModel.tags
                        AssistChip(onClick = { viewModel.toggleTag(tag) },
                            label = { Text(tag) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.secondary.copy(
                                    0.2f
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
