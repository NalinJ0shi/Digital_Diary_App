// app/src/main/java/com/example/digitaldiary/DiaryItem.kt
package com.example.digitaldiary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

    val dateStr = remember(entry.timestamp) {
        val now = Calendar.getInstance()
        val entryDate = Calendar.getInstance().apply { timeInMillis = entry.timestamp }
        val isToday = now.get(Calendar.YEAR) == entryDate.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == entryDate.get(Calendar.DAY_OF_YEAR)
        now.add(Calendar.DAY_OF_YEAR, -1)
        val isYesterday = now.get(Calendar.YEAR) == entryDate.get(Calendar.YEAR) &&
                now.get(Calendar.DAY_OF_YEAR) == entryDate.get(Calendar.DAY_OF_YEAR)
        when {
            isToday -> "Today"
            isYesterday -> "Yesterday"
            else -> SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault()).format(Date(entry.timestamp))
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .padding(bottom = 4.dp, start = 8.dp, end = 8.dp)
            .combinedClickable(
                onClick = { if (showDeleteIcon) showDeleteIcon = false else onEdit(entry) },
                onLongClick = { showDeleteIcon = true }
            ),
        colors = CardDefaults.cardColors(
            // Glassmorphism effect
            containerColor = Color.White.copy(alpha = 0.12f)
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFF1F5F9) // Light text
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = entry.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFCBD5E1), // Softer light text
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (showDeleteIcon) {
                IconButton(
                    onClick = { onDeleteRequest(entry) },
                    modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp)
                ) {
                    Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFF87171))
                }
            }
        }
    }
}