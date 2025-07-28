package com.example.apdhelper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StartViewModel : ViewModel() {

    fun onGoogleSignInClick(signIn: () -> Unit) {
        viewModelScope.launch {
            signIn()
        }
    }

    fun onLoginClick(navigateToLogin: () -> Unit) {
        navigateToLogin()
    }

    fun onRegisterClick(navigateToRegister: () -> Unit) {
        navigateToRegister()
    }
}
