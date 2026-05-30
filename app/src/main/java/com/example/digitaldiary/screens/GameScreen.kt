package com.example.digitaldiary.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.example.digitaldiary.CustomBottomNavBar
import com.example.digitaldiary.UniversalBackgroundWrapper
import kotlinx.coroutines.launch

@Composable
fun BreathingScreen(
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val breathDurationMillis = 4000

    // Create a custom solid moody color brush for your game screen background field
    val gameScreenBackgroundBrush = remember {
        Brush.linearGradient(
            colors = listOf(Color(0xFF0F172A), Color(0xFF0F172A))
        )
    }

    // 1. Wrap the layout inside the DesignSystem Scenery wrapper
    UniversalBackgroundWrapper(
        backgroundBrush = gameScreenBackgroundBrush
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent, // MUST remain transparent to let design system hills shine through
            bottomBar = {
                CustomBottomNavBar(
                    selectedTab = 2,
                    onCalendarClick = onCalendarClick,
                    onChartClick = onChartClick,
                    onGameClick = onGameClick,
                    onProfileClick = onProfileClick,
                    onAddEntry = onAddEntry
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Interactive Tracking Stack
                Box(contentAlignment = Alignment.Center) {

                    // Your exact outer ring tracker indicator component
                    CircularProgressIndicator(
                        progress = { progress.value },
                        modifier = Modifier.size(260.dp),
                        strokeWidth = 14.dp,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    )

                    Box(modifier = Modifier.size(200.dp)) {
                        // 2. Your exact original animation file asset logic restored safely
                        AndroidView(
                            modifier = Modifier.fillMaxSize(),
                            factory = { context ->
                                RiveAnimationView(context).apply {
                                    setRiveResource(
                                        resId = R.raw.breath, // RESTORED: Your exact original asset reference file
                                        stateMachineName = "State Machine 1"
                                    )
                                }
                            },
                            update = { view ->
                                try {
                                    // RESTORED: Your original boolean trigger input configuration parameters
                                    view.setBooleanState("State Machine 1", "IsPressed", isPressed)
                                } catch (e: Exception) {
                                    println("RIVE ERROR: Could not find State Machine or Input name!")
                                }
                            }
                        )

                        // Glass interaction shielding target layer overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            isPressed = true
                                            scope.launch {
                                                progress.animateTo(
                                                    targetValue = 1f,
                                                    animationSpec = tween(durationMillis = breathDurationMillis, easing = LinearEasing)
                                                )
                                            }

                                            tryAwaitRelease()

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

                Spacer(modifier = Modifier.height(40.dp))

                // Guided breathing label indicators
                if (progress.value > 0f || isPressed) {
                    Text(
                        text = if (isPressed) "Breath in!" else "Breath out!",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFFE2E8F0)
                    )
                } else {
                    Spacer(modifier = Modifier.height(34.dp))
                }
            }
        }
    }
}