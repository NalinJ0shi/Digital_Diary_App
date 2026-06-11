package com.example.digitaldiary.screens

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import com.example.digitaldiary.main.CustomBottomNavBar
import com.example.digitaldiary.main.UniversalBackgroundWrapper
import com.example.digitaldiary.database.DiaryEntry
import com.example.digitaldiary.database.DiaryItem
import com.nalin.my_digitaldiary.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
    onAddEntry: () -> Unit,
    onEditEntry: (DiaryEntry) -> Unit,
    onDeleteEntry: (DiaryEntry) -> Unit
) {
    val context = LocalContext.current
    val todayMillis = System.currentTimeMillis()
    var selectedDateMillis by remember { mutableLongStateOf(todayMillis) }

    // --- DATE PICKER STATE ---
    var showDatePicker by remember { mutableStateOf(false) }
    val initialCal = remember { Calendar.getInstance() }
    var displayedMonth by remember { mutableIntStateOf(initialCal.get(Calendar.MONTH)) }
    var displayedYear by remember { mutableIntStateOf(initialCal.get(Calendar.YEAR)) }

    // --- DESIGN COLORS ---
    val emptyCircleColor = Color(0xFFEDE9E1)
    val textColor = Color(0xFFE2E8F0)
    val mutedTextColor = Color(0xFF94A3B8)
    val innerFillColor = Color(0xFFEDE9E1)
    val todayRingColor = Color(0xD3E0FF36)

    // --- CALENDAR MATH ---
    val calendar = remember(displayedMonth, displayedYear) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, displayedYear)
            set(Calendar.MONTH, displayedMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val currentMonthYear = remember(calendar) {
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(calendar.time)
    }
    val firstDayOfWeek = remember(calendar) { calendar.get(Calendar.DAY_OF_WEEK) - 1 }
    val daysInMonth = remember(calendar) { calendar.getActualMaximum(Calendar.DAY_OF_MONTH) }

    val todayCalc = remember { Calendar.getInstance().apply { timeInMillis = todayMillis } }
    val todayYear = todayCalc.get(Calendar.YEAR)
    val todayMonth = todayCalc.get(Calendar.MONTH)
    val todayDayNumber = todayCalc.get(Calendar.DAY_OF_MONTH)

    val entriesByDay = remember(entries, calendar) {
        val map = mutableMapOf<Int, DiaryEntry>()
        val tempCal = Calendar.getInstance()
        entries.forEach { entry ->
            tempCal.timeInMillis = entry.timestamp
            if (tempCal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) && tempCal.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                map[tempCal.get(Calendar.DAY_OF_MONTH)] = entry
            }
        }
        map
    }

    val selectedEntry = remember(selectedDateMillis, entries) {
        val selectedCal = Calendar.getInstance().apply { timeInMillis = selectedDateMillis }
        val sYear = selectedCal.get(Calendar.YEAR)
        val sMonth = selectedCal.get(Calendar.MONTH)
        val sDay = selectedCal.get(Calendar.DAY_OF_MONTH)
        val tempCal = Calendar.getInstance()
        entries.find { entry ->
            tempCal.timeInMillis = entry.timestamp
            tempCal.get(Calendar.YEAR) == sYear && tempCal.get(Calendar.MONTH) == sMonth && tempCal.get(Calendar.DAY_OF_MONTH) == sDay
        }
    }

    // 1. ALL LOCAL CANVAS MATH DELETED. Wrapped entirely in UniversalBackgroundWrapper
    UniversalBackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, top = 30.dp) // Manually sets its exact spot
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.potted_plant),
                        contentDescription = "Navigate to Garden",
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFFFFFFFF)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                    horizontalAlignment = Alignment.Start
                )
                {
                    Spacer(modifier = Modifier.height(66.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { showDatePicker = true }
                            .padding(start = 128.dp, bottom = 8.dp) // Extra padding for perfect text alignment
                    )
                    {
                        Text(
                            text = currentMonthYear,
                            color = Color(0xFFFFFFFF),
                            fontSize = 26.sp, // Made larger and bolder as its own title block
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "",
                            tint = Color(0xFFFFFFFF),
                            modifier = Modifier.size(28.dp) // Scaled up arrow to match new text size
                        )
                    }
                }
            },


            bottomBar = {
                CustomBottomNavBar(0, onCalendarClick, onChartClick, onGameClick, onProfileClick, onAddEntry)
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(24.dp))

                val weekdays = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    weekdays.forEach { day -> Text(text = day, color = CardWhite, modifier = Modifier.weight(4f), textAlign = TextAlign.Center, fontSize = 18.sp) }
                }

                LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(1.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
                    items(firstDayOfWeek) { Box(modifier = Modifier.size(48.dp)) }
                    items(daysInMonth) { dayIndex ->
                        val dayNumber = dayIndex + 1
                        val entryForDay = entriesByDay[dayNumber]
                        val hasMoodData = entryForDay != null
                        val moodRating = entryForDay?.dayRating ?: 3
                        val emotionDrawableId = when (moodRating) { 1 -> R.drawable.sad; 2 -> R.drawable.tire; 3 -> R.drawable.surprise; 4 -> R.drawable.happy; 5 -> R.drawable.exicted; else -> R.drawable.surprise }
                        val isToday = calendar.get(Calendar.YEAR) == todayYear && calendar.get(Calendar.MONTH) == todayMonth && dayNumber == todayDayNumber

                        val clickCal = Calendar.getInstance().apply {
                            set(Calendar.YEAR, displayedYear)
                            set(Calendar.MONTH, displayedMonth)
                            set(Calendar.DAY_OF_MONTH, dayNumber)
                        }
                        val isSelected = clickCal.timeInMillis == selectedDateMillis

                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                            val selectedTime = clickCal.timeInMillis
                            if (selectedTime > todayMillis) Toast.makeText(context, "No writing in the future!", Toast.LENGTH_SHORT).show()
                            else { selectedDateMillis = selectedTime; if (!hasMoodData) onDateSelected(selectedTime) }
                        }) {
                            Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                                if (isToday) { Icon(painter = painterResource(id = R.drawable.calender_face), contentDescription = null, modifier = Modifier.fillMaxSize(), tint = todayRingColor); Icon(painter = painterResource(id = R.drawable.calender_face), contentDescription = null, modifier = Modifier.size(38.dp), tint = innerFillColor) }
                                else { Icon(painter = painterResource(id = R.drawable.calender_face), contentDescription = null, modifier = Modifier.fillMaxSize(), tint = emptyCircleColor) }
                                if (hasMoodData) Icon(painter = painterResource(id = emotionDrawableId), contentDescription = null, modifier = Modifier.fillMaxSize(), tint = Color.Unspecified)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = dayNumber.toString(), color = if (isSelected) todayRingColor else Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
                    if (selectedEntry != null) DiaryItem(selectedEntry, onEditEntry, { onDeleteEntry(it) })
                    else Text(text = "No thoughts logged for this day yet.", color = mutedTextColor, fontSize = 16.sp, textAlign = TextAlign.Center)
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        if (showDatePicker) {
            MonthYearPickerModal(
                currentMonth = displayedMonth,
                currentYear = displayedYear,
                onDismiss = { showDatePicker = false },
                onConfirm = { newMonth, newYear ->
                    displayedMonth = newMonth
                    displayedYear = newYear
                    showDatePicker = false
                }
            )
        }
    }
}

// --- DATE PICKER COMPONENTS ---

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MonthYearPickerModal(
    currentMonth: Int,
    currentYear: Int,
    onDismiss: () -> Unit,
    onConfirm: (month: Int, year: Int) -> Unit
) {
    val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    val years = (currentYear - 5..currentYear + 5).map { it.toString() }

    var selectedMonth by remember { mutableIntStateOf(currentMonth) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Month",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color(0xFF1E293B)
                )
                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(Color(0xFFF1F5F9).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        WheelPicker(
                            modifier = Modifier.weight(1f),
                            items = months,
                            initialIndex = currentMonth,
                            onItemSelected = { selectedMonth = it }
                        )
                        WheelPicker(
                            modifier = Modifier.weight(1f),
                            items = years,
                            initialIndex = years.indexOf(currentYear.toString()),
                            onItemSelected = { selectedYear = years[it].toInt() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text("Cancel", color = Color(0xFF475569), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Button(
                        onClick = { onConfirm(selectedMonth, selectedYear) },
                        modifier = Modifier.weight(1f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Text("OK", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelPicker(
    modifier: Modifier = Modifier,
    items: List<String>,
    initialIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val itemHeight = 48.dp

    val paddedItems = remember(items) { listOf("", "") + items + listOf("", "") }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                if (index in items.indices) {
                    onItemSelected(index)
                }
            }
    }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        modifier = modifier.height(itemHeight * 5),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        itemsIndexed(paddedItems) { index, item ->
            val isCenter = listState.firstVisibleItemIndex == index - 2

            Box(
                modifier = Modifier
                    .height(itemHeight)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = if (isCenter) FontWeight.ExtraBold else FontWeight.Medium,
                        fontSize = if (isCenter) 20.sp else 16.sp
                    ),
                    color = if (isCenter) Color(0xFF0F172A) else Color(0xFF94A3B8)
                )
            }
        }
    }
}