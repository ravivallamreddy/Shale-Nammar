package com.example.shalenamma

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import androidx.work.*
import com.example.shalenamma.viewmodel.LanguageViewModel
import com.example.shalenamma.navigation.AppNavigation
import com.example.shalenamma.ui.theme.ShaleNammaTheme
import com.example.shalenamma.utils.NotificationHelper

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Permission handled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Optional: Keep splash screen visible until some condition is met
        // splashScreen.setKeepOnScreenCondition { ... }
        
        lifecycleScope.launch {
            NotificationHelper.createNotificationChannel(this@MainActivity)
            WorkManager.getInstance(this@MainActivity).cancelAllWork()
        }
        checkNotificationPermission()

        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val langViewModel: LanguageViewModel = viewModel()
            val isDarkMode by viewModel.isDarkMode
            val userRole by viewModel.userRole

            // Subscribe to FCM Topics based on role
            LaunchedEffect(userRole) {
                if (userRole == UserRole.ADMIN) {
                    FirebaseMessaging.getInstance().subscribeToTopic("admins")
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("parents")
                } else {
                    FirebaseMessaging.getInstance().subscribeToTopic("parents")
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("admins")
                }
            }

            ShaleNammaTheme(darkTheme = isDarkMode) {
                AppNavigation(mainVM = viewModel, langVM = langViewModel)
            }
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
