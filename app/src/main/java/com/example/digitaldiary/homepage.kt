package com.example.digitaldiary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onNavigateToAdd: () -> Unit) {
    // Keeping the "Secret Sauce" gradient consistent
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFF3E8FF), // Soft lavender
            Color(0xFFFFFFFF)  // White
        )
    )

    Scaffold(
        // The Floating Action Button (FAB)
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = Color(0xFF9333EA),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Entry")
            }
        }
    ) { padding ->
        // The Background Container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "My Digital Diary",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF4B218B)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No secrets yet... hit the + to write one!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}