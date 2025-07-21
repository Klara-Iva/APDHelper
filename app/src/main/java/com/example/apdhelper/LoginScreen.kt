package com.example.apdhelper

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.apdhelper.ui.theme.Background
import com.example.apdhelper.ui.theme.Primary
import com.example.apdhelper.ui.theme.Secondary
import com.example.apdhelper.ui.theme.TextPrimary
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    var showResetDialog by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }
    var resetLoading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth() .verticalScroll(
                rememberScrollState()
            )
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .size(80.dp)
                    .background(
                        color = Primary, shape = RoundedCornerShape(20.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = "Heart Outline",
                    tint = Secondary,
                    modifier = Modifier.size(50.dp)
                )
            }

            Text(
                text = "Welcome Back!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We're so glad you're here. Let's continue your wellness journey together.",
                fontSize = 14.sp,
                color = TextPrimary.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        "your.email@example.com", color = TextPrimary.copy(alpha = 0.5f)
                    )
                },
                label = { Text("Email Address", color = TextPrimary) },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, color = Secondary, shape = RoundedCornerShape(12.dp)
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = password,
                onValueChange = { password = it },
                placeholder = {
                    Text(
                        "Enter your secure password", color = TextPrimary.copy(alpha = 0.5f)
                    )
                },
                label = { Text("Password", color = TextPrimary) },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = Primary
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp, color = Secondary, shape = RoundedCornerShape(12.dp)
                    )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    loading = true
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
                            .show()
                        loading = false
                        return@Button
                    }
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        loading = false
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !loading
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        color = Background, modifier = Modifier.size(24.dp), strokeWidth = 2.dp
                    )
                } else {
                    Text(text = "Sign In Safely ✨", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                showResetDialog = true
                resetEmail = "" // inicijalno prazno
            }) {
                Text(
                    text = "Forgot your password? We'll help you reset it 🔒",
                    fontSize = 12.sp,
                    color = TextPrimary.copy(alpha = 0.7f)
                )
            }

            TextButton(onClick = {
                navController.navigate("register")
            }) {
                Text(
                    text = "New to our community? Join us today! 🌈",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )
            }
            if (showResetDialog) {
                AlertDialog(onDismissRequest = { if (!resetLoading) showResetDialog = false },
                    title = { Text("Reset Password") },
                    text = {
                        Column {
                            Text("Please enter your email to receive a password reset link.")
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = resetEmail,
                                onValueChange = { resetEmail = it },
                                singleLine = true,
                                placeholder = { Text("your.email@example.com") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
                                ),
                                enabled = !resetLoading,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (resetEmail.isBlank()) {
                                    Toast.makeText(
                                        context, "Please enter your email", Toast.LENGTH_SHORT
                                    ).show()
                                    return@TextButton
                                }
                                resetLoading = true
                                auth.sendPasswordResetEmail(resetEmail)
                                    .addOnCompleteListener { task ->
                                        resetLoading = false
                                        if (task.isSuccessful) {
                                            Toast.makeText(
                                                context,
                                                "Reset email sent to $resetEmail",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            showResetDialog = false
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Failed to send reset email: ${task.exception?.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                            }, enabled = !resetLoading
                        ) {
                            if (resetLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp), strokeWidth = 2.dp
                                )
                            } else {
                                Text("Send")
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { if (!resetLoading) showResetDialog = false }) {
                            Text("Cancel")
                        }
                    })
            }
        }
    }
}
