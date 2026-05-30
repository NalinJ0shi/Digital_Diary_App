package com.example.digitaldiary.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    // 1. We wrap the ENTIRE screen in your Master Wrapper.
    // This instantly paints the gradient and hills in the background.
    UniversalBackgroundWrapper {

        Box(modifier = Modifier.fillMaxSize()) {

            // 2. Main Content Column
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 60.dp, bottom = 100.dp), // Padded so nav bar doesn't overlap
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // --- Profile Header ---
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Avatar",
                        modifier = Modifier.size(50.dp),
                        tint = AppDesignTokens.primaryText
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "My Profile",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = AppDesignTokens.primaryText
                )

                Text(
                    text = "Journaling since 2024",
                    fontSize = 14.sp,
                    color = AppDesignTokens.secondaryText
                )

                Spacer(modifier = Modifier.height(40.dp))

                // --- Settings Cards (Using your Design System) ---
                UniversalDesignCard(
                    // You can pass your specific CardConfiguration here if your component requires it,
                    // or just use standard modifiers if you set default parameters in DesignSystem.kt
                    modifier = Modifier.fillMaxWidth().height(70.dp),
                    onClick = { /* TODO: Settings Action */ }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = AppDesignTokens.accentColor
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                "Settings",
                                fontWeight = FontWeight.SemiBold,
                                color = AppDesignTokens.primaryText
                            )
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Go",
                            tint = AppDesignTokens.secondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                UniversalDesignCard(
                    modifier = Modifier.fillMaxWidth().height(70.dp),
                    onClick = { /* TODO: Notification Action */ }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = AppDesignTokens.accentColor
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                "Notifications",
                                fontWeight = FontWeight.SemiBold,
                                color = AppDesignTokens.primaryText
                            )
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = "Go",
                            tint = AppDesignTokens.secondaryText
                        )
                    }
                }
            }

            // 3. Bottom Navigation Bar layered on top of the wrapper
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