package com.example.digitaldiary

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nalin.my_digitaldiary.R

// 1. THE DESIGN TOKENS & PROFILE MODELS
data class AppThemeProfile(
    val backgroundBrush: Brush,
    val backHillColor: Color,
    val frontHillColor: Color,
    val navBarRiveResId: Int,
    val activeIconColor: Color,
    val graphLineColor: Color,
    val calendarTodayRingColor: Color
)

object AppDesignTokens {
    val backgroundColor = Color(0xFFF5F6F8)
    val surfaceColor = Color(0xFFFFFFFF)
    val primaryText = Color(0xFF2C2C2C)
    val secondaryText = Color(0xFFA0A0A0)
    val accentColor = Color(0xFF6EBE80)

    val ForestTheme = AppThemeProfile(
        backgroundBrush = Brush.verticalGradient(listOf(Color(0xFFAEBE93), Color(0xFFD4DAE1))),
        backHillColor = Color(0xFFC6D7AC),
        frontHillColor = Color(0xFFDAEBC0),
        navBarRiveResId = R.raw.smiley,
        activeIconColor = Color(0xFF4CA18A),
        graphLineColor = Color(0xFF4CA18A),
        calendarTodayRingColor = Color(0xFF4CA18A)
    )
    val OceanTheme = AppThemeProfile(
        backgroundBrush = Brush.verticalGradient(listOf(Color(0xFF7DD3FC), Color(0xFFE0F2FE))),
        backHillColor = Color(0xFF38BDF8),
        frontHillColor = Color(0xFF85C9EA),
        navBarRiveResId = R.raw.smiley2,
        activeIconColor = Color(0xFF38BDF8),
        graphLineColor = Color(0xFF38BDF8),
        calendarTodayRingColor = Color(0xFF0EA5E9)
    )
    val SunsetTheme = AppThemeProfile(
        // A beautiful golden-hour gradient for the sky background
        backgroundBrush = Brush.verticalGradient(
            listOf(Color(0xFFFED7AA), Color(0xFFFEE2E2)) // Soft Peach to Warm Mallow Pink
        ),
        backHillColor = Color(0xFFFCA5A5),          // Warm Coral Pink back hills
        frontHillColor = Color(0xFFFFEDD5),         // Light Sunset Cream front hills
        navBarRiveResId = R.raw.smiley3, // Your new peach Rive file!
        activeIconColor = Color(0xFFFB923C),        // Your chosen Peach accent color
        graphLineColor = Color(0xFFFB923C),         // Peach stats graph line & dots
        calendarTodayRingColor = Color(0xFFFB923C)  // Peach calendar today circle indicator
    )
    val NordicTheme = AppThemeProfile(
        // A crisp, clean winter slate-blue to ice gradient
        backgroundBrush = Brush.verticalGradient(
            listOf(Color(0xFFCBD5E1), Color(0xFFE0F2FE))
        ),
        backHillColor = Color(0xFF94A3B8),          // Slate gray-blue back hills
        frontHillColor = Color(0xFFE2E8F0),         // Soft frosted ice front hills
        navBarRiveResId = R.raw.smiley4, // Your new Nordic Rive file!
        activeIconColor = Color(0xFF06B6D4),        // Crisp Teal/Cyan accent color
        graphLineColor = Color(0xFF06B6D4),         // Teal graph lines & dots
        calendarTodayRingColor = Color(0xFF06B6D4)  // Teal calendar today circle indicator
    )

    val UniversalTopBgColor = Color(0xFFAEBE93)
    val UniversalBottomBgColor = Color(0xFFD4DAE1)
    val UniversalBrush = Brush.verticalGradient(
        colors = listOf(UniversalTopBgColor, UniversalBottomBgColor)
    )
}

// 2. UNIVERSAL BACKGROUND WRAPPER (DYNAMIC FIGMA HILLS ENGINE)
@Composable
fun UniversalBackgroundWrapper(
    modifier: Modifier = Modifier,
    themeProfile: AppThemeProfile = AppDesignTokens.ForestTheme,
    content: @Composable BoxScope.() -> Unit
) {

    val hillPathString = "M285 17.4657C203.574 -21.8322 183.5 17.4659 114.5 17.4658L-3 17.4657V203.801H402V27.8015C402 27.8015 352 49.8013 285 17.4657Z"
    val hillPath = remember { PathParser().parsePathString(hillPathString).toPath() }

    val hill2PathString = "M309.5 15.5557C225.712 -29.7517 192.778 63.778 117 15.5555C62 -19.4445 -3 15.5557 -3 15.5557V264.055H402V45.5552C402 45.5552 328.771 25.9765 309.5 15.5557Z"
    val hill2Path = remember { PathParser().parsePathString(hill2PathString).toPath() }

    Box(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {


            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(brush = themeProfile.backgroundBrush)
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(402f / 264f)
                    .align(Alignment.BottomCenter)
            ) {
                val scaleX = size.width / 402f
                val scaleY = size.height / 264f
                withTransform({ scale(scaleX, scaleY, Offset.Zero) }) {
                    drawPath(path = hill2Path, color = themeProfile.backHillColor)
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(402f / 204f)
                    .align(Alignment.BottomCenter)
            ) {
                val scaleX = size.width / 402f
                val scaleY = size.height / 204f
                withTransform({ scale(scaleX, scaleY, Offset.Zero) }) {
                    drawPath(path = hillPath, color = themeProfile.frontHillColor)
                }
            }
        }

        content()
    }
}

// 3. THE MASTER CARD SHELL COMPONENT
@Composable
fun UniversalDesignCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    backgroundColor: Color = AppDesignTokens.surfaceColor,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = if (onClick != null) modifier.clickable { onClick() } else modifier,
        shape = RoundedCornerShape(cornerRadius),
        color = backgroundColor,
        shadowElevation = 0.dp
    ) {
        content()
    }
}