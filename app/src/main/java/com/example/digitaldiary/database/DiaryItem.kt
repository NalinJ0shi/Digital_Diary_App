// app/src/main/java/com/example/digitaldiary/DiaryItem.kt
package com.example.digitaldiary.database

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            .wrapContentHeight()
            .padding(bottom = 12.dp, start = 8.dp, end = 8.dp)
            .combinedClickable(
                onClick = { if (showDeleteIcon) showDeleteIcon = false else onEdit(entry) },
                onLongClick = { showDeleteIcon = true }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        // Change defaultElevation to 0.dp to completely remove the box shadow
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF64748B), // Darker gray for clear slate contrast
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF1E293B) // Dark slate primary font
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = entry.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF475569) // Darker readable body gray text
                    // 3. Max lines and text truncation modifiers completely removed to enable natural layout wrapping
                )
            }
            if (showDeleteIcon) {
                IconButton(
                    onClick = { onDeleteRequest(entry) },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp)
                ) {
                    Icon(Icons.Default.Delete, "Delete", tint = Color(0xFFF87171))
                }
            }
        }
    }
}