package com.example.digitaldiary // Make sure this is at the top of the file

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.nalin.my_digitaldiary.R // Required to access the res/raw folder
import kotlinx.coroutines.delay

@Composable
fun CustomSplashScreen(onTimeout: () -> Unit) {
    // Keeps the screen active for 2 seconds
    LaunchedEffect(Unit) {
        delay(2500)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFC0C26D)),
        contentAlignment = Alignment.Center
    ) {
        // Loads your Rive animation instead of text
        AndroidView(
            factory = { context ->
                RiveAnimationView(context).apply {
                    setRiveResource(R.raw.ghost)
                    play() // Auto-plays the animation
                }
            },
            // You can adjust this size to make the mascot larger or smaller
            modifier = Modifier.size(250.dp)
        )
    }
}