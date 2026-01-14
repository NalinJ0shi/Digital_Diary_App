package com.example.digitaldiary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiaryItem(
    entry: DiaryEntry,
    onEdit: (DiaryEntry) -> Unit,
    onDeleteRequest: (DiaryEntry) -> Unit
) {
    var showDeleteIcon by remember(entry.id) { mutableStateOf(false) }
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
            .combinedClickable(
                onClick = {
                    if (showDeleteIcon) showDeleteIcon = false
                    else onEdit(entry)
                },
                onLongClick = { showDeleteIcon = true }
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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

                if (showDeleteIcon) {
                    IconButton(
                        onClick = { onDeleteRequest(entry) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFDC2626)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B5563),
                lineHeight = 20.sp,
                maxLines = 3, // Show only the first 3 lines
                overflow = TextOverflow.Ellipsis // Add "..." if the text is too long
            )
        }
    }
}