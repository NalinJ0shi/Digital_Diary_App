package com.example.digitaldiary

import com.nalin.my_digitaldiary.R
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import kotlinx.coroutines.launch

@Composable
fun BreathingScreen(
    // NEW: We added these parameters so MainActivity has a place to pass the navigation logic
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    // Tracks whether the user is actively holding down on the screen
    var isPressed by remember { mutableStateOf(false) }

    // Controls the smooth filling and draining of the progress ring (0f to 1f)
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Time in milliseconds for a full breath in/out (e.g., 4 seconds)
    val breathDurationMillis = 4000

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 1. THE PROGRESS RING
        CircularProgressIndicator(
            progress = { progress.value },
            modifier = Modifier.size(260.dp),
            strokeWidth = 14.dp,
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        // 2. THE TOUCH AREA & RIVE ANIMATION
        // 2. THE TOUCH AREA & RIVE ANIMATION
        Box(
            modifier = Modifier.size(200.dp)
        ) {
            // LAYER 1 (BOTTOM): The Rive Animation
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    RiveAnimationView(context).apply {
                        setRiveResource(
                            resId = R.raw.breath,
                            stateMachineName = "State Machine 1"
                        )
                    }
                },
                update = { view ->
                    try {
                        view.setBooleanState("State Machine 1", "IsPressed", isPressed)
                    } catch (e: Exception) {
                        println("RIVE ERROR: Could not find State Machine or Input name!")
                    }
                }
            )

            // LAYER 2 (TOP): The Invisible "Glass Shield" Touch Catcher
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                // --- BREATH IN (User holds down) ---
                                isPressed = true
                                scope.launch {
                                    progress.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(durationMillis = breathDurationMillis, easing = LinearEasing)
                                    )
                                }

                                // Pauses here until the user lets go of the screen
                                tryAwaitRelease()

                                // --- BREATH OUT (User releases) ---
                                isPressed = false
                                scope.launch {
                                    progress.animateTo(
                                        targetValue = 0f,
                                        animationSpec = tween(durationMillis = breathDurationMillis, easing = LinearEasing)
                                    )
                                }
                            }
                        )
                    }
            )
        }
    }
}