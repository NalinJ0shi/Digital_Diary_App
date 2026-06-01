package com.example.digitaldiary.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.example.digitaldiary.CustomBottomNavBar
import com.example.digitaldiary.UniversalBackgroundWrapper
import com.example.digitaldiary.database.DiaryEntry
import com.nalin.my_digitaldiary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GardenScreen(
    entries: List<DiaryEntry>,
    canAdd: Boolean,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onAddEntry: () -> Unit,
    onOpenCalendar: () -> Unit,
    onEditEntry: (DiaryEntry) -> Unit,
    onDeleteEntry: (DiaryEntry) -> Unit,
    onNavigateToChart: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    // 1. The Trigger: Flips to true instantly when the screen opens
    var startAnimation by remember { mutableStateOf(false) }

    // 2. The Shared Animation: Drives BOTH the green bar and the Rive Plant
    val streakProgress by animateFloatAsState(
        targetValue = if (startAnimation) (5f / 7f) else 0f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "StreakFill"
    )

    // Fires instantly on screen load
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    UniversalBackgroundWrapper {
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
                // Using weight(1f) here automatically centers the plant in the empty space
                // AND naturally pushes your frosted Streak Card all the way to the bottom!
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        modifier = Modifier.size(300.dp), // Adjust plant size here
                        factory = { context ->
                            RiveAnimationView(context).apply {
                                setRiveResource(
                                    resId = R.raw.work,
                                    stateMachineName = "State Machine 1"
                                )
                            }
                        },
                        update = { view ->
                            try {
                                // Maps the 0.0-1.0 Compose float directly to your 0-100 Rive input
                                view.setNumberState("State Machine 1", "Number 1", streakProgress * 100f)
                            } catch (e: Exception) {
                                println("RIVE ERROR: Could not find State Machine or Input name!")
                            }
                        }
                    )
                }

                // --- 4. THE DUOLINGO STYLE STREAK CARD ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.85f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = "Keep your garden blooming!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF6EBE80)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Layer 1: The Day Labels (M, T, W...) placed ABOVE the bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val days = listOf("M", "T", "W", "T", "F", "S", "S")
                            days.forEachIndexed { index, day ->
                                Text(
                                    text = day,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (index < 5) Color(0xFF6EBE80) else Color(0xFFA0A0A0)
                                )
                            }
                        }

                        // Layer 2: THE ANIMATED BAR
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            // The Empty Background Track (Light Gray)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                                    .background(Color(0xFFF4F5F7).copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                            )

                            // The Animated Fill Bar (Green)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = streakProgress)
                                    .height(16.dp)
                                    .background(Color(0xFF6EBE80), RoundedCornerShape(8.dp))
                                    .align(Alignment.CenterStart)
                            )
                        }
                    }
                }

                // Breathing room above the nav bar
                Spacer(modifier = Modifier.height(104.dp))
            }
        }
    }
}