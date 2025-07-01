package com.example.apdhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.apdhelper.bottomnavigationbar.BottomBar
import com.example.apdhelper.bottomnavigationbar.NavigationGraph
import com.example.apdhelper.ui.theme.APDHelperTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            APDHelperTheme {
                val navController = rememberNavController()
                var isBottomBarVisible by remember { mutableStateOf(false) }

                val auth = FirebaseAuth.getInstance()
                val currentUser = auth.currentUser

                // Prikazujemo bottom bar samo ako je korisnik u app flow
                // ili kad je login uspješan (preko onBottomBarVisibilityChanged)

                // Na startu - ako je korisnik prijavljen ide odmah u app flow
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
}
