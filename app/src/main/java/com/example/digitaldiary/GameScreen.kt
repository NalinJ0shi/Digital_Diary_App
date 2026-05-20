// app/src/main/java/com/example/digitaldiary/GameScreen.kt
package com.example.digitaldiary

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nalin.my_digitaldiary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    onBack: () -> Unit,
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
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = "Go Home",
                            Modifier.size(32.dp),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            CustomBottomNavBar(
                selectedTab = 2, // 2 = Game Icon Active
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
            Text(text = "Your future mini-games will go here!", color = Color.White)
        }
    }
}