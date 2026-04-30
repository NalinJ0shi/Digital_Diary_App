package com.example.digitaldiary

import com.nalin.my_digitaldiary.R
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView

@Composable
fun MoodSliderScreen(
    onSaveEntry: (Int) -> Unit // Callback to handle saving or navigating away
) {
    // Setting up the 1-to-10 scale, starting in the middle
    var moodLevel by remember { mutableFloatStateOf(5f) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "How are you feeling today?",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // The Rive Mascot integration
        // The Rive Mascot integration
        AndroidView(
            modifier = Modifier.size(250.dp),
            factory = { context ->
                RiveAnimationView(context).apply {
                    setRiveResource(
                        resId = R.raw.mascot, // <-- MAKE SURE THIS MATCHES YOUR FILE NAME
                        stateMachineName = "State Machine 1" // <-- MUST MATCH EXACTLY
                    )
                }
            },
            update = { view ->
                val mappedRiveValue = (moodLevel - 1f) * (100f / 9f)

                // We wrap this in a try-catch so the app doesn't crash if the names don't match!
                try {
                    view.setNumberState(
                        "State Machine 1", // <-- MUST MATCH EXACTLY
                        "Number 1",      // <-- MUST MATCH EXACTLY WHAT IS IN RIVE
                        mappedRiveValue
                    )
                } catch (e: Exception) {
                    println("RIVE ERROR: Could not find State Machine or Input name!")
                    e.printStackTrace()
                }
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // The Slider
        Slider(
            value = moodLevel,
            onValueChange = { moodLevel = it },
            valueRange = 1f..10f,
            steps = 8, // 8 steps between 1 and 10 snaps the slider exactly to whole numbers
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )

        Text(
            text = "Level: ${moodLevel.toInt()}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { onSaveEntry(moodLevel.toInt()) },
            modifier = Modifier.size(width = 200.dp, height = 50.dp)
        ) {
            Text("Save Mood")
        }
    }
}