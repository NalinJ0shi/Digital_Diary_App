// app/src/main/java/com/example/digitaldiary/CustomBottomNavBar.kt
package com.example.digitaldiary

import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.nalin.my_digitaldiary.R

@Composable
fun CustomBottomNavBar(
    selectedTab: Int, // Tells the navbar which icon should be green
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val groundColor = Color(0xFF042F2E)
    val navBarColor = Color(0xFFF8FAFC)
    val activeIconColor = groundColor
    val inactiveIconColor = Color(0xFF94A3B8)

    val totalClickableHeight = 100.dp
    val visualBarHeight = 80.dp
    val curveWidthPx = 330f // How wide the top gap is
    val curveDepthPx = 130f // How deep the U-shape dips down
    val cornerSmoothingPx = 80f  // How wide the smooth transition corners are
    val iconSeparationWidth = 190.dp  // --- CONTROL: ICON DEAD-ZONE (Pushes left/right icons apart) ---

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(totalClickableHeight),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(visualBarHeight)
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    shape = NavBarCutoutShape(
                        cutoutWidth = curveWidthPx,
                        cutoutDepth = curveDepthPx,
                        cornerSmoothing = cornerSmoothingPx
                    )
                    clip = true
                    shadowElevation = 20f
                }
                .background(navBarColor)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCalendarClick,
                    modifier = Modifier.offset(x = 5.dp, y = -5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_silhouette),
                        contentDescription = "Calendar",
                        modifier = Modifier.size(32.dp),
                        tint = if (selectedTab == 0) activeIconColor else inactiveIconColor
                    )
                }
                IconButton(
                    onClick = onChartClick,
                    modifier = Modifier.offset(x = 25.dp, y = -5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.graph__1_),
                        contentDescription = "Chart",
                        modifier = Modifier.size(32.dp),
                        tint = if (selectedTab == 1) activeIconColor else inactiveIconColor
                    )
                }
            }

            Spacer(modifier = Modifier.width(iconSeparationWidth))

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onGameClick,
                    modifier = Modifier.offset(x = -25.dp, y = -5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.dead),
                        contentDescription = "Games",
                        modifier = Modifier.size(32.dp),
                        tint = if (selectedTab == 2) activeIconColor else inactiveIconColor
                    )
                }
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier.offset(x = -5.dp, y = -5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Profile",
                        modifier = Modifier.size(32.dp),
                        tint = if (selectedTab == 3) activeIconColor else inactiveIconColor
                    )
                }
            }
        }

        AndroidView(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomCenter)
                .offset(x = 0.dp, y = (-25).dp),
            factory = { context ->
                RiveAnimationView(context).apply {
                    setRiveResource(
                        resId = R.raw.happ_button2,
                        stateMachineName = "State Machine 1",
                        autoplay = true
                    )
                }
            },
            update = { view ->
                view.setOnTouchListener { v, event ->
                    v.onTouchEvent(event)
                    if (event.action == android.view.MotionEvent.ACTION_UP) {
                        v.performClick()
                        coroutineScope.launch {
                            delay(300)
                            onAddEntry()
                        }
                    }
                    true
                }
            }
        )
    }
}

class NavBarCutoutShape(
    private val cutoutWidth: Float,
    private val cutoutDepth: Float,
    private val cornerSmoothing: Float
) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            val center = width / 2f
            val halfWidth = cutoutWidth / 2f

            moveTo(0f, 0f)
            lineTo(center - halfWidth, 0f)

            cubicTo(
                x1 = center - halfWidth + cornerSmoothing, y1 = 0f,
                x2 = center - halfWidth + cornerSmoothing, y2 = cutoutDepth,
                x3 = center, y3 = cutoutDepth
            )

            cubicTo(
                x1 = center + halfWidth - cornerSmoothing, y1 = cutoutDepth,
                x2 = center + halfWidth - cornerSmoothing, y2 = 0f,
                x3 = center + halfWidth, y3 = 0f
            )

            lineTo(width, 0f)
            lineTo(width, height)
            lineTo(0f, height)
            close()
        }
        return Outline.Generic(path)
    }
}