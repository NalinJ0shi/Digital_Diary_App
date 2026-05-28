package com.example.digitaldiary.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.digitaldiary.AppDesignTokens
import com.example.digitaldiary.UniversalDesignCard
import com.example.digitaldiary.AssetPosition
import com.example.digitaldiary.BalancedContentRow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nalin.my_digitaldiary.R
import com.example.digitaldiary.CustomBottomNavBar
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.foundation.clickable

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
    // Extract definitions cleanly from our single truth Design Tokens object file
    val tokens = AppDesignTokens
    val cardStyles = AppDesignTokens.CardStyles

    Scaffold(
        containerColor = tokens.backgroundColor,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Replaced Settings Icon with your signature potted-plant vector link
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.potted_plant),
                        contentDescription = "Go Home",
                        modifier = Modifier.size(28.dp),
                        tint = tokens.primaryText
                    )
                }

                Text(
                    text = "Account",
                    color = tokens.primaryText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Streak gold counter module box badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .background(tokens.surfaceColor, RoundedCornerShape(12.dp))
                        .border(1.dp, tokens.borderColor, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Streak",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFBBF24)
                    )
                    Text(text = "365", color = tokens.primaryText, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // ==================================================
            // TYPE A: LARGE PRIMARY CARD (Unlock Premium Advertisement Banner)
            // ==================================================
            UniversalDesignCard(config = cardStyles.LargePrimary) {
                BalancedContentRow(
                    title = "Unlock Premium Plan",
                    subtitle = "Get unlimited icons, custom fonts, themes & advanced charts analysis.",
                    assetPosition = AssetPosition.Left // Figma layout: Icon on LEFT
                ) {
                    // Illustration slot content container mapping
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0x1F9F7AEA)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color(0xFF9F7AEA), modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ==================================================
            // TYPE B: MEDIUM STATISTICS CONTAINER FRAME ("choco" card element link)
            // ==================================================
            UniversalDesignCard(config = cardStyles.MediumStat, onClick = { /* Edit action */ }) {
                BalancedContentRow(
                    title = "choco",
                    assetPosition = AssetPosition.Left
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(tokens.dividerColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = tokens.secondaryText, modifier = Modifier.size(22.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Section Label Divider Row Node
            Text(
                text = "My records",
                color = tokens.secondaryText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth().padding(start = 8.dp, bottom = 12.dp)
            )

            // ==================================================
            // SPLIT STATISTICS ROW COMPONENT (Recorded Days vs Photos split matrix layout)
            // ==================================================
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = tokens.surfaceColor,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, tokens.borderColor)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Column Frame segment: Recorded Days
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Recorded days", color = tokens.secondaryText, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "365", color = tokens.primaryText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }

                    // Strict vertical vector hairline divider separator element
                    Box(modifier = Modifier.height(36.dp).width(1.dp).background(tokens.borderColor))

                    // Right Column Frame segment: Photos
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Photos", color = tokens.secondaryText, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "12", color = tokens.primaryText, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ==================================================
            // TYPE C: PACKED INTEGRATED ROW TILE LIST BLOCK
            // ==================================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tokens.surfaceColor, RoundedCornerShape(16.dp))
                    .border(1.dp, tokens.borderColor, RoundedCornerShape(16.dp))
            ) {
                // Settings Row Item 1
                DailyBeanListItem(title = "App Themes", icon = Icons.Default.Build, showDivider = true)
                // Settings Row Item 2
                DailyBeanListItem(title = "Custom Icon Packs", icon = Icons.Default.Menu, showDivider = true)
                // Settings Row Item 3
                DailyBeanListItem(title = "Daily Reminders", icon = Icons.Default.Notifications, showDivider = false)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Destructive standalone logout listing card panel element block
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tokens.surfaceColor, RoundedCornerShape(16.dp))
                    .border(1.dp, tokens.borderColor, RoundedCornerShape(16.dp))
            ) {
                DailyBeanListItem(
                    title = "Log Out Account",
                    icon = Icons.Default.ExitToApp,
                    titleColor = tokens.destructiveText, // Dynamic red highlight mapping
                    showDivider = false
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// Reusable list sub-component binding logic straight into Master Token Layout configurations
@Composable
fun DailyBeanListItem(
    title: String,
    icon: ImageVector,
    showDivider: Boolean,
    titleColor: Color = AppDesignTokens.primaryText
) {
    Column(modifier = Modifier.fillMaxWidth().clickable { /* Actions */ }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = titleColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, color = titleColor, fontSize = 15.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = AppDesignTokens.secondaryText, modifier = Modifier.size(18.dp))
        }
        if (showDivider) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 1.dp, color = AppDesignTokens.dividerColor)
        }
    }
}