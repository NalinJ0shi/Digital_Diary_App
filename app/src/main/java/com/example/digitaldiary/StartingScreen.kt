package com.example.digitaldiary

import com.nalin.my_digitaldiary.R
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun StartScreen(isDarkMode: Boolean, onEnter: () -> Unit) {
    val topColor = if (isDarkMode) Color(0xFF1A1A2E) else Color(0xFFF3E8FF)
    val bottomColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val titleColor = if (isDarkMode) Color(0xFFE9D5FF) else Color(0xFF4B218B)
    val gradientBrush = Brush.verticalGradient(colors = listOf(topColor, bottomColor))

    Box(
        modifier = Modifier.fillMaxSize().background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        // --- FLOATING MOODS ---
        // Happy drifting on the top left
        FloatingElement(resId = R.drawable.happy, xOffset = (-120).dp, yOffset = (0).dp, duration = 3000)

        // Meh hanging out on the middle right
        FloatingElement(resId = R.drawable.meh, xOffset = 0.dp, yOffset = 350.dp, duration = 3500)

        // Anxious floating at the bottom left
        FloatingElement(resId = R.drawable.anxios, xOffset = (150).dp, yOffset = 100.dp, duration = 4000)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "My Digital Diary",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Your secrets are safe with me.",
                fontSize = 16.sp,
                color = if (isDarkMode) Color.LightGray else Color.Gray
            )

            Spacer(modifier = Modifier.height(120.dp))
            SwipeToUnlock(onUnlock = onEnter)
        }
    }
}

@Composable
fun FloatingElement(resId: Int, xOffset: androidx.compose.ui.unit.Dp, yOffset: androidx.compose.ui.unit.Dp, duration: Int) {
    val transition = rememberInfiniteTransition(label = "floating")
    val bobbingOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "yBob"
    )

    Image(
        painter = painterResource(id = resId),
        contentDescription = null,
        modifier = Modifier
            .offset(x = xOffset, y = yOffset + bobbingOffset.dp)
            .size(400.dp)
            .alpha(0.5f) // Very subtle so the text stays readable
    )
}

@Composable
fun SwipeToUnlock(onUnlock: () -> Unit) {
    val trackWidth = 280.dp
    val thumbSize = 56.dp
    val trackWidthPx = with(LocalDensity.current) { (trackWidth - thumbSize).toPx() }
    var offsetX by remember { mutableStateOf(0f) }

    Box(
        modifier = Modifier
            .width(trackWidth)
            .height(thumbSize)
            .background(Color.Black.copy(alpha = 0.1f), CircleShape)
            .padding(4.dp)
    ) {
        Text(
            text = "Swipe to enter",
            modifier = Modifier.align(Alignment.Center).alpha(1f - (offsetX / trackWidthPx)),
            color = Color.Gray,
            fontSize = 16.sp
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(thumbSize - 8.dp)
                .background(Color(0xFF9333EA), CircleShape)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX = (offsetX + delta).coerceIn(0f, trackWidthPx)
                    },
                    onDragStopped = {
                        if (offsetX >= trackWidthPx * 0.9f) {
                            offsetX = trackWidthPx
                            onUnlock()
                        } else {
                            offsetX = 0f
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.KeyboardArrowRight, null, tint = Color.White)
        }
    }
}