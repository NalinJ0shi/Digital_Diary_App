package com.example.digitaldiary.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.digitaldiary.CustomBottomNavBar
import com.example.digitaldiary.UniversalBackgroundWrapper
import com.example.digitaldiary.database.DiaryEntry
import com.nalin.my_digitaldiary.R

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
    UniversalBackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                CustomBottomNavBar(
                    selectedTab = -1,
                    onCalendarClick = onOpenCalendar,
                    onChartClick = onNavigateToChart,
                    onGameClick = onNavigateToGame,
                    onProfileClick = onNavigateToProfile,
                    onAddEntry = onAddEntry
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) {
                        Icon(
                            painter = painterResource(id = R.drawable.calender_face),
                            contentDescription = null,
                            modifier = Modifier.size(42.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}