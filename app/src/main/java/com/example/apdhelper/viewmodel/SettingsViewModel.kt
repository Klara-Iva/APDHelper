package com.example.apdhelper.ui.screens.settings

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val showDeleteDialog: Boolean = false
)

class SettingsViewModel(private val app: Application) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun onResetPassword() {
        val email = auth.currentUser?.email
        if (!email.isNullOrBlank()) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(app, "Password reset email sent to $email", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(app, "Failed to send reset email.", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(app, "Email not available.", Toast.LENGTH_SHORT).show()
        }
    }

    fun onSignOut(navigateToStart: () -> Unit) {
        auth.signOut()
        navigateToStart()
    }

    fun showDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    fun confirmDeleteAccount(navigateToStart: () -> Unit) {
        val user = auth.currentUser
        val uid = user?.uid
        if (uid != null) {
            firestore.collection("users").document(uid).delete().addOnCompleteListener {
                user.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(app, "Account deleted", Toast.LENGTH_SHORT).show()
                        navigateToStart()
                    } else {
                        Toast.makeText(
                            app,
                            "Failed to delete user: ${task.exception?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    fun toggleDarkMode() {
        Toast.makeText(app, "Theme toggled (demo only)", Toast.LENGTH_SHORT).show()
    }
}
