package com.example.digitaldiary

import com.nalin.my_digitaldiary.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView

@Composable
fun MoodSliderScreen(
    // UPDATED: Now expects only Content (String) and Mood (Int)
    onSaveEntry: (String, Int) -> Unit
) {
    var moodLevel by remember { mutableFloatStateOf(5f) }
    var entryContent by remember { mutableStateOf("") }

    // NEW: Tracks if the user has touched the slider yet
    var hasSlided by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "How are you feeling today?",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AndroidView(
            modifier = Modifier.size(200.dp),
            factory = { context ->
                RiveAnimationView(context).apply {
                    setRiveResource(
                        resId = R.raw.mascot1,
                        stateMachineName = "State Machine 1"
                    )
                }
            },
            update = { view ->
                val mappedRiveValue = (moodLevel - 1f) * (100f / 9f)
                try {
                    view.setNumberState("State Machine 1", "Number 1", mappedRiveValue)
                } catch (e: Exception) {
                    println("RIVE ERROR: Could not find State Machine or Input name!")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Slider(
            value = moodLevel,
            onValueChange = {
                moodLevel = it
                hasSlided = true // Triggered when they move the slider
            },
            valueRange = 1f..10f,
            steps = 8,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
        )

        Text(
            text = "Level: ${moodLevel.toInt()}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // NEW: Only show the text field and button IF they have slided
        if (hasSlided) {
            TextField(
                value = entryContent,
                onValueChange = { entryContent = it },
                placeholder = { Text("Write your diary entry...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp), // Rounds the corners
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,   // Removes the sharp bottom line
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onSaveEntry(entryContent, moodLevel.toInt()) },
                modifier = Modifier.size(width = 200.dp, height = 50.dp)
            ) {
                Text("Save Entry")
            }
        }
    }
}