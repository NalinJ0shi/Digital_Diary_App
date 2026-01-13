package com.example.plantform

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    entries: List<DiaryEntry>,
    canAdd: Boolean,                // Controls if the button is active
    onAddEntry: () -> Unit,         // Action when + is clicked
    onOpenCalendar: () -> Unit,     // Action when Calendar icon is clicked
    onEditEntry: (DiaryEntry) -> Unit // Action when a card is tapped
) {
    val PurpleDiary = Color(0xFF9333EA)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Using 'Edit' as the logo icon since it's built-in and safe
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = PurpleDiary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "My Diary",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                },
                navigationIcon = {
                    // The Time Machine Button!
                    IconButton(onClick = onOpenCalendar) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Open Calendar",
                            tint = Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                // Centering the FAB inside the bottom bar
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = { if (canAdd) onAddEntry() },
                        // Button turns Grey if 'canAdd' is false (1-minute rule active)
                        containerColor = if (canAdd) PurpleDiary else Color.LightGray,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Entry")
                    }
                }
            }
        }
    ) { paddingValues ->
        // The List of Entries
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp), // Extra padding so FAB doesn't cover content
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(entries) { entry ->
                DiaryItem(
                    entry = entry,
                    onEdit = onEditEntry // Tap card to edit
                )
            }
        }
    }
}