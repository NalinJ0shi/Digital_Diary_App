package com.nalin.my_digitaldiary
import com.nalin.my_digitaldiary.R
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MainScreen(isDarkMode: Boolean) {
    val topColor = if (isDarkMode) Color(0xFF1A1A2E) else Color(0xFFF3E8FF)
    val bottomColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFFFFFFF)
    val titleColor = if (isDarkMode) Color(0xFFE9D5FF) else Color(0xFF4B218B)
    val gradientBrush = Brush.verticalGradient(colors = listOf(topColor, bottomColor))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        FloatingBlobs(isDarkMode = isDarkMode)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

//            AndroidView(
//                modifier = Modifier
//                    .size(250.dp)
//                    .padding(bottom = 16.dp),
//                factory = { context ->
//                    RiveAnimationView(context).apply {
//                        setRiveResource(
//                            resId = R.raw.leaf,
////                            animationName = "Active" // Your animation timeline name
//                        )
//                    }
//                }
//            )
            AndroidView(
                modifier = Modifier
                    .size(250.dp)
                    .padding(bottom = 16.dp),
                factory = { context ->
                    RiveAnimationView(context).apply {
                        setRiveResource(
                            resId = R.raw.plant,
                            stateMachineName = "State Machine 1",
                            autoplay = true
                        )
                    }
                }
            )
            Text(
                text = "Nalin Don't lose hope",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
        }
    }
}
@Composable
fun FloatingBlobs(isDarkMode: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "blob_transition")

    // Animations for creating continuous circular/drifting movement
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase2"
    )

    val phase3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase3"
    )

    // Adjust the colors of the blobs based on dark/light mode
    val blobColor1 = if (isDarkMode) Color(0xFF6B21A8).copy(alpha = 0.4f) else Color(0xFFD8B4FE).copy(alpha = 0.6f)
    val blobColor2 = if (isDarkMode) Color(0xFF4C1D95).copy(alpha = 0.4f) else Color(0xFFE9D5FF).copy(alpha = 0.6f)
    val blobColor3 = if (isDarkMode) Color(0xFF86198F).copy(alpha = 0.4f) else Color(0xFFFBCFE8).copy(alpha = 0.6f)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Blob 1 - Floating around the Top Left
        drawCircle(
            color = blobColor1,
            radius = width * 0.35f,
            center = Offset(
                x = width * 0.2f + (sin(phase1) * width * 0.1f),
                y = height * 0.2f + (cos(phase1) * height * 0.1f)
            )
        )

        // Blob 2 - Floating around the Bottom Right
        drawCircle(
            color = blobColor2,
            radius = width * 0.45f,
            center = Offset(
                x = width * 0.8f + (cos(phase2) * width * 0.15f),
                y = height * 0.7f + (sin(phase2) * height * 0.15f)
            )
        )

        // Blob 3 - Floating gently in the Middle
        drawCircle(
            color = blobColor3,
            radius = width * 0.25f,
            center = Offset(
                x = width * 0.5f + (sin(phase3) * width * 0.2f),
                y = height * 0.5f + (cos(phase3) * height * 0.15f)
            )
        )
    }
}