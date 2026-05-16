package com.example.shalenammapride.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class RoleItem(
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun RolesSection(

    onParentClick: () -> Unit,

    onAdminClick: () -> Unit
) {

    Column {

        // 📘 TITLE
        Text(
            text = "Continue As",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(18.dp))

        // 👨‍👩‍👧 PARENT CARD
        RoleCard(

            role = RoleItem(
                title = "Parent",
                subtitle = "Track student updates & activities",
                icon = Icons.Default.Person,
                color = Color(0xFF1565C0)
            ),

            onClick = onParentClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🛠 ADMIN CARD
        RoleCard(

            role = RoleItem(
                title = "Admin",
                subtitle = "Manage school announcements & reports",
                icon = Icons.Default.AdminPanelSettings,
                color = Color(0xFFD81B60)
            ),

            onClick = onAdminClick
        )
    }
}

@Composable
fun RoleCard(

    role: RoleItem,

    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },

        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🎨 ICON BOX
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                role.color.copy(alpha = 0.15f),
                                role.color.copy(alpha = 0.05f)
                            )
                        ),

                        shape = RoundedCornerShape(22.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = role.icon,
                    contentDescription = null,
                    tint = role.color,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(18.dp))

            // 📄 TEXTS
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = role.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = role.subtitle,
                    color = Color.Gray
                )
            }
        }
    }
}