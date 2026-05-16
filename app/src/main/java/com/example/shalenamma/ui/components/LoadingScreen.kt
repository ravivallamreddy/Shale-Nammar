package com.example.shalenammapride.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {

    // 🔄 ANIMATION
    val infiniteTransition =
        rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(

        initialValue = 0.85f,

        targetValue = 1.15f,

        animationSpec = infiniteRepeatable(

            animation = tween(
                durationMillis = 900
            ),

            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1565C0),
                        Color(0xFF42A5F5)
                    )
                )
            ),

        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🏫 LOGO
            Surface(
                modifier = Modifier.scale(scale),

                shape = CircleShape,

                color = Color.White.copy(alpha = 0.2f)
            ) {

                Box(
                    modifier = Modifier.size(130.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "🏫",
                        style = MaterialTheme.typography.displayLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 🔷 APP NAME
            Text(
                text = "Shale-Namma Pride",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Our School • Our Pride",
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // ⏳ LOADING
            CircularProgressIndicator(
                color = Color.White
            )
        }
    }
}