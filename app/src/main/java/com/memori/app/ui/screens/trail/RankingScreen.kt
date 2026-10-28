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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memori.app.data.remote.RankingItem
import com.memori.app.data.remote.RetrofitClient
import com.memori.app.data.remote.SessionManager
import com.memori.app.ui.theme.RedPrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun RankingScreen(onBackToHome: () -> Unit) {
    val context = LocalContext.current
    var rankingList by remember { mutableStateOf<List<RankingItem>>(emptyList()) }
    var carregando by remember { mutableStateOf(true) }
    var erro by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        SessionManager.carregarToken(context)
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getRanking()
            }
            if (response.isSuccessful) {
                rankingList = response.body()?.ranking ?: emptyList()
            } else {
                erro = "Erro ao carregar ranking"
            }
        } catch (e: Exception) {
            erro = "Erro de conexão"
        }
        carregando = false
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
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
                // Header — mostra o primeiro colocado
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text("Ranking", fontSize = 24.sp, color = Color.Gray)
                        if (rankingList.isNotEmpty()) {
                            Text(
                                "#01 ${rankingList[0].idUsuario.nomeUsuario}",
                                color = RedPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                "${rankingList[0].pontosTotal} pts",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Surface(
                        color = Color(0xFFC5E1A5),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.TrendingUp, null, tint = Color(0xFF689F38), modifier = Modifier.size(20.dp))
                            Icon(Icons.Default.Star, null, tint = Color(0xFF689F38), modifier = Modifier.size(16.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Avatar do primeiro colocado
                Box(contentAlignment = Alignment.Center) {
                    Text("👑", modifier = Modifier.offset(y = (-55).dp), fontSize = 40.sp)
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .border(4.dp, Color(0xFFFFD54F), CircleShape)
                            .padding(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFFFD54F), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🏆", fontSize = 40.sp)
                        }
                    }
                }

                Text(
                    text = if (rankingList.isNotEmpty()) rankingList[0].idUsuario.nomeUsuario else "",
                    modifier = Modifier.padding(vertical = 12.dp),
                    fontSize = 22.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold
                )

                Divider(
                    modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 16.dp),
                    color = Color.LightGray
                )

                // Lista de ranking
                when {
                    carregando -> {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = RedPrimary)
                        }
                    }
                    erro.isNotEmpty() -> {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(erro, color = Color.Gray)
                        }
                    }
                    rankingList.isEmpty() -> {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text("Nenhum resultado ainda", color = Color.Gray)
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Pula o primeiro (já aparece no topo)
                            itemsIndexed(rankingList.drop(1)) { index, item ->
                                RankingItemRow(rank = index + 2, item = item)
                            }
                        }
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
fun RankingItemRow(rank: Int, item: RankingItem) {
    val emojis = listOf("🥈", "🥉", "🎖️", "⭐", "✨")
    val cores = listOf(
        Color(0xFF90CAF9), Color(0xFFFFCC80), Color(0xFFA5D6A7),
        Color(0xFFCE93D8), Color(0xFFEF9A9A)
    )
    val emoji = emojis.getOrElse(rank - 2) { "🎮" }
    val cor = cores.getOrElse(rank - 2) { Color(0xFFEEEEEE) }

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
                modifier = Modifier.fillMaxSize().background(cor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("#${rank.toString().padStart(2, '0')}", color = RedPrimary, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.idUsuario.nomeUsuario, color = Color.Gray, fontSize = 14.sp)
                Text(
                    ".".repeat(20),
                    color = Color.LightGray,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                    maxLines = 1
                )
                Text("${item.pontosTotal} pts", color = Color.Gray, fontSize = 14.sp)
            }
            Divider(modifier = Modifier.padding(top = 4.dp), color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}