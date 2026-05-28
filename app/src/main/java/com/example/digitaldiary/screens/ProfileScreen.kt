package com.example.digitaldiary.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitaldiary.CustomBottomNavBar
import com.nalin.my_digitaldiary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    // --- EXACT MINIMALIST DARK DESIGN THEME COLORS ---
    val backgroundColor = Color(0xFF12161A) // Deep structural panel charcoal background
    val surfaceColor = Color(0xFF1A1F26)     // Menu item tile fill
    val borderColor = Color(0xFF282F38)     // Distinct outline borders
    val premiumBannerColor = Color(0xFF1E2640) // Royal dark blue for premium card

    val textColor = Color(0xFFECEFF4)        // Bright white/gray text
    val mutedTextColor = Color(0xFF7E8996)   // Subtle description text
    val accentPurple = Color(0xFF9F7AEA)     // Locked premium purple color

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                // Top Header containing Settings icon, Avatar, and Streak stats on one row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Settings Gear Icon (Left side)
                    IconButton(onClick = onBack) { // Restored your home navigation!
                        Icon(
                            painter = painterResource(id = R.drawable.potted_plant),
                            contentDescription = "Go Home",
                            modifier = Modifier.size(28.dp), // Made it slightly larger to stand out
                            tint = textColor
                        )
                    }

                    // Centered Profile Avatar Placeholder Circle
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(surfaceColor)
                            .border(1.dp, borderColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.size(22.dp),
                            tint = mutedTextColor
                        )
                    }

                    // Streak Count Component (Right side)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .background(surfaceColor, RoundedCornerShape(12.dp))
                            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Streak",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFFFBBF24) // Gold Star
                        )
                        Text(
                            text = "365",
                            color = textColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            bottomBar = {
                CustomBottomNavBar(
                    selectedTab = 3, // Profile index position
                    onCalendarClick = onCalendarClick,
                    onChartClick = onChartClick,
                    onGameClick = onGameClick,
                    onProfileClick = onProfileClick,
                    onAddEntry = onAddEntry
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // --- PREMIUM ADVERTISEMENT BANNER ---
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    color = premiumBannerColor,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, borderColor) // FIXED: Changed BoxStroke to BorderStroke
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Locked Indicator Icon Area
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0x1F9F7AEA)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Premium Feature",
                                tint = accentPurple,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Banner Promotion Messages
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Unlock Premium Plan",
                                color = textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Get unlimited icons, custom fonts, themes & advanced charts analysis.",
                                color = mutedTextColor,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Upgrade Call-To-Action Click Button
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Upgrade link",
                            tint = mutedTextColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // --- MENUS INTEGRATED BOX CONTAINER ---
                // Groups options tightly inside full-width blocks like DailyBean
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surfaceColor, RoundedCornerShape(16.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                ) {
                    DailyBeanMenuItem(
                        icon = Icons.Default.Build,
                        title = "App Themes",
                        textColor = textColor,
                        mutedTextColor = mutedTextColor,
                        showDivider = true
                    )
                    DailyBeanMenuItem(
                        icon = Icons.Default.Menu,
                        title = "Custom Icon Packs",
                        textColor = textColor,
                        mutedTextColor = mutedTextColor,
                        showDivider = true
                    )
                    DailyBeanMenuItem(
                        icon = Icons.Default.Notifications,
                        title = "Daily Reminders",
                        textColor = textColor,
                        mutedTextColor = mutedTextColor,
                        showDivider = false
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- SECONDARY SETTINGS OPTION BLOCK ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surfaceColor, RoundedCornerShape(16.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                ) {
                    DailyBeanMenuItem(
                        icon = Icons.Default.Info,
                        title = "Help & Feedback Support",
                        textColor = textColor,
                        mutedTextColor = mutedTextColor,
                        showDivider = true
                    )
                    DailyBeanMenuItem(
                        icon = Icons.Default.Share,
                        title = "Share Feelsy App",
                        textColor = textColor,
                        mutedTextColor = mutedTextColor,
                        showDivider = false
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- DESTRUCTIVE LOG OUT BUTTON ---
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(surfaceColor, RoundedCornerShape(16.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                ) {
                    DailyBeanMenuItem(
                        icon = Icons.Default.ExitToApp,
                        title = "Log Out Account",
                        textColor = Color(0xFFF87171), // Clean red indicator color tone
                        mutedTextColor = mutedTextColor,
                        showDivider = false
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- EXACT DAILYBEAN ROW LIST SEPARATOR BLUEPRINT ---
@Composable
fun DailyBeanMenuItem(
    icon: ImageVector,
    title: String,
    textColor: Color,
    mutedTextColor: Color,
    showDivider: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Handle MenuItem Execution Click Block */ }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = textColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = title,
                    color = textColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Navigate option",
                tint = mutedTextColor,
                modifier = Modifier.size(18.dp)
            )
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 1.dp,
                color = Color(0xFF282F38) // Distinct clean divider stroke
            )
        }
    }
}