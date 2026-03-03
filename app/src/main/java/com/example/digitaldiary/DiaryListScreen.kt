// app/src/main/java/com/example/digitaldiary/DiaryListScreen.kt
package com.example.digitaldiary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Edit,
                            null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "My Diary",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenCalendar) {
                        Icon(Icons.Default.DateRange, "Calendar")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    FloatingActionButton(
                        onClick = { if (canAdd) onAddEntry() },
                        containerColor = if (canAdd) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(Icons.Default.Add, "Add")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entries, key = { it.id }) { entry ->
                DiaryItem(entry = entry, onEdit = onEditEntry, onDeleteRequest = { entryToDelete = it })
            }
        }

        if (entryToDelete != null) {
            AlertDialog(
                onDismissRequest = { entryToDelete = null },
                title = { Text("Delete Entry?") },
                text = { Text("Are you sure?") },
                confirmButton = {
                    TextButton(onClick = {
                        entryToDelete?.let { onDeleteEntry(it) }
                        entryToDelete = null
                    }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = { TextButton(onClick = { entryToDelete = null }) { Text("Cancel") } }
            )
        }
    }
}