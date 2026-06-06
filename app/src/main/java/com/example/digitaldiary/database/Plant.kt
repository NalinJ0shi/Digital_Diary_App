package com.example.digitaldiary.database

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import app.rive.runtime.kotlin.RiveAnimationView
import com.example.digitaldiary.UniversalBackgroundWrapper
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
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (unlockedPlants.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Complete your first week to store plants here!", color = Color.White.copy(alpha = 0.6f))
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(unlockedPlants) { plant ->
                            Card(
                                modifier = Modifier
                                    .aspectRatio(0.8f)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFC8D2C8).copy(alpha = 0.3f))
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
                                        AndroidView(
                                            modifier = Modifier.size(150.dp),
                                            factory = { context ->
                                                RiveAnimationView(context).apply {
                                                    val resId = if (plant.plantTier == 1) R.raw.treeyo else R.raw.treeyo2
                                                    setRiveResource(resId = resId, stateMachineName = "State Machine 1")
                                                }
                                            }
                                        )
                                    }

                                    val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(plant.unlockDate))
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "Week ${plant.plantTier}", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(text = dateStr, color = Color.White.copy(alpha = 0.6f), fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}