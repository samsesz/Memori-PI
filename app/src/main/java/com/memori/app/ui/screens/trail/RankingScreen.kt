package com.memori.app.ui.screens.trail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memori.app.ui.theme.RedPrimary

data class RankingUser(
    val name: String,
    val time: String,
    val emoji: String,
    val color: Color
)

@Composable
fun RankingScreen(onBackToHome: () -> Unit) {
    val mockUsers = listOf(
        RankingUser("Larissa (Você)", "00:28:15 h", "😊", Color(0xFFFFD54F)),
        RankingUser("PRIMEIRO LUGAR", "00:25:40 h", "👑", Color(0xFFFF5252)),
        RankingUser("Mateus", "00:30:40 h", "🟡", Color(0xFFFFD54F)),
        RankingUser("Beatriz", "00:32:12 h", "🔵", Color(0xFF64B5F6)),
        RankingUser("Rodrigo", "00:35:05 h", "🟢", Color(0xFF81C784)),
        RankingUser("Ana", "00:40:00 h", "🟣", Color(0xFFBA68C8))
    )

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        // Background Map (Simulado)
        Box(modifier = Modifier.fillMaxSize().background(Color.Gray.copy(alpha = 0.1f)))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(24.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text("Ranking", fontSize = 24.sp, color = Color.Gray)
                        Text("#01 ${mockUsers[1].name}", color = RedPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(mockUsers[1].time, color = Color.Gray, fontSize = 14.sp)
                    }
                    Surface(
                        color = Color(0xFFC5E1A5),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.TrendingUp, null, tint = Color(0xFF689F38), modifier = Modifier.size(20.dp))
                            Icon(Icons.Default.Star, null, tint = Color(0xFF689F38), modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Crown and Top User Avatar
                Box(contentAlignment = Alignment.Center) {
                    Text("👑", modifier = Modifier.offset(y = (-55).dp), fontSize = 40.sp)
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .border(4.dp, Color(0xFFFFD54F), CircleShape)
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().background(mockUsers[0].color, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(mockUsers[0].emoji, fontSize = 40.sp)
                        }
                    }
                }

                Text(
                    text = mockUsers[0].name,
                    modifier = Modifier.padding(vertical = 12.dp),
                    fontSize = 22.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )

                Divider(modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 16.dp), color = Color.LightGray)

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(mockUsers.drop(1)) { index, user ->
                        RankingItem(index + 2, user)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text("Voltar ao Início", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun RankingItem(rank: Int, user: RankingUser) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .border(2.dp, RedPrimary, CircleShape)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize().background(user.color, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(user.emoji, fontSize = 20.sp)
            }
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text("#0$rank", color = RedPrimary, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(user.name, color = Color.Gray, fontSize = 14.sp)
                Text(
                    ".".repeat(20),
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    maxLines = 1
                )
                Text(user.time, color = Color.Gray, fontSize = 14.sp)
            }
            Divider(modifier = Modifier.padding(top = 4.dp), color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}
