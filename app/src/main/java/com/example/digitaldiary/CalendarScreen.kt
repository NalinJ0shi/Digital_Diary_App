// app/src/main/java/com/example/digitaldiary/CalendarScreen.kt
package com.example.digitaldiary

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import com.nalin.my_digitaldiary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    entries: List<DiaryEntry>,
    onDateSelected: (Long) -> Unit,
    onBack: () -> Unit,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    val context = LocalContext.current
    val todayMillis = System.currentTimeMillis()

    // --- DESIGN COLORS ---
    val emptyCircleColor = Color(0xFFEDE9E1)
    val textColor = Color(0xFFE2E8F0)
    val mutedTextColor = Color(0xFF94A3B8)
    val innerFillColor = Color(0xFFEDE9E1)
    val todayRingColor = Color(0xD3E0FF36)

    val happyColor = Color(0xFF4CAF50)
    val neutralColor = Color(0xFFFFEB3B)
    val sadColor = Color(0xFFF44336)

    // --- GRADIENT & HILLS ---
    val topColor = Color(0xFF0F172A)
    val bottomColor = Color(0xFF064E3B)
    val gradientBrush = Brush.verticalGradient(colors = listOf(topColor, bottomColor))

    val hillColor = Color(0xFFDAEBC0)
    val hillPathString = "M285 17.4657C203.574 -21.8322 183.5 17.4659 114.5 17.4658L-3 17.4657V203.801H402V27.8015C402 27.8015 352 49.8013 285 17.4657Z"
    val hillPath = remember { androidx.compose.ui.graphics.vector.PathParser().parsePathString(hillPathString).toPath() }

    val hill2Color = Color(0xFFC6D7AC)
    val hill2PathString = "M309.5 15.5557C225.712 -29.7517 192.778 63.778 117 15.5555C62 -19.4445 -3 15.5557 -3 15.5557V264.055H402V45.5552C402 45.5552 328.771 25.9765 309.5 15.5557Z"
    val hill2Path = remember { androidx.compose.ui.graphics.vector.PathParser().parsePathString(hill2PathString).toPath() }

    // --- CALENDAR MATH ---
    val calendar = remember { Calendar.getInstance() }
    val currentMonthYear = remember {
        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        monthYearFormat.format(calendar.time)
    }

    val firstDayOfWeek = remember {
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.get(Calendar.DAY_OF_WEEK) - 1
    }

    val daysInMonth = remember {
        calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    val todayCalc = remember { Calendar.getInstance().apply { timeInMillis = todayMillis } }
    val todayYear = todayCalc.get(Calendar.YEAR)
    val todayMonth = todayCalc.get(Calendar.MONTH)
    val todayDayNumber = todayCalc.get(Calendar.DAY_OF_MONTH)

    // --- PRE-PROCESS ENTRIES ---
    val entriesByDay = remember(entries) {
        val map = mutableMapOf<Int, DiaryEntry>()
        val tempCal = Calendar.getInstance()

        entries.forEach { entry ->
            tempCal.timeInMillis = entry.timestamp
            if (tempCal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                tempCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                map[tempCal.get(Calendar.DAY_OF_MONTH)] = entry
            }
        }
        map
    }

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
                    translate(top = -40f) { drawPath(path = hill2Path, color = hill2Color) }
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

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.home),
                            contentDescription = "Go Home",
                            Modifier.size(32.dp),
                            tint = textColor
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = currentMonthYear, color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(1.dp))
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Change Month", tint = textColor)
                    }
                    IconButton(onClick = { /* Handle Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = textColor)
                    }
                }
            },
            bottomBar = {
                CustomBottomNavBar(
                    selectedTab = 0,
                    onCalendarClick = onCalendarClick,
                    onChartClick = onChartClick,
                    onGameClick = onGameClick,
                    onProfileClick = onProfileClick,
                    onAddEntry = onAddEntry
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), // Reduced bottom margin
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    weekdays.forEach { day ->
                        Text(text = day, color = mutedTextColor, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 14.sp)
                    }
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp), // --- FIXED: Tightened space between rows ---
                    contentPadding = PaddingValues(bottom = 120.dp)
                ) {
                    items(firstDayOfWeek) {
                        Box(modifier = Modifier.size(48.dp))
                    }

                    items(daysInMonth) { dayIndex ->
                        val dayNumber = dayIndex + 1

                        val entryForDay = entriesByDay[dayNumber]
                        val hasMoodData = entryForDay != null

                        // Default to 3 (surprise/neutral) if something goes wrong
                        val moodRating = entryForDay?.dayRating ?: 3

                        // --- NEW: Map the 1-5 rating directly to your new drawable vectors ---
                        val emotionDrawableId = when (moodRating) {
                            1 -> R.drawable.sad
                            2 -> R.drawable.tire
                            3 -> R.drawable.surprise
                            4 -> R.drawable.happy
                            5 -> R.drawable.exicted
                            else -> R.drawable.surprise
                        }

                        val isToday = calendar.get(Calendar.YEAR) == todayYear &&
                                calendar.get(Calendar.MONTH) == todayMonth &&
                                dayNumber == todayDayNumber

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                val clickCal = Calendar.getInstance()
                                clickCal.set(Calendar.DAY_OF_MONTH, dayNumber)
                                val selectedTime = clickCal.timeInMillis

                                if (selectedTime > todayMillis) {
                                    Toast.makeText(context, "No writing in the future!", Toast.LENGTH_SHORT).show()
                                } else {
                                    onDateSelected(selectedTime)
                                }
                            }
                        ) {
                            Box(
                                modifier = Modifier.size(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    isToday -> {
                                        Icon(
                                            painter = painterResource(id = R.drawable.calender_face),
                                            contentDescription = "Today Outer Ring",
                                            modifier = Modifier.fillMaxSize(),
                                            tint = todayRingColor
                                        )
                                        Icon(
                                            painter = painterResource(id = R.drawable.calender_face),
                                            contentDescription = "Today Inner Fill",
                                            modifier = Modifier.size(38.dp),
                                            tint = innerFillColor
                                        )
                                    }
                                    hasMoodData -> {
                                        // --- NEW: Stack the vectors inside the Box ---

                                        // LAYER 1: The standard organic calendar face shape as a background
                                        Icon(
                                            painter = painterResource(id = R.drawable.calender_face),
                                            contentDescription = "Empty Custom Day Shape Background",
                                            modifier = Modifier.fillMaxSize(),
                                            tint = emptyCircleColor
                                        )

                                        // LAYER 2: Your custom Figma emoji vector drawn perfectly over top
                                        Icon(
                                            painter = painterResource(id = emotionDrawableId),
                                            contentDescription = "Mood Emoji Face",
                                            modifier = Modifier.fillMaxSize(),
                                            // CRITICAL: Unspecified prevents Compose from overriding your Figma colors!
                                            tint = Color.Unspecified
                                        )
                                    }
                                    else -> {
                                        Icon(
                                            painter = painterResource(id = R.drawable.calender_face),
                                            contentDescription = "Empty Custom Day Shape",
                                            modifier = Modifier.fillMaxSize(),
                                            tint = emptyCircleColor
                                        )
                                    }
                                }
                            }

                            // --- FIXED: Minimal text spacer gap ---
                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = dayNumber.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}