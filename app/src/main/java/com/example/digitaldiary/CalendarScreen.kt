package com.example.digitaldiary

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// THE FIX: Tells the file where to find your res/drawable/ folder
import com.nalin.my_digitaldiary.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onDateSelected: (Long) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val today = System.currentTimeMillis()

    // --- DESIGN COLORS ---
    val bgColor = Color(0xFF1E1E1E) // Dark grey/black from mockup
    val emptyCircleColor = Color(0xFF424D58) // The grey placeholder circles
    val textColor = Color(0xFFE2E8F0)
    val mutedTextColor = Color(0xFF94A3B8)

    // Placeholder color for a "Happy" day
    val sampleMoodColor = Color(0xFF4CAF50)

    // --- CALENDAR MATH ---
    val calendar = Calendar.getInstance()
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    val currentMonthYear = monthYearFormat.format(calendar.time)

    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    Scaffold(
        containerColor = bgColor,
        topBar = {
            // CUSTOM HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = textColor)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentMonthYear,
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Change Month", tint = textColor)
                }

                IconButton(onClick = { /* Handle Share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = textColor)
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            // --- WEEKDAY LABELS ---
            val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                weekdays.forEach { day ->
                    Text(
                        text = day,
                        color = mutedTextColor,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }

            // --- THE CUSTOM GRID ---
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Fill empty spaces before the 1st
                items(firstDayOfWeek) {
                    Box(modifier = Modifier.size(48.dp))
                }

                // 2. Draw the days
                items(daysInMonth) { dayIndex ->
                    val dayNumber = dayIndex + 1

                    // Dummy data: Day 1 is happy, rest are empty
                    val hasMoodData = (dayNumber == 1)

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
                        // THE CUSTOM IMPERFECT CIRCLE
                        Box(
                            modifier = Modifier.size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Draws your vector background and colors it dynamically
                            Icon(
                                painter = painterResource(id = R.drawable.calender_face),
                                contentDescription = "Day Background",
                                modifier = Modifier.fillMaxSize(),
                                tint = if (hasMoodData) sampleMoodColor else emptyCircleColor
                            )

                            // Draws the face on top
                            if (hasMoodData) {
                                Text("🙂", fontSize = 24.sp) // We will replace this with your face vector later!
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dayNumber.toString(),
                            color = if (hasMoodData) sampleMoodColor else textColor,
                            fontSize = 12.sp,
                            fontWeight = if (hasMoodData) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
    }
}