package com.memori.app.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.memori.app.ui.screens.profile.AchievementsScreen
import com.memori.app.ui.screens.profile.ProfileScreen
import com.memori.app.ui.theme.RedPrimary
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.ui.draw.alpha

@Composable
fun MainScreen(onNavigateToTrailStart: () -> Unit, onLogout: () -> Unit) {
    val bottomNavController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    var showTrailDialog by remember { mutableStateOf(false) }
    
    // Menu ajustado para coincidir com a imagem
    val items = listOf("Explorar", "Perfil", "Conquistas")
    val icons = listOf(Icons.Default.CompassCalibration, Icons.Default.Person, Icons.Default.Image)
    val routes = listOf("map", "profile", "achievements")

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                // Menu "Cilíndrico" (Cápsula) flutuante conforme a foto
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 24.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    // Botão "INICIAR TRILHA" acima dos botões principais
                    Surface(
                        modifier = Modifier
                            .offset(y = (-90).dp) // Posicionado acima da barra
                            .size(70.dp)
                            .clickable { showTrailDialog = true },
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 10.dp,
                        border = BorderStroke(3.dp, RedPrimary)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🚩", fontSize = 32.sp) // Emoji de Iniciar Trilha
                        }
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        shape = RoundedCornerShape(40.dp),
                        color = Color.White,
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items.forEachIndexed { index, item ->
                                val isSelected = selectedItem == index
                                
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .let { 
                                            if (index == 1 && isSelected) {
                                                // Destaque do perfil no meio
                                                it.border(2.dp, RedPrimary, CircleShape).padding(4.dp)
                                            } else it
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    IconButton(onClick = {
                                        selectedItem = index
                                        bottomNavController.navigate(routes[index]) {
                                            popUpTo(bottomNavController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }) {
                                        if (index == 1) {
                                            // Ícone de perfil amarelado como na foto
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(CircleShape)
                                                    .background(if (isSelected) Color(0xFFFFD54F) else Color.LightGray),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("😑", fontSize = 20.sp)
                                            }
                                        } else {
                                            Icon(
                                                icons[index],
                                                contentDescription = item,
                                                tint = if (isSelected) RedPrimary else RedPrimary.copy(alpha = 0.4f),
                                                modifier = Modifier.size(32.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                NavHost(navController = bottomNavController, startDestination = "map") {
                    composable("map") {
                        HomeMapScreen(
                            onNavigateToProfile = {
                                selectedItem = 1
                                bottomNavController.navigate("profile") {
                                    popUpTo(bottomNavController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                    composable("profile") {
                        ProfileScreen(onLogout = onLogout)
                    }
                    composable("achievements") {
                        AchievementsScreen()
                    }
                }
            }
        }

        if (showTrailDialog) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { showTrailDialog = false },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .width(320.dp)
                        .padding(16.dp)
                        .clickable(enabled = false) {},
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                            Row(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                repeat(3) {
                                    Icon(
                                        Icons.Default.StarOutline,
                                        null,
                                        tint = Color(0xFFFFD54F),
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(x = 10.dp, y = (-10).dp),
                                color = Color(0xFF8BC34A),
                                shape = RoundedCornerShape(bottomStart = 8.dp, topEnd = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.TrendingUp,
                                        null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Icon(
                                        Icons.Default.Star,
                                        null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Lanternas Brilhantes",
                            color = Color.Gray,
                            fontSize = 20.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🏮", fontSize = 100.sp, modifier = Modifier.alpha(0.3f))
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ajude Yumi a encontrar as Lanternas Brilhantes a tempo do evento Tooro Nagashi!",
                            modifier = Modifier.padding(horizontal = 24.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = {
                                showTrailDialog = false
                                onNavigateToTrailStart()
                            },
                            modifier = Modifier.fillMaxWidth(0.8f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                            shape = RoundedCornerShape(28.dp)
                        ) {
                            Text(
                                "Jogar",
                                color = Color.White,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
