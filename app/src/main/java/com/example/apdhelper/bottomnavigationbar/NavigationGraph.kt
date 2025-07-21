package com.example.apdhelper.bottomnavigationbar

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import com.example.apdhelper.*

@Composable
fun NavigationGraph(
    navController: NavHostController,
    onBottomBarVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = "start") {

        composable("start") {
            onBottomBarVisibilityChanged(false)
            StartScreen(navController)
        }

        composable("login") {
            onBottomBarVisibilityChanged(false)
            LoginScreen(navController)
        }

        composable("register") {
            onBottomBarVisibilityChanged(false)
            RegisterScreen(navController)
        }

        composable("home") {
            onBottomBarVisibilityChanged(true)
            HomeScreen(navController)
        }

        composable("profile") {
            onBottomBarVisibilityChanged(true)
            ProfileScreen(navController)
        }

        composable("notes") {
            onBottomBarVisibilityChanged(true)
            NotesScreen(navController)
        }
    }
}
