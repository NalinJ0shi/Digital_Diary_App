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

//github.com/NalinJ0shi/Digital_Diary_App
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenScreen(
    entries: List<DiaryEntry>,
    streakCount: Int,
    canAdd: Boolean,
    isDarkMode: Boolean,
    currentPlantTier: Int,           // NEW
    onUnlockPlant: (Int) -> Unit,    // NEW
    onToggleTheme: () -> Unit,
    onAddEntry: () -> Unit,
    onOpenCalendar: () -> Unit,
    onEditEntry: (DiaryEntry) -> Unit,
    onDeleteEntry: (DiaryEntry) -> Unit,
    onNavigateToChart: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var progressTarget by remember { mutableFloatStateOf(0f) }
    var showLevelUpScreen by remember { mutableStateOf(false) }

    val currentDayIndex = (((progressTarget * 7).toInt()) - 1).coerceIn(0, 6)
    val streakProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "StreakFill",
        finishedListener = { finalValue ->
            // Trigger Level Up sequence if streak hits max on Tier 1
            if (finalValue >= 0.99f && currentPlantTier == 1) {
                showLevelUpScreen = true
            }
        }
    )

    LaunchedEffect(showLevelUpScreen) {
        if (showLevelUpScreen) {
            delay(600)
            onUnlockPlant(1)
            progressTarget = 0f
            delay(2000)
            showLevelUpScreen = false
        }
    }

    LaunchedEffect(Unit) {
        progressTarget = 1f/7f
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
                        val riveResource = if (currentPlantTier == 1) R.raw.treeyo else R.raw.treeyo2
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
                                        // new
                                        // This sends the slider progress (0-100) to BOTH treeyo and treeyo2
                                        // so neither of them freeze!
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
                                text = if (currentPlantTier == 1) "Week 1 Growing" else "Week 2 Growing",
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
                                val days = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
                                days.forEachIndexed { index, day ->
                                    // This condition evaluates true for the new dynamic index value
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