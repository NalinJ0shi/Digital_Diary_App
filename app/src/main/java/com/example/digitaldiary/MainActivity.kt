// app/src/main/java/com/example/digitaldiary/MainActivity.kt
package com.example.digitaldiary

import app.rive.runtime.kotlin.core.Rive
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.digitaldiary.ui.theme.PlantformTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    private val viewModel: DiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Initialize Rive for animations
        Rive.init(applicationContext)

        setContent {
            val systemInDark = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(systemInDark) }

            PlantformTheme(darkTheme = isDarkMode)
            {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    val entries by viewModel.allEntries.collectAsState(initial = emptyList())
                    val latestEntry by viewModel.latestEntry.collectAsState(initial = null)

                    // Keep track of the current time for the "can add" cooldown logic
                    var now by remember { mutableLongStateOf(System.currentTimeMillis()) }

                    LaunchedEffect(Unit) {
                        while (true) {
                            delay(1000)
                            now = System.currentTimeMillis()
                        }
                    }

                    // Simple rate-limiting logic: 1 minute between entries
                    val lastTimestamp = latestEntry?.timestamp ?: 0L
                    val canAddNow = (now - lastTimestamp > 60000L)

                    // Navigation state
                    var currentScreen by remember { mutableStateOf("CUSTOM_SPLASH") }
                    var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
                    var entryToEdit by remember { mutableStateOf<DiaryEntry?>(null) }

                    // Navigation Router
                    when (currentScreen) {
                        "CUSTOM_SPLASH" -> {
                            CustomSplashScreen(onTimeout = { currentScreen = "LIST" })
                        }
                        "LIST" -> {
                            DiaryListScreen(
                                entries = entries,
                                canAdd = canAddNow,
                                isDarkMode = isDarkMode,
                                onToggleTheme = { isDarkMode = !isDarkMode },
                                onAddEntry = {
                                    if (canAddNow) {
                                        selectedDate = System.currentTimeMillis()
                                        entryToEdit = null
                                        currentScreen = "MOOD_SLIDER"
                                    } else {
                                        Toast.makeText(this@MainActivity, "Wait! 1-minute limit active.", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                onOpenCalendar = { currentScreen = "CALENDAR" },
                                onEditEntry = { entry ->
                                    selectedDate = entry.timestamp
                                    entryToEdit = entry
                                    currentScreen = "EDIT"
                                },
                                onDeleteEntry = { entry -> viewModel.delete(entry) }
                            )
                        }
                        "BLANK" -> {
                            BlankScreen()
                        }
                        "MOOD_SLIDER" -> {
                            MoodSliderScreen(
                                onSaveEntry = { moodValue ->
                                    val dateTitle = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(selectedDate))
                                    viewModel.saveEntry(
                                        title = dateTitle,
                                        content = entryToEdit?.content ?: "",
                                        date = selectedDate,
                                        dayRating = moodValue,
                                        existingEntry = entryToEdit
                                    )
                                    currentScreen = "LIST"
                                }
                            )
                        }
                        "CALENDAR" -> {
                            CalendarScreen(
                                entries = entries,
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
                                        dayRating = entryToEdit?.dayRating ?: 5,
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
    }
}
