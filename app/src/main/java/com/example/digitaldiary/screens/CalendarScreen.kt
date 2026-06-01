package com.example.digitaldiary.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.digitaldiary.CustomBottomNavBar
import com.example.digitaldiary.database.DiaryEntry
import com.example.digitaldiary.database.DiaryItem
import com.nalin.my_digitaldiary.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape

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

    // --- GRADIENT & HILLS ---
    val hillColor = Color(0xFFDAEBC0)
    val hillPathString = "M285 17.4657C203.574 -21.8322 183.5 17.4659 114.5 17.4658L-3 17.4657V203.801H402V27.8015C402 27.8015 352 49.8013 285 17.4657Z"
    val hillPath = remember { PathParser().parsePathString(hillPathString).toPath() }

    val hill2Color = Color(0xFFC6D7AC)
    val hill2PathString = "M309.5 15.5557C225.712 -29.7517 192.778 63.778 117 15.5555C62 -19.4445 -3 15.5557 -3 15.5557V264.055H402V45.5552C402 45.5552 328.771 25.9765 309.5 15.5557Z"
    val hill2Path = remember { PathParser().parsePathString(hill2PathString).toPath() }

    // --- CALENDAR MATH ---
    // Updated to react to the currently selected displayedMonth and displayedYear
    val calendar = remember(displayedMonth, displayedYear) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, displayedYear)
            set(Calendar.MONTH, displayedMonth)
            set(Calendar.DAY_OF_MONTH, 1) // Important for firstDayOfWeek calculation
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

    // THE FIX: Universal brush reference for downstream canvas logic
    val gradientBrush = com.example.digitaldiary.AppDesignTokens.UniversalBrush

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(402f / 204f).align(Alignment.BottomCenter)) {
            val scaleX = size.width / 402f
            val scaleY = size.height / 204f
            withTransform({ scale(scaleX, scaleY, Offset.Zero) }) { translate(top = -40f) { drawPath(path = hill2Path, color = hill2Color) } }
        }
        Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(402f / 204f).align(Alignment.BottomCenter)) {
            val scaleX = size.width / 402f
            val scaleY = size.height / 204f
            withTransform({ scale(scaleX, scaleY, Offset.Zero) }) { drawPath(path = hillPath, color = hillColor) }
        }

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Row(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 62.dp, bottom = 6.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(painter = painterResource(id = R.drawable.potted_plant), contentDescription = "Go Home", Modifier.size(32.dp), tint = textColor)
                    }

                    // Added clickable area to open the date picker modal
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { showDatePicker = true }
                    ) {
                        Text(text = currentMonthYear, color = textColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(1.dp))
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Change Month", tint = textColor)
                    }

                    IconButton(onClick = { }) { Icon(Icons.Default.Share, contentDescription = "Share", tint = textColor) }
                }
            },
            bottomBar = {
                CustomBottomNavBar(0, onCalendarClick, onChartClick, onGameClick, onProfileClick, onAddEntry)
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(24.dp))

                val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    weekdays.forEach { day -> Text(text = day, color = mutedTextColor, modifier = Modifier.weight(1f), textAlign = TextAlign.Center, fontSize = 14.sp) }
                }

                LazyVerticalGrid(columns = GridCells.Fixed(7), modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp), contentPadding = PaddingValues(bottom = 16.dp)) {
                    items(firstDayOfWeek) { Box(modifier = Modifier.size(48.dp)) }
                    items(daysInMonth) { dayIndex ->
                        val dayNumber = dayIndex + 1
                        val entryForDay = entriesByDay[dayNumber]
                        val hasMoodData = entryForDay != null
                        val moodRating = entryForDay?.dayRating ?: 3
                        val emotionDrawableId = when (moodRating) { 1 -> R.drawable.sad; 2 -> R.drawable.tire; 3 -> R.drawable.surprise; 4 -> R.drawable.happy; 5 -> R.drawable.exicted; else -> R.drawable.surprise }
                        val isToday = calendar.get(Calendar.YEAR) == todayYear && calendar.get(Calendar.MONTH) == todayMonth && dayNumber == todayDayNumber

                        // Updated clickCal so it uses the currently viewed month, not the actual current system month
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

        // Trigger the Modal when showDatePicker is true
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

// --- NEW COMPONENTS FOR THE BOTTOM SHEET PICKER ---

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MonthYearPickerModal(
    currentMonth: Int, // 0 - 11
    currentYear: Int,
    onDismiss: () -> Unit,
    onConfirm: (month: Int, year: Int) -> Unit
) {
    val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    // Generate a list of years (e.g., from 5 years ago to 5 years in the future)
    val years = (currentYear - 5..currentYear + 5).map { it.toString() }

    var selectedMonth by remember { mutableIntStateOf(currentMonth) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFFFFFFF),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp, top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Text(
                text = "Select Month",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                color = Color(0xFF1E293B)
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Picker Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                // The thin horizontal divider lines for the selection zone
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(Color(0xFFF1F5F9).copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                )

                // Two-column layout
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Month Column
                    WheelPicker(
                        modifier = Modifier.weight(1f),
                        items = months,
                        initialIndex = currentMonth,
                        onItemSelected = { selectedMonth = it }
                    )
                    // Year Column
                    WheelPicker(
                        modifier = Modifier.weight(1f),
                        items = years,
                        initialIndex = years.indexOf(currentYear.toString()),
                        onItemSelected = { selectedYear = years[it].toInt() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Secondary Cancel Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text("Cancel", color = Color(0xFF475569), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                // Primary Confirm Button
                Button(
                    onClick = { onConfirm(selectedMonth, selectedYear) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text("Confirm", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

// The internal snapping wheel engine
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

    // Pad the list with 2 empty items on top and bottom so first/last items can snap to the center
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
            // The center item is always exactly 2 slots away from the first visible item
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