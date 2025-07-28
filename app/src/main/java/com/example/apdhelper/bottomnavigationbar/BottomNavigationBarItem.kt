package com.example.apdhelper.bottomnavigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItems(
    val route: String, val title: String? = null, val icon: ImageVector? = null
) {
    object Home : BottomNavigationItems(
        route = "home", title = "Home", icon = Icons.Outlined.Home
    )

    object Test : BottomNavigationItems(
        route = "questionnaire", title = "Test", icon = Icons.Outlined.Create
    )

    object Insights : BottomNavigationItems(
        route = "insights", title = "Insight", icon = Icons.Outlined.Lightbulb
    )

    object Notes : BottomNavigationItems(
        route = "notes", title = "Notes", icon = Icons.Outlined.AddCircle
    )

    object ProfileScreen : BottomNavigationItems(
        route = "profile", title = "Profile", icon = Icons.Outlined.AccountCircle
    )
}
