package com.example.apdhelper.bottomnavigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItems(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object Home : BottomNavigationItems(
        route = "home", title = "Home", icon = Icons.Outlined.Home
    )

    object Test : BottomNavigationItems(
        route = "test", title = "Test", icon = Icons.Outlined.Create
    )

    object Notes : BottomNavigationItems(
        route = "notes", title = "Notes", icon = Icons.Outlined.AddCircle
    )

    object ProfileScreen : BottomNavigationItems(
        route = "profile", title = "Profile", icon = Icons.Outlined.AccountCircle
    )
}
