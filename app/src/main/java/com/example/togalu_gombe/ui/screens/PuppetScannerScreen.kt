package com.example.togalu_gombe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.togalu_gombe.viewmodel.TogaluViewModel
import kotlinx.coroutines.delay

@Composable
fun PuppetScannerScreen(viewModel: TogaluViewModel) {
    val isKannada by viewModel.isKannada.collectAsState()
    var isScanning by remember { mutableStateOf(false) }
    var scannedPuppetName by remember { mutableStateOf<String?>(null) }
    var scannedPuppetPowers by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(isScanning) {
        if (isScanning) {
            delay(2000) // Simulate scanning delay
            scannedPuppetName = if (isKannada) "ಹನುಮಂತ" else "Hanuman"
            scannedPuppetPowers = if (isKannada) "ಅತಿಮಾನುಷ ಶಕ್ತಿ, ಹಾರಾಟ" else "Super strength, flight"
            isScanning = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Simulated Camera Viewfinder
        Box(
            modifier = Modifier
                .size(300.dp)
                .border(2.dp, if (isScanning) Color(0xFFD4AF37) else Color.Gray, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (scannedPuppetName != null && !isScanning) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(scannedPuppetName!!, color = Color(0xFFD4AF37), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(scannedPuppetPowers!!, color = Color.White, fontSize = 16.sp)
                }
            } else if (isScanning) {
                Text(if (isKannada) "ಸ್ಕ್ಯಾನ್ ಮಾಡಲಾಗುತ್ತಿದೆ..." else "Scanning...", color = Color.White)
            } else {
                Text(if (isKannada) "ಗೊಂಬೆಯನ್ನು ಸ್ಕ್ಯಾನ್ ಮಾಡಿ" else "Point at a puppet", color = Color.Gray)
            }
        }

        Button(
            onClick = {
                isScanning = true
                scannedPuppetName = null
                scannedPuppetPowers = null
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37))
        ) {
            Text(if (isKannada) "ಸ್ಕ್ಯಾನ್" else "Scan", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}
