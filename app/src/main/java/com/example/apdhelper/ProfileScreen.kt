package com.example.apdhelper

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast

@Composable
fun ProfileScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    var name by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }

    val uid = auth.currentUser?.uid

    LaunchedEffect(uid) {
        if (uid != null) {
            firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        name = document.getString("name")
                        email = document.getString("email")
                    } else {
                        Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                    loading = false
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error loading user data: ${e.message}", Toast.LENGTH_LONG).show()
                    loading = false
                }
        } else {
            navController.navigate("start") {
                popUpTo("profile") { inclusive = true }
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (loading) {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Name: ${name ?: "Unknown"}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Email: ${email ?: "Unknown"}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = {
                    auth.signOut()
                    navController.navigate("start") {
                        popUpTo("profile") { inclusive = true }
                    }
                }) {
                    Text(text = "Logout")
                }
            }
        }
    }
}
