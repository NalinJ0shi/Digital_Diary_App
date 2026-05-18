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

        // 3. NEW ROUTE: The Mood Slider Screen
        composable("mood_screen") {
            MoodSliderScreen(
                onSaveEntry = { selectedMoodLevel ->
                    // For now, this just slides back to the home screen after they pick a mood!
                    // Later, we can make this save to the database or go to the text editor.
                    navController.popBackStack()
                }
            )
        }

        // 4. CALENDAR FIX: Replaced the Blank Screen with your actual CalendarScreen
        composable("calendar_screen") {
            CalendarScreen(
                entries = entries, // Passes the dots to your calendar
                onDateSelected = { dateInMillis ->
                    /* TODO: What happens when they tap a specific day? */
                },
                onBack = { navController.popBackStack() } // Makes your top-left arrow work
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