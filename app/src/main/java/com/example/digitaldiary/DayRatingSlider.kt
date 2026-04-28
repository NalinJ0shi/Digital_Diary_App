package com.example.digitaldiary

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DayRatingSlider(
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "How was your day? $rating/10",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF9333EA) // Purple match
        )

        Slider(
            value = rating.toFloat(),
            onValueChange = { onRatingChange(it.toInt()) },
            valueRange = 1f..10f,
            steps = 9, // This creates the "clicks" for 1, 2, 3... 10
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF9333EA),
                activeTrackColor = Color(0xFF9333EA)
            )
        )
    }
}