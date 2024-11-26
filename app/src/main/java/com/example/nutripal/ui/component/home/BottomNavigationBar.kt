package com.example.nutripal.ui.component.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nutripal.ui.theme.Primary

sealed class BottomNavItem(val title: String, val icon: @Composable () -> Unit, val route: String) {
    object Home : BottomNavItem("Home", { Icon(Icons.Filled.Home, contentDescription = "Home") }, "home")
    object Profile : BottomNavItem("Profile", { Icon(Icons.Filled.Person, contentDescription = "Profile") }, "profile")
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, currentRoute: String, navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp) // Reduced height for the navigation bar
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(0f),
            containerColor = Color.White,
            tonalElevation = 0.dp
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = { item.icon() },
                    label = { Text(item.title) },
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    modifier = Modifier
                        .width(140.dp)
                        .weight(1f),
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Primary,  // Assuming Primary is Blue
                        selectedTextColor = Primary,
                        unselectedIconColor = Color.Gray, // Assuming Disabled is Gray
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    }
}