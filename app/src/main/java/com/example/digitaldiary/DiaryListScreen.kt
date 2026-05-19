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

            // --- CONTROL: NAVBAR OVERALL HEIGHT ---
            val totalClickableHeight = 100.dp
            val visualBarHeight = 80.dp

            // --- CONTROL: SHAPE DRAWING (The Physical Curve) ---
            val curveWidthPx = 330f      // How wide the top gap is
            val curveDepthPx = 130f      // How deep the U-shape dips down
            val cornerSmoothingPx = 80f  // How wide the smooth transition corners are

            // --- CONTROL: ICON DEAD-ZONE (Pushes left/right icons apart) ---
            val iconSeparationWidth = 190.dp

            // --- CONTROL: ICON SIZES ---
            val icon1Size = 32.dp
            val icon2Size = 32.dp
            val icon3Size = 32.dp
            val icon4Size = 32.dp

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(totalClickableHeight),
                contentAlignment = Alignment.BottomCenter
            ) {

                // --- CONTROL: THE UNBREAKABLE SHAPE LAYER ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(visualBarHeight)
                        .align(Alignment.BottomCenter)
                        .graphicsLayer {
                            shape = NavBarCutoutShape(
                                cutoutWidth = curveWidthPx,
                                cutoutDepth = curveDepthPx,
                                cornerSmoothing = cornerSmoothingPx
                            )
                            clip = true
                            shadowElevation = 20f
                        }
                        .background(navBarColor)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                selectedTab = 0
                                onOpenCalendar()
                            },
                            // --- CONTROL: ICON 1 POSITIONING ---
                            modifier = Modifier.offset(x = 5.dp, y = -5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar_blank),
                                contentDescription = "Nav Item 1",
                                modifier = Modifier.size(icon1Size),
                                tint = if (selectedTab == 0) activeIconColor else inactiveIconColor
                            )
                        }
                        IconButton(
                            onClick = {
                                selectedTab = 1
                                onNavigateToChart()
                            },
                            // --- CONTROL: ICON 2 POSITIONING ---
                            modifier = Modifier.offset(x = 25.dp, y = -5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chart_line),
                                contentDescription = "Nav Item 2",
                                modifier = Modifier.size(icon2Size),
                                tint = if (selectedTab == 1) activeIconColor else inactiveIconColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(iconSeparationWidth))

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                selectedTab = 2
                                onNavigateToGame()
                            },
                            // --- CONTROL: ICON 3 POSITIONING ---
                            modifier = Modifier.offset(x = -25.dp, y = -5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.game_controller),
                                contentDescription = "Nav Item 3",
                                modifier = Modifier.size(icon3Size),
                                tint = if (selectedTab == 2) activeIconColor else inactiveIconColor
                            )
                        }
                        IconButton(
                            onClick = {
                                selectedTab = 3
                                onNavigateToProfile()
                            },
                            // --- CONTROL: ICON 4 POSITIONING ---
                            modifier = Modifier.offset(x = -5.dp, y = -5.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.user),
                                contentDescription = "Nav Item 4",
                                modifier = Modifier.size(icon4Size),
                                tint = if (selectedTab == 3) activeIconColor else inactiveIconColor
                            )
                        }
                    }
                }

                AndroidView(
                    modifier = Modifier
                        // --- CONTROL: RIVE BUTTON SIZE & POSITION ---
                        .size(120.dp)
                        .align(Alignment.BottomCenter)
                        .offset(x = 0.dp, y = (-25).dp),
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
                                    delay(300)
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

class NavBarCutoutShape(
    private val cutoutWidth: Float,
    private val cutoutDepth: Float,
    private val cornerSmoothing: Float
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            val center = width / 2f
            val halfWidth = cutoutWidth / 2f

            moveTo(0f, 0f)
            lineTo(center - halfWidth, 0f)

            cubicTo(
                x1 = center - halfWidth + cornerSmoothing, y1 = 0f,
                x2 = center - halfWidth + cornerSmoothing, y2 = cutoutDepth,
                x3 = center, y3 = cutoutDepth
            )

            cubicTo(
                x1 = center + halfWidth - cornerSmoothing, y1 = cutoutDepth,
                x2 = center + halfWidth - cornerSmoothing, y2 = 0f,
                x3 = center + halfWidth, y3 = 0f
            )

            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        return Outline.Generic(path)
    }
}