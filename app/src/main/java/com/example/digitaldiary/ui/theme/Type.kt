package com.example.digitaldiary.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.nalin.my_digitaldiary.R

// Define the Josefin Sans Font Family
val JosefinSans = FontFamily(
    Font(R.font.josefinsans_regular, FontWeight.Normal),
    Font(R.font.josefinsans_semibold, FontWeight.Medium),
    Font(R.font.josefinsans_bold, FontWeight.Bold)
)

// Configure all typography styles to use Josefin Sans by default
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = JosefinSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = JosefinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = JosefinSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    // You can override bodyMedium, headlineMedium, etc. similarly if needed
)