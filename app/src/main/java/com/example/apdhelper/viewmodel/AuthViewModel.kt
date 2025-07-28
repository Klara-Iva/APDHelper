package com.example.apdhelper.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apdhelper.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var loading by mutableStateOf(false)

    var resetEmail by mutableStateOf("")
    var resetLoading by mutableStateOf(false)
    var showResetDialog by mutableStateOf(false)

    fun signIn(
        onSuccess: () -> Unit, onError: (String) -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            onError("Please fill in all fields")
            return
        }

        loading = true
        viewModelScope.launch {
            val result = repository.signIn(email, password)
            loading = false
            result.onSuccess { onSuccess() }.onFailure { onError(it.message ?: "Unknown error") }
        }
    }

    fun sendResetEmail(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (resetEmail.isBlank()) {
            onError("Please enter your email")
            return
        }

        resetLoading = true
        viewModelScope.launch {
            val result = repository.sendResetEmail(resetEmail)
            resetLoading = false
            result.onSuccess { onSuccess() }.onFailure { onError(it.message ?: "Unknown error") }
        }
    }
}
