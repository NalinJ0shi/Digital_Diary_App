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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
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
    val density = LocalDensity.current

    // Grounded Theme Colors
    val topColor = Color(0xFF0F172A)
    val bottomColor = Color(0xFF064E3B)
    val groundColor = Color(0xFF042F2E)
    val titleColor = Color(0xFFE2E8F0)
    val navBarColor = Color(0xFFF8FAFC)
    val gradientBrush = Brush.verticalGradient(colors = listOf(topColor, bottomColor))

    // Front Hill Variables
    val hillColor = Color(0xFFDAEBC0)
    val hillPathString = "M285 17.4657C203.574 -21.8322 183.5 17.4659 114.5 17.4658L-3 17.4657V203.801H402V27.8015C402 27.8015 352 49.8013 285 17.4657Z"
    val hillPath = remember {
        androidx.compose.ui.graphics.vector.PathParser().parsePathString(hillPathString).toPath()
    }

    // Back Hill Variables
    val hill2Color = Color(0xFFC6D7AC) // Slightly darker/greyer for depth
    val hill2PathString = "M309.5 15.5557C225.712 -29.7517 192.778 63.778 117 15.5555C62 -19.4445 -3 15.5557 -3 15.5557V264.055H402V45.5552C402 45.5552 328.771 25.9765 309.5 15.5557Z"
    val hill2Path = remember {
        androidx.compose.ui.graphics.vector.PathParser().parsePathString(hill2PathString).toPath()
    }

    // Calculate the radius for the cutout
    val cutoutRadiusPx = with(density) { 75.dp.toPx() }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(402f / 204f)
                        .align(Alignment.BottomCenter)
                ) {
                    val scaleX = size.width / 402f
                    val scaleY = size.height / 204f

                    withTransform({
                        scale(scaleX, scaleY, Offset.Zero)
                    }) {
                        // 1. BACK HILL (Drawn first so it stays behind)
                        // Note: I shifted this up by 40 pixels so you can see it peeking out.
                        // Once you paste your real SVG, you can remove this `translate` block!
                        translate(top = -40f) {
                            drawPath(
                                path = hill2Path,
                                color = hill2Color
                            )
                        }

                        // 2. FRONT HILL (Drawn second)
                        drawPath(
                            path = hillPath,
                            color = hillColor
                        )
                    }
                }

//                // 1. The Plant
//                AndroidView(
//                    modifier = Modifier
//                        .size(120.dp)
//                        .offset(x = (-100).dp, y = (-50).dp),
//                    factory = { context ->
//                        RiveAnimationView(context).apply {
//                            setRiveResource(
//                                resId = R.raw.plant,
//                                stateMachineName = "State Machine 1",
//                                autoplay = true
//                            )
//                        }
//                    }
//                )
//
//                // 2. The Tree
//                AndroidView(
//                    modifier = Modifier
//                        .size(200.dp)
//                        .offset(x = 100.dp, y = (-120).dp),
//                    factory = { context ->
//                        RiveAnimationView(context).apply {
//                            setRiveResource(
//                                resId = R.raw.tree2,
//                                stateMachineName = "State Machine 1",
//                                autoplay = true
//                            )
//                        }
//                    }
//                )

                // 3. The Curved Navigation Bar (Bottom Layer)
                NavigationBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .graphicsLayer {
                            shape = BottomNavCurveShape(cutoutRadiusPx)
                            clip = true
                            shadowElevation = 16f
                        },
                    containerColor = navBarColor,
                    contentColor = groundColor
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Diary") },
                        label = { Text("Diary") },
                        selected = true,
                        onClick = { /* Handle Navigation */ },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = groundColor,
                            selectedTextColor = groundColor,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )

                    // Invisible spacer item to keep the middle clear for your button
                    Spacer(modifier = Modifier.weight(1f))

                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Face, contentDescription = "Moods") },
                        label = { Text("Moods") },
                        selected = false,
                        onClick = { /* Handle Navigation */ },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = groundColor,
                            selectedTextColor = groundColor,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }

                // 4. THE RIVE BUTTON (Top Layer - Center, floating inside the cutout)
                AndroidView(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = (-15).dp),
                    factory = { context ->
                        RiveAnimationView(context).apply {
                            setRiveResource(
                                resId = R.raw.happ_button2,
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

                                coroutineScope.launch {
                                    delay(150)
                                    onAddEntry()
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
}

class BottomNavCurveShape(private val cutoutRadius: Float) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            val center = width / 2f

            val curveSpread = cutoutRadius * 1.5f

            moveTo(0f, 0f)
            lineTo(center - curveSpread, 0f)

            cubicTo(
                x1 = center - cutoutRadius * 0.8f, y1 = 0f,
                x2 = center - cutoutRadius, y2 = cutoutRadius,
                x3 = center, y3 = cutoutRadius
            )
            cubicTo(
                x1 = center + cutoutRadius, y1 = cutoutRadius,
                x2 = center + cutoutRadius * 0.8f, y2 = 0f,
                x3 = center + curveSpread, y3 = 0f
            )

            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        return Outline.Generic(path)
    }
}