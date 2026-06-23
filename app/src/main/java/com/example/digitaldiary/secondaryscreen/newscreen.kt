package com.example.digitaldiary.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.digitaldiary.miscellaneousBS.UniversalBackgroundWrapper
import com.example.digitaldiary.ui.theme.JosefinSans

@Composable
fun YellowScreen() {
    UniversalBackgroundWrapper {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "yellow",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = JosefinSans,
                color = Color(0xFF1E293B)
            )
        }
    }
}