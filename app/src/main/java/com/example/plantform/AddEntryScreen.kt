package com.example.plantform

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddEntryScreen(
    existingEntry: DiaryEntry? = null,
    onSave: (String) -> Unit, // Changed: Only accepts Content now!
    onCancel: () -> Unit
) {
    // No more Title state!
    var content by remember { mutableStateOf(existingEntry?.content ?: "") }
    val PurpleDiary = Color(0xFF9333EA)

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {

        // Title Input is GONE. Just the content now.
        TextField(
            value = content,
            onValueChange = { content = it },
            placeholder = { Text("Write about your day...", fontSize = 18.sp) },
            modifier = Modifier.fillMaxWidth().weight(1f),
            textStyle = TextStyle(fontSize = 18.sp, lineHeight = 28.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) { Text("Cancel", color = Color.Gray) }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                // Only save if content isn't empty
                onClick = { if (content.isNotBlank()) onSave(content) },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleDiary),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(if (existingEntry == null) "Save Entry" else "Update Entry")
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}