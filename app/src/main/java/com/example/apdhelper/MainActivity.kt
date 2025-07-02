package com.example.apdhelper

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apdhelper.bottomnavigationbar.BottomBar
import com.example.apdhelper.bottomnavigationbar.NavigationGraph
import com.example.apdhelper.ui.theme.APDHelperTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

private const val RC_SIGN_IN = 9001


class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            APDHelperTheme {
                val navController = rememberNavController()
                var isBottomBarVisible by remember { mutableStateOf(false) }

                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser

                LaunchedEffect(currentUser) {
                    if (currentUser != null) {
                        navController.navigate("home") {
                            popUpTo("start") { inclusive = true }
                        }
                        isBottomBarVisible = true
                    }
                }

                androidx.compose.material3.Scaffold(
                    bottomBar = {
                        if (isBottomBarVisible) {
                            BottomBar(navController = navController, state = true)
                        }
                    }
                ) { padding ->
                    androidx.compose.foundation.layout.Box(modifier = androidx.compose.ui.Modifier.padding(padding)) {
                        NavigationGraph(
                            navController = navController,
                            onBottomBarVisibilityChanged = { visible -> isBottomBarVisible = visible }
                        )
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
                    if (account.email != currentUserEmail) {
                        firebaseAuthWithGoogle(idToken) { userExists ->
                            if (userExists) {
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            } else {
                                navController.navigate("start") {
                                    popUpTo("start") { inclusive = true }
                                }
                            }
                        }
                    } else {
                        navController.navigate("start") {
                            popUpTo("start") { inclusive = true }
                        }
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign-In failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String,
        onComplete: (Boolean) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val uid = currentUser.uid
                    val email = currentUser.email ?: ""

                    firestore.collection("users").document(uid).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                                onComplete(true) // User exists
                            } else {
                                val newUser = hashMapOf(
                                    "name" to (currentUser.displayName ?: "Unknown"),
                                    "email" to email,
                                   )
                                firestore.collection("users").document(uid).set(newUser)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "New user created successfully!", Toast.LENGTH_SHORT).show()
                                        onComplete(false) // New user created
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to create user: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error fetching user: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(this, "Google Sign-In failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
