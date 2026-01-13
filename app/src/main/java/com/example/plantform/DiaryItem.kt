package com.example.plantform

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DiaryItem(
    entry: DiaryEntry,
    onEdit: (DiaryEntry) -> Unit
) {
    val dateStr = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(Date(entry.timestamp))
    val moodEmoji = when (entry.mood) {
        "happy" -> "🙂"
        "sad" -> "🙁"
        "neutral" -> "😐"
        else -> ""
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onEdit(entry) }, // Tapping the whole card now triggers editing
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Just Date and Mood now. Clean and simple.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = dateStr, style = MaterialTheme.typography.labelMedium, color = Color.Gray)

                if (moodEmoji.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = moodEmoji, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = entry.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF111827)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563),
                lineHeight = 24.sp
            )
        }
    }
}