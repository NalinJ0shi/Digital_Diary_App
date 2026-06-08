package com.example.digitaldiary.screens

import com.nalin.my_digitaldiary.R
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.example.digitaldiary.CustomBottomNavBar
import com.example.digitaldiary.UniversalBackgroundWrapper
import com.example.digitaldiary.ui.theme.JosefinSans
import kotlinx.coroutines.launch
import kotlin.math.abs

// Simple data class to hold our exercise info
data class BreathingExercise(
    val title: String,
    val isAvailable: Boolean
)

@Composable
fun BreathingScreen(
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    // State to track if we are in the carousel view (null) or inside an exercise
    var selectedExercise by remember { mutableStateOf<BreathingExercise?>(null) }

    // 1. Wrap the layout inside the DesignSystem Scenery wrapper
    UniversalBackgroundWrapper {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
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

            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                if (selectedExercise == null) {
                    // Show the Carousel if no exercise is selected
                    ExerciseCarouselView(
                        onExerciseSelected = { exercise ->
                            selectedExercise = exercise
                        }
                    )
                } else {
                    // Show your Rive Animation if an exercise is selected
                    ActiveBreathingView(
                        onClose = { selectedExercise = null }
                    )
                }
            }
        }
    }
}

// Updated data class: Description removed

@Composable
fun ExerciseCarouselView(onExerciseSelected: (BreathingExercise) -> Unit) {
    // List of cards for the carousel (updated to match new data class)
    val exercises = listOf(
        BreathingExercise("Box Breathing", true),
        BreathingExercise("4-7-8 Method", false),
        BreathingExercise("Lion's Breath", false)
    )

    // Sizing Calculations
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Width ~40% of screen & Reduced height
    val cardWidth = 230.dp
    val cardHeight = 340.dp

    // Padding to ensure the first and last items can snap exactly to the center
    val horizontalPadding = (screenWidth - cardWidth) / 2

    val listState = rememberLazyListState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Breathing Exercises",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                fontFamily = JosefinSans
            ),
            modifier = Modifier
                .padding(start = 34.dp, bottom = 32.dp)
        )
        
        LazyRow(
            state = listState,
            // Snapping behavior so it locks onto the center card
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(exercises) { index, exercise ->
                Card(
                    modifier = Modifier
                        .width(cardWidth)
                        .height(cardHeight)
                        // Dynamic Scaling & Positioning based on scroll offset
                        .graphicsLayer {
                            val itemInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
                            if (itemInfo != null) {
                                val viewportCenter = listState.layoutInfo.viewportEndOffset / 2
                                val itemCenter = itemInfo.offset + itemInfo.size / 2
                                val distance = abs(viewportCenter - itemCenter).toFloat()
                                val maxDistance = listState.layoutInfo.viewportEndOffset.toFloat()

                                // Scale down items as they move away from the center (Max shrink to 80%)
                                val scale = 1f - (distance / maxDistance) * 0.4f
                                val coercedScale = scale.coerceIn(0.80f, 1f)
                                scaleX = coercedScale
                                scaleY = coercedScale

                                // Push side items down slightly to make the center item look "higher"
                                translationY = distance * 0.1f
                            }
                        }
                        // Applied custom colors here based on availability
                        .clickable(enabled = exercise.isAvailable) {
                            onExerciseSelected(exercise)
                        },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (exercise.isAvailable) Color(0xFFC0E6BA)
                        else Color(0xFFEAF9E7)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = exercise.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = JosefinSans
                            ),
                            // I set the text color to a dark slate gray for great contrast against both green backgrounds
                            color = Color(0xFF1E293B),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveBreathingView(onClose: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val breathDurationMillis = 4000

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back Button to return to Carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onClose) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back to exercises",
                    tint = Color(0xFF475569)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Interactive Tracking Stack (Your original code)
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { progress.value },
                modifier = Modifier.size(260.dp),
                strokeWidth = 14.dp,
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
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
                    fontWeight = FontWeight.Medium,
                    fontFamily = JosefinSans
                ),
                color = Color(0xFF475569)
            )
        } else {
            Spacer(modifier = Modifier.height(34.dp))
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
