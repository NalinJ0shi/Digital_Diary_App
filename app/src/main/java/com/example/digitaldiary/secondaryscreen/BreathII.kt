package com.example.digitaldiary.screens

import com.nalin.my_digitaldiary.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.example.digitaldiary.miscellaneousBS.UniversalBackgroundWrapper
import com.example.digitaldiary.ui.theme.JosefinSans
import kotlinx.coroutines.delay

enum class BreathingPhase {
    BREATH_OUT, // Phase 1: 50% Green replaces Gray
    BREATH_IN   // Phase 2: Gray replaces 50% Green
}

@Composable
fun ActiveBreathingScreen(
    exerciseTitle: String,
    onBackClick: () -> Unit
) {
    var currentPhase by remember { mutableStateOf(BreathingPhase.BREATH_OUT) }
    var riveView by remember { mutableStateOf<RiveAnimationView?>(null) }

    // Absolute Manual Control Ticker Loop that counts upward infinitely
    var manualProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        delay(1000L)

        while (true) {
            // Determine Phase based on whether the floor of manualProgress is Even or Odd
            val currentStep = manualProgress.toInt()

            if (currentStep % 2 == 0) {
                currentPhase = BreathingPhase.BREATH_OUT

                riveView?.fireState("State Machine 1", "next_phase")

                val target = currentStep + 1f
                while (manualProgress < target) {
                    manualProgress += 0.005f
                    delay(15L)
                }
                manualProgress = target // Lock precisely at the whole number
            } else {
                // ODD steps (1f->2f, 3f->4f, etc.) = Breath In (Gray fills 50% Green)
                currentPhase = BreathingPhase.BREATH_IN

                // Trigger Rive to advance to the Breath In animation state
                riveView?.fireState("State Machine 1", "next_phase")

                val target = currentStep + 1f
                while (manualProgress < target) {
                    manualProgress += 0.005f
                    delay(15L)
                }
                manualProgress = target // Lock precisely at the whole number
            }
        }
    }

    // Color Swatches
    val baseGrayColor = Color(0xFFE2E8F0).copy(alpha = 0.5f)
    val activeGreen50Opacity = Color(0xFF9EF163).copy(alpha = 0.5f)

    UniversalBackgroundWrapper {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Navigation Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to exercises",
                            tint = Color(0xFF475569)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = exerciseTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = JosefinSans,
                            color = Color(0xFF1E293B)
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // The Centered Interaction Zone
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(280.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidthPx = 20.dp.toPx()
                        val cornerRadiusPx = 48.dp.toPx()

                        val padding = strokeWidthPx / 2
                        val squareSize = Size(size.width - (padding * 2), size.height - (padding * 2))

                        val fullPath = Path().apply {
                            addRoundRect(
                                RoundRect(
                                    left = padding, top = padding,
                                    right = padding + squareSize.width, bottom = padding + squareSize.height,
                                    radiusX = cornerRadiusPx, radiusY = cornerRadiusPx
                                )
                            )
                        }

                        val pathMeasure = PathMeasure()
                        pathMeasure.setPath(fullPath, false)

                        // 1. Map progress perfectly into a clockwise 0.0 -> 1.0 fraction
                        val segmentProgress = manualProgress % 1f

                        val splitDistance = if (segmentProgress == 0f && manualProgress > 0f) {
                            pathMeasure.length
                        } else {
                            pathMeasure.length * segmentProgress
                        }

                        // 2. Even/Odd Step Logic for Color Swapping
                        val currentStep = manualProgress.toInt()
                        val (activeFillColor, emptyFillColor) = if (currentStep % 2 == 0) {
                            // Even Step: 50% Green fills up, displacing Gray
                            Pair(activeGreen50Opacity, baseGrayColor)
                        } else {
                            // Odd Step: Gray fills up (erases), displacing 50% Green
                            Pair(baseGrayColor, activeGreen50Opacity)
                        }

                        // 3. Draw Segment A (Active Fill running forward clockwise)
                        if (splitDistance > 0f) {
                            val activePath = Path()
                            pathMeasure.getSegment(
                                startDistance = 0f,
                                stopDistance = splitDistance,
                                destination = activePath,
                                startWithMoveTo = true
                            )
                            drawPath(
                                path = activePath,
                                color = activeFillColor,
                                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                            )
                        }

                        // 4. Draw Segment B (Empty Fill ahead clearing out of the way)
                        if (splitDistance < pathMeasure.length) {
                            val emptyRemainingPath = Path()
                            pathMeasure.getSegment(
                                startDistance = splitDistance,
                                stopDistance = pathMeasure.length,
                                destination = emptyRemainingPath,
                                startWithMoveTo = true
                            )
                            drawPath(
                                path = emptyRemainingPath,
                                color = emptyFillColor,
                                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                            )
                        }
                    }

                    // The Clean Rive Core View
                    Box(modifier = Modifier.size(300.dp)) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                RiveAnimationView(context).apply {
                                    setRiveResource(
                                        resId = R.raw.breatho1,
                                        stateMachineName = "State Machine 1"
                                    )
                                    riveView = this
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Dynamic UI guidance text
                Text(
                    text = when (currentPhase) {
                        BreathingPhase.BREATH_OUT -> "Breathe In..."
                        BreathingPhase.BREATH_IN -> "Breathe Out..."
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = JosefinSans
                    ),
                    color = Color(0xFF475569)
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}