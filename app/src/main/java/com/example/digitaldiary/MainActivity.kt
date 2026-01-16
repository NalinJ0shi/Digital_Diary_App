package com.example.digitaldiary

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
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

            // Using mutableLongStateOf fixes comparison errors
            var now by remember { mutableLongStateOf(System.currentTimeMillis()) }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(1000)
                    now = System.currentTimeMillis()
                }
            }

            // 'L' suffix ensures Long comparison
            val canAddNow = latestEntry == null || (now - latestEntry!!.timestamp > 60000L)

            var currentScreen by remember { mutableStateOf("LIST") }
            var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
            var entryToEdit by remember { mutableStateOf<DiaryEntry?>(null) }

            when (currentScreen) {
                "LIST" -> {
                    DiaryListScreen(
                        entries = entries,
                        canAdd = canAddNow,
                        onAddEntry = {
                            if (canAddNow) {
                                selectedDate = System.currentTimeMillis()
                                entryToEdit = null
                                currentScreen = "EDIT"
                            } else {
                                Toast.makeText(this, "Wait! Testing limit active.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onOpenCalendar = { currentScreen = "CALENDAR" },
                        onEditEntry = { entry ->
                            selectedDate = entry.timestamp
                            entryToEdit = entry
                            currentScreen = "EDIT"
                        },
                        onDeleteEntry = { entry -> viewModel.delete(entry) }
                        // No more passing onBackup or onRestore here!
                    )
                }
                "CALENDAR" -> {
                    CalendarScreen(
                        onBack = { currentScreen = "LIST" },
                        onDateSelected = { date ->
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
                            val dateTitle = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(selectedDate))
                            viewModel.saveEntry(
                                title = dateTitle,
                                content = content,
                                date = selectedDate,
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