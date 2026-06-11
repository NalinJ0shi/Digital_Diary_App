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
import com.example.digitaldiary.main.CustomBottomNavBar
import com.example.digitaldiary.main.UniversalBackgroundWrapper
import com.example.digitaldiary.ui.theme.JosefinSans
import kotlinx.coroutines.launch
import kotlin.math.abs

// Added animationRes parameter to map unique files to each exercise card
data class BreathingExercise(
    val title: String,
    val isAvailable: Boolean,
    val animationRes: Int
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
    var selectedExercise by remember { mutableStateOf<BreathingExercise?>(null) }

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
                    ExerciseCarouselView(
                        onExerciseSelected = { exercise -> selectedExercise = exercise }
                    )
                } else {
                    ActiveBreathingView(
                        exercise = selectedExercise!!,
                        onClose = { selectedExercise = null }
                    )
                }
            }
        }
    }
}

@Composable
fun ExerciseCarouselView(onExerciseSelected: (BreathingExercise) -> Unit) {
    // Linked 3 specific Rive raw assets down to your specific design cards
    // PLACEHOLDER NOTE: Swap R.raw.breath1 out for your custom filename variants if named differently (e.g., breath2, breath3)
    val exercises = listOf(
        BreathingExercise("Box Breathing", true, R.raw.breath2),
        BreathingExercise("4-7-8 Method", false, R.raw.breath4),
        BreathingExercise("Lion's Breath", false, R.raw.breath5)
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val cardWidth = 230.dp
    val cardHeight = 340.dp
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
            modifier = Modifier.padding(start = 34.dp, bottom = 24.dp)
        )

        LazyRow(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(exercises) { index, exercise ->
                val isCardCentered = remember(listState.firstVisibleItemIndex) {
                    listState.firstVisibleItemIndex == index
                }

                Card(
                    modifier = Modifier
                        .width(cardWidth)
                        .height(cardHeight)
                        .graphicsLayer {
                            val itemInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull { it.index == index }
                            if (itemInfo != null) {
                                val viewportCenter = listState.layoutInfo.viewportEndOffset / 2
                                val itemCenter = itemInfo.offset + itemInfo.size / 2
                                val distance = abs(viewportCenter - itemCenter).toFloat()
                                val maxDistance = listState.layoutInfo.viewportEndOffset.toFloat()

                                val scale = 1f - (distance / maxDistance) * 0.4f
                                val coercedScale = scale.coerceIn(0.80f, 1f)
                                scaleX = coercedScale
                                scaleY = coercedScale

                                translationY = distance * 0.1f
                            }
                        }
                        .clickable(enabled = exercise.isAvailable) {
                            onExerciseSelected(exercise)
                        },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (exercise.isAvailable) Color(0xFFC0E6BA) else Color(0xFFEAF9E7)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    // Changed to a Box container so you can freely overlay, float, and coordinate text positions
                    Box(
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ) {

                        // Animation Surface Container
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AndroidView(
                                // =================================================================
                                // COMMIT 1: POSITION & RESIZE THE CAROUSEL RIVE ANIMATION HERE
                                // =================================================================
                                modifier = Modifier
                                    .size(160.dp)
                                    .offset(x = 0.dp, y = -10.dp),
                                // =================================================================
                                factory = { context ->
                                    RiveAnimationView(context).apply {
                                        setRiveResource(
                                            resId = exercise.animationRes, // Loads specific animation resource
                                            stateMachineName = "State Machine 1",
                                            autoplay = false
                                        )
                                    }
                                },
                                update = { view ->
                                    // REMOVED '&& exercise.isAvailable' so animations preview even if the card is locked
                                    if (isCardCentered) {
                                        if (!view.isPlaying) view.play()
                                    } else {
                                        if (view.isPlaying) view.pause()
                                    }
                                }
                            )
                        }

                        // =================================================================
                        // COMMIT 2: MANUALLY CONTROL THE POSITION OF THE TEXT CARD HERE
                        // Alter alignment rules, padding sizes, or add an individual .offset()
                        // =================================================================
                        Text(
                            text = exercise.title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = JosefinSans
                            ),
                            color = Color(0xFF1E293B),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.BottomCenter) // Control baseline attachment alignment point
                                .padding(bottom = 12.dp)       // Manual bottom position spacing tuning
                                .offset(x = 0.dp, y = 0.dp)    // Fine-grained pixel grid layout override values
                        )
                        // =================================================================
                    }
                }
            }
        }
    }
}

@Composable
fun ActiveBreathingView(exercise: BreathingExercise, onClose: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val breathDurationMillis = 4000

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
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
                                resId = exercise.animationRes, // Loads specific animation resource inside screen active loop
                                stateMachineName = "State Machine 1"
                            )
                        }
                    },
                    update = { view ->
                        try {
                            view.setBooleanState("State Machine 1", "IsPressed", isPressed)
                        } catch (e: Exception) {
                            println("RIVE ERROR: Input parameters mapping failed!")
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