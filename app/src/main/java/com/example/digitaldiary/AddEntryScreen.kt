package com.example.digitaldiary

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
    onSave: (String) -> Unit,
    onCancel: () -> Unit
) {
    var content by remember { mutableStateOf(existingEntry?.content ?: "") }
    val PurpleDiary = Color(0xFF9333EA)

    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {

        // CHANGED: Added a Spacer to push the text area down a bit
        Spacer(modifier = Modifier.height(48.dp))

        TextField(
            value = content,
            onValueChange = { content = it },
            placeholder = {
                Text(
                    text = "\"It's not a bad life, just a bad day.\"",
                    fontSize = 18.sp,
                    color = Color.Black.copy(alpha = 0.2f)
                )
            },
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