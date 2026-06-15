package com.example.digitaldiary.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.digitaldiary.main.CustomBottomNavBar
import com.example.digitaldiary.main.UniversalBackgroundWrapper
import com.example.digitaldiary.ui.theme.JosefinSans
import kotlin.math.abs

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
    var selectedExercise by remember { mutableStateOf<BreathingExercise?>(null) }

    if (selectedExercise == null) {
        // Show the Carousel view with the Bottom Navigation Bar
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
                    ExerciseCarouselView(
                        onExerciseSelected = { exercise ->
                            selectedExercise = exercise
                        },
                        onBackClick = onBack
                    )
                }
            }
        }
    } else {
        // Navigate completely to your new screen file, hiding the bottom bar
        ActiveBreathingScreen(
            exerciseTitle = selectedExercise!!.title,
            onBackClick = { selectedExercise = null }
        )
    }
}

@Composable
fun ExerciseCarouselView(
    onExerciseSelected: (BreathingExercise) -> Unit,
    onBackClick: () -> Unit
) {
    val exercises = listOf(
        BreathingExercise("Box Breathing", true),
        BreathingExercise("4-7-8 Method", false),
        BreathingExercise("Lion's Breath", false)
    )

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val cardWidth = 230.dp
    val cardHeight = 340.dp
    val horizontalPadding = (screenWidth - cardWidth) / 2
    val listState = rememberLazyListState()

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
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.potted_plant),
                    contentDescription = "Navigate to Garden",
                    tint = Color(0xFFFFFFFF),
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(0.5f))

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
                            color = Color(0xFF1E293B),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}