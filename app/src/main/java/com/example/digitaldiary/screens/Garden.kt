package com.example.digitaldiary.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
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
    streakCount: Int, // This now represents total unique days logged!
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
    var isLevelingUp by remember { mutableStateOf(false) }

    var visualProgressOverride by remember { mutableStateOf<Float?>(null) }

    // 1. Progress math is now entirely based on increments of 7 unique days logged
    val finalizedFraction = remember(streakCount) {
        if (streakCount == 0) 0f
        else {
            val remainder = streakCount % 7
            if (remainder == 0 && streakCount > 0) 1f else remainder / 7f
        }
    }

    var targetProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(streakCount, finalizedFraction) {
        if (finalizedFraction > 0f && !isLevelingUp) {
            targetProgress = (finalizedFraction - 1f / 7f).coerceAtLeast(0f)
            delay(200)
            targetProgress = finalizedFraction
        } else if (!isLevelingUp) {
            targetProgress = 0f
        }
    }

    val streakProgress by animateFloatAsState(
        targetValue = visualProgressOverride ?: targetProgress,
        animationSpec = if (visualProgressOverride != null) {
            snap()
        } else {
            tween(durationMillis = 1500, easing = FastOutSlowInEasing)
        },
        label = "StreakFill"
    )

    // 2. LEVEL-UP SEQUENCER: Safely resets progress behind the white screen
    LaunchedEffect(streakCount, finalizedFraction) {
        if (finalizedFraction >= 0.99f && currentPlantTier < 8 && !isLevelingUp) {
            isLevelingUp = true

            delay(1700)
            showLevelUpScreen = true

            delay(2000)

            visualProgressOverride = 0f
            onUnlockPlant(currentPlantTier) // Level up the plant tier permanently

            delay(200)

            visualProgressOverride = null
            showLevelUpScreen = false
            isLevelingUp = false
        }
    }

    // 3. MOOD HISTORY LOGIC: Grabs the last 5 logs to create a reflection strip
    val recentMoods = remember(entries) {
        entries.take(5).map { entry ->
            // Convert numerical mood rating into a friendly text emoji representation
            when (entry.dayRating) {
                in 0..20 -> "😢"
                in 21..40 -> "🙁"
                in 41..60 -> "😐"
                in 61..80 -> "🙂"
                else -> "😄"
            }
        }.reversed() // Keeps them chronologically left-to-right
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

                    // --- THE RIVE PLANT ANIMATION ---
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
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
                                        val appliedProgress = if (showLevelUpScreen && visualProgressOverride == 0f) 0f else streakProgress * 100f
                                        view.setNumberState(stateMachine, "Number 1", appliedProgress)
                                    } catch (e: Exception) {
                                        println("RIVE ERROR: Input parameters mapping failed!")
                                    }
                                }
                            )
                        }
                    }

                    // --- THE NEW MILESTONE CARD WITH MOOD HISTORY STRIP ---
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
                                text = "Plant Tier $currentPlantTier (Total Days Logged: $streakCount)",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF6EBE80)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // REPLACED ROW: Shows rolling emotional footprint instead of rigid calendar days
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                                    .padding(bottom = 12.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Recent Moods: ",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFA0A0A0)
                                )
                                Spacer(modifier = Modifier.width(4.dp))

                                if (recentMoods.isEmpty()) {
                                    Text("No entries yet", fontSize = 14.sp, color = Color(0xFFA0A0A0))
                                } else {
                                    recentMoods.forEach { emoji ->
                                        Text(text = emoji, fontSize = 22.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                }
                            }

                            // PROGRESS BAR FILL SCREEN
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
                                    contentDescription = "Milestone Leaf",
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

            // VISUAL LEVEL UP OVERLAY REVEAL
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