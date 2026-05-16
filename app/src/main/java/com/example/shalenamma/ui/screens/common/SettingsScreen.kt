package com.example.shalenamma.ui.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shalenamma.MainViewModel
import com.example.shalenamma.viewmodel.LanguageViewModel
import com.example.shalenamma.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    mainVM: MainViewModel,
    langVM: LanguageViewModel
) {
    var pushNotifications by remember { mutableStateOf(true) }
    val isDarkMode by mainVM.isDarkMode

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Rounded.ChevronLeft, contentDescription = "Back", modifier = Modifier.size(32.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main Settings Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Dark Mode
                    SettingsRow(
                        icon = Icons.Rounded.DarkMode,
                        iconColor = Color(0xFF2196F3),
                        title = "Dark Mode",
                        subtitle = if (isDarkMode) "ENABLED" else "DISABLED",
                        onClick = { mainVM.toggleTheme() }
                    ) {
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { mainVM.toggleTheme() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF2196F3)
                            )
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp), 
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // Language
                    SettingsRow(
                        icon = Icons.Rounded.Translate,
                        iconColor = Color(0xFF2196F3),
                        title = "Language / ಭಾಷೆ",
                        subtitle = "CURRENT: ${if (langVM.isKannada) "KANNADA" else "ENGLISH"}",
                        onClick = { langVM.toggleLanguage() }
                    ) {
                        Text(
                            text = if (langVM.isKannada) "ಕ" else "EN", 
                            fontWeight = FontWeight.Bold, 
                            color = Color(0xFF2196F3),
                            fontSize = 14.sp
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp), 
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // Push Notifications
                    SettingsRow(
                        icon = Icons.Rounded.NotificationsNone,
                        iconColor = Color(0xFF9C27B0),
                        title = "Push Notifications",
                        subtitle = if (pushNotifications) "ENABLED" else "DISABLED",
                        onClick = { pushNotifications = !pushNotifications }
                    ) {
                        Switch(
                            checked = pushNotifications,
                            onCheckedChange = { pushNotifications = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF2196F3)
                            )
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp), 
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // Security & Privacy
                    SettingsRow(
                        icon = Icons.Rounded.Shield,
                        iconColor = Color(0xFFFFC107),
                        title = "Security & Privacy",
                        subtitle = "SECURE DATA",
                        onClick = { navController.navigate(Screen.Security.route) }
                    ) {
                        Icon(
                            Icons.Rounded.ChevronRight, 
                            contentDescription = null, 
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp), 
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // About School
                    SettingsRow(
                        icon = Icons.Rounded.School,
                        iconColor = Color(0xFF4CAF50),
                        title = "About Our School",
                        subtitle = "GENERAL INFO",
                        onClick = { navController.navigate(Screen.SchoolDetails.route) }
                    ) {
                        Icon(
                            Icons.Rounded.ChevronRight, 
                            contentDescription = null, 
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp), 
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // App Version
                    SettingsRow(
                        icon = Icons.Rounded.Info,
                        iconColor = Color(0xFF94A3B8),
                        title = "App Version",
                        subtitle = "V1.2.4 PRODUCTION"
                    ) {
                        Text(
                            text = "Stable", 
                            fontSize = 12.sp, 
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    onClick: (() -> Unit)? = null,
    action: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = iconColor.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title, 
                    style = MaterialTheme.typography.bodyMedium, 
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle, 
                    style = MaterialTheme.typography.labelSmall, 
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        action()
    }
}
