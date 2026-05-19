// app/src/main/java/com/example/digitaldiary/CalendarScreen.kt
package com.example.digitaldiary

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
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
    val today = System.currentTimeMillis()

    // --- CONTROL: DESIGN COLORS ---
    val bgColor = Color(0xFF1E1E1E)
    val emptyCircleColor = Color(0xFF424D58)
    val textColor = Color(0xFFE2E8F0)
    val mutedTextColor = Color(0xFF94A3B8)

    val happyColor = Color(0xFF4CAF50)
    val neutralColor = Color(0xFFFFEB3B)
    val sadColor = Color(0xFFF44336)

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

    Scaffold(
        containerColor = bgColor,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // --- FORCE RESET TO HOME ---
                IconButton(onClick = onBack) {
                    Icon(
                        painter = painterResource(id = R.drawable.house_line),
                        contentDescription = "Go Home",
                        tint = textColor
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = currentMonthYear, color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Change Month", tint = textColor)
                }
                IconButton(onClick = { /* Handle Share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = textColor)
                }
            }
        },
        bottomBar = {
            // --- CONTROL: REUSABLE NAVBAR COMPONENT ---
            CustomBottomNavBar(
                selectedTab = 0, // Keeps the Calendar tab highlighted green
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
            val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(firstDayOfWeek) {
                    Box(modifier = Modifier.size(48.dp))
                }

                items(daysInMonth) { dayIndex ->
                    val dayNumber = dayIndex + 1

                    val entryForDay = entriesByDay[dayNumber]
                    val hasMoodData = entryForDay != null
                    val moodRating = entryForDay?.dayRating ?: 5

                    val circleColor = when {
                        !hasMoodData -> emptyCircleColor
                        moodRating >= 8 -> happyColor
                        moodRating >= 4 -> neutralColor
                        else -> sadColor
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable {
                            val clickCal = Calendar.getInstance()
                            clickCal.set(Calendar.DAY_OF_MONTH, dayNumber)
                            val selectedTime = clickCal.timeInMillis

                            if (selectedTime > today) {
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
                            // --- CONTROL: UNBREAKABLE SLIGHTLY IRREGULAR CIRCLE BACKGROUND ---
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(color = circleColor, shape = OrganicCircleShape())
                            )

                            if (hasMoodData) {
                                Icon(
                                    painter = painterResource(id = R.drawable.calender_face),
                                    contentDescription = "Mood Face",
                                    modifier = Modifier.size(24.dp),
                                    tint = bgColor
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = dayNumber.toString(),
                            color = if (hasMoodData) circleColor else textColor,
                            fontSize = 12.sp,
                            fontWeight = if (hasMoodData) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}

class OrganicCircleShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            val w = size.width
            val h = size.height

            moveTo(w * 0.5f, h * 0.02f)
            cubicTo(w * 0.88f, h * 0.01f, w * 0.99f, h * 0.22f, w * 0.98f, h * 0.5f)
            cubicTo(w * 0.97f, h * 0.78f, w * 0.78f, h * 0.96f, w * 0.48f, h * 0.98f)
            cubicTo(w * 0.18f, h * 1.00f, w * 0.02f, h * 0.76f, w * 0.01f, h * 0.48f)
            cubicTo(w * 0.00f, h * 0.20f, w * 0.22f, h * 0.03f, w * 0.5f, h * 0.02f)
            close()
        }
        return Outline.Generic(path)
    }
}