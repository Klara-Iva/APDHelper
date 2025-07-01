package com.example.apdhelper.bottomnavigationbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import com.example.apdhelper.ui.theme.Purple40



@Composable
fun BottomBar(
    navController: NavHostController, state: Boolean, modifier: Modifier = Modifier
) {
    val screens = listOf(
        BottomNavigationItems.Home,
        BottomNavigationItems.ProfileScreen
    )

    Box(
        modifier = modifier
            .background(Color(android.graphics.Color.parseColor("#D8BFD8")))
            .height(60.dp)
            .zIndex(1f)
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            screens.forEach { screen ->
                NavigationBarItem(
                    label = { Text(text = screen.title!!) },
                    icon = { Icon(imageVector = screen.icon!!, contentDescription = "") },
                    selected = currentRoute == screen.route,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedTextColor = Purple40,
                        selectedTextColor = Color.White,
                        selectedIconColor = Color.White,
                        unselectedIconColor = Purple40,
                        indicatorColor = Color(0x00000000),
                    )
                )
            }
        }
    }
}