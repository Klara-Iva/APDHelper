package com.example.apdhelper.model

data class Note(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val date: String = "",
    val moodLevel: Int = 0,
    val anxietyLevel: Int = 0,
    val triggers: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)