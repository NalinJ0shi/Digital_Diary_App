package com.example.digitaldiary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    entries: List<DiaryEntry>,
    canAdd: Boolean,
    onAddEntry: () -> Unit,
    onOpenCalendar: () -> Unit,
    onEditEntry: (DiaryEntry) -> Unit,
    onDeleteEntry: (DiaryEntry) -> Unit,
)
{
    val PurpleDiary = Color(0xFF9333EA)
    var entryToDelete by remember { mutableStateOf<DiaryEntry?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Edit, null, tint = PurpleDiary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("My Diary", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenCalendar) {
                        Icon(Icons.Default.DateRange, "Calendar", tint = Color.Gray)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = Color.White, tonalElevation = 8.dp) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    FloatingActionButton(
                        onClick = { if (canAdd) onAddEntry() },
                        containerColor = if (canAdd) PurpleDiary else Color.LightGray,
                        contentColor = Color.White
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
                    TextButton(onClick = { onDeleteEntry(entryToDelete!!); entryToDelete = null }) {
                        Text("Delete", color = Color.Red)
                    }
                },
                dismissButton = { TextButton(onClick = { entryToDelete = null }) { Text("Cancel") } }
            )
        }
    }
}