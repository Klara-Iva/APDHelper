package com.example.apdhelper.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel : ViewModel() {

    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var passwordVisible by mutableStateOf(false)
    var confirmPasswordVisible by mutableStateOf(false)
    var loading by mutableStateOf(false)

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun registerUser(context: Context, onSuccess: () -> Unit) {
        if (name.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        loading = true
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                loading = false
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""
                    val userMap = hashMapOf("name" to name, "email" to email)
                    firestore.collection("users").document(uid).set(userMap)
                        .addOnSuccessListener { onSuccess() }.addOnFailureListener {
                            Toast.makeText(context, "Failed to save user data", Toast.LENGTH_LONG)
                                .show()
                        }
                } else {
                    Toast.makeText(
                        context,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
