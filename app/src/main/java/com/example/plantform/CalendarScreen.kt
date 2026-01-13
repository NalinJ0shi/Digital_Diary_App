package com.example.plantform

import android.widget.CalendarView
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    onDateSelected: (Long) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val today = System.currentTimeMillis()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Time Machine") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // The Classic Calendar View
            AndroidView(
                factory = { ctx ->
                    CalendarView(ctx).apply {
                        // Limit the calendar so they can't scroll endlessly
                        maxDate = today
                    }
                },
                update = { view ->
                    view.setOnDateChangeListener { _, year, month, dayOfMonth ->
                        // Create a calendar object to get the timestamp
                        val clickCal = Calendar.getInstance()
                        clickCal.set(year, month, dayOfMonth)
                        val selectedTime = clickCal.timeInMillis

                        if (selectedTime > today) {
                            Toast.makeText(context, "No writing in the future!", Toast.LENGTH_SHORT).show()
                        } else {
                            onDateSelected(selectedTime)
                        }
                    }
                }
            )
        }
    }
}