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
    // Shows "Monday, January 15"
    val dateStr = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault()).format(Date(entry.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp) // Adjusted height for one-line content
            .padding(bottom = 4.dp, start = 8.dp, end = 8.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(24.dp), // ROUNDED SHAPE RESTORED
                ambientColor = Color.Black.copy(alpha = 0.1f),
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .combinedClickable(
                onClick = {
                    if (showDeleteIcon) showDeleteIcon = false
                    else onEdit(entry)
                },
                onLongClick = { showDeleteIcon = true }
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(24.dp) // ROUNDED SHAPE RESTORED
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.Start, // LEFT ALIGNMENT
                verticalArrangement = Arrangement.Center
            ) {
                // LEFT-ALIGNED HEADER
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dateStr,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.2.sp
                        ),
                        color = Color(0xFF1F2937)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // LEFT-ALIGNED ONE-LINE CONTENT
                Text(
                    text = entry.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4B5563),
                    textAlign = TextAlign.Start, // LEFT ALIGNMENT
                    maxLines = 1, // LIMITED TO ONE LINE
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )
            }

            // DELETE BUTTON
            if (showDeleteIcon) {
                IconButton(
                    onClick = { onDeleteRequest(entry) },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}