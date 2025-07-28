package com.example.apdhelper

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HelpScreen(navController: NavController) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var message by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Need Help?",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Describe your issue") },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
            maxLines = 6
        )

        Button(
            onClick = {
                if (user == null) {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                sending = true

                val requestData = mapOf(
                    "email" to (user.email ?: "Unknown"),
                    "message" to message,
                    "timestamp" to Timestamp.now()
                )

                db.collection("help_requests").add(requestData).addOnSuccessListener {
                        Toast.makeText(context, "Message sent! 💌", Toast.LENGTH_SHORT).show()
                        message = ""
                        navController.popBackStack()
                    }.addOnFailureListener {
                        Toast.makeText(
                            context, "Failed to send: ${it.localizedMessage}", Toast.LENGTH_LONG
                        ).show()
                    }.addOnCompleteListener {
                        sending = false
                    }
            }, enabled = message.isNotBlank() && !sending, modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (sending) "Sending..." else "Send")
        }
    }
}
