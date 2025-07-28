package com.example.apdhelper

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apdhelper.ui.components.profile.AccountActionButton
import com.example.apdhelper.ui.screens.settings.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = { Text("Settings and Privacy") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
            }
        }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
        )
    }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Account", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            AccountActionButton("Reset Password", Icons.Outlined.Lock) {
                viewModel.onResetPassword()
            }

            AccountActionButton("Delete My Account", Icons.Outlined.Delete) {
                viewModel.showDeleteDialog()
            }

            AccountActionButton("Sign Out", Icons.Outlined.Logout) {
                viewModel.onSignOut {
                    navController.navigate("start") {
                        popUpTo("settings") { inclusive = true }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("App", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            AccountActionButton("Toggle Dark Mode", Icons.Outlined.DarkMode) {
                viewModel.toggleDarkMode()
            }

            if (uiState.showDeleteDialog) {
                AlertDialog(onDismissRequest = { viewModel.hideDeleteDialog() },
                    title = { Text("Delete Account") },
                    text = {
                        Text("Are you sure you want to permanently delete your account and all your data?")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.hideDeleteDialog()
                            viewModel.confirmDeleteAccount {
                                navController.navigate("start") {
                                    popUpTo("settings") { inclusive = true }
                                }
                            }
                        }) {
                            Text("Yes, Delete", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.hideDeleteDialog() }) {
                            Text("Cancel")
                        }
                    })
            }
        }
    }

    BackHandler {
        navController.popBackStack()
    }
}
