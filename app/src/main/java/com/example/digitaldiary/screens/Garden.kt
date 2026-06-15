package com.example.digitaldiary.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.example.digitaldiary.main.CustomBottomNavBar
import com.example.digitaldiary.main.UniversalBackgroundWrapper
import com.example.digitaldiary.database.DiaryEntry
import com.nalin.my_digitaldiary.R
import kotlinx.coroutines.delay
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenScreen(
    entries: List<DiaryEntry>,
    streakCount: Int,
    canAdd: Boolean,
    isDarkMode: Boolean,
    currentPlantTier: Int,
    onUnlockPlant: (Int) -> Unit,
    onToggleTheme: () -> Unit,
    onAddEntry: () -> Unit,
    onOpenCalendar: () -> Unit,
    onEditEntry: (DiaryEntry) -> Unit,
    onDeleteEntry: (DiaryEntry) -> Unit,
    onNavigateToChart: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var showLevelUpScreen by remember { mutableStateOf(false) }

    // Calculate the 0.0f to 1.0f progress bar fill from the active streak count
    val realProgressFraction = remember(streakCount) {
        if (streakCount == 0) 0f
        else {
            val remainder = streakCount % 7
            if (remainder == 0 && streakCount > 0) 1f else remainder / 7f
        }
    }

    // NEW SHIFTER LOGIC: Dynamically rearranges the text labels based on when the streak began
    val shiftedDays = remember(streakCount, entries) {
        val standardWeek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")

        val lastEntryCalendar = Calendar.getInstance().apply {
            entries.firstOrNull()?.let { mostRecentEntry ->
                timeInMillis = mostRecentEntry.timestamp
            }
        }
        val todayWeekdayIndex = lastEntryCalendar.get(Calendar.DAY_OF_WEEK) - 1

        if (streakCount <= 1) {
            val shiftOffset = todayWeekdayIndex
            standardWeek.drop(shiftOffset) + standardWeek.take(shiftOffset)
        } else {
            val daysBackToStart = (streakCount - 1) % 7
            val startWeekdayIndex = (todayWeekdayIndex - daysBackToStart + 7) % 7
            standardWeek.drop(startWeekdayIndex) + standardWeek.take(startWeekdayIndex)
        }
    }

    val currentDayIndex = remember(streakCount) {
        if (streakCount == 0) -1
        else {
            val position = (streakCount - 1) % 7
            position.coerceIn(0, 6)
        }
    }

    val streakProgress by animateFloatAsState(
        targetValue = realProgressFraction,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "StreakFill",
        finishedListener = { finalValue ->
            if (finalValue >= 0.99f && currentPlantTier < 8) {
                showLevelUpScreen = true
            }
        }
    )

    // FIXED: Delayed Tier State Logic
    LaunchedEffect(showLevelUpScreen) {
        if (showLevelUpScreen) {
            // 1. Keep the overlay visible for 2 seconds while the completed plant sits behind it
            delay(2000)

            // 2. Safely upgrade the plant tier in the database/state behind the opaque overlay
            onUnlockPlant(currentPlantTier)

            // 3. Give Compose a tiny moment to process the asset swap and reset the progress fraction
            delay(100)

            // 4. Fade out the overlay to reveal the brand-new baby plant tier starting at 0%
            showLevelUpScreen = false
        }
    }

    UniversalBackgroundWrapper {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Color.Transparent,
                bottomBar = {
                    CustomBottomNavBar(
                        selectedTab = 0,
                        onCalendarClick = onOpenCalendar,
                        onChartClick = onNavigateToChart,
                        onGameClick = onNavigateToGame,
                        onProfileClick = onNavigateToProfile,
                        onAddEntry = onAddEntry
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // --- 3. THE RIVE PLANT ANIMATION ---
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        // FIXED: Ensure Rive stays locked onto the current tier until the level up sequence officially completes
                        val activeTier = currentPlantTier

                        val riveResource = when (activeTier) {
                            1 -> R.raw.tree01
                            2 -> R.raw.tree02
                            3 -> R.raw.tree03
                            4 -> R.raw.tree04
                            5 -> R.raw.tree05
                            6 -> R.raw.tree06
                            7 -> R.raw.delete02
                            8 -> R.raw.delete01
                            else -> R.raw.tree01
                        }
                        val stateMachine = "State Machine 1"

                        key(activeTier) {
                            AndroidView(
                                modifier = Modifier.size(400.dp),
                                factory = { context ->
                                    RiveAnimationView(context).apply {
                                        setRiveResource(
                                            resId = riveResource,
                                            stateMachineName = stateMachine
                                        )
                                    }
                                },
                                update = { view ->
                                    try {
                                        // If the level up screen is currently covering everything,
                                        // we visually drop the animation state down to 0% if the resource updates early
                                        val appliedProgress = if (showLevelUpScreen && realProgressFraction == 1f) 100f else streakProgress * 100f
                                        view.setNumberState(stateMachine, "Number 1", appliedProgress)
                                    } catch (e: Exception) {
                                        println("RIVE ERROR: Input parameters mapping failed!")
                                    }
                                }
                            )
                        }
                    }

                    // --- 4. THE DUOLINGO STYLE STREAK CARD ---
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8D2C8).copy(alpha = 0.9f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            Text(
                                text = "Week $currentPlantTier Growing (Streak: $streakCount Days)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6EBE80)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                shiftedDays.forEachIndexed { index, day ->
                                    val isActive = index == currentDayIndex

                                    Text(
                                        text = day,
                                        fontSize = if (isActive) 22.sp else 15.sp,
                                        fontWeight = if (isActive) FontWeight.ExtraBold else FontWeight.Bold,
                                        color = if (isActive) Color(0xFF6EBE80) else Color(0xFFA0A0A0)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                val unfilledColor = Color(0xFFCFE1D4)
                                val filledColor = Color(0xFF6EBE80)

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(end = 12.dp)
                                        .height(16.dp)
                                        .background(unfilledColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = streakProgress)
                                        .padding(end = 12.dp)
                                        .height(16.dp)
                                        .background(filledColor, RoundedCornerShape(8.dp))
                                        .align(Alignment.CenterStart)
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.leaf__1_),
                                    contentDescription = "Streak Goal Leaf",
                                    modifier = Modifier
                                        .size(38.dp)
                                        .align(Alignment.CenterEnd)
                                        .offset(x = 4.dp),
                                    tint = if (streakProgress >= 0.99f) filledColor else unfilledColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(174.dp))
                }
            }

            // --- 5. VISUAL LEVEL UP OVERLAY REVEAL ---
            AnimatedVisibility(
                visible = showLevelUpScreen,
                enter = fadeIn(animationSpec = tween(600)),
                exit = fadeOut(animationSpec = tween(800)),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.95f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "PLANT UNLOCKED!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF6EBE80)
                    )
                }
            }
        }
    }
}