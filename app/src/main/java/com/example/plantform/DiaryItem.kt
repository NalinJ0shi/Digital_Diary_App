package com.example.plantform

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DiaryItem(entry: DiaryEntry) {
    // Helper to format date like "Monday, January 12, 2026"
    val dateStr = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(Date(entry.timestamp))

    // Helper to pick the mood icon
    val moodEmoji = when (entry.mood) {
        "happy" -> "🙂"
        "sad" -> "🙁"
        "neutral" -> "😐"
        else -> ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp), // Spacing between cards
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header: Date + Mood (Left) | Edit + Delete (Right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left side: Date and Mood
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange, // Calendar icon
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                    if (moodEmoji.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = moodEmoji, fontSize = 16.sp)
                    }
                }

                // Right side: Action Buttons
                Row {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp).padding(end = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFDC2626), // Tailwind Red-600 hex
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF111827) // Tailwind Gray-900
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Content
            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563), // Tailwind Gray-600
                lineHeight = 24.sp
            )
        }
    }
}