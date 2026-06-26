// app/src/main/java/com/example/digitaldiary/CustomBottomNavBar.kt
package com.example.digitaldiary.miscellaneousBS

import android.view.MotionEvent
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
    riveResId: Int,
    themeProfile: com.example.digitaldiary.miscellaneousBS.AppThemeProfile,
    onCalendarClick: () -> Unit,
    onChartClick: () -> Unit,
    onGameClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddEntry: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    // Tracks the native view reference so we can update its boolean inputs
    var riveView by remember { mutableStateOf<RiveAnimationView?>(null) }

    // TIMER LOGIC: Controls the state machine's idleLoop input layer dynamically
    LaunchedEffect(selectedTab) {
        // Ensure it starts as false when switching screens
        riveView?.setBooleanState("State Machine 1", "idleLoop", false)

        delay(5000) // Wait 5 seconds

        // Tell the second layer to switch from NoActive to LoopAnimation
        riveView?.setBooleanState("State Machine 1", "idleLoop", true)
    }

    val groundColor = Color(0xFF042F2E)
    val navBarColor = Color(0xFFF8FAFC)
    val activeIconColor = themeProfile.activeIconColor
    val inactiveIconColor = Color(0xFF94A3B8)

    val totalClickableHeight = 120.dp
    val visualBarHeight = 100.dp
    val curveWidthPx = 360f
    val curveDepthPx = 140f
    val cornerSmoothingPx = 70f
    val iconSeparationWidth = 190.dp

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
                    modifier = Modifier.offset(x = 5.dp, y = -15.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_silhouette),
                        contentDescription = "Calendar",
                        modifier = Modifier.size(24.dp),
                        tint = if (selectedTab == 0) activeIconColor else inactiveIconColor
                    )
                }
                IconButton(
                    onClick = onChartClick,
                    modifier = Modifier.offset(x = 25.dp, y = -15.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.graph),
                        contentDescription = "Chart",
                        modifier = Modifier.size(24.dp),
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
                    modifier = Modifier.offset(x = -25.dp, y = -15.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.dead),
                        contentDescription = "Games",
                        modifier = Modifier.size(24.dp),
                        tint = if (selectedTab == 2) activeIconColor else inactiveIconColor
                    )
                }
                IconButton(
                    onClick = onProfileClick,
                    modifier = Modifier.offset(x = -5.dp, y = -15.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.user),
                        contentDescription = "Profile",
                        modifier = Modifier.size(24.dp),
                        tint = if (selectedTab == 3) activeIconColor else inactiveIconColor
                    )
                }
            }
        }

        key(riveResId){
            AndroidView(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomCenter)
                    .offset(x = 0.dp, y = (-40).dp),
                factory = { context ->
                    RiveAnimationView(context).apply {
                        setRiveResource(
                            resId = riveResId,
                            stateMachineName = "State Machine 1",
                            autoplay = true // Run State Machine layers immediately
                        )
                    }.also {
                        riveView = it
                    }
                },
                update = { view ->
                    riveView = view

                    view.setOnTouchListener { v, event ->
                        v.onTouchEvent(event)
                        when (event.action) {
                            MotionEvent.ACTION_DOWN -> {
                                // Change your primary pointer interaction state
                                view.setBooleanState("State Machine 1", "active", true)
                            }
                            MotionEvent.ACTION_UP -> {
                                v.performClick()
                                view.setBooleanState("State Machine 1", "active", false)
                                coroutineScope.launch {
                                    delay(300)
                                    onAddEntry()
                                }
                            }
                        }
                        true
                    }
                }
            )
        }
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