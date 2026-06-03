package com.example.digitaldiary.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitaldiary.AppDesignTokens
import com.example.digitaldiary.CustomBottomNavBar
import com.example.digitaldiary.UniversalBackgroundWrapper
import com.example.digitaldiary.UniversalDesignCard

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    UniversalBackgroundWrapper {
        Box(modifier = Modifier.fillMaxSize()) {

            // Main Content Column - Changed to Start alignment for left-aligned text
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 100.dp, bottom = 100.dp),
                horizontalAlignment = Alignment.Start
            ) {

                // --- 1. Account Section ---
                Text(
                    text = "Account",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                UniversalDesignCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp), // Large account card
                    onClick = { /* TODO: Account Action */ }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left side of the card: Username and ID
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = "Username", // Replace with actual user state later
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ID: 987654321",
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )
                        }

                        // Right side of the card: Chevron
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Go to Account",
                            tint = Color.Gray
                        )
                    }
                }

                // Generous spacing between sections
                Spacer(modifier = Modifier.height(20.dp))

                // --- 2. My Records Section ---
                Text(
                    text = "My records",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 2-Column Grid using a Row with weighted items
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Stat Card 1
                    UniversalDesignCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        onClick = { /* TODO: Action */ }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "14", // Replace with real diary entry count
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = AppDesignTokens.primaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Entries",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Stat Card 2
                    UniversalDesignCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        onClick = { /* TODO: Action */ }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "5", // Replace with real streak count
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = AppDesignTokens.primaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Plants",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Generous spacing before the next section
                Spacer(modifier = Modifier.height(20.dp))

                // --- 3. About Section ---
                Text(
                    text = "More",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(14.dp))

                UniversalDesignCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    onClick = { /* TODO: About Action */ }
                ) { Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Theme",
                            fontWeight = FontWeight.Bold,
                            color = Color.DarkGray,
                            fontSize = 18.sp
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "View About",
                            tint = Color.Gray
                        )
                    }}
                Spacer(modifier = Modifier.height(14.dp))
                UniversalDesignCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    onClick = { /* TODO: About Action */ }
                ) { Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Notifications",
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        fontSize = 18.sp
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "View About",
                        tint = Color.Gray
                    )
                }}
            }

            // Bottom Navigation Bar
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                CustomBottomNavBar(
                    selectedTab = 4,
                    onCalendarClick = onCalendarClick,
                    onChartClick = onChartClick,
                    onGameClick = onGameClick,
                    onProfileClick = onProfileClick,
                    onAddEntry = onAddEntry
                )
            }
        }
    }
}
