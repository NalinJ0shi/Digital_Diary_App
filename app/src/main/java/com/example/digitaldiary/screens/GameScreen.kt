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
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.example.digitaldiary.CustomBottomNavBar
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

    Scaffold(
        // UPDATED: Now uses your exact dark background color
        containerColor = Color(0xFF0F172A),
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

            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress.value },
                    modifier = Modifier.size(260.dp),
                    strokeWidth = 14.dp,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), // Dimmed track for dark mode
                )

                Box(modifier = Modifier.size(200.dp)) {
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

            if (progress.value > 0f || isPressed) {
                Text(
                    text = if (isPressed) "Breath in!" else "Breath out!",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    // UPDATED: Light text to pop against the dark 0xFF0F172A background
                    color = Color(0xFFE2E8F0)
                )
            } else {
                Spacer(modifier = Modifier.height(34.dp))
            }
        }
    }
}