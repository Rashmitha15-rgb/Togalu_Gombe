package com.example.togalu_gombe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.togalu_gombe.ui.screens.*
import com.example.togalu_gombe.ui.theme.Togalu_GombeTheme
import com.example.togalu_gombe.viewmodel.TogaluViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: TogaluViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Togalu_GombeTheme {
                TogaluApp(viewModel)
            }
        }
    }
}

sealed class BottomNavItem(var titleEn: String, var titleKn: String, var icon: ImageVector, var route: String) {
    object LiveAssist : BottomNavItem("Live", "ಲೈವ್", Icons.Filled.PlayArrow, "live")
    object Scanner : BottomNavItem("Scan", "ಸ್ಕ್ಯಾನ್", Icons.Filled.Search, "scan")
    object Gallery : BottomNavItem("Gallery", "ಗ್ಯಾಲರಿ", Icons.Filled.List, "gallery")
    object Artist : BottomNavItem("Artist", "ಕಲಾವಿದ", Icons.Filled.Person, "artist")
    object History : BottomNavItem("History", "ಇತಿಹಾಸ", Icons.Filled.ShoppingCart, "history") // Using shopping cart temp
}

@Composable
fun TogaluApp(viewModel: TogaluViewModel) {
    val navController = rememberNavController()
    val isKannada by viewModel.isKannada.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color(0xFF121212)),
        bottomBar = { BottomNavigationBar(navController, isKannada) },
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text(if (isKannada) "ತೊಗಲು ಗೊಂಬೆ" else "Togalu Gombe", color = Color(0xFFD4AF37)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E1E1E)),
                actions = {
                    Button(
                        onClick = { viewModel.toggleLanguage() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                    ) {
                        Text(if (isKannada) "EN" else "KN", color = Color.White)
                    }
                }
            )
        }
    ) { innerPadding ->
        NavigationGraph(navController = navController, viewModel = viewModel, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, isKannada: Boolean) {
    val items = listOf(
        BottomNavItem.LiveAssist,
        BottomNavItem.Scanner,
        BottomNavItem.Gallery,
        BottomNavItem.Artist,
        BottomNavItem.History
    )

    NavigationBar(containerColor = Color(0xFF1E1E1E), contentColor = Color(0xFFD4AF37)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.titleEn) },
                label = { Text(if (isKannada) item.titleKn else item.titleEn) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color(0xFFD4AF37),
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFFD4AF37)
                )
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, viewModel: TogaluViewModel, modifier: Modifier) {
    NavHost(navController, startDestination = BottomNavItem.LiveAssist.route, modifier = modifier) {
        composable(BottomNavItem.LiveAssist.route) { LiveAssistScreen(viewModel) }
        composable(BottomNavItem.Scanner.route) { PuppetScannerScreen(viewModel) }
        composable(BottomNavItem.Gallery.route) { PuppetGalleryScreen(viewModel) }
        composable(BottomNavItem.Artist.route) { ArtistConnectScreen(viewModel) }
        composable(BottomNavItem.History.route) { HistoryFeedScreen(viewModel) }
    }
}