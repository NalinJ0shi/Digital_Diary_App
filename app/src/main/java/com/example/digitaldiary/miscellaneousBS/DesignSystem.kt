package com.example.digitaldiary.miscellaneousBS

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

// 1. THE DESIGN TOKENS
data class AppThemeProfile(
    val backgroundBrush: Brush,
    val backHillColor: Color,
    val frontHillColor: Color,
    val navBarRiveResId: Int
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
        navBarRiveResId = com.nalin.my_digitaldiary.R.raw.smiley // Default
    )

    val OceanTheme = AppThemeProfile(
        backgroundBrush = Brush.verticalGradient(listOf(Color(0xFF7DD3FC), Color(0xFFE0F2FE))),
        backHillColor = Color(0xFF38BDF8),
        frontHillColor = Color(0xFF7DD3FC),
        navBarRiveResId = com.nalin.my_digitaldiary.R.raw.smiley2 // Replace with your second Rive file
    )

    // Baseline fallback background colors & gradient
    val UniversalTopBgColor = Color(0xFFAEBE93)
    val UniversalBottomBgColor = Color(0xFFD4DAE1)
    val UniversalBrush = Brush.verticalGradient(
        colors = listOf(UniversalTopBgColor, UniversalBottomBgColor)
    )
}
// 2. UNIVERSAL BACKGROUND WRAPPER (FIGMA HILLS)
@Composable
fun UniversalBackgroundWrapper(
    modifier: Modifier = Modifier,
    themeProfile: AppThemeProfile = AppDesignTokens.ForestTheme, // Use the profile here
    content: @Composable BoxScope.() -> Unit
) {
    val hillPathString = "M285 17.4657C203.574 -21.8322 183.5 17.4659 114.5 17.4658L-3 17.4657V203.801H402V27.8015C402 27.8015 352 49.8013 285 17.4657Z"
    val hillPath = remember { PathParser().parsePathString(hillPathString).toPath() }

    val hill2PathString = "M309.5 15.5557C225.712 -29.7517 192.778 63.778 117 15.5555C62 -19.4445 -3 15.5557 -3 15.5557V264.055H402V45.5552C402 45.5552 328.771 25.9765 309.5 15.5557Z"
    val hill2Path = remember { PathParser().parsePathString(hill2PathString).toPath() }

    Box(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Apply dynamic background
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(brush = themeProfile.backgroundBrush)
            }

            // Apply dynamic back hill
            Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(402f / 264f).align(Alignment.BottomCenter)) {
                val scaleX = size.width / 402f
                val scaleY = size.height / 264f
                withTransform({ scale(scaleX, scaleY, Offset.Zero) }) {
                    drawPath(path = hill2Path, color = themeProfile.backHillColor)
                }
            }

            // Apply dynamic front hill
            Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(402f / 204f).align(Alignment.BottomCenter)) {
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