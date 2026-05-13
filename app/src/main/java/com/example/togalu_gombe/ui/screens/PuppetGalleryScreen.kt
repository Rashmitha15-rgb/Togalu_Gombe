package com.example.togalu_gombe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.togalu_gombe.data.Puppet
import com.example.togalu_gombe.viewmodel.TogaluViewModel

@Composable
fun PuppetGalleryScreen(viewModel: TogaluViewModel) {
    val puppets by viewModel.allPuppets.collectAsState()
    val isKannada by viewModel.isKannada.collectAsState()
    
    var selectedPuppet by remember { mutableStateOf<Puppet?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Text(
            text = if (isKannada) "ಗೊಂಬೆಗಳ ಗ್ಯಾಲರಿ" else "Puppet Gallery",
            color = Color(0xFFD4AF37),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(puppets) { puppet ->
                PuppetThumbnail(
                    puppet = puppet,
                    isKannada = isKannada,
                    onClick = { selectedPuppet = puppet }
                )
            }
        }
    }

    selectedPuppet?.let { puppet ->
        ZoomablePuppetDialog(
            puppet = puppet,
            isKannada = isKannada,
            onDismiss = { selectedPuppet = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuppetThumbnail(puppet: Puppet, isKannada: Boolean, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        modifier = Modifier.aspectRatio(0.8f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Placeholder for actual image
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.DarkGray, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("Image", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isKannada) puppet.nameKn else puppet.nameEn,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ZoomablePuppetDialog(puppet: Puppet, isKannada: Boolean, onDismiss: () -> Unit) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .background(Color.Black)
                        .pointerInput(Unit) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(1f, 5f)
                                if (scale > 1f) {
                                    offsetX += pan.x
                                    offsetY += pan.y
                                } else {
                                    offsetX = 0f
                                    offsetY = 0f
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Zoomable Image Placeholder
                    Box(
                        modifier = Modifier
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offsetX,
                                translationY = offsetY
                            )
                            .size(200.dp)
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(if (isKannada) puppet.nameKn else puppet.nameEn, color = Color.White)
                    }
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isKannada) puppet.nameKn else puppet.nameEn,
                        color = Color(0xFFD4AF37),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isKannada) puppet.symbolismKn else puppet.symbolismEn,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
