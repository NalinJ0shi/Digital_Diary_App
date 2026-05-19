// app/src/main/java/com/example/digitaldiary/ChartScreen.kt
package com.example.digitaldiary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import com.nalin.my_digitaldiary.R
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(
    onNavigateBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    val bgColor = Color(0xFF0F172A)

    Scaffold(
        containerColor = bgColor,
        topBar = {
            TopAppBar(
                title = { Text("Charts", color = Color.White) },
                navigationIcon = {
                    // --- CHANGED FROM onCalendarClick TO onNavigateBack ---
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.house_line),
                            contentDescription = "Go Home",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            CustomBottomNavBar(
                selectedTab = 1, // 1 = Chart Icon Active
                onCalendarClick = onCalendarClick,
                onChartClick = onChartClick,
                onGameClick = onGameClick,
                onProfileClick = onProfileClick,
                onAddEntry = onAddEntry
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Your future charts will go here!", color = Color.White)
        }
    }
}