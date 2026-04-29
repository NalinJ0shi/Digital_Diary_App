package com.example.digitaldiary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun CustomSplashScreen(onTimeout: () -> Unit) {
    // LaunchedEffect guarantees this timer only starts once when the screen appears
    LaunchedEffect(Unit) {
        delay(2000) // 2000 milliseconds = exactly 2 seconds
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "hello",
            color = Color.White,
            fontSize = 32.sp
        )
    }
}