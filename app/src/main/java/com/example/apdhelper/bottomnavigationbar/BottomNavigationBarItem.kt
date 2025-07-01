package com.example.apdhelper.bottomnavigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItems(
    val route: String, val title: String? = null, val icon: ImageVector? = null
) {

    object Home : BottomNavigationItems(
        route = "home", title = "Home", icon = Icons.Outlined.Home
    )

    object ProfileScreen : BottomNavigationItems(
        route = "profile", title = "Profile", icon = Icons.Outlined.AccountCircle
    )
}