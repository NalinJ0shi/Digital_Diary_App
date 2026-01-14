package com.example.digitaldiary

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

            var now by remember { mutableLongStateOf(System.currentTimeMillis()) }
            LaunchedEffect(Unit) {
                while (true) {
                    delay(1000)
                    now = System.currentTimeMillis()
                }
            }

            val canAddNow = latestEntry == null || (now - latestEntry!!.timestamp > 60000)

            var currentScreen by remember { mutableStateOf("LIST") }
            var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
            var entryToEdit by remember { mutableStateOf<DiaryEntry?>(null) }

            fun navigateToList() {
                currentScreen = "LIST"
            }

            fun navigateToCalendar() {
                currentScreen = "CALENDAR"
            }

            fun navigateToEdit(entry: DiaryEntry? = null, date: Long? = null) {
                entryToEdit = entry
                selectedDate = date ?: entry?.timestamp ?: System.currentTimeMillis()
                currentScreen = "EDIT"
            }

            when (currentScreen) {
                "LIST" -> {
                    DiaryListScreen(
                        entries = entries,
                        canAdd = canAddNow,
                        onAddEntry = {
                            if (canAddNow) {
                                navigateToEdit()
                            } else {
                                Toast.makeText(this, "Wait! Testing limit active.", Toast.LENGTH_SHORT).show()
                            }
                        },
                        onOpenCalendar = { navigateToCalendar() },
                        onEditEntry = { entry -> navigateToEdit(entry) },
                        onDeleteEntry = { entry -> viewModel.delete(entry) }
                    )
                }
                "CALENDAR" -> {
                    CalendarScreen(
                        onBack = { navigateToList() },
                        onDateSelected = { date ->
                            val existing = viewModel.getEntryForDate(date, entries)
                            navigateToEdit(existing, date)
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
                            navigateToList()
                        },
                        onCancel = { navigateToList() }
                    )
                }
            }
        }
    }
}