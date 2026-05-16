package com.example.shalenamma.ui.screens.parent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronLeft
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    navController: NavController,
    rtVM: com.example.shalenamma.viewmodel.RealtimeDatabaseViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var feedbackText by remember { mutableStateOf("") }
    var anonymous by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Academics") }
    
    val categories = listOf("Academics", "Facilities", "Teacher Conduct", "Safety")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopAppBar(
            title = { Text("Quick Feedback", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Rounded.ChevronLeft, contentDescription = "Back", modifier = Modifier.size(32.dp))
                }
            }
        )

        Column(modifier = Modifier.padding(20.dp)) {
            Text("What would you like to share?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            // CATEGORY CHIPS
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.take(2).forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.takeLast(2).forEach { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // INPUT
            OutlinedTextField(
                value = feedbackText,
                onValueChange = { if (it.length <= 500) feedbackText = it },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                placeholder = { Text("Tell us more...") },
                shape = RoundedCornerShape(16.dp),
                supportingText = {
                    Text("${feedbackText.length}/500", modifier = Modifier.fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.End)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ANONYMOUS TOGGLE
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = anonymous, onCheckedChange = { anonymous = it })
                Text("Submit anonymously", style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { 
                    rtVM.submitFeedback(selectedCategory, feedbackText)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Submit Feedback", fontWeight = FontWeight.Bold)
            }
        }
    }
}
