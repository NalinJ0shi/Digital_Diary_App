package com.example.plantform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    // 1. Hire the Manager (ViewModel)
    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 2. READ: This "collects" entries from the database.
            // The warning disappears because we pass 'entries' to the screen below!
            val entries by viewModel.allEntries.collectAsState(initial = emptyList())

            var currentScreen by remember { mutableStateOf("LIST") }

            if (currentScreen == "LIST") {
                DiaryListScreen(
                    entries = entries, // Passing the real database entries here
                    onAddEntry = { currentScreen = "ADD" }
                )
            } else {
                AddEntryScreen(
                    onSave = { title, content ->
                        // CALL THE MANAGER, NOT THE LIST!
                        viewModel.addEntry(title, content)
                        currentScreen = "LIST"
                    },
                    onCancel = { currentScreen = "LIST" }
                )
            }
        }
    }
}