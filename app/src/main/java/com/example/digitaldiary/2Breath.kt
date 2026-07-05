package com.example.digitaldiary

import com.nalin.my_digitaldiary.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import app.rive.runtime.kotlin.core.Fit
import com.example.digitaldiary.screens.ActiveBreathingScreen
import com.example.digitaldiary.screens.FourSevenEightBreathingScreen
import com.example.digitaldiary.ui.theme.JosefinSans
import kotlin.math.abs

data class BreathingExercise(
    val title: String,
    val isAvailable: Boolean
)

@Composable
fun BreathingScreen(
    riveResId: Int,
    themeProfile: AppThemeProfile,
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    var selectedExercise by remember { mutableStateOf<BreathingExercise?>(null) }

    when {
        selectedExercise == null -> {
            UniversalBackgroundWrapper(themeProfile = themeProfile) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.Transparent,
                    bottomBar = {
                        CustomBottomNavBar(
                            selectedTab = 2,
                            riveResId = riveResId,
                            themeProfile = themeProfile,
                            onCalendarClick = onCalendarClick,
                            onChartClick = onChartClick,
                            onGameClick = onGameClick,
                            onProfileClick = onProfileClick,
                            onAddEntry = onAddEntry
                        )
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                        ExerciseCarouselView(
                            onExerciseSelected = { exercise ->
                                selectedExercise = exercise
                            },
                            onBackClick = onBack
                        )
                    }
                }
            }
        }
        selectedExercise?.title == "4-7-8 Method" -> {
            FourSevenEightBreathingScreen(
                onBackClick = { selectedExercise = null }
            )
        }
        else -> {
            ActiveBreathingScreen(
                exerciseTitle = selectedExercise!!.title,
                onBackClick = { selectedExercise = null }
            )
        }
    }
}

@Composable
fun ExerciseCarouselView(
    onExerciseSelected: (BreathingExercise) -> Unit,
    onBackClick: () -> Unit
) {
    val exercises = listOf(
        BreathingExercise("Box Breathing", true),
        BreathingExercise("4-7-8 Method", true),
        BreathingExercise("Lion's Breath", true)
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = 230.dp
    val cardHeight = 340.dp

    // Spacing configuration
    val spaceBetweenItems = 16.dp
    val horizontalPadding = (screenWidth - cardWidth) / 2

    // Calculate structural pixel offset so index 1 lands exactly in the dead center
    val density = LocalDensity.current
    val initialOffsetPx = with(density) {
        val itemSpacingPx = spaceBetweenItems.roundToPx()
        // Pushes it backward into alignment by the structural padding space minus the item spacing gap
        -itemSpacingPx
    }

    // Initialize state targeted directly at index 1 ("4-7-8 Method")
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 1,
        initialFirstVisibleItemScrollOffset = initialOffsetPx
    )

    val centeredItemIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            if (visibleItems.isEmpty()) 1 else { // Fallback defaulting to target index
                val viewportCenter = layoutInfo.viewportEndOffset / 2
                visibleItems.minByOrNull { itemInfo ->
                    val itemCenter = itemInfo.offset + itemInfo.size / 2
                    abs(viewportCenter - itemCenter)
                }?.index ?: 1
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {}

        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            text = "Breathing Exercises",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = Color(0xFF6C7430),
                fontFamily = JosefinSans
            ),
            modifier = Modifier
                .padding(start = 20.dp, bottom = 32.dp)
        )

        LazyRow(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            horizontalArrangement = Arrangement.spacedBy(spaceBetweenItems),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(exercises) { index, exercise ->
                val isCentered = index == centeredItemIndex

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
                        },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFEAF9E7)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val textFontSize = 28.sp
                        val textYOffset = 74.dp

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = textYOffset)
                                .align(Alignment.TopCenter),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = exercise.title,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontSize = textFontSize,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = JosefinSans
                                    ),
                                    color = Color(0xFF1E293B),
                                    textAlign = TextAlign.Center
                                )
                            }

                            if (!exercise.isAvailable) {
                                Spacer(modifier = Modifier.height(62.dp))
                                Text(
                                    text = "coming soon.",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = JosefinSans
                                    ),
                                    color = Color(0xFF64748B),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        if (exercise.isAvailable) {
                            val riveWidth = 340.dp
                            val riveHeight = 340.dp
                            val riveXOffset = 5.dp
                            val riveYOffset = 35.dp

                            AndroidView(
                                modifier = Modifier
                                    .size(width = riveWidth, height = riveHeight)
                                    .offset(x = riveXOffset, y = riveYOffset)
                                    .align(Alignment.Center),
                                factory = { context ->
                                    RiveAnimationView(context).apply {
                                        this.fit = Fit.FILL
                                        this.alignment = app.rive.runtime.kotlin.core.Alignment.CENTER

                                        val resId = when (index) {
                                            0 -> R.raw.card04
                                            1 -> R.raw.card02
                                            else -> R.raw.card03
                                        }

                                        setRiveResource(
                                            resId = resId,
                                            stateMachineName = "State Machine 1"
                                        )
                                    }
                                },
                                update = { view ->
                                    if (isCentered) {
                                        view.play()
                                    } else {
                                        view.pause()
                                    }
                                }
                            )
                        }

                        if (exercise.isAvailable) {
                            Text(
                                text = "tap to open",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = JosefinSans
                                ),
                                color = Color(0xFF413B3B),
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 32.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable(enabled = exercise.isAvailable) {
                                    onExerciseSelected(exercise)
                                }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}