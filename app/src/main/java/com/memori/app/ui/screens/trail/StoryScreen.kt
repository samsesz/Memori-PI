package com.memori.app.ui.screens.trail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memori.app.R
import com.memori.app.ui.theme.RedPrimary
import kotlinx.coroutines.delay

// ---------------------------------------------------------------------------
// Edite os textos dos diálogos aqui
// ---------------------------------------------------------------------------

val dialogosIntro = listOf(
    "Olá! Eu sou Yumi, sua guia nessa aventura!",
    "Você está prestes a descobrir os segredos do Conjunto KKKK...",
    "Este lugar é muito especial para o Vale do Ribeira.",
    "Mas antes, preciso da sua ajuda para encontrar as Lanternas Brilhantes!",
    "Vamos começar? Escaneie o marcador no local para dar o primeiro passo!"
)

val dialogosPostAR = listOf(
    "Incrível! Você encontrou o primeiro fragmento!",
    "Agora vem o verdadeiro desafio...",
    "Reconstrua o mosaico do KKKK para revelar o segredo final!",
    "Eu confio em você. Boa sorte!"
)

// ---------------------------------------------------------------------------
// StoryScreen
// ---------------------------------------------------------------------------

@Composable
fun StoryScreen(
    dialogos: List<String>,
    onFinished: () -> Unit
) {
    var dialogoIndex by remember { mutableIntStateOf(0) }
    var textoVisivel by remember { mutableStateOf("") }
    var digitando by remember { mutableStateOf(true) }


    val imagens = listOf(
        R.drawable.yumi,
        R.drawable.yumi_boca,
    )
    var imagemIndex by remember { mutableIntStateOf(0) }


    LaunchedEffect(dialogoIndex) {
        textoVisivel = ""
        digitando = true
        for (char in dialogos[dialogoIndex]) {
            textoVisivel += char
            delay(35)
        }
        digitando = false
    }

    LaunchedEffect(digitando) {
        if (digitando) {
            while (digitando) {
                delay(250)
                imagemIndex = (imagemIndex + 1) % imagens.size
            }
        }
        imagemIndex = 0
    }

    val escala by animateFloatAsState(
        targetValue = if (digitando) 1.04f else 1f,
        animationSpec = tween(250),
        label = "escala_personagem"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(48.dp))

            Text(
                text = "Yumi",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = RedPrimary
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imagens[imagemIndex]),
                    contentDescription = "Yumi",
                    modifier = Modifier
                        .fillMaxHeight(0.75f)
                        .aspectRatio(0.6f)
                        .graphicsLayer {
                            scaleX = escala
                            scaleY = escala
                        }
                )
            }

            // Caixa de diálogo — toque avança
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (digitando) {
                            textoVisivel = dialogos[dialogoIndex]
                            digitando = false
                        } else {
                            if (dialogoIndex < dialogos.size - 1) {
                                dialogoIndex++
                            } else {
                                onFinished()
                            }
                        }
                    }
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomEnd = 24.dp),
                color = Color.White.copy(alpha = 0.92f),
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = textoVisivel,
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        lineHeight = 24.sp,
                        minLines = 3
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${dialogoIndex + 1} / ${dialogos.size}",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )
                        AnimatedVisibility(
                            visible = !digitando,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = if (dialogoIndex < dialogos.size - 1)
                                    "Toque para continuar ▶"
                                else
                                    "Toque para começar ▶",
                                fontSize = 12.sp,
                                color = RedPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}