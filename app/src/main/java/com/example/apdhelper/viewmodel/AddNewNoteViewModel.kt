package com.example.apdhelper.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apdhelper.model.Note
import com.example.apdhelper.repository.NotesRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNewNoteViewModel : ViewModel() {

    private val repository = NotesRepository()

    val date: String =
        SimpleDateFormat("dd. MM. yyyy. HH:mm:ss", Locale.getDefault()).format(Date())
    val formattedDate: String =
        SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault()).format(Date())

    var title by mutableStateOf("")
    var content by mutableStateOf("")
    var mood by mutableStateOf(5f)
    var anxiety by mutableStateOf(5f)
    var triggers by mutableStateOf(setOf<String>())
    var tags by mutableStateOf(setOf<String>())

    var triggerInput by mutableStateOf("")
    var tagInput by mutableStateOf("")

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

    fun addTrigger() {
        val input = triggerInput.trim()
        if (input.isNotEmpty() && input !in triggers) {
            triggers = triggers + input
            triggerInput = ""
        }
    }

    fun toggleTrigger(trigger: String) {
        triggers = if (trigger in triggers) triggers - trigger else triggers + trigger
    }

    fun addTag() {
        val input = tagInput.trim()
        if (input.isNotEmpty() && input !in tags) {
            tags = tags + input
            tagInput = ""
        }
    }

    fun toggleTag(tag: String) {
        tags = if (tag in tags) tags - tag else tags + tag
    }

    fun saveNote(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val note = Note(
            title = title,
            content = content,
            date = date,
            moodLevel = mood.toInt(),
            anxietyLevel = anxiety.toInt(),
            triggers = triggers.toList(),
            tags = tags.toList()
        )

        viewModelScope.launch {
            try {
                repository.addNote(note)
                onSuccess()
            } catch (e: Exception) {
                onError(e.message ?: "Error saving note")
            }
        }
    }
}
