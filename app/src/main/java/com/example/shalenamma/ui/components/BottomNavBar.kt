package com.example.shalenamma.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Composable
fun BottomNavBar(navController: NavController) {

    val items = listOf(

        BottomNavItem(
            route = "dashboard",
            title = "Home",
            icon = Icons.Default.Home
        ),

        BottomNavItem(
            route = "announcements",
            title = "Updates",
            icon = Icons.Default.Notifications
        ),

        BottomNavItem(
            route = "calendar",
            title = "Calendar",
            icon = Icons.Default.CalendarMonth
        ),

        BottomNavItem(
            route = "profile",
            title = "Profile",
            icon = Icons.Default.Person
        )
    )

    val navBackStackEntry =
        navController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry.value?.destination?.route

    NavigationBar(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp
                )
            ),

        containerColor = Color.White,

        tonalElevation = 10.dp
    ) {

        items.forEach { item ->

            NavigationBarItem(

                selected = currentRoute == item.route,

                onClick = {

                    navController.navigate(item.route) {

                        popUpTo(navController.graph.startDestinationId)

                        launchSingleTop = true
                    }
                },

                icon = {

                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },

                label = {
                    Text(item.title)
                },

                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF1565C0),
                    selectedTextColor = Color(0xFF1565C0),
                    indicatorColor = Color(0xFFE3F2FD)
                )
            )
        }
    }
}