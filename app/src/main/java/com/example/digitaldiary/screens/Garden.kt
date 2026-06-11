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

//github.com/NalinJ0shi/Digital_Diary_App
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
    val shiftedDays = remember(streakCount) {
        val standardWeek = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")

        // Find today's real weekday index (Sun = 1, Mon = 2 ... Sat = 7)
        val todayWeekdayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1

        if (streakCount <= 1) {
            // If there's no streak or it's Day 1, today's weekday becomes Day 1 (the first column)
            val shiftOffset = todayWeekdayIndex
            standardWeek.drop(shiftOffset) + standardWeek.take(shiftOffset)
        } else {
            // Find how many days back this current 7-day row page block started
            val daysBackToStart = (streakCount - 1) % 7

            // Subtract that offset to calculate the weekday index when the streak segment began
            val startWeekdayIndex = (todayWeekdayIndex - daysBackToStart + 7) % 7

            // Slice and rotate the list so the streak start day moves to the front index 0
            standardWeek.drop(startWeekdayIndex) + standardWeek.take(startWeekdayIndex)
        }
    }

    // The active highlighted item index always moves sequentially from 0 to 6 in sync with progress
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

    LaunchedEffect(showLevelUpScreen) {
        if (showLevelUpScreen) {
            delay(600)
            onUnlockPlant(currentPlantTier)
            delay(2000)
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
                        val riveResource = when (currentPlantTier) {
                            1 -> R.raw.treeyo
                            2 -> R.raw.treeyo2
                            3 -> R.raw.tree3
                            4 -> R.raw.tree4
                            5 -> R.raw.tree5
                            6 -> R.raw.tree6
                            7 -> R.raw.tree7
                            8 -> R.raw.tree8
                            else -> R.raw.treeyo
                        }
                        val stateMachine = "State Machine 1"

                        key(currentPlantTier) {
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
                                        view.setNumberState(stateMachine, "Number 1", streakProgress * 100f)
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
                                // Updated to iterate over our new shiftedDays collection instead of hardcoded strings
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