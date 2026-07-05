package com.example.digitaldiary.screens

import com.nalin.my_digitaldiary.R
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.digitaldiary.ui.theme.JosefinSans
import kotlinx.coroutines.delay

enum class FourSevenEightPhase(val durationMs: Long, val text: String) {
    IN(4000L, "Breathe In... (4s)"),
    HOLD(7000L, "Hold... (7s)"),
    OUT(8000L, "Breathe Out... (8s)")
}

@Composable
fun FourSevenEightBreathingScreen(
    onBackClick: () -> Unit
) {
    var currentPhase by remember { mutableStateOf(FourSevenEightPhase.IN) }
    var riveView by remember { mutableStateOf<RiveAnimationView?>(null) }

    // Normalized progress tracking (0.0f to 1.0f) for the current phase
    var phaseProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        delay(1000L)
        while (true) {
            // --- PHASE 1: IN (4 Seconds) ---
            currentPhase = FourSevenEightPhase.IN
            riveView?.fireState("State Machine 1", "next_phase") // Update Rive if needed

            val inDuration = FourSevenEightPhase.IN.durationMs
            val stepsIn = (inDuration / 16L).toInt()
            for (i in 0..stepsIn) {
                phaseProgress = i.toFloat() / stepsIn
                delay(16L)
            }

            // --- PHASE 2: HOLD (7 Seconds) ---
            currentPhase = FourSevenEightPhase.HOLD
            riveView?.fireState("State Machine 1", "next_phase")

            val holdDuration = FourSevenEightPhase.HOLD.durationMs
            val stepsHold = (holdDuration / 16L).toInt()
            for (i in 0..stepsHold) {
                phaseProgress = i.toFloat() / stepsHold
                delay(16L)
            }

            // --- PHASE 3: OUT (8 Seconds) ---
            currentPhase = FourSevenEightPhase.OUT
            riveView?.fireState("State Machine 1", "next_phase")

            val outDuration = FourSevenEightPhase.OUT.durationMs
            val stepsOut = (outDuration / 16L).toInt()
            for (i in 0..stepsOut) {
                phaseProgress = i.toFloat() / stepsOut
                delay(16L)
            }
        }
    }

    // Color Theme Customization
    val baseGrayColor = Color(0xFFE2E8F0).copy(alpha = 0.5f)
    val activeBlueColor = Color(0xFF38BDF8).copy(alpha = 0.6f) // Swapped to Blue to contrast Box Breathing
    val activeOrangeColor = Color(0xFFFB923C).copy(alpha = 0.6f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAF9E7))
    ) {
        Scaffold(
            containerColor = Color.Transparent
        )
        { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                Spacer(modifier = Modifier.weight(1f))

                // Interactive Progress Track
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
                        val totalLength = pathMeasure.length
                        val currentDistance = totalLength * phaseProgress

                        // Choose colors based on active pacing states
                        val (fillColor, remColor) = when (currentPhase) {
                            FourSevenEightPhase.IN -> Pair(activeBlueColor, baseGrayColor)
                            FourSevenEightPhase.HOLD -> Pair(activeOrangeColor, activeBlueColor)
                            FourSevenEightPhase.OUT -> Pair(baseGrayColor, activeOrangeColor)
                        }

                        // Segment A: Drawing active progress
                        if (currentDistance > 0f) {
                            val activePath = Path()
                            pathMeasure.getSegment(0f, currentDistance, activePath, true)
                            drawPath(
                                path = activePath,
                                color = fillColor,
                                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                            )
                        }

                        // Segment B: Drawing remaining background track
                        if (currentDistance < totalLength) {
                            val remainingPath = Path()
                            pathMeasure.getSegment(currentDistance, totalLength, remainingPath, true)
                            drawPath(
                                path = remainingPath,
                                color = remColor,
                                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                            )
                        }
                    }

                    // Animation Engine Box
                    Box(modifier = Modifier.size(300.dp)) {
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                RiveAnimationView(context).apply {
                                    setRiveResource(
                                        resId = R.raw.breatho1, // Uses same layout rig asset
                                        stateMachineName = "State Machine 1"
                                    )
                                    riveView = this
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // the breath in breath out text
                Text(
                    text = currentPhase.text,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = JosefinSans
                    ),
                    color = Color(0xFF475569)
                )
                Spacer(modifier = Modifier.weight(1f))

                //the back icon
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .size(60.dp)
                        .background(Color(0xFF475569), shape = CircleShape) // Change hex color here for full background control!
                        .clickable { onBackClick() }
                ) { Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White // Change icon arrow color here!
                ) }
            }
        }
    }
}
