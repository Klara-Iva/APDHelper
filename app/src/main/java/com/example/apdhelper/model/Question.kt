package com.example.apdhelper.model

data class Question(
    val label: String,
    val category: String,
    val tag: String,
    val isNumeric: Boolean = false,
    val options: List<String> = emptyList(),
    val displayOptions: List<String> = options
)