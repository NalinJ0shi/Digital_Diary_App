package com.example.digitaldiary

import com.nalin.my_digitaldiary.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import kotlin.math.roundToInt // NEW: Imported the rounding function

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodSliderScreen(
    existingEntry: DiaryEntry? = null,
    onSaveEntry: (String, Int) -> Unit,
    onBack: () -> Unit
) {
    // Start at 3f (The physical Center Dot)
    var moodLevel by remember {
        mutableFloatStateOf(existingEntry?.dayRating?.toFloat() ?: 3f)
    }
    var entryContent by remember {
        mutableStateOf(existingEntry?.content ?: "")
    }
    var hasSlided by remember { mutableStateOf(existingEntry != null) }

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
                        resId = R.raw.ghost2,
                        stateMachineName = "State Machine 1"
                    )
                }
            },
            update = { view ->
                // HIDDEN MATH: Smoothly maps the 1-5 data scale to the 0-100 Rive timeline
                val mappedRiveValue = (moodLevel - 1f) * 25f
                try {
                    view.setNumberState("State Machine 1", "NumberInput", mappedRiveValue)
                } catch (e: Exception) {
                    println("RIVE ERROR: Could not find State Machine or Input name!")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // HIDDEN 5-STEP SLIDER OVER A 3-DOT TRACK
        Slider(
            value = moodLevel,
            onValueChange = {
                moodLevel = it
                hasSlided = true
            },
            valueRange = 1f..5f, // DATA: 5 hidden steps for the database
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            thumb = {
                // The main draggable thumb dot
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(color = Color.Black, shape = CircleShape)
                )
            },
            track = {
                // VISUALS: Your exact 3-dot track design
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Thin horizontal line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color.Black)
                    )

                    // Fixed Left Dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.Black, shape = CircleShape)
                            .align(Alignment.CenterStart)
                    )

                    // Fixed Center Dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.Black, shape = CircleShape)
                            .align(Alignment.Center)
                    )

                    // Fixed Right Dot
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.Black, shape = CircleShape)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        )

        // VISUALS: Your exact 3 labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Bad", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Neutral", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Good", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Smart Reveal Text Field
        if (hasSlided) {
            TextField(
                value = entryContent,
                onValueChange = { entryContent = it },
                placeholder = { Text("Write your diary entry...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                // NEW: Uses roundToInt() to accurately convert the continuous float scale to your 1-5 database integers
                onClick = { onSaveEntry(entryContent, moodLevel.roundToInt()) },
                modifier = Modifier.size(width = 200.dp, height = 50.dp)
            ) {
                Text("Save Entry")
            }
        }
    }
}