package com.example.shalenamma.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.shalenamma.MainViewModel
import com.example.shalenamma.UserRole
import com.example.shalenamma.viewmodel.LanguageViewModel
import com.example.shalenamma.navigation.PlaceholderScreen
import com.example.shalenamma.ui.navigation.Screen
import com.example.shalenamma.ui.screens.admin.UploadMealScreen
import com.example.shalenamma.ui.screens.admin.PostAnnouncementScreen
import com.example.shalenamma.ui.screens.admin.UploadStudentStarScreen
import com.example.shalenamma.ui.screens.parent.DailyMealScreen
import com.example.shalenamma.ui.screens.parent.FeedbackScreen
import com.example.shalenamma.ui.screens.parent.StudentStarsScreen
import com.example.shalenamma.ui.screens.common.FacilityTourScreen
import com.example.shalenamma.ui.screens.common.NotificationScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavController,
    mainVM: MainViewModel = viewModel(),
    langVM: LanguageViewModel = viewModel(),
    authVM: com.example.shalenamma.viewmodel.AuthViewModel = viewModel(),
    rtVM: com.example.shalenamma.viewmodel.RealtimeDatabaseViewModel = viewModel()
) {
    val navController = rememberNavController()
    val userRole by mainVM.userRole
    val isSyncing by mainVM.isSyncing
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val leftItems = listOf(
        Screen.Home,
        Screen.Calendar
    )
    val rightItems = listOf(
        Screen.Announcements,
        Screen.Profile
    )

    Scaffold(
        topBar = {
            if (currentRoute != Screen.Home.route) {
                TopAppBar(
                    title = {
                        Text(
                            text = langVM.text("Shale Namma", "ಶಾಲೆ ನಮ್ಮ"),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        com.example.shalenamma.ui.components.LanguageToggle(langVM)
                        Spacer(modifier = Modifier.width(8.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    )
                )
            }
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT ITEMS
                    leftItems.forEach { screen ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            BottomNavItem(screen, navController)
                        }
                    }

                    // CENTER ADD BUTTON
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                        ) {
                            Surface(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clickable {
                                        if (userRole == UserRole.ADMIN) {
                                            navController.navigate(Screen.UploadMeal.route)
                                        } else {
                                            navController.navigate(Screen.Feedback.route)
                                        }
                                    },
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                shadowElevation = 4.dp
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (userRole == UserRole.ADMIN) "MEALS" else "HELP",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 10.sp
                            )
                        }
                    }

                    // RIGHT ITEMS
                    rightItems.forEach { screen ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            BottomNavItem(screen, navController)
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (isSyncing) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.tertiary)
            }
            
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            ) {
                composable(Screen.Home.route) { 
                    DashboardScreen(
                        isAdmin = userRole == UserRole.ADMIN,
                        onNavigate = { route -> navController.navigate(route) },
                        langVM = langVM,
                        rtVM = rtVM
                    )
                }
                composable(Screen.DailyMeal.route) { DailyMealScreen(isAdmin = userRole == UserRole.ADMIN, onBack = { navController.popBackStack() }, onNavigateToUpload = { navController.navigate(Screen.UploadMeal.route) }, rtVM = rtVM) }
                composable(Screen.UploadMeal.route) { UploadMealScreen(onBack = { navController.popBackStack() }, rtVM = rtVM) }
                composable(Screen.Announcements.route) { 
                    com.example.shalenamma.ui.screens.parent.AnnouncementsScreen(
                        navController = navController,
                        isAdmin = userRole == UserRole.ADMIN,
                        langVM = langVM,
                        rtVM = rtVM
                    ) 
                }
                composable(Screen.PostAnnouncement.route) { 
                    com.example.shalenamma.ui.screens.admin.PostAnnouncementScreen(onBack = { navController.popBackStack() }, rtVM = rtVM)
                }
                composable(Screen.Profile.route) { ProfileScreen(navController, rootNavController, mainVM, authVM) }
                composable(Screen.Feedback.route) { 
                    if (userRole == UserRole.ADMIN) {
                        com.example.shalenamma.ui.screens.admin.AdminFeedbackScreen(navController, rtVM = rtVM)
                    } else {
                        FeedbackScreen(navController, rtVM = rtVM)
                    }
                }
                composable(Screen.Settings.route) { 
                    SettingsScreen(
                        navController = navController,
                        mainVM = mainVM,
                        langVM = langVM
                    )
                }
                composable(Screen.Notifications.route) { NotificationScreen(navController) }
                composable(Screen.Calendar.route) { CalendarScreen(navController) }
                composable(Screen.StudentStars.route) { 
                    StudentStarsScreen(
                        navController = navController,
                        isAdmin = userRole == UserRole.ADMIN,
                        onNavigateToUpload = { navController.navigate(Screen.UploadStudentStar.route) },
                        rtVM = rtVM
                    ) 
                }
                composable(Screen.UploadStudentStar.route) { UploadStudentStarScreen(onBack = { navController.popBackStack() }, rtVM = rtVM) }
                composable(Screen.FacilityTour.route) { FacilityTourScreen(navController) }
                composable(Screen.SchoolDetails.route) { SchoolDetailsScreen(navController) }
                composable(Screen.Security.route) { SecurityScreen(navController) }
            }
        }
    }
}

@Composable
fun BottomNavItem(screen: Screen, navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(
                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                indication = null
            ) {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            .padding(vertical = 8.dp)
            .width(64.dp)
    ) {
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Icon(
            imageVector = getIconForScreen(screen),
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(24.dp)
        )

        if (selected) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = screen.label.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 10.sp
            )
        }
    }
}

private fun getIconForScreen(screen: Screen): androidx.compose.ui.graphics.vector.ImageVector {
    return when(screen) {
        Screen.Home -> Icons.Rounded.Home
        Screen.Calendar -> Icons.Rounded.CalendarMonth
        Screen.Profile -> Icons.Rounded.Person
        Screen.DailyMeal -> Icons.Rounded.Restaurant
        Screen.Announcements -> Icons.Rounded.Campaign
        Screen.Settings -> Icons.Rounded.Settings
        else -> Icons.Rounded.Circle
    }
}
