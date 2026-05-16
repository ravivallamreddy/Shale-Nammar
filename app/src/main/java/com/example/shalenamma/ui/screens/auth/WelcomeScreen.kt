package com.example.shalenamma.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.shalenamma.R

import com.example.shalenamma.MainViewModel
import com.example.shalenamma.UserRole
import com.example.shalenamma.viewmodel.LanguageViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WelcomeScreen(
    navController: NavController,
    langVM: LanguageViewModel = viewModel(),
    mainVM: MainViewModel = viewModel()
) {

    // 🌐 TEXTS
    val appTitle =
        langVM.text("Shale-Namma Pride", "ಶಾಲೆ-ನಮ್ಮ ಪ್ರೈಡ್")

    val tagline =
        langVM.text("Our School, Our Pride", "ನಮ್ಮ ಶಾಲೆ, ನಮ್ಮ ಹೆಮ್ಮೆ")

    val parentTitle =
        langVM.text("Continue as Parent", "ಪೋಷಕರಾಗಿ ಮುಂದುವರಿಯಿರಿ")

    val parentSubtitle =
        langVM.text(
            "View child progress & school updates",
            "ಮಕ್ಕಳ ಪ್ರಗತಿ ಮತ್ತು ಶಾಲೆಯ ಮಾಹಿತಿ ವೀಕ್ಷಿಸಿ"
        )

    val adminTitle =
        langVM.text("Continue as Admin", "ಆಡಳಿತಗಾರರಾಗಿ ಮುಂದುವರಿಯಿರಿ")

    val adminSubtitle =
        langVM.text(
            "Manage school activities & notices",
            "ಶಾಲಾ ಚಟುವಟಿಕೆಗಳನ್ನು ನಿರ್ವಹಿಸಿ"
        )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )
            )
    ) {
        // 🖼️ WATERMARK / COVER IMAGE
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .offset(x = 100.dp, y = (-100).dp)
                .alpha(0.05f),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(40.dp))

            // 🏫 LOGO
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                modifier = Modifier.size(150.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔷 TITLE
            Text(
                text = appTitle,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = tagline,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(50.dp))

            // 👨‍👩‍👧 PARENT CARD
            WelcomeRoleCard(
                title = parentTitle,
                subtitle = parentSubtitle,
                icon = Icons.Default.Groups,
                iconColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                onClick = {
                    mainVM.setRole(UserRole.PARENT)
                    navController.navigate("login")
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 🛠 ADMIN CARD
            WelcomeRoleCard(
                title = adminTitle,
                subtitle = adminSubtitle,
                icon = Icons.Default.AdminPanelSettings,
                iconColor = MaterialTheme.colorScheme.secondary,
                backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                onClick = {
                    mainVM.setRole(UserRole.ADMIN)
                    navController.navigate("login")
                }
            )


            Spacer(modifier = Modifier.weight(1f))

            // 🔻 FOOTER
            Text(
                text = langVM.text(
                    "OUR SCHOOL • OUR PRIDE",
                    "ನಮ್ಮ ಶಾಲೆ • ನಮ್ಮ ಹೆಮ್ಮೆ"
                ),

                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                style = MaterialTheme.typography.labelSmall
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun WelcomeRoleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    backgroundColor: Color,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,

        modifier = Modifier.fillMaxWidth(),

        shape = MaterialTheme.shapes.large,

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🎨 ICON BOX
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),

                shape = MaterialTheme.shapes.medium
            ) {

                Box(
                    modifier = Modifier.size(78.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(38.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            // 📄 TEXTS
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}