package com.example.plantform

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddEntryScreen(onSave: (String, String) -> Unit, onCancel: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val PurpleDiary = Color(0xFF9333EA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Title Input - No border, big bold text
        TextField(
            value = title,
            onValueChange = { title = it },
            placeholder = { Text("Title", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Content Input - Clean writing canvas
        TextField(
            value = content,
            onValueChange = { content = it },
            placeholder = { Text("Write about your day...", fontSize = 18.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textStyle = TextStyle(fontSize = 18.sp, lineHeight = 28.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        // Navigation Row at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onCancel) {
                Text("Cancel", color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { if (title.isNotBlank()) onSave(title, content) },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleDiary),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Save Entry")
            }
        }
    }
}