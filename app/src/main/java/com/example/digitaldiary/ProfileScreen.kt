package com.example.digitaldiary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    // --- DESIGN COLORS ---
    val topColor = Color(0xFF0F172A)
    val bottomColor = Color(0xFF064E3B)
    val gradientBrush = Brush.verticalGradient(colors = listOf(topColor, bottomColor))

    val textColor = Color(0xFFE2E8F0)
    val mutedTextColor = Color(0xFF94A3B8)
    val cardColor = Color(0x33FFFFFF) // Glassmorphic translucent
    val accentColor = Color(0xFFDAEBC0) // Hill color for accents

    Box(modifier = Modifier.fillMaxSize().background(gradientBrush)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.potted_plant), // Your custom home/back arrow
                            contentDescription = "Go Home",
                            modifier = Modifier.size(32.dp),
                            tint = textColor
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "",
                        color = textColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            bottomBar = {
                CustomBottomNavBar(
                    selectedTab = 3, // Assuming 3 is your Profile tab index
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
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                // --- 1. USER AVATAR ---
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user), // Your custom user vector
                        contentDescription = "Profile Avatar",
                        modifier = Modifier.size(55.dp),
                        tint = bottomColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- 2. USER INFO ---
                Text(
                    text = "Digital Explorer", // Placeholder name
                    color = textColor,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "hello@digitaldiary.com",
                    color = mutedTextColor,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // --- 3. INTERACTIVE MENU LIST ---
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Account Settings",
                    subtitle = "Manage your personal details",
                    textColor = textColor,
                    cardColor = cardColor,
                    accentColor = accentColor
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProfileMenuItem(
                    icon = Icons.Default.Lock,
                    title = "Privacy & Security",
                    subtitle = "Passcode and biometrics",
                    textColor = textColor,
                    cardColor = cardColor,
                    accentColor = accentColor
                )
                Spacer(modifier = Modifier.height(12.dp))

                ProfileMenuItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Daily journaling reminders",
                    textColor = textColor,
                    cardColor = cardColor,
                    accentColor = accentColor
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Destructive Log Out Button
                ProfileMenuItem(
                    icon = Icons.Default.ExitToApp,
                    title = "Log Out",
                    subtitle = "See you later!",
                    textColor = Color(0xFFF44336), // Soft Red
                    cardColor = cardColor,
                    accentColor = accentColor,
                    isDestructive = true
                )
            }
        }
    }
}

// --- REUSABLE MENU COMPONENT ---
@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    textColor: Color,
    cardColor: Color,
    accentColor: Color,
    isDestructive: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: Handle Click Event */ },
        color = cardColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isDestructive) Color(0x33F44336) else Color(0x33DAEBC0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isDestructive) Color(0xFFF44336) else accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Block
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
            }

            // Trailing Navigation Arrow
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Go",
                tint = Color(0xFF94A3B8)
            )
        }
    }
}