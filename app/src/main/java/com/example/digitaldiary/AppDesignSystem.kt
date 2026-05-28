package com.example.digitaldiary

import androidx.compose.foundation.BorderStroke
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

object AppDesignTokens {
    // Shared Baselines
    val backgroundColor = Color(0xFF12161A)
    val surfaceColor = Color(0xFF1A1F26)
    val borderColor = Color(0xFF282F38)
    val dividerColor = Color(0xFF282F38)
    val primaryText = Color(0xFFECEFF4)
    val secondaryText = Color(0xFF7E8996)
    val destructiveText = Color(0xFFF87171)

    // The 3 Card Hierarchies (A, B, C) Configurations
    object CardStyles {
        // Type A: Large Primary Card
        val LargePrimary = CardConfiguration(
            paddingValues = PaddingValues(all = 20.dp),
            cornerRadius = 16.dp,
            backgroundColor = Color(0xFF1E2640), // Royal Indigo Dark Blue Fill
            hasBorder = true
        )
        // Type B: Medium Statistic Card
        val MediumStat = CardConfiguration(
            paddingValues = PaddingValues(vertical = 20.dp, horizontal = 12.dp),
            cornerRadius = 16.dp,
            backgroundColor = Color(0xFF1A1F26),
            hasBorder = false
        )
        // Type C: Standard List Card Rows
        val ListCardRow = CardConfiguration(
            paddingValues = PaddingValues(horizontal = 16.dp, vertical = 18.dp),
            cornerRadius = 0.dp, // Flat item rows inside wrapped container structures
            backgroundColor = Color.Transparent,
            hasBorder = false
        )
    }
}

// Data holder class mapping specific architectural layout properties
data class CardConfiguration(
    val paddingValues: PaddingValues,
    val cornerRadius: Dp,
    val backgroundColor: Color,
    val hasBorder: Boolean
)

// Controls if illustration/SVG asset pins to the Left or Right boundary edges
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
        border = if (config.hasBorder) BorderStroke(1.dp, AppDesignTokens.borderColor) else null
    ) {
        Box(modifier = Modifier.padding(config.paddingValues)) {
            content()
        }
    }
}

// ==========================================
// 3. FLEXIBLE REUSABLE ELEMENT BLOCK BLUEPRINTS
// ==========================================
@Composable
fun BalancedContentRow(
    title: String,
    subtitle: String? = null,
    assetPosition: AssetPosition = AssetPosition.Left,
    titleFontWeight: FontWeight = FontWeight.SemiBold,
    titleColor: Color = AppDesignTokens.primaryText,
    illustrationSlot: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // If illustration goes to the LEFT edge
        if (assetPosition == AssetPosition.Left) {
            illustrationSlot()
            Spacer(modifier = Modifier.width(16.dp))
        }

        // Text Content Block column wrapping (automatically consumes relative fluid space)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = titleColor,
                fontSize = 16.sp,
                fontWeight = titleFontWeight
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    color = AppDesignTokens.secondaryText,
                    fontSize = 12.sp
                )
            }
        }

        // If illustration goes to the RIGHT edge
        if (assetPosition == AssetPosition.Right) {
            Spacer(modifier = Modifier.width(16.dp))
            illustrationSlot()
        }
    }
}
