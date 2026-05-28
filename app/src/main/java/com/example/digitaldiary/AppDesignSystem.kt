package com.example.digitaldiary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ==========================================
// 1. LIGHT THEME TOKENS (DAILYBEAN STYLE)
// ==========================================
object AppDesignTokens {
    // Soft, Low-Stress Palette
    val backgroundColor = Color(0xFFF5F6F8) // Light warm gray/cream
    val surfaceColor = Color(0xFFFFFFFF)    // Pure white cards
    val primaryText = Color(0xFF2C2C2C)     // Bold dark
    val secondaryText = Color(0xFFA0A0A0)   // Medium gray
    val dividerColor = Color(0xFFF0F0F0)    // Almost invisible divider
    val accentColor = Color(0xFF6EBE80)     // Soft green accent

    // The 3 Card Hierarchies
    object CardStyles {
        // Type A: Large Primary Card (Identities/Info)
        val LargePrimary = CardConfiguration(
            paddingValues = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
            cornerRadius = 28.dp,
            backgroundColor = surfaceColor
        )
        // Type B: Medium Statistic Card (Quick Glance)
        val MediumStat = CardConfiguration(
            paddingValues = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            cornerRadius = 24.dp,
            backgroundColor = surfaceColor
        )
        // Type C: List Cards (Settings/Navigation - grouped inside a container)
        val ListCardRow = CardConfiguration(
            paddingValues = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
            cornerRadius = 0.dp,
            backgroundColor = Color.Transparent
        )
    }
}

data class CardConfiguration(
    val paddingValues: PaddingValues,
    val cornerRadius: Dp,
    val backgroundColor: Color
)

enum class AssetPosition { Left, Right }

// ==========================================
// 2. THE MASTER CARD SHELL COMPONENT
// ==========================================
@Composable
fun UniversalDesignCard(
    config: CardConfiguration,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickableModifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(clickableModifier),
        color = config.backgroundColor,
        shape = RoundedCornerShape(config.cornerRadius),
        shadowElevation = 0.dp // "Almost invisible" / "Floating paper feel"
    ) {
        Box(modifier = Modifier.padding(config.paddingValues)) {
            content()
        }
    }
}

// ==========================================
// 3. FLEXIBLE REUSABLE ELEMENT BLOCK
// ==========================================
@Composable
fun BalancedContentRow(
    title: String,
    subtitle: String? = null,
    assetPosition: AssetPosition = AssetPosition.Right, // Defaulting to Right as requested
    titleFontWeight: FontWeight = FontWeight.Bold,
    titleColor: Color = AppDesignTokens.primaryText,
    illustrationSlot: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Illustration Left
        if (assetPosition == AssetPosition.Left) {
            illustrationSlot()
            Spacer(modifier = Modifier.width(20.dp))
        }

        // Text Content Block (Left Aligned)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = titleColor,
                fontSize = 18.sp,
                fontWeight = titleFontWeight
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    color = AppDesignTokens.secondaryText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Illustration Right (Text on left -> illustration on right)
        if (assetPosition == AssetPosition.Right) {
            Spacer(modifier = Modifier.width(20.dp))
            illustrationSlot()
        }
    }
}