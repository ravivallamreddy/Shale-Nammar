package com.example.shalenamma.ui.screens.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import com.example.shalenamma.R
import com.example.shalenamma.ui.navigation.Screen
import com.example.shalenamma.viewmodel.LanguageViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun DashboardScreen(
    isAdmin: Boolean = false,
    onNavigate: (String) -> Unit,
    langVM: LanguageViewModel = viewModel(),
    rtVM: com.example.shalenamma.viewmodel.RealtimeDatabaseViewModel = viewModel()
) {
    val latestAnnouncement = rtVM.liveAnnouncements.firstOrNull()
    
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            DashboardHeader(isAdmin = isAdmin, onNavigate = onNavigate, langVM = langVM, rtVM = rtVM)
            
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                WelcomeHeroCard(isAdmin = isAdmin, langVM = langVM)
                
                Spacer(modifier = Modifier.height(28.dp))
                
                AnnouncementsPreview(onNavigate, langVM, latestAnnouncement)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = langVM.text("Quick Actions", "ತ್ವರಿತ ಕ್ರಮಗಳು"),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                ActionGrid(isAdmin, onNavigate, langVM = langVM)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                ActivityFeed(onNavigate, langVM, rtVM)
                
                Spacer(modifier = Modifier.height(100.dp)) // Padding for bottom bar
            }
        }
    }
}

@Composable
fun DashboardHeader(
    isAdmin: Boolean,
    onNavigate: (String) -> Unit,
    langVM: LanguageViewModel = viewModel(),
    rtVM: com.example.shalenamma.viewmodel.RealtimeDatabaseViewModel = viewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 4.dp
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(42.dp).padding(4.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = langVM.text("Shale Namma", "ಶಾಲೆ ನಮ್ಮ"),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    val feedbackStatus = if (rtVM.isFeedbackFetching) "..." else rtVM.activeFeedbackCount.toString()
                    Text(
                        text = langVM.text(
                            if (isAdmin) "SCHOOL ADMIN • $feedbackStatus ACTIVE" else "PARENT",
                            if (isAdmin) "ಶಾಲಾ ನಿರ್ವಾಹಕರು • $feedbackStatus ಸಕ್ರಿಯ" else "ಪೋಷಕರು"
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onNavigate(Screen.Notifications.route) },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface, CircleShape).size(40.dp)
                ) {
                    Icon(
                        Icons.Rounded.NotificationsNone, 
                        contentDescription = "Notifications", 
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                IconButton(
                    onClick = { onNavigate(Screen.Settings.route) },
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface, CircleShape).size(40.dp)
                ) {
                    Icon(
                        Icons.Rounded.Settings, 
                        contentDescription = "Settings", 
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(80.dp)) // Room for global language toggle
            }
        }
    }
}

@Composable
fun WelcomeHeroCard(isAdmin: Boolean, langVM: LanguageViewModel = viewModel()) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Decorative background icons
            Icon(
                Icons.Rounded.School,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.15f),
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = 20.dp, y = 10.dp)
            )
            
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.CenterStart)
            ) {
                Surface(
                    color = Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = langVM.text(
                            "WELCOME BACK",
                            "ಪುನಃ ಸ್ವಾಗತ"
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = langVM.text("GHPS Mysore Central", "ಸರ್ಕಾರಿ ಹಿರಿಯ ಪ್ರಾಥಮಿಕ ಶಾಲೆ, ಮೈಸೂರು"),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = langVM.text("Empowering the next generation", "ಮುಂದಿನ ಪೀಳಿಗೆಯನ್ನು ಸಬಲೀಕರಣಗೊಳಿಸುವುದು"),
                    color = Color.White.copy(alpha = 0.85f),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun AnnouncementsPreview(
    onNavigate: (String) -> Unit, 
    langVM: LanguageViewModel,
    latest: com.example.shalenamma.viewmodel.AnnouncementUpdate? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate(Screen.Announcements.route) },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondary,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Rounded.Campaign, contentDescription = null, tint = Color.White)
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (latest != null) latest.tag else langVM.text("LATEST ANNOUNCEMENT", "ಇತ್ತೀಚಿನ ಪ್ರಕಟಣೆ"),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = latest?.title ?: langVM.text("School Annual Day celebration on Friday", "ಶುಕ್ರವಾರ ಶಾಲಾ ವಾರ್ಷಿಕೋತ್ಸವ ಆಚರಣೆ"),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
            
            Icon(
                Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun ActionGrid(isAdmin: Boolean, onNavigate: (String) -> Unit, langVM: LanguageViewModel = viewModel()) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DashboardActionCard(
                modifier = Modifier.weight(1f),
                title = langVM.text("Daily Meal", "ದೈನಂದಿನ ಊಟ"),
                description = if (isAdmin) langVM.text("Update menu", "ಮೆನು ನವೀಕರಿಸಿ") else langVM.text("Today's menu", "ಇಂದಿನ ಮೆನು"),
                icon = Icons.Rounded.Restaurant,
                containerColor = Color(0xFFFFCC80),
                contentColor = if (isSystemInDarkTheme()) Color(0xFFFFB74D) else Color(0xFFE65100),
                onClick = { onNavigate(Screen.DailyMeal.route) }
            )
            DashboardActionCard(
                modifier = Modifier.weight(1f),
                title = langVM.text("Student Stars", "ವಿದ್ಯಾರ್ಥಿ ತಾರೆಗಳು"),
                description = langVM.text("Top achievers", "ಉನ್ನತ ಸಾಧಕರು"),
                icon = Icons.Rounded.AutoAwesome,
                containerColor = Color(0xFFCE93D8),
                contentColor = if (isSystemInDarkTheme()) Color(0xFFE1BEE7) else Color(0xFF4A148C),
                onClick = { onNavigate(Screen.StudentStars.route) }
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DashboardActionCard(
                modifier = Modifier.weight(1f),
                title = langVM.text("Facility Tour", "ಶಾಲಾ ಪ್ರವಾಸ"),
                description = langVM.text("Explore school", "ಶಾಲೆಯನ್ನು ಅನ್ವೇಷಿಸಿ"),
                icon = Icons.Rounded.LocationCity,
                containerColor = Color(0xFFA5D6A7),
                contentColor = if (isSystemInDarkTheme()) Color(0xFFC8E6C9) else Color(0xFF1B5E20),
                onClick = { onNavigate(Screen.FacilityTour.route) }
            )
            DashboardActionCard(
                modifier = Modifier.weight(1f),
                title = langVM.text("Feedback", "ಅಭಿಪ್ರಾಯ"),
                description = if (isAdmin) langVM.text("Read reports", "ವರದಿಗಳನ್ನು ಓದಿ") else langVM.text("Share thoughts", "ನಿಮ್ಮ ಅನಿಸಿಕೆ"),
                icon = Icons.Rounded.QuestionAnswer,
                containerColor = Color(0xFF81D4FA),
                contentColor = if (isSystemInDarkTheme()) Color(0xFFB3E5FC) else Color(0xFF01579B),
                onClick = { onNavigate(Screen.Feedback.route) }
            )
        }
    }
}

@Composable
fun DashboardActionCard(
    modifier: Modifier,
    title: String,
    description: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isPressed) 0.95f else 1f, label = "scale")

    Card(
        modifier = modifier
            .height(160.dp)
            .scale(scale)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes.any { it.pressed }) {
                            isPressed = true
                        } else if (event.changes.all { !it.pressed }) {
                            isPressed = false
                        }
                    }
                }
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor.copy(alpha = 0.15f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, containerColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(14.dp),
                color = containerColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(24.dp))
                }
            }
            
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        Icons.Rounded.ArrowForward,
                        contentDescription = null,
                        tint = contentColor.copy(alpha = 0.5f),
                        modifier = Modifier.size(14.dp)
                    )
                }
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ActivityFeed(
    onNavigate: (String) -> Unit, 
    langVM: LanguageViewModel,
    rtVM: com.example.shalenamma.viewmodel.RealtimeDatabaseViewModel = viewModel()
) {
    val items = rtVM.recentActivity
    val isFetching = rtVM.isFetching

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = langVM.text("Recent Activity", "ಇತ್ತೀಚಿನ ಚಟುವಟಿಕೆ"),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(onClick = { onNavigate(Screen.Notifications.route) }) {
                Text(langVM.text("See all", "ಎಲ್ಲವನ್ನೂ ನೋಡಿ"), fontWeight = FontWeight.Bold)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (isFetching && items.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(modifier = Modifier.size(30.dp))
            }
        } else if (items.isEmpty()) {
            Text(
                text = langVM.text("No recent updates", "ಯಾವುದೇ ಇತ್ತೀಚಿನ ಅಪ್‌ಡೇಟ್‌ಗಳಿಲ್ಲ"),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.padding(vertical = 20.dp).fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        } else {
            items.forEach { activity ->
                ActivityItem(
                    icon = when(activity.type) {
                        "Meal Update" -> Icons.Rounded.Restaurant
                        "Notice" -> Icons.Rounded.Campaign
                        "Star" -> Icons.Rounded.Star
                        "Feedback" -> Icons.Rounded.QuestionAnswer
                        else -> Icons.Rounded.Update
                    },
                    color = when(activity.type) {
                        "Meal Update" -> Color(0xFFFFE0B2)
                        "Notice" -> Color(0xFFC8E6C9)
                        "Star" -> Color(0xFFFFF9C4)
                        "Feedback" -> Color(0xFFE1BEE7)
                        else -> Color.LightGray
                    },
                    title = activity.title,
                    time = calculateTimeAgo(activity.timestamp, langVM),
                    langVM = langVM
                )
            }
        }
    }
}

private fun calculateTimeAgo(timestamp: Long, langVM: LanguageViewModel): String {
    val diff = System.currentTimeMillis() - timestamp
    val mins = diff / (1000 * 60)
    val hours = mins / 60
    
    return when {
        mins < 1 -> langVM.text("Just now", "ಈಗ ತಾನೆ")
        mins < 60 -> langVM.text("$mins mins ago", "$mins ನಿಮಿಷಗಳ ಹಿಂದೆ")
        hours < 24 -> langVM.text("$hours hours ago", "$hours ಗಂಟೆಗಳ ಹಿಂದೆ")
        else -> java.text.SimpleDateFormat("d MMM", java.util.Locale.US).format(java.util.Date(timestamp))
    }
}

@Composable
fun ActivityItem(
    icon: ImageVector,
    color: Color,
    title: String,
    time: String,
    langVM: LanguageViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = if (isSystemInDarkTheme()) color.copy(alpha = 0.2f) else color.copy(alpha = 0.4f),
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = if (isSystemInDarkTheme()) color else color.darken(), modifier = Modifier.size(20.dp))
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

// Helper extension to darken a color slightly for icons
fun Color.darken(factor: Float = 0.6f): Color {
    return Color(
        red = this.red * factor,
        green = this.green * factor,
        blue = this.blue * factor,
        alpha = this.alpha
    )
}
