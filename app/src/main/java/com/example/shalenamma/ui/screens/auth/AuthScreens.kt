package com.example.shalenamma.ui.screens.auth

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shalenamma.ui.theme.BrandBlue
import com.example.shalenamma.viewmodel.AuthViewModel
import com.example.shalenamma.viewmodel.LanguageViewModel

@Composable
fun AuthEntryScreen(
    onAuthSuccess: () -> Unit,
    authVM: AuthViewModel = viewModel(),
    langVM: LanguageViewModel = viewModel()
) {
    var step by remember { mutableStateOf(1) } // 1: Role Select, 2: Login/Signup
    var selectedRole by remember { mutableStateOf("Parent") }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // Language Toggle at top
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp).padding(top = 24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { langVM.toggleLanguage() }) {
                Text(
                    text = if (langVM.isKannada) "English" else "ಕನ್ನಡ",
                    color = BrandBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        AnimatedContent(
            targetState = step,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "AuthTransition"
        ) { currentStep ->
            when (currentStep) {
                1 -> RoleSelectionView(
                    langVM = langVM,
                    onRoleSelected = { role ->
                        selectedRole = role
                        step = 2
                    }
                )
                2 -> LoginSignupView(
                    role = selectedRole,
                    langVM = langVM,
                    authVM = authVM,
                    onBack = { step = 1 },
                    onSuccess = onAuthSuccess
                )
            }
        }
    }
}

@Composable
fun RoleSelectionView(langVM: LanguageViewModel, onRoleSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Rounded.School,
            contentDescription = null,
            tint = BrandBlue,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Shale-Namma Pride",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = BrandBlue
        )
        Text(
            text = langVM.text("Our School, Our Pride", "ನಮ್ಮ ಶಾಲೆ, ನಮ್ಮ ಹೆಮ್ಮೆ"),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = langVM.text("Who is logging in?", "ಯಾರು ಲಾಗಿನ್ ಮಾಡುತ್ತಿದ್ದಾರೆ?"),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        RoleCard(
            title = langVM.text("Parent", "ಪೋಷಕರು"),
            subtitle = langVM.text("Track your child's progress", "ಮಕ್ಕಳ ಪ್ರಗತಿಯನ್ನು ಗಮನಿಸಿ"),
            icon = Icons.Rounded.People,
            color = BrandBlue,
            onClick = { onRoleSelected("Parent") }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        RoleCard(
            title = langVM.text("School Admin", "ಶಾಲಾ ನಿರ್ವಾಹಕರು"),
            subtitle = langVM.text("Manage school activities", "ಶಾಲಾ ಚಟುವಟಿಕೆಗಳನ್ನು ನಿರ್ವಹಿಸಿ"),
            icon = Icons.Rounded.AdminPanelSettings,
            color = MaterialTheme.colorScheme.secondary,
            onClick = { onRoleSelected("Admin") }
        )
    }
}

@Composable
fun LoginSignupView(
    role: String,
    langVM: LanguageViewModel,
    authVM: AuthViewModel,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var displayName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var isSignup by remember { mutableStateOf(false) }

    val isLoggedIn by authVM.isLoggedIn.collectAsState()
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) onSuccess()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = onBack, modifier = Modifier.align(Alignment.Start)) {
            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
        }
        
        Text(
            text = if (isSignup) langVM.text("Create Account", "ಖಾತೆ ತೆರೆಯಿರಿ") else langVM.text("Welcome Back", "ಮರಳಿ ಸ್ವಾಗತ"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = langVM.text("Continuing as $role", "$role ಆಗಿ ಮುಂದುವರಿಯಿರಿ"),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )
        
        if (isSignup) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (authVM.isLoading) {
            CircularProgressIndicator(color = BrandBlue)
        } else {
            Button(
                onClick = { 
                    if (isSignup) authVM.signup(email, password, displayName, username, role)
                    else authVM.login(email, password)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(if (isSignup) "Sign Up" else "Login")
            }
        }

        authVM.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isSignup = !isSignup }) {
            Text(if (isSignup) "Already have an account? Login" else "New here? Create an account")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("OR", color = Color.Gray)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = { /* Google Auth demo */ },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(Icons.Rounded.Login, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continue with Google")
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
