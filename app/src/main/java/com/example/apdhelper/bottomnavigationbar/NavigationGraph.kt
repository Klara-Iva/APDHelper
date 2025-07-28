package com.example.apdhelper.bottomnavigationbar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.apdhelper.AboutScreen
import com.example.apdhelper.HelpScreen
import com.example.apdhelper.SettingsScreen
import com.example.apdhelper.StartScreen
import com.example.apdhelper.ui.screen.auth.LoginScreen
import com.example.apdhelper.ui.screen.auth.RegisterScreen
import com.example.apdhelper.ui.screen.home.HomeScreen
import com.example.apdhelper.ui.screen.insights.InsightsScreen
import com.example.apdhelper.ui.screen.loading.LoadingScreen
import com.example.apdhelper.ui.screen.notes.AddNewNoteScreen
import com.example.apdhelper.ui.screen.notes.NotesScreen
import com.example.apdhelper.ui.screen.profile.ProfileScreen
import com.example.apdhelper.ui.screen.questionnaire.QuestionnaireScreen
import com.example.apdhelper.ui.screens.QuestionsScreen
import com.example.apdhelper.ui.screens.ResultsScreen

@Composable
fun NavigationGraph(
    navController: NavHostController, onBottomBarVisibilityChanged: (Boolean) -> Unit
) {
    NavHost(navController = navController, startDestination = "loadingscreen") {

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

        composable("insights") {
            InsightsScreen(navController)
        }

        composable("notes") {
            onBottomBarVisibilityChanged(true)
            NotesScreen(navController)
        }

        composable("questionnaire") {
            onBottomBarVisibilityChanged(true)
            QuestionnaireScreen(navController)
        }

        composable("loadingscreen") {
            onBottomBarVisibilityChanged(false)
            LoadingScreen(navController = navController)
        }

        composable("questions") {
            onBottomBarVisibilityChanged(true)
            QuestionsScreen(navController)
        }

        composable("results") {
            onBottomBarVisibilityChanged(true)
            ResultsScreen(navController = navController)
        }

        composable("add_note") {
            AddNewNoteScreen(navController)
        }

        composable("help") {
            HelpScreen(navController)
        }

        composable("settings") {
            SettingsScreen(navController)
        }

        composable("about") {
            AboutScreen(navController)
        }
    }
}
