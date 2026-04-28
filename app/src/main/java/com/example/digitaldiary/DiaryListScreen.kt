// app/src/main/java/com/example/digitaldiary/DiaryListScreen.kt
package com.example.digitaldiary

import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
// RIVE IMPORTS
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
) {
    var entryToDelete by remember { mutableStateOf<DiaryEntry?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Grounded Theme Colors
    val topColor = Color(0xFF0F172A)
    val bottomColor = Color(0xFF064E3B)
    val groundColor = Color(0xFF042F2E)
    val titleColor = Color(0xFFE2E8F0)
    val gradientBrush = Brush.verticalGradient(colors = listOf(topColor, bottomColor))

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Journey", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = titleColor) },
                navigationIcon = { IconButton(onClick = onOpenCalendar) { Icon(Icons.Default.DateRange, "Calendar", tint = titleColor) } },
                actions = { IconButton(onClick = onToggleTheme) { Icon(if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode, "Theme", tint = titleColor) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                // 1. Hill/Ground Arc (Bottom Layer)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = groundColor,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = true,
                        topLeft = Offset(0f, size.height * 0.4f),
                        size = Size(size.width, size.height * 1.5f)
                    )
                }

                // 2. The Plant (Middle Layer - Left)
                AndroidView(
                    modifier = Modifier
                        .size(120.dp)
                        .offset(x = (-100).dp, y = (-100).dp),
                    factory = { context ->
                        RiveAnimationView(context).apply {
                            setRiveResource(
                                resId = R.raw.plant,
                                stateMachineName = "State Machine 1",
                                autoplay = true
                            )
                        }
                    }
                )

                // 3. The Tree (Middle Layer - Right)
                AndroidView(
                    modifier = Modifier
                        .size(200.dp)
                        .offset(x = 100.dp, y = (-200).dp),
                    factory = { context ->
                        RiveAnimationView(context).apply {
                            setRiveResource(
                                resId = R.raw.tree2,
                                stateMachineName = "State Machine 1",
                                autoplay = true
                            )
                        }
                    }
                )

                // 4. THE RIVE BUTTON (Top Layer - Center)
                AndroidView(
                    modifier = Modifier
                        .size(120.dp)
                        .offset(y = (-10).dp),
                    factory = { context ->
                        RiveAnimationView(context).apply {
                            setRiveResource(
                                resId = R.raw.happ_button2,
                                // IMPORTANT: Ensure this name EXACTLY matches your state machine name in Rive
                                stateMachineName = "State Machine 1",
                                autoplay = true
                            )
                        }
                    },
                    update = { view ->
                        view.setOnTouchListener { v, event ->
                            v.onTouchEvent(event)

                            if (event.action == android.view.MotionEvent.ACTION_UP) {
                                v.performClick()

                                // LAUNCH THE DELAY HERE
                                coroutineScope.launch {
                                    delay(150) // Wait 0.15 seconds
                                    onAddEntry() // THEN change the screen
                                }
                            }
                            true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(gradientBrush).padding(paddingValues)) {
            if (entries.isEmpty()) {
                Text(
                    "No entries yet. Tap the friend on the ground!",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.LightGray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 16.dp, bottom = 140.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(entries, key = { it.id }) { entry ->
                        DiaryItem(entry = entry, onEdit = onEditEntry, onDeleteRequest = { entryToDelete = it })
                    }
                }
            }
        }
        // ... Delete Dialog Logic
    }
}