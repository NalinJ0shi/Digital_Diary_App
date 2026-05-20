package com.example.digitaldiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import app.rive.runtime.kotlin.core.Rive

import com.example.digitaldiary.ui.theme.PlantformTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        Rive.init(this)

        setContent {
            PlantformTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
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
    val diaryViewModel: DiaryViewModel = viewModel(
        factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as android.app.Application)
    )

    var isDarkMode by remember { mutableStateOf(false) }
    val entries by diaryViewModel.allEntries.collectAsState(initial = emptyList())

    NavHost(navController = navController, startDestination = "home") {

        // --- HOME SCREEN ---
        composable("home") {
            DiaryListScreen(
                entries = entries,
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
                onEditEntry = { entry -> /* Handle Editing */ },
                onDeleteEntry = { entry -> diaryViewModel.delete(entry) }
            )
        }

        // --- MOOD SLIDER ---
        // --- MOOD SLIDER ---
        // --- MOOD SLIDER ---
        composable("mood_screen") {
            MoodSliderScreen(
                onSaveEntry = { content, moodScore ->
                    diaryViewModel.saveEntry(
                        title = "", // Title is gone, so we save it as blank
                        content = content,
                        date = System.currentTimeMillis(),
                        dayRating = moodScore
                    )
                    navController.popBackStack()
                }
            )
        }

        // --- CALENDAR SCREEN ---
        composable("calendar_screen") {
            CalendarScreen(
                entries = entries,
                onDateSelected = { dateInMillis -> },
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
                onAddEntry = { navController.navigate("mood_screen") }
            )
        }

        // --- CHART SCREEN ---
        composable("chart_screen") {
            ChartScreen(
                onNavigateBack = {
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
            GameScreen(
                onNavigateBack = {
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
                onNavigateBack = {
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