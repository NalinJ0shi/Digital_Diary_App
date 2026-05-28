package com.example.digitaldiary.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitaldiary.AppDesignTokens
import com.example.digitaldiary.AssetPosition
import com.example.digitaldiary.BalancedContentRow
import com.example.digitaldiary.CustomBottomNavBar
import com.example.digitaldiary.UniversalDesignCard

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
    val tokens = AppDesignTokens
    val cardStyles = AppDesignTokens.CardStyles

    Scaffold(
        containerColor = tokens.backgroundColor, // Light gray/cream background
        bottomBar = {
            CustomBottomNavBar(
                selectedTab = 3,
                onCalendarClick = onCalendarClick,
                onChartClick = onChartClick,
                onGameClick = onGameClick,
                onProfileClick = onProfileClick,
                onAddEntry = onAddEntry
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // HEADER
            Text(
                text = "Account",
                color = tokens.primaryText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
            )

            // ==================================================
            // TYPE A: LARGE PRIMARY CARD (Identity)
            // Text on left, Illustration on right
            // ==================================================
            UniversalDesignCard(config = cardStyles.LargePrimary, onClick = { }) {
                BalancedContentRow(
                    title = "choco",
                    subtitle = "Free Plan",
                    assetPosition = AssetPosition.Right
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(tokens.backgroundColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Avatar", tint = tokens.secondaryText, modifier = Modifier.size(32.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "My records",
                color = tokens.secondaryText,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth().padding(start = 8.dp, bottom = 16.dp)
            )

            // ==================================================
            // TYPE B: MEDIUM STATISTIC CARDS (2-Column Grid)
            // ==================================================
            UniversalDesignCard(config = cardStyles.MediumStat) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Column: Recorded Days
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Recorded days", color = tokens.secondaryText, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "365", color = tokens.primaryText, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    // Soft Divider
                    Box(modifier = Modifier.height(40.dp).width(1.dp).background(tokens.dividerColor))

                    // Right Column: Photos
                    Column(modifier = Modifier.weight(1f).padding(start = 24.dp)) {
                        Text(text = "Photos", color = tokens.secondaryText, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "12", color = tokens.primaryText, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ==================================================
            // TYPE C: LIST CARDS (Widgets, Invite a friend)
            // Icon left, Chevron right
            // ==================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tokens.surfaceColor, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp)) // Clips the ripple effect to the rounded corners
            ) {
                DailyBeanListRow(title = "Widgets", icon = Icons.Default.Star, showDivider = true)
                DailyBeanListRow(title = "Invite a Friend", icon = Icons.Default.Favorite, showDivider = false)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// Reusable Sub-component for Type C (List Cards)
@Composable
fun DailyBeanListRow(
    title: String,
    icon: ImageVector,
    showDivider: Boolean
) {
    val tokens = AppDesignTokens

    Column(modifier = Modifier.fillMaxWidth().clickable { /* Actions */ }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp), // Generous padding
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tokens.secondaryText, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                text = title,
                color = tokens.primaryText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = tokens.secondaryText, modifier = Modifier.size(20.dp))
        }
        if (showDivider) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), thickness = 1.dp, color = tokens.dividerColor)
        }
    }
}