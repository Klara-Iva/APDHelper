package com.example.apdhelper.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apdhelper.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 30.dp)
                    .size(80.dp)
                    .background(
                        color = colorScheme.primary, shape = RoundedCornerShape(20.dp)
                    ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = colorScheme.secondary,
                    modifier = Modifier.size(50.dp)
                )
            }

            Text(
                text = "Welcome Back!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We're so glad you're here. Let's continue your wellness journey together.",
                fontSize = 14.sp,
                color = colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(value = viewModel.email,
                onValueChange = { viewModel.email = it },
                placeholder = {
                    Text(
                        "your.email@example.com",
                        color = colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                },
                label = { Text("Email Address", color = colorScheme.onBackground) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colorScheme.secondary, RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorScheme.onBackground,
                    unfocusedTextColor = colorScheme.onBackground,
                    focusedLabelColor = colorScheme.onBackground.copy(alpha = 0.8f),
                    unfocusedLabelColor = colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = viewModel.password,
                onValueChange = { viewModel.password = it },
                placeholder = {
                    Text(
                        "Enter your secure password",
                        color = colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                },
                label = { Text("Password", color = colorScheme.onBackground) },
                singleLine = true,
                visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (viewModel.passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    IconButton(onClick = {
                        viewModel.passwordVisible = !viewModel.passwordVisible
                    }) {
                        Icon(
                            imageVector = image,
                            contentDescription = null,
                            tint = colorScheme.primary
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, colorScheme.secondary, RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorScheme.onBackground,
                    unfocusedTextColor = colorScheme.onBackground,
                    focusedLabelColor = colorScheme.onBackground.copy(alpha = 0.8f),
                    unfocusedLabelColor = colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.signIn(onSuccess = {
                        Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT).show()
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }, onError = { message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !viewModel.loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary, contentColor = colorScheme.onPrimary
                )
            ) {
                if (viewModel.loading) {
                    CircularProgressIndicator(
                        color = colorScheme.background,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Sign In Safely ✨",
                        fontWeight = FontWeight.SemiBold,
                        color = colorScheme.background
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = {
                viewModel.showResetDialog = true
                viewModel.resetEmail = ""
            }) {
                Text(
                    "Forgot your password? We'll help you reset it 🔒",
                    fontSize = 12.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            TextButton(onClick = {
                navController.navigate("register")
            }) {
                Text(
                    "New to our community? Join us today! 🌈",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.primary
                )
            }

            if (viewModel.showResetDialog) {
                AlertDialog(onDismissRequest = {
                    if (!viewModel.resetLoading) viewModel.showResetDialog = false
                }, title = { Text("Reset Password") }, text = {
                    Column {
                        Text("Please enter your email to receive a password reset link.")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = viewModel.resetEmail,
                            onValueChange = { viewModel.resetEmail = it },
                            singleLine = true,
                            placeholder = { Text("your.email@example.com") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            enabled = !viewModel.resetLoading,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }, confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.sendResetEmail(onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Reset email sent to ${viewModel.resetEmail}",
                                    Toast.LENGTH_LONG
                                ).show()
                                viewModel.showResetDialog = false
                            }, onError = { message ->
                                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                            })
                        }, enabled = !viewModel.resetLoading
                    ) {
                        if (viewModel.resetLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp), strokeWidth = 2.dp
                            )
                        } else {
                            Text("Send")
                        }
                    }
                }, dismissButton = {
                    TextButton(onClick = {
                        if (!viewModel.resetLoading) viewModel.showResetDialog = false
                    }) {
                        Text("Cancel")
                    }
                })
            }
        }
    }
}
