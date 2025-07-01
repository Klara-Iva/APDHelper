package com.example.apdhelper

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun StartScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { navController.navigate("login") }) {
                Text("Login")
            }
            Button(onClick = { navController.navigate("register") }) {
                Text("Register")
            }
        }
    }
}
