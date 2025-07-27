package com.example.apdhelper

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.apdhelper.bottomnavigationbar.BottomBar
import com.example.apdhelper.bottomnavigationbar.NavigationGraph
import com.example.apdhelper.ui.theme.APDHelperTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val LocalGoogleSignInLauncher = compositionLocalOf<() -> Unit> {
    error("Google Sign-In launcher not provided")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            var isBottomBarVisible by remember { mutableStateOf(false) }
            val auth = FirebaseAuth.getInstance()
            val firestore = FirebaseFirestore.getInstance()
            val context = this

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(Exception::class.java)
                    val idToken = account?.idToken
                    if (idToken != null) {
                        val credential = GoogleAuthProvider.getCredential(idToken, null)
                        auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                            if (authTask.isSuccessful) {
                                val currentUser = auth.currentUser
                                if (currentUser != null) {
                                    val uid = currentUser.uid
                                    val email = currentUser.email ?: ""

                                    firestore.collection("users").document(uid).get()
                                        .addOnSuccessListener { document ->
                                            if (document.exists()) {
                                                navController.navigate("home") {
                                                    popUpTo("start") { inclusive = true }
                                                }
                                                isBottomBarVisible = true
                                            } else {
                                                val newUser = hashMapOf(
                                                    "name" to (currentUser.displayName
                                                        ?: "Unknown"), "email" to email
                                                )
                                                firestore.collection("users").document(uid)
                                                    .set(newUser).addOnSuccessListener {
                                                        Toast.makeText(
                                                            context,
                                                            "New user created successfully!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        navController.navigate("home") {
                                                            popUpTo("start") { inclusive = true }
                                                        }
                                                        isBottomBarVisible = true
                                                    }.addOnFailureListener { e ->
                                                        Toast.makeText(
                                                            context,
                                                            "Failed to create user: ${e.message}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                        }
                                }
                            } else {
                                Toast.makeText(context, "Sign-in failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Sign-in failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            CompositionLocalProvider(LocalGoogleSignInLauncher provides {
                launcher.launch(googleSignInClient.signInIntent)
            }) {
                APDHelperTheme {

                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    val db = FirebaseFirestore.getInstance()
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())



                    Scaffold(bottomBar = {
                        if (isBottomBarVisible) {
                            BottomBar(navController = navController)
                        }
                    }) { padding ->
                        Box(modifier = Modifier.padding(padding)) {
                            NavigationGraph(navController = navController,
                                onBottomBarVisibilityChanged = { visible ->
                                    isBottomBarVisible = visible
                                })
                        }
                        LaunchedEffect(auth.currentUser) {
                            if (auth.currentUser != null) {
                                navController.navigate("home") {
                                    popUpTo("start") { inclusive = true }
                                }
                                isBottomBarVisible = true

                            }


                            if (uid != null) {
                                db.collection("users").document(uid).collection("visits")
                                    .document(today)
                                    .set(mapOf("timestamp" to com.google.firebase.Timestamp.now()))
                            }
                        }

                    }

                }
            }
        }
    }
}

