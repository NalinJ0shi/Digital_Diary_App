package com.example.digitaldiary.database

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.example.digitaldiary.main.UniversalBackgroundWrapper
import com.nalin.my_digitaldiary.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantCollectionScreen(
    viewModel: DiaryViewModel,
    onBack: () -> Unit
) {
    val unlockedPlants by viewModel.unlockedPlants.collectAsState(initial = emptyList())

    UniversalBackgroundWrapper {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("My Plant Library", fontWeight = FontWeight.Bold, color = Color.White) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.resetPlantLibrary()
                                onBack()
                            }
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset Data", tint = Color.Red)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // NEW: Streamlined to dynamically map a grid of all 8 weeks
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(8) { index ->
                        val currentTier = index + 1

                        // Week 1 is always unlocked by default. Higher tiers check the database.
                        val matchingUnlock = unlockedPlants.find { it.plantTier == currentTier }
                        val isUnlocked = currentTier == 1 || matchingUnlock != null

                        val subtitle = if (isUnlocked) {
                            if (currentTier == 1) {
                                "Default Unlocked"
                            } else {
                                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                    .format(Date(matchingUnlock?.unlockDate ?: System.currentTimeMillis()))
                            }
                        } else {
                            "Locked"
                        }

                        PlantCardItem(
                            tier = currentTier,
                            label = "Week $currentTier Plant",
                            subtitle = subtitle,
                            isUnlocked = isUnlocked
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlantCardItem(tier: Int, label: String, subtitle: String, isUnlocked: Boolean) {
    // NEW: Card fades slightly if the milestone plant is still locked
    val containerAlpha = if (isUnlocked) 0.3f else 0.1f
    val contentAlpha = if (isUnlocked) 1.0f else 0.4f

    Card(
        modifier = Modifier
            .aspectRatio(0.8f)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFC8D2C8).copy(alpha = containerAlpha))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                // NEW: Standardized asset loading fallback tree definitions
                val resId = remember(tier) {
                    when (tier) {
                        1 -> R.raw.treeyo
                        2 -> R.raw.treeyo2
                        3 -> R.raw.tree3
                        4 -> R.raw.tree4
                        5 -> R.raw.tree5
                        6 -> R.raw.tree6
                        7 -> R.raw.tree7
                        8 -> R.raw.tree8
                        else -> R.raw.treeyo
                    }
                }

                AndroidView(
                    modifier = Modifier.size(150.dp),
                    factory = { context ->
                        RiveAnimationView(context).apply {
                            setRiveResource(resId = resId, stateMachineName = "State Machine 1")
                        }
                    },
                    update = { view ->
                        // Optional styling alpha overlay adjustments for non-active items
                        view.alpha = contentAlpha
                    }
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = contentAlpha),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = if (isUnlocked) 0.6f else 0.3f),
                    fontSize = 12.sp
                )
            }
        }
    }
}