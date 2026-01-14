package com.example.plantform

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val entries by viewModel.allEntries.collectAsState(initial = emptyList())
            val latestEntry by viewModel.latestEntry.collectAsState(initial = null)

            // Testing Timer: Update 'now' every second to check the 1-minute rule
            var now by remember { mutableStateOf(System.currentTimeMillis()) }
            LaunchedEffect(Unit) {
                while(true) {
                    delay(1000)
                    now = System.currentTimeMillis()
                }
            }

            // Logic: Can add if NO entries exist OR last entry was > 60s ago
            val canAddNow = latestEntry == null || (now - latestEntry!!.timestamp > 60000)

            var currentScreen by remember { mutableStateOf("LIST") }
            var selectedDate by remember { mutableStateOf(System.currentTimeMillis()) }
            var entryToEdit by remember { mutableStateOf<DiaryEntry?>(null) }

            when (currentScreen) {
                "LIST" -> {
                    DiaryListScreen(
                        entries = entries,
                        canAdd = canAddNow,
                        onAddEntry = {
                            if (canAddNow) {
                                selectedDate = System.currentTimeMillis() // Reset to Now
                                entryToEdit = null
                                currentScreen = "EDIT"
                            } else {
                                Toast.makeText(this, "Wait! Testing limit active.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onOpenCalendar = { currentScreen = "CALENDAR" }, // Go to Calendar
                        onEditEntry = { entry ->
                            selectedDate = entry.timestamp // Use entry's date
                            entryToEdit = entry
                            currentScreen = "EDIT"
                        },
                        onDeleteEntry = { entry ->
                            // This is the bridge! We tell the ViewModel to delete the entry
                            viewModel.delete(entry)
                        }
                    )
                }
                "CALENDAR" -> {
                    CalendarScreen(
                        onBack = { currentScreen = "LIST" },
                        onDateSelected = { date ->
                            // Check if we already have an entry for this date
                            val existing = viewModel.getEntryForDate(date, entries)
                            selectedDate = date
                            entryToEdit = existing
                            currentScreen = "EDIT"
                        }
                    )
                }
                "EDIT" -> {
                    AddEntryScreen(
                        existingEntry = entryToEdit,
                        onSave = { content ->
                            // Formatter for the Title
                            val dateTitle = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(selectedDate))

                            viewModel.saveEntry(
                                title = dateTitle,
                                content = content,
                                date = selectedDate, // Use the selected date (past or present)
                                existingEntry = entryToEdit
                            )
                            currentScreen = "LIST"
                        },
                        onCancel = { currentScreen = "LIST" }
                    )
                }
            }
        }
    }
}