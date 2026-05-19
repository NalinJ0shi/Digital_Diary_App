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

    // 1. MOVED UP: Now ALL screens can see your database entries!
    val entries by diaryViewModel.allEntries.collectAsState(initial = emptyList())

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            DiaryListScreen(
                entries = entries,
                canAdd = true,
                isDarkMode = isDarkMode,
                onToggleTheme = { isDarkMode = !isDarkMode },

                // 2. RIVE BUTTON FIX: This now navigates to your Mood screen
                onAddEntry = { navController.navigate("mood_screen") },

                onOpenCalendar = { navController.navigate("calendar_screen") },
                onNavigateToChart = { navController.navigate("chart_screen") },
                onNavigateToGame = { navController.navigate("game_screen") },
                onNavigateToProfile = { navController.navigate("profile_screen") },

                onEditEntry = { entry -> /* TODO: Handle Editing */ },
                onDeleteEntry = { entry -> diaryViewModel.delete(entry) }
            )
        }

        // 3. MOOD SLIDER ROUTE
        composable("mood_screen") {
            MoodSliderScreen(
                onSaveEntry = { selectedMoodLevel ->
                    navController.navigate("calendar_screen") {
                        popUpTo("home") { inclusive = false }
                        // This prevents the calendar from opening twice if the screen refreshes!
                        launchSingleTop = true
                    }
                }
            )
        }

        // 4. CALENDAR ROUTE
        composable("calendar_screen") {
            CalendarScreen(
                entries = entries,
                onDateSelected = { dateInMillis ->
                    /* TODO */
                },
                onBack = {
                    // Wipes the entire navigation history clean and forces a fresh jump Home
                    navController.navigate("home") {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable("chart_screen") {
            BlankScreen(screenName = "Charts") { navController.popBackStack() }
        }

        composable("game_screen") {
            BlankScreen(screenName = "Games") { navController.popBackStack() }
        }

        composable("profile_screen") {
            BlankScreen(screenName = "Profile") { navController.popBackStack() }
        }
    }
}