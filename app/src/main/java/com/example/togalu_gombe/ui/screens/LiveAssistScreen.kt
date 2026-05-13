package com.example.togalu_gombe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.togalu_gombe.viewmodel.TogaluViewModel

@Composable
fun LiveAssistScreen(viewModel: TogaluViewModel) {
    val scenes by viewModel.scenesForCurrentPlay.collectAsState()
    val isKannada by viewModel.isKannada.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadScenesForPlay(1) // Load Ramayana by default
    }

    if (scenes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFFD4AF37))
        }
        return
    }

    val pagerState = rememberPagerState(pageCount = { scenes.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212)) // Dark shadow-theater theme
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val scene = scenes[page]
            SceneCard(
                title = if (isKannada) scene.titleKn else scene.titleEn,
                summary = if (isKannada) scene.summaryKn else scene.summaryEn,
                characters = scene.characters,
                sceneNumber = scene.sceneNumber
            )
        }

        // Pager indicator
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(scenes.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color(0xFFD4AF37) else Color.Gray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .background(color, shape = androidx.compose.foundation.shape.CircleShape)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun SceneCard(title: String, summary: String, characters: String, sceneNumber: Int) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Scene $sceneNumber",
                color = Color(0xFFD4AF37), // Gold accent
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = summary,
                color = Color.LightGray,
                fontSize = 18.sp,
                lineHeight = 26.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Characters on stage: $characters",
                color = Color(0xFFAAAAAA),
                fontSize = 14.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}
