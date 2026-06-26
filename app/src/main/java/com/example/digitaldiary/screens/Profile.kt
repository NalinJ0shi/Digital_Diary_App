package com.example.digitaldiary.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitaldiary.miscellaneousBS.AppDesignTokens
import com.example.digitaldiary.miscellaneousBS.CustomBottomNavBar
import com.example.digitaldiary.miscellaneousBS.UniversalBackgroundWrapper
import com.example.digitaldiary.miscellaneousBS.UniversalDesignCard
import com.nalin.my_digitaldiary.R

@Composable
fun ProfileScreen(
    riveResId: Int,
    entriesCount: Int,
    plantsCount: Int,
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit,
    onNavigateToPlantCollection: () -> Unit
) {
    val primaryGreenColor = Color(0xFFFFFFFF)
    val scrollState = rememberScrollState()

    UniversalBackgroundWrapper {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                // --- UNIFIED TOP NAVIGATION ACTION BAR ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 46.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.potted_plant),
                            contentDescription = "Go Home",
                            modifier = Modifier.size(32.dp),
                            tint = primaryGreenColor
                        )
                    }
                }

                // Inner content column with vertical scrolling enabled
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp)
                        .padding(top = 8.dp, bottom = 100.dp),
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
                        // Stat Card 1 - Entries
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
                                    text = entriesCount.toString(),
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

                        // Stat Card 2 - Plants
                        UniversalDesignCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(110.dp),
                            onClick = onNavigateToPlantCollection
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = plantsCount.toString(),
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Theme Card with Left Icon
                    UniversalDesignCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        onClick = { /* TODO: Theme Action */ }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.potted_plant), // Replace with your theme icon filename
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    text = "Theme",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray,
                                    fontSize = 18.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "View Theme",
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- 3. Support Section ---
                    Text(
                        text = "Support",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Write a review Card
                    UniversalDesignCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        onClick = { /* TODO: Review Action */ }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.thumbs_up), // Replace with your review icon filename
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    text = "Write a review",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray,
                                    fontSize = 18.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "Write a review",
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Invite a Friend Card
                    UniversalDesignCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp),
                        onClick = { /* TODO: Invite Action */ }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.users), // Replace with your invite icon filename
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Gray
                                )
                                Text(
                                    text = "Invite a Friend",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray,
                                    fontSize = 18.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "Invite a Friend",
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- CENTERED BOTTOM ICONS ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* TODO: Icon 1 Action */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.behance_logo),
                                contentDescription = "Social 1",
                                modifier = Modifier.size(28.dp),
                                tint = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(onClick = { /* TODO: Icon 2 Action */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.instagram_logo),
                                contentDescription = "Social 2",
                                modifier = Modifier.size(28.dp),
                                tint = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(onClick = { /* TODO: Icon 3 Action */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.linkedin_logo__1_),
                                contentDescription = "Social 3",
                                modifier = Modifier.size(28.dp),
                                tint = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                CustomBottomNavBar(
                    riveResId = riveResId,
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