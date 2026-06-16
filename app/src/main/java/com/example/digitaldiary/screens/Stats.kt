package com.example.digitaldiary.screens

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitaldiary.main.CustomBottomNavBar
import com.example.digitaldiary.main.UniversalBackgroundWrapper
import com.example.digitaldiary.database.DiaryEntry
import com.nalin.my_digitaldiary.R
import java.text.SimpleDateFormat
import java.util.*

// --- Soft Color Palette ---
val CardWhite = Color.White
val PrimaryGreen = Color(0xFF6EBE80)
val TextDark = Color(0xFF2C2C2C)
val TextGray = Color(0xFFA0A0A0)
val GridBarColor = Color(0xFFF4F5F7)
val GridLineColor = Color(0xFFE2E8F0)

// Safely keeping this as a fallback color for the 0% proportion empty states
val EmptyBgColorFallback = Color(0xFF0F172A)

// Mood Colors (Best to Worst)
val MoodColors = listOf(
    Color(0xFF6EBE80), // 5: Best
    Color(0xFF8FCE9D), // 4: Good
    Color(0xFFBBE5C5), // 3: Okay
    Color(0xFFE2E8E4), // 2: Bad
    Color(0xFFC7CDCE)  // 1: Terrible
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(
    entries: List<DiaryEntry>,
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    var isWeekly by remember { mutableStateOf(true) }
    // Sorting chronologically before grouping so the timeline flows correctly
    val dailyAverages = remember(entries, isWeekly) {
        val sortedEntries = entries.sortedBy { it.timestamp } // [OLD] Rest of sorting stays same

        if (isWeekly) {
            // [OLD] Your existing weekly 7-day calculation logic goes here
            sortedEntries.groupBy {
                val calendar = Calendar.getInstance().apply { timeInMillis = it.timestamp }
                SimpleDateFormat("d MMM", Locale.getDefault()).format(calendar.time)
            }.mapValues { entryMap ->
                entryMap.value.map { it.dayRating.toDouble() }.average().toFloat()
            }.toList().takeLast(7)
        } else {
            // [NEW] Monthly calculation logic (e.g., grouping by 4 weeks instead of days)
            sortedEntries.groupBy {
                val calendar = Calendar.getInstance().apply { timeInMillis = it.timestamp }
                "Week " + calendar.get(Calendar.WEEK_OF_MONTH)
            }.mapValues { entryMap ->
                entryMap.value.map { it.dayRating.toDouble() }.average().toFloat()
            }.toList()
        }
    }

    // Proportion Logic: Calculates % of each mood score (1-5)
    val proportions = remember(entries, isWeekly) {
        // [NEW] Filter entries first based on the active timeframe (7 days vs 30 days)
        val filteredEntries = if (isWeekly) {
            entries.takeLast(7) // Example fallback filter
        } else {
            entries.takeLast(30)
        }

        val total = filteredEntries.size.coerceAtLeast(1) // [OLD] Logic formula remains unchanged
        val counts = filteredEntries.groupingBy { it.dayRating }.eachCount()
        (5 downTo 1).map { mood ->
            (counts[mood] ?: 0).toFloat() / total
        }
    }

    // Swapped raw gradient box with your global single-source design framework component
    UniversalBackgroundWrapper {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.potted_plant),
                contentDescription = "Navigate to Garden",
                tint = Color(0xFFFFFFFF),
                modifier = Modifier.size(32.dp)
            )
        }
        Scaffold(
            // Make the Scaffold transparent so the universal box background shows through
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {},
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            bottomBar = {
                CustomBottomNavBar(
                    selectedTab = 1,
                    onCalendarClick = onCalendarClick,
                    onChartClick = onChartClick,
                    onGameClick = onGameClick,
                    onProfileClick = onProfileClick,
                    onAddEntry = onAddEntry
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. Weekly/Monthly Tabs
                Row(modifier = Modifier.padding(bottom = 16.dp)) {
                    Text(
                        text = "Weekly",
                        color = if (isWeekly) PrimaryGreen else TextGray, // [NEW] Dynamic color
                        fontWeight = if (isWeekly) FontWeight.Bold else FontWeight.Medium, // [NEW] Dynamic weight
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { isWeekly = true }
                    )
                    Text(
                        text = "Monthly",
                        color = if (!isWeekly) PrimaryGreen else TextGray, // [NEW] Dynamic color
                        fontWeight = if (!isWeekly) FontWeight.Bold else FontWeight.Medium, // [NEW] Dynamic weight
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clickable { isWeekly = false }
                    )
                }

                // 2. Date Selector

                // 3. Mood Flow Card (The Graph)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Mood flow", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                        Spacer(modifier = Modifier.height(24.dp))

                        Box(modifier = Modifier.fillMaxWidth().height(130.dp)) {
                            if (dailyAverages.isEmpty()) {
                                Text("Not enough data.", Modifier.align(Alignment.Center), color = TextGray)
                            } else {
                                StraightLineBarGraph(dataPoints = dailyAverages)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 4. Mood Proportion Card (The Stats)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 40.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text("Mood proportion", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextDark)
                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GridBarColor)
                        ) {
                            proportions.forEachIndexed { index, weight ->
                                if (weight > 0) {
                                    Box(
                                        modifier = Modifier
                                            .weight(weight)
                                            .fillMaxHeight()
                                            .background(MoodColors[index])
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            proportions.forEachIndexed { index, percentage ->
                                val percentInt = (percentage * 100).toInt()
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clip(CircleShape)
                                            .background(MoodColors[index])
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (percentInt > 0) PrimaryGreen.copy(alpha = 0.1f) else EmptyBgColorFallback)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "$percentInt%",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (percentInt > 0) PrimaryGreen else TextGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StraightLineBarGraph(dataPoints: List<Pair<String, Float>>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val maxPoint = 5f
        val minPoint = 1f

        val textPaddingBottom = 60f
        val yAxisPadding = 40.dp.toPx()

        val graphWidth = size.width - yAxisPadding
        val graphHeight = size.height - textPaddingBottom
        val spacePerNode = graphWidth / if (dataPoints.size > 1) (dataPoints.size - 1) else 1
        val heightRatio = graphHeight / (maxPoint - minPoint)

        val textPaint = Paint().apply {
            color = android.graphics.Color.parseColor("#A0A0A0")
            textSize = 28f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }

        // LAYER 1: The Static 4-Week Background Grid
        val weekSpacing = graphWidth / 3
        for (i in 0..3) {
            val gridX = yAxisPadding + (i * weekSpacing)
            drawLine(
                color = GridLineColor,
                start = Offset(gridX, 0f),
                end = Offset(gridX, graphHeight),
                strokeWidth = 2.dp.toPx()
            )
        }

        // LAYER 2: Draw Y-Axis Mood Dots (The static scale on the left)
        for (i in 5 downTo 1) {
            val y = graphHeight - ((i - minPoint) * heightRatio)
            drawCircle(
                color = MoodColors[5 - i],
                radius = 6.dp.toPx(),
                center = Offset(10.dp.toPx(), y)
            )
        }

        // Pre-calculate path nodes for data
        val path = Path()
        val coordinates = mutableListOf<Offset>()

        dataPoints.forEachIndexed { index, data ->
            val x = yAxisPadding + (index * spacePerNode)
            val y = graphHeight - ((data.second - minPoint) * heightRatio)
            coordinates.add(Offset(x, y))

            // Draw the exact date text centered under this data point
            drawContext.canvas.nativeCanvas.drawText(
                data.first,
                x,
                size.height - 10f,
                textPaint
            )
        }

        // FIXED LAYER PIPELINE ORDER:
        // LAYER 3: Draw the data point rings/anchors first
        coordinates.forEach { offset ->
            drawCircle(color = Color.White, radius = 7.dp.toPx(), center = offset)
            drawCircle(color = PrimaryGreen, radius = 4.dp.toPx(), center = offset)
        }

        // LAYER 4: Draw the Line *ON TOP* so it cleanly enters and intersects the nodes
        if (coordinates.isNotEmpty()) {
            path.moveTo(coordinates.first().x, coordinates.first().y)
            for (i in 1 until coordinates.size) {
                path.lineTo(coordinates[i].x, coordinates[i].y)
            }

            drawPath(
                path = path,
                color = PrimaryGreen,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}