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
import com.example.digitaldiary.CustomBottomNavBar
import com.example.digitaldiary.UniversalBackgroundWrapper
import com.example.digitaldiary.database.DiaryEntry

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
    // 1. The Trigger: This flips to true the millisecond the screen opens
    var startAnimation by remember { mutableStateOf(false) }

    // 2. The Animation: Smoothly glides from 0% to your target streak (e.g., 5 out of 7 days)
    val streakProgress by animateFloatAsState(
        targetValue = if (startAnimation) (5f / 7f) else 0f,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
        label = "StreakFill"
    )

    // Fires instantly on screen load for testing
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    UniversalBackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                CustomBottomNavBar(
                    selectedTab = 0, // 0 highlights the Home/Garden icon
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

                // --- POSITION CONTROLLER ---
                // By using weight(1f), this pushes the card all the way to the bottom.
                // If you want to move the card higher up manually, delete .weight(1f)
                // and replace it with a fixed height, for example: Spacer(modifier = Modifier.height(200.dp))
                Spacer(modifier = Modifier.weight(1f))

                // --- DUOLINGO STYLE STREAK CARD ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    // Made the card slightly transparent (alpha = 0.85f) to see hills behind it
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
                            color = Color(0xFF6EBE80) // Green text to match the bar
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
                                    fontSize = 12.sp,
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
                                    // Thinner bar since we don't have circles anymore
                                    .height(16.dp)
                                    .background(Color(0xFFF4F5F7).copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                            )

                            // The Animated Fill Bar (Green)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction = streakProgress) // Width driven by the animation
                                    .height(16.dp)
                                    .background(Color(0xFF6EBE80), RoundedCornerShape(8.dp))
                                    .align(Alignment.CenterStart)
                            )
                        }
                    }
                }

                // Add a tiny bit of breathing room right above the bottom nav bar
                Spacer(modifier = Modifier.height(220.dp))
            }
        }
    }
}