package com.example.plantform

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(entries: List<DiaryEntry>, onAddEntry: () -> Unit) {
    // Let's define that nice purple color from your design
    val PurpleDiary = Color(0xFF9333EA)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        // The Book Icon
                        Icon(
                            imageVector = Icons.Default.Menu, // Using Menu as a placeholder for Book if needed
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
                    IconButton(onClick = { /* Open Drawer */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
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
                tonalElevation = 8.dp,
                actions = {
                    IconButton(onClick = { /* Navigate Home */ }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = PurpleDiary)
                    }
                    IconButton(onClick = { /* Navigate Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onAddEntry,
                        containerColor = PurpleDiary,
                        contentColor = Color.White
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Entry")
                    }
                }
            )
        }
    ) { paddingValues ->
        // The List of Entries
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp), // Side padding
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp), // Extra bottom padding so FAB doesn't cover last item
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(entries) { entry ->
                DiaryItem(entry = entry)
            }
        }
    }
}