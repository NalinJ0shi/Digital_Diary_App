package com.example.digitaldiary

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.nalin.my_digitaldiary.R
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ProfileScreen(
    riveResId: Int,
    themeProfile: AppThemeProfile,
    entriesCount: Int,
    plantsCount: Int,
    onToggleTheme: (String) -> Unit,
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit,
    onNavigateToPlantCollection: () -> Unit,
    onNavigateToInvite: () -> Unit,
)
{
    val primaryGreenColor = Color(0xFFFFFFFF)
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var showThemeSelection by remember { mutableStateOf(false) }

    UniversalBackgroundWrapper(themeProfile = themeProfile) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (!showThemeSelection) {
                // ---Main column---
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 46.dp, end = 16.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {}

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(horizontal = 24.dp)
                            .padding(top = 8.dp, bottom = 100.dp),
                        horizontalAlignment = Alignment.Start
                    )
                    //Main thing
                    {
                        Spacer(modifier = Modifier.height(20.dp))
                        //My Records
                        Text(
                            text = "My records",
                            fontSize = 33.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            UniversalDesignCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(110.dp),
                                onClick = { /* Action */ }
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
                            UniversalDesignCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(110.dp),
                                onClick = { /* Action disabled as we discarded the plant library navigation */ }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = plantsCount.toString(), // This parameter now receives the notesCount value from MainActivity
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 28.sp,
                                        color = AppDesignTokens.primaryText
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Notes", // Updated UI display name
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        UniversalDesignCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),
                            onClick = { showThemeSelection = true } // <-- OPENS THE GALLERY
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
                        //Support
                        Text(
                            text = "Support",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        UniversalDesignCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),
                            onClick = { openPlayStoreForReview(context) }
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
                                        painter = painterResource(id = R.drawable.thumbs_up),
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
                        Spacer(modifier = Modifier.height(20.dp))
                        //Legal
                        Text(
                            text = "Legal",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        UniversalDesignCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(70.dp),
                            onClick = { openPlayStoreForReview(context) }
                        )
                        {
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
                                        painter = painterResource(id = R.drawable.thumbs_up),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Gray
                                    )
                                    Text(
                                        text = "Privacy Policy",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.DarkGray,
                                        fontSize = 18.sp
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = "Privacy Policy",
                                    tint = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        //icons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://www.behance.net/primenalinjoshi")
                                )
                                context.startActivity(intent)
                            })
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.behance_logo),
                                    contentDescription = "Social 1",
                                    modifier = Modifier.size(28.dp),
                                    tint = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/mon00nono0o/"))
                                context.startActivity(intent)
                            })
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.instagram_logo),
                                    contentDescription = "Social 2",
                                    modifier = Modifier.size(28.dp),
                                    tint = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("www.linkedin.com/in"))
                                context.startActivity(intent)
                            })
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.linkedin_logo),
                                    contentDescription = "Social 3",
                                    modifier = Modifier.size(28.dp),
                                    tint = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
            //theme
            else {
                // --- 2. THEME SELECTION ---
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.Start
                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 46.dp, bottom = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { showThemeSelection = false }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Back to Profile Settings",
                                tint = primaryGreenColor,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Themes Collection",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryGreenColor
                        )
                    }

                    // GRID ROW 1: Forest and Ocean side-by-side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // --- FOREST THEME CARD SELECTION ---
                        val isForestActive = themeProfile == AppDesignTokens.ForestTheme
                        UniversalDesignCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(160.dp),
                            onClick = { if (!isForestActive) onToggleTheme("forest") }
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(45.dp)
                                            .background(
                                                AppDesignTokens.ForestTheme.backgroundBrush,
                                                RoundedCornerShape(12.dp)
                                            )
                                    )
                                    Text(
                                        text = "Forest\nLandscape",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = AppDesignTokens.primaryText
                                    )
                                }
                                if (isForestActive) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected Theme Indicator",
                                        tint = themeProfile.activeIconColor,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(12.dp)
                                    )
                                }
                            }
                        }

                        // --- OCEAN THEME CARD SELECTION ---
                        val isOceanActive = themeProfile == AppDesignTokens.OceanTheme
                        UniversalDesignCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(160.dp),
                            onClick = { if (!isOceanActive) onToggleTheme("ocean") }
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(45.dp)
                                            .background(
                                                AppDesignTokens.OceanTheme.backgroundBrush,
                                                RoundedCornerShape(12.dp)
                                            )
                                    )
                                    Text(
                                        text = "Ocean\nAquatic",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = AppDesignTokens.primaryText
                                    )
                                }
                                if (isOceanActive) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected Theme Indicator",
                                        tint = themeProfile.activeIconColor,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(12.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // GRID ROW 2: Cozy Sunset and Nordic Frost side-by-side
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // --- SUNSET THEME CARD SELECTION ---
                        val isSunsetActive = themeProfile == AppDesignTokens.SunsetTheme
                        UniversalDesignCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(160.dp),
                            onClick = { if (!isSunsetActive) onToggleTheme("sunset") }
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(45.dp)
                                            .background(
                                                AppDesignTokens.SunsetTheme.backgroundBrush,
                                                RoundedCornerShape(12.dp)
                                            )
                                    )
                                    Text(
                                        text = "Cozy\nSunset",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = AppDesignTokens.primaryText
                                    )
                                }
                                if (isSunsetActive) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected Theme Indicator",
                                        tint = themeProfile.activeIconColor,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(12.dp)
                                    )
                                }
                            }
                        }

                        // --- NORDIC FROST THEME CARD SELECTION ---
                        val isNordicActive = themeProfile == AppDesignTokens.NordicTheme
                        UniversalDesignCard(
                            modifier = Modifier
                                .weight(1f)
                                .height(160.dp),
                            onClick = { if (!isNordicActive) onToggleTheme("nordic") }
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(45.dp)
                                            .background(
                                                AppDesignTokens.NordicTheme.backgroundBrush,
                                                RoundedCornerShape(12.dp)
                                            )
                                    )
                                    Text(
                                        text = "Nordic\nFrost",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = AppDesignTokens.primaryText
                                    )
                                }
                                if (isNordicActive) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected Theme Indicator",
                                        tint = themeProfile.activeIconColor,
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            //don't know what this is
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                CustomBottomNavBar(
                    selectedTab = 3,
                    riveResId = riveResId,
                    themeProfile = themeProfile,
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