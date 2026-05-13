package com.example.togalu_gombe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.togalu_gombe.viewmodel.TogaluViewModel

@Composable
fun ArtistConnectScreen(viewModel: TogaluViewModel) {
    val storeItems by viewModel.storeItems.collectAsState()
    val isKannada by viewModel.isKannada.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(16.dp)
    ) {
        Text(
            text = if (isKannada) "ಕಲಾವಿದರೊಂದಿಗೆ ಸಂಪರ್ಕಿಸಿ" else "Artist Connect",
            color = Color(0xFFD4AF37),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(storeItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(Color.DarkGray, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isKannada) item.nameKn else item.nameEn,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "₹${item.price}",
                                color = Color(0xFFD4AF37),
                                fontSize = 16.sp
                            )
                        }
                        Button(
                            onClick = { /* Handle purchase */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
                        ) {
                            Text(if (isKannada) "ಖರೀದಿಸಿ" else "Buy", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}
