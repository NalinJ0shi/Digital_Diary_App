package com.example.digitaldiary

import android.app.Application
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
import com.example.digitaldiary.miscellaneousBS.AppDesignTokens
import com.example.digitaldiary.miscellaneousBS.UniversalBackgroundWrapper
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
import com.example.digitaldiary.secondaryscreen.GardenScreen
import com.example.digitaldiary.screens.MoodSliderScreen
import com.example.digitaldiary.screens.ProfileScreen
import com.example.digitaldiary.screens.YellowScreen
import com.example.digitaldiary.secondaryscreen.PlantCollectionScreen
import com.example.digitaldiary.ui.theme.AppGlobalGradient
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
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
                        .background(AppGlobalGradient)
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

    val context = LocalContext.current
    val diaryViewModel: DiaryViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application))

    // 1. PERSISTENCE PERSIST: Initialize SharedPreferences disk storage access context
    val sharedPreferences = remember {
        context.getSharedPreferences("user_settings", android.content.Context.MODE_PRIVATE)
    }

    // 2. LOAD ON BOOT: Fetch last saved theme string key marker. Defaults safely to "forest"
    val savedThemeKey = remember {
        sharedPreferences.getString("selected_theme", "forest") ?: "forest"
    }

    // 3. SCALE-SAFE ENGINES: Map initial state seamlessly via when-branch initialization
    var currentThemeProfile by remember {
        mutableStateOf(
            when (savedThemeKey) {
                "ocean" -> AppDesignTokens.OceanTheme
                "forest" -> AppDesignTokens.ForestTheme
                "sunset" -> AppDesignTokens.SunsetTheme
                "nordic" -> AppDesignTokens.NordicTheme
                else -> AppDesignTokens.ForestTheme
            }
        )
    }

    val entries by diaryViewModel.allEntries.collectAsState(initial = emptyList())
    val unlockedPlants by diaryViewModel.unlockedPlants.collectAsState(initial = emptyList())

    val streakCount = diaryViewModel.getStreak(entries)
    val currentPlantTier = diaryViewModel.getCurrentPlantTier(unlockedPlants)

    NavHost(navController = navController,
        startDestination = "calendar_screen",
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None })
    {
        composable("yellow_screen") {
            YellowScreen()
        }

        composable("home") {
            UniversalBackgroundWrapper(themeProfile = currentThemeProfile) {
                GardenScreen(
                    entries = entries,
                    streakCount = streakCount,
                    canAdd = true,
                    isDarkMode = currentThemeProfile == AppDesignTokens.OceanTheme,
                    currentPlantTier = currentPlantTier,
                    onUnlockPlant = { tier -> diaryViewModel.unlockNewPlant(tier + 1) },
                    // Handles the blind toggle layout click on the Garden dashboard view
                    onToggleTheme = {
                        val targetKey = if (currentThemeProfile == AppDesignTokens.ForestTheme) "ocean" else "forest"
                        currentThemeProfile = if (targetKey == "ocean") AppDesignTokens.OceanTheme else AppDesignTokens.ForestTheme
                        sharedPreferences.edit().putString("selected_theme", targetKey).apply()
                    },
                    onAddEntry = { navController.navigate("mood_screen") },
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
        }

        composable("plant_collection") {
            PlantCollectionScreen(
                viewModel = diaryViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "mood_screen?timestamp={timestamp}",
            arguments = listOf(navArgument("timestamp") {
                type = NavType.LongType
                defaultValue = -1L
            } )
        )
        { backStackEntry ->
            val timestamp = backStackEntry.arguments?.getLong("timestamp") ?: -1L

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

        composable("calendar_screen") {
            CalendarScreen(
                themeProfile = currentThemeProfile,
                riveResId = currentThemeProfile.navBarRiveResId,
                entries = entries,
                onDateSelected = { dateInMillis ->
                    navController.navigate("mood_screen?timestamp=$dateInMillis")
                },
                onCalendarClick = {  },
                onBack = {
                    navController.navigate("yellow_screen") {
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
                onEditEntry = { entry ->
                    navController.navigate("mood_screen?timestamp=${entry.timestamp}")
                },
                onDeleteEntry = { entry ->
                    diaryViewModel.delete(entry)
                }
            )
        }

        composable("chart_screen") {
            ChartScreen(
                themeProfile = currentThemeProfile,
                riveResId = currentThemeProfile.navBarRiveResId,
                entries = entries,
                onBack = {
                    navController.navigate("yellow_screen") {
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
                onChartClick = {  },
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

        composable("game_screen") {
            BreathingScreen(
                themeProfile = currentThemeProfile,
                riveResId = currentThemeProfile.navBarRiveResId,
                onBack = {
                    navController.navigate("yellow_screen") {
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
                onGameClick = {  },
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

        composable("profile_screen") {
            ProfileScreen(
                themeProfile = currentThemeProfile,
                riveResId = currentThemeProfile.navBarRiveResId,
                entriesCount = entries.size,
                plantsCount = unlockedPlants.size + 1,
                // FIXED & FUTURE-PROOF LAMBDA: Safely maps incoming explicit card string requests
                onToggleTheme = { themeKey ->
                    currentThemeProfile = when (themeKey) {
                        "ocean" -> AppDesignTokens.OceanTheme
                        "forest" -> AppDesignTokens.ForestTheme
                        "sunset" -> AppDesignTokens.SunsetTheme
                        "nordic" -> AppDesignTokens.NordicTheme
                        else -> AppDesignTokens.ForestTheme
                    }
                    sharedPreferences.edit().putString("selected_theme", themeKey).apply()
                },
                onBack = {
                    navController.navigate("yellow_screen") {
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
                onProfileClick = {  },
                onAddEntry = { navController.navigate("mood_screen") },
                onNavigateToPlantCollection = { navController.navigate("plant_collection") }
            )
        }
    }
}