package com.example.nutripal.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.nutripal.ui.theme.Disabled
import com.example.nutripal.ui.theme.Primary

sealed class BottomNavItem(val title: String, val icon: @Composable () -> Unit, val route: String) {
    object Home : BottomNavItem("Home", { Icon(Icons.Filled.Home, contentDescription = "Home") }, "home")
    object Profile : BottomNavItem("Profile", { Icon(Icons.Filled.Person, contentDescription = "Profile") }, "profile")
}

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier) {
    var selectedItem by remember { mutableStateOf(0) }
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
            // Add a Spacer between Home and Profile to ensure equal space
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        item.icon()
                    },
                    label = {
                        Text(
                            text = item.title,
                            fontSize = 12.sp
                        )
                    },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index },
                    modifier = Modifier
                        .width(140.dp) // Set width for each icon
                        .weight(1f), // Make it take equal space
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Primary,
                        selectedTextColor = Primary,
                        unselectedIconColor = Disabled,
                        unselectedTextColor = Disabled
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar()
}
