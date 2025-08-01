package com.example.apdhelper.bottomnavigationbar

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(
        BottomNavigationItems.Home,
        BottomNavigationItems.Test,
        BottomNavigationItems.Insights,
        BottomNavigationItems.Notes,
        BottomNavigationItems.ProfileScreen
    )

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp,
        modifier = Modifier.height(56.dp)
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            NavigationBarItem(selected = selected, onClick = {
                if (!selected) {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }, icon = {
                item.icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = item.title,
                        tint = if (selected) Color(0xFF9F7AEA) else Color(0xFFB9B5C9),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }, label = {
                item.title?.let {
                    Text(
                        text = it,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selected) Color(0xFF9F7AEA) else Color(0xFFB9B5C9)
                    )
                }
            }, alwaysShowLabel = true, colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent
            ), interactionSource = remember { MutableInteractionSource() })
        }
    }
}
