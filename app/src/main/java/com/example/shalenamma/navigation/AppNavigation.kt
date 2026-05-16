package com.example.shalenamma.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Announcement
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shalenamma.MainViewModel
import com.example.shalenamma.viewmodel.LanguageViewModel
import com.example.shalenamma.viewmodel.AuthViewModel
import com.example.shalenamma.ui.navigation.Screen
import com.example.shalenamma.ui.screens.auth.*
import com.example.shalenamma.ui.screens.admin.*
import com.example.shalenamma.ui.screens.parent.*
import com.example.shalenamma.ui.screens.common.*
import com.example.shalenamma.ui.components.LanguageToggle
import androidx.compose.ui.Alignment
import androidx.compose.ui.zIndex
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.statusBarsPadding

@Composable
fun AppNavigation(
    mainVM: MainViewModel = viewModel(),
    langVM: LanguageViewModel = viewModel(),
    authVM: AuthViewModel = viewModel(),
    rtVM: com.example.shalenamma.viewmodel.RealtimeDatabaseViewModel = viewModel()
) {
    val navController = rememberNavController()
    val isLoggedIn by authVM.isLoggedIn.collectAsState()
    val userProfile by authVM.userProfile.collectAsState()

    // 🚪 Centralized Authentication Redirect
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            // Only auto-navigate to main_screen if we are currently on welcome/login/register
            val currentRoute = navController.currentDestination?.route
            if (currentRoute == "welcome" || currentRoute == "login" || currentRoute == "register") {
                navController.navigate("main_screen") {
                    popUpTo("welcome") { inclusive = true }
                }
            }
        } else {
            // When logged out, go to welcome screen (role selection)
            navController.navigate("welcome") {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // 🔄 Sync MainViewModel with Firebase User Profile
    LaunchedEffect(userProfile) {
        if (userProfile != null) {
            userProfile?.let { profile ->
                mainVM.setUserInfo(profile.displayName, profile.email)
                // 🛡️ Robust Role Check (Case-Insensitive)
                val isServerAdmin = profile.role.equals("Admin", ignoreCase = true) || 
                                   profile.role.equals("ADMIN", ignoreCase = true)
                
                mainVM.setRole(if (isServerAdmin) com.example.shalenamma.UserRole.ADMIN else com.example.shalenamma.UserRole.PARENT)
            }
        } else {
            // Only reset if we are not in the process of logging in
            if (!isLoggedIn) {
                mainVM.reset()
            }
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "main_screen" else "welcome"
        ) {
            composable("welcome") { 
                WelcomeScreen(navController, langVM = langVM, mainVM = mainVM) 
            }
            
            composable("login") {
                LoginScreen(navController, mainVM = mainVM, langVM = langVM, authVM = authVM)
            }

            composable("register") {
                RegisterScreen(navController, mainVM = mainVM, langVM = langVM, authVM = authVM)
            }
            
            composable("main_screen") { 
                MainScreen(navController, mainVM = mainVM, langVM = langVM, authVM = authVM, rtVM = rtVM)
            }

            composable(Screen.PostAnnouncement.route) {
                PostAnnouncementScreen(onBack = { navController.popBackStack() }, rtVM = rtVM)
            }

            composable(Screen.UploadStudentStar.route) {
                UploadStudentStarScreen(onBack = { navController.popBackStack() }, rtVM = rtVM)
            }
        }

        // 🌐 Global Language Toggle (Top Right Corner)
        LanguageToggle(
            langVM = langVM,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .statusBarsPadding()
                .padding(top = 12.dp, end = 16.dp)
                .zIndex(99f)
        )
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    // Legacy component if needed, but MainScreen has its own custom bottom bar now
}

@Composable
fun PlaceholderScreen(name: String) {
    Surface(modifier = Modifier.fillMaxSize()) {
        androidx.compose.foundation.layout.Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text(text = "$name Screen", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
