// app/src/main/java/com/example/digitaldiary/DiaryListScreen.kt
package com.example.digitaldiary

import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.nalin.my_digitaldiary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListScreen(
    entries: List<DiaryEntry>,
    canAdd: Boolean,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onAddEntry: () -> Unit,
    onOpenCalendar: () -> Unit,
    onEditEntry: (DiaryEntry) -> Unit,
    onDeleteEntry: (DiaryEntry) -> Unit,
    onNavigateToChart: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToProfile: () -> Unit,
)
{
    var entryToDelete by remember { mutableStateOf<DiaryEntry?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    var selectedTab by remember { mutableIntStateOf(0) }

    val topColor = Color(0xFF0F172A)
    val bottomColor = Color(0xFF064E3B)
    val groundColor = Color(0xFF042F2E)
    val titleColor = Color(0xFFE2E8F0)
    val navBarColor = Color(0xFFF8FAFC)
    val gradientBrush = Brush.verticalGradient(colors = listOf(topColor, bottomColor))

    val activeIconColor = groundColor
    val inactiveIconColor = Color(0xFF94A3B8)

    val hillColor = Color(0xFFDAEBC0)
    val hillPathString = "M285 17.4657C203.574 -21.8322 183.5 17.4659 114.5 17.4658L-3 17.4657V203.801H402V27.8015C402 27.8015 352 49.8013 285 17.4657Z"
    val hillPath = remember {
        androidx.compose.ui.graphics.vector.PathParser().parsePathString(hillPathString).toPath()
    }

    val hill2Color = Color(0xFFC6D7AC)
    val hill2PathString = "M309.5 15.5557C225.712 -29.7517 192.778 63.778 117 15.5555C62 -19.4445 -3 15.5557 -3 15.5557V264.055H402V45.5552C402 45.5552 328.771 25.9765 309.5 15.5557Z"
    val hill2Path = remember {
        androidx.compose.ui.graphics.vector.PathParser().parsePathString(hill2PathString).toPath()
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Journey", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = titleColor) },
                actions = { },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent),
            )
        },

        bottomBar = {
            CustomBottomNavBar(
                selectedTab = 0, // Calendar/Home is active
                onCalendarClick = onOpenCalendar,
                onChartClick = onNavigateToChart,
                onGameClick = onNavigateToGame,
                onProfileClick = onNavigateToProfile,
                onAddEntry = onAddEntry
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(gradientBrush)) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(402f / 204f)
                        .align(Alignment.BottomCenter)
                ) {
                    val scaleX = size.width / 402f
                    val scaleY = size.height / 204f
                    withTransform({ scale(scaleX, scaleY, Offset.Zero) }) {
                        translate(top = -40f) {
                            drawPath(path = hill2Path, color = hill2Color)
                        }
                    }
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(402f / 204f)
                        .align(Alignment.BottomCenter)
                ) {
                    val scaleX = size.width / 402f
                    val scaleY = size.height / 204f
                    withTransform({ scale(scaleX, scaleY, Offset.Zero) }) {
                        drawPath(path = hillPath, color = hillColor)
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(entries, key = { it.id }) { entry ->
                    DiaryItem(entry = entry, onEdit = onEditEntry, onDeleteRequest = { entryToDelete = it })
                }
            }
        }
    }
}