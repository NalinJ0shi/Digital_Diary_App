package com.example.digitaldiary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    entries: List<DiaryEntry>,
    canAdd: Boolean,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onAddEntry: () -> Unit,
    onOpenCalendar: () -> Unit,
    onEditEntry: (DiaryEntry) -> Unit,
    onDeleteEntry: (DiaryEntry) -> Unit,
) {
    var entryToDelete by remember { mutableStateOf<DiaryEntry?>(null) }

    // 1. SMART GRADIENT COLORS
    // If it's dark mode, use deep navy/black. If light, use lavender/white.
    val topColor = if (isDarkMode) Color(0xFF1A1A2E) else Color(0xFFF3E8FF)
    val bottomColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val titleColor = if (isDarkMode) Color(0xFFE9D5FF) else Color(0xFF4B218B)

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(topColor, bottomColor)
    )

    Scaffold(
        containerColor = Color.Transparent, // Ensure the scaffold doesn't block the gradient
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Journey",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = titleColor // Use the smart title color
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onOpenCalendar) {
                        Icon(Icons.Default.DateRange, "Calendar", tint = titleColor)
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            "Theme",
                            tint = titleColor
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.Transparent, tonalElevation = 0.dp) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    FloatingActionButton(
                        onClick = { if (canAdd) onAddEntry() },
                        containerColor = if (canAdd) Color(0xFF9333EA) else Color.Gray,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, "Add")
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush) // The smart gradient is applied here
            .padding(paddingValues)
        ) {
            if (entries.isEmpty()) {
                Text(
                    "No entries yet. Tap + to start!",
                    modifier = Modifier.align(Alignment.Center),
                    color = if (isDarkMode) Color.LightGray else Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(entries, key = { it.id }) { entry ->
                        DiaryItem(
                            entry = entry,
                            onEdit = onEditEntry,
                            onDeleteRequest = { entryToDelete = it }
                        )
                    }
                }
            }
        }

        // Delete Dialog logic remains the same
        if (entryToDelete != null) {
            AlertDialog(
                onDismissRequest = { entryToDelete = null },
                title = { Text("Delete Entry?") },
                text = { Text("Are you sure? This secret will be gone forever!") },
                confirmButton = {
                    TextButton(onClick = {
                        entryToDelete?.let { onDeleteEntry(it) }
                        entryToDelete = null
                    }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
                },
                dismissButton = { TextButton(onClick = { entryToDelete = null }) { Text("Cancel") } }
            )
        }
    }
}