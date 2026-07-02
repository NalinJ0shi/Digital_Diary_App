package com.example.digitaldiary

import com.nalin.my_digitaldiary.R
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import kotlin.math.floor
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodSliderScreen(
    existingEntry: DiaryEntry? = null,
    onSaveEntry: (String, Int) -> Unit,
    onBack: () -> Unit
)
{
    // Start at 3f (The physical Center Dot)
    var moodLevel by remember { mutableFloatStateOf(existingEntry?.dayRating?.toFloat() ?: 3f) }
    var entryContent by remember { mutableStateOf(existingEntry?.content ?: "") }
    var hasSlided by remember { mutableStateOf(existingEntry != null) }

    // CONTROL PALETTE: Define your exact 5 colors here (one for each Rive state)
    val colorStep1 = Color(0xFF6C6363) // State 1: Very Bad
    val colorStep2 = Color(0xFFB09090) // State 2: Somewhat Bad
    val colorStep3 = Color(0xFF5B5454) // State 3: Neutral
    val colorStep4 = Color(0xFF8FB26F) // State 4: Somewhat Good
    val colorStep5 = Color(0xFF9D61B8) // State 5: Very Good

    // MATHEMATICAL BLENDING
    val targetBackgroundColor = remember(moodLevel) {
        val baseInteger = floor(moodLevel).toInt()
        val fraction = moodLevel - baseInteger

        when (baseInteger) {
            1 -> lerp(colorStep1, colorStep2, fraction)
            2 -> lerp(colorStep2, colorStep3, fraction)
            3 -> lerp(colorStep3, colorStep4, fraction)
            4 -> lerp(colorStep4, colorStep5, fraction)
            5 -> colorStep5
            else -> colorStep3
        }
    }
    val animatedBackgroundColor by animateColorAsState(
        targetValue = targetBackgroundColor,
        animationSpec = tween(durationMillis = 100),
        label = "BackgroundColorAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackgroundColor) // Apply the dynamic blended color here
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "What's Hanging?",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        AndroidView(
            modifier = Modifier.size(200.dp),
            factory = { context ->
                RiveAnimationView(context).apply {
                    setRiveResource(
                        resId = R.raw.gp2,
                        stateMachineName = "State Machine 1"
                    )
                }
            },
            update = { view ->
                // Smoothly maps the 1-5 data scale to the 0-100 Rive timeline
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
            valueRange = 1f..5f, // DATA: 5 steps for Rive states and DB saving
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(color = Color.White, shape = CircleShape)
                )
            },
            track = {
                // VISUALS: Your 3-dot track design remains intact
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
                            .background(Color.White.copy(alpha = 0.6f))
                    )

                    // Fixed Left Dot (Maps to Step 1)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.White.copy(alpha = 0.8f), shape = CircleShape)
                            .align(Alignment.CenterStart)
                    )

                    // Fixed Center Dot (Maps to Step 3)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.White.copy(alpha = 0.8f), shape = CircleShape)
                            .align(Alignment.Center)
                    )

                    // Fixed Right Dot (Maps to Step 5)
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.White.copy(alpha = 0.8f), shape = CircleShape)
                            .align(Alignment.CenterEnd)
                    )
                }
            }
        )

        // VISUALS: Your 3 labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Bad", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            Text(text = "Neutral", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
            Text(text = "Good", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.8f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Smart Reveal Text Field & Buttons
        if (hasSlided) {
            TextField(
                value = entryContent,
                onValueChange = { entryContent = it },
                placeholder = { Text("...", color = Color.White.copy(alpha = 0.5f)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.15f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            // BUTTON ROW: Cancel and Save Actions side-by-side
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = { onSaveEntry(entryContent, moodLevel.roundToInt()) },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Save Entry")
                }
            }
        } else {
            // Standalone Cancel button if they haven't interacted yet
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier
                    .width(150.dp)
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White
                ),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
            ) {
                Text("Cancel")
            }
        }
    }
}