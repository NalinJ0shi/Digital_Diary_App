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
import com.example.digitaldiary.main.AppDesignTokens
import com.example.digitaldiary.main.CustomBottomNavBar
import com.example.digitaldiary.main.UniversalBackgroundWrapper
import com.example.digitaldiary.main.UniversalDesignCard

@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit,
    onNavigateToPlantCollection: () -> Unit // new
) {
    UniversalBackgroundWrapper {
        Box(modifier = Modifier.fillMaxSize()) {

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
                        .height(100.dp),
                    onClick = { /* TODO: Account Action */ }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(
                                text = "Username",
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

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Go to Account",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // --- 2. My Records Section ---
                Text(
                    text = "My records",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

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
                                text = "14",
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

                    // Stat Card 2 - The "Plants" Card
                    UniversalDesignCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp),
                        onClick = onNavigateToPlantCollection // new
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "5",
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp,
                                color = AppDesignTokens.primaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Plants", // new
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

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

            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                CustomBottomNavBar(
                    selectedTab = 3,
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