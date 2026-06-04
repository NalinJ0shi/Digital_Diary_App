// app/src/main/java/com/example/digitaldiary/MainActivity.kt
package com.example.digitaldiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import app.rive.runtime.kotlin.core.Rive
import com.example.digitaldiary.database.DiaryViewModel
import com.example.digitaldiary.screens.BreathingScreen
import com.example.digitaldiary.screens.CalendarScreen
import com.example.digitaldiary.screens.ChartScreen
import com.example.digitaldiary.screens.GardenScreen
import com.example.digitaldiary.screens.MoodSliderScreen
import com.example.digitaldiary.screens.ProfileScreen
import com.example.digitaldiary.ui.theme.AppGlobalGradient


import com.example.digitaldiary.ui.theme.PlantformTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        Rive.init(this)

        setContent {
            PlantformTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppGlobalGradient) // Crucial line: attaches your white/soft gradient
                ) {
                    DiaryAppNavigation()
                }
            }
        }
    }
}

@Composable
fun DiaryAppNavigation() {
    val navController = rememberNavController()

    val context = androidx.compose.ui.platform.LocalContext.current
    val diaryViewModel: DiaryViewModel = viewModel(factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as android.app.Application))

    var isDarkMode by remember { mutableStateOf(false) }
    val entries by diaryViewModel.allEntries.collectAsState(initial = emptyList())
    val streakCount = diaryViewModel.getStreak(entries)

    NavHost(navController = navController, startDestination = "calendar_screen") {

        // --- HOME SCREEN ---
        composable("home") {
            GardenScreen(
                entries = entries,
                streakCount = streakCount,
                canAdd = true,
                isDarkMode = isDarkMode,
                onToggleTheme = { isDarkMode = !isDarkMode },
                onAddEntry = { navController.navigate("mood_screen") },

                // Clicking navbar items from Home page
                onOpenCalendar = {
                    navController.navigate("calendar_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToChart = {
                    navController.navigate("chart_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToGame = {
                    navController.navigate("game_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfile = {
                    navController.navigate("profile_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onEditEntry = { entry ->
                    navController.navigate("mood_screen?timestamp=${entry.timestamp}")
                },
                onDeleteEntry = { entry -> diaryViewModel.delete(entry) }
            )
        }

        // --- MOOD SLIDER ---
        composable(
            route = "mood_screen?timestamp={timestamp}",
            arguments = listOf(navArgument("timestamp") {
                type = androidx.navigation.NavType.LongType
                defaultValue = -1L // Indicates a new entry
            } )
        ) { backStackEntry ->
            val timestamp = backStackEntry.arguments?.getLong("timestamp") ?: -1L

            // Find entry if timestamp is passed
            val existingEntry = if (timestamp != -1L) {
                entries.find { it.timestamp == timestamp }
            } else null

            MoodSliderScreen(
                existingEntry = existingEntry,
                onSaveEntry = { content, moodScore ->
                    diaryViewModel.saveEntry(
                        title = "",
                        content = content,
                        date = if (timestamp != -1L) timestamp else System.currentTimeMillis(),
                        dayRating = moodScore,
                        existingEntry = existingEntry
                    )
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        // --- CALENDAR SCREEN ---
        composable("calendar_screen") {
            CalendarScreen(
                entries = entries,
                onDateSelected = { dateInMillis ->
                    navController.navigate("mood_screen?timestamp=$dateInMillis")
                },
                onCalendarClick = { /* Already on calendar */ },

                // --- THIS IS THE FORCE CLEAR RULE FOR THE HOUSE_LINE ---
                onBack = {
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },

                onChartClick = {
                    navController.navigate("chart_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onGameClick = {
                    navController.navigate("game_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onProfileClick = {
                    navController.navigate("profile_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onAddEntry = { navController.navigate("mood_screen") },

                // --- FIXED: Added the required parameters here so the compiler knows how to handle the DiaryItem cards edits/deletes ---
                onEditEntry = { entry ->
                    navController.navigate("mood_screen?timestamp=${entry.timestamp}")
                },
                onDeleteEntry = { entry ->
                    diaryViewModel.delete(entry)
                }
            )
        }

        // --- CHART SCREEN ---
        composable("chart_screen") {
            ChartScreen(
                entries = entries,
                onBack = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onCalendarClick = {
                    navController.navigate("calendar_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onChartClick = { /* Already here */ },
                onGameClick = {
                    navController.navigate("game_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onProfileClick = {
                    navController.navigate("profile_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onAddEntry = { navController.navigate("mood_screen") }
            )
        }

        // --- GAME SCREEN ---
        composable("game_screen") {
            BreathingScreen(
                onBack = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onCalendarClick = {
                    navController.navigate("calendar_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onChartClick = {
                    navController.navigate("chart_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onGameClick = { /* Already here */ },
                onProfileClick = {
                    navController.navigate("profile_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onAddEntry = { navController.navigate("mood_screen") }
            )
        }

        // --- PROFILE SCREEN ---
        composable("profile_screen") {
            ProfileScreen(
                onBack = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onCalendarClick = {
                    navController.navigate("calendar_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onChartClick = {
                    navController.navigate("chart_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onGameClick = {
                    navController.navigate("game_screen") {
                        popUpTo("home") { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onProfileClick = { /* Already here */ },
                onAddEntry = { navController.navigate("mood_screen") }
            )
        }
    }
}
