package com.memori.app.ui.screens.trail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.memori.app.R
import kotlinx.coroutines.delay
import java.util.Collections

// ---------------------------------------------------------------------------
// Data helpers
// ---------------------------------------------------------------------------

/** Splits a Bitmap into a 3×3 grid and returns the 9 pieces (index 8 = empty). */
fun splitBitmap(source: Bitmap): List<Bitmap> {
    val tileW = source.width / 3
    val tileH = source.height / 3
    return (0 until 9).map { i ->
        val row = i / 3
        val col = i % 3
        Bitmap.createBitmap(source, col * tileW, row * tileH, tileW, tileH)
    }
}

/** Generates a solvable shuffle by making random valid moves from the solved state. */
fun shuffleSolvable(tiles: MutableList<Int>) {
    tiles.clear()
    tiles.addAll(0..8)
    var emptyIndex = 8
    repeat(200) {
        val row = emptyIndex / 3
        val col = emptyIndex % 3
        val neighbors = mutableListOf<Int>()
        if (row > 0) neighbors.add(emptyIndex - 3)
        if (row < 2) neighbors.add(emptyIndex + 3)
        if (col > 0) neighbors.add(emptyIndex - 1)
        if (col < 2) neighbors.add(emptyIndex + 1)
        val moveToIndex = neighbors.random()
        Collections.swap(tiles, emptyIndex, moveToIndex)
        emptyIndex = moveToIndex
    }
}

/**
 * When the user taps a tile, we move it into the empty slot if they are
 * in the same row or column (sliding all tiles between them as a group
 * would be complex; here we do the simpler "only direct neighbour" rule
 * which is standard for 8-puzzle).
 */
fun handleTap(tappedIndex: Int, tiles: MutableList<Int>): Boolean {
    val emptyIndex = tiles.indexOf(8)
    val tappedRow = tappedIndex / 3
    val tappedCol = tappedIndex % 3
    val emptyRow  = emptyIndex / 3
    val emptyCol  = emptyIndex % 3

    val isNeighbour = (tappedRow == emptyRow && kotlin.math.abs(tappedCol - emptyCol) == 1) ||
            (tappedCol == emptyCol && kotlin.math.abs(tappedRow - emptyRow) == 1)

    return if (isNeighbour) {
        Collections.swap(tiles, tappedIndex, emptyIndex)
        true
    } else false
}

fun isSolved(tiles: List<Int>) = tiles == (0..8).toList()

// ---------------------------------------------------------------------------
// Puzzle Screen
// ---------------------------------------------------------------------------

@Composable
fun PuzzleScreen(onPuzzleCompleted: (Int) -> Unit, onExit: () -> Unit) {
    val context = LocalContext.current

    // Load and split bitmap once
    val tileBitmaps: List<Bitmap> = remember {
        val src = BitmapFactory.decodeResource(context.resources, R.drawable.kkkk_puzzle)
        // Scale to a square so pieces are uniform
        val side = minOf(src.width, src.height)
        val square = Bitmap.createBitmap(src, 0, 0, side, side)
        splitBitmap(square)
    }

    val tiles = remember {
        mutableStateListOf<Int>().apply {
            addAll(0..8)
            shuffleSolvable(this)
        }
    }

    var timeElapsed      by remember { mutableIntStateOf(0) }
    var puzzleFinished   by remember { mutableStateOf(false) }
    var showDialog       by remember { mutableStateOf(false) }
    var selectedIndex    by remember { mutableStateOf<Int?>(null) }
    var invalidTap       by remember { mutableStateOf(false) }

    // Timer
    LaunchedEffect(puzzleFinished) {
        while (!puzzleFinished) {
            delay(1000)
            timeElapsed++
        }
    }

    // Clear invalid-tap highlight
    LaunchedEffect(invalidTap) {
        if (invalidTap) {
            delay(400)
            invalidTap = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        // Exit button
        IconButton(
            onClick = onExit,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(48.dp)
                .background(Color.White, CircleShape)
                .border(1.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Sair", tint = Color.Gray)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Reconstrua o KKKK",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            // Timer chip
            Surface(
                shape = RoundedCornerShape(50),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "⏱️ ${timeElapsed}s",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Instruction hint
            Text(
                text = "Toque em uma peça ao lado do espaço vazio para movê-la",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Puzzle grid
            Surface(
                modifier = Modifier
                    .size(312.dp)
                    .padding(4.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    for (row in 0..2) {
                        Row(modifier = Modifier.weight(1f)) {
                            for (col in 0..2) {
                                val index     = row * 3 + col
                                val tileValue = tiles[index]
                                val isEmpty   = tileValue == 8
                                val isSelected = selectedIndex == index

                                PuzzleTile(
                                    bitmap      = if (isEmpty) null else tileBitmaps[tileValue],
                                    isSelected  = isSelected,
                                    modifier    = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .padding(2.dp)
                                        .clickable(enabled = !puzzleFinished && !isEmpty) {
                                            val moved = handleTap(index, tiles)
                                            if (moved) {
                                                selectedIndex = null
                                                if (isSolved(tiles)) {
                                                    puzzleFinished = true
                                                    showDialog = true
                                                }
                                            } else {
                                                // Highlight the tapped tile briefly to give feedback
                                                selectedIndex = index
                                                invalidTap = true
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        puzzleFinished = false
                        selectedIndex  = null
                        shuffleSolvable(tiles)
                        timeElapsed = 0
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    colors    = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape     = RoundedCornerShape(24.dp)
                ) {
                    Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Embaralhar", fontSize = 12.sp, maxLines = 1)
                }

                Button(
                    onClick = {
                        puzzleFinished = true
                        showDialog     = true
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape   = RoundedCornerShape(24.dp),
                    enabled = !puzzleFinished   // still available; only disabled once already finished
                ) {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Finalizar", fontSize = 12.sp, maxLines = 1)
                }
            }
        }

        // Completion dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                title   = { Text("🎉 Parabéns!") },
                text    = { Text("Você reconstruiu o KKKK em ${timeElapsed} segundos!") },
                confirmButton = {
                    Button(onClick = {
                        showDialog = false
                        onPuzzleCompleted(timeElapsed)
                    }) {
                        Text("Continuar")
                    }
                }
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Single puzzle tile
// ---------------------------------------------------------------------------

@Composable
fun PuzzleTile(
    bitmap: Bitmap?,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.93f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tileScale"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(4.dp))
            .background(
                if (bitmap == null) Color.LightGray.copy(alpha = 0.2f)
                else Color.Transparent
            )
            .then(
                if (isSelected) Modifier.border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                else Modifier
            )
    ) {
        if (bitmap != null) {
            androidx.compose.foundation.Image(
                bitmap         = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier       = Modifier.fillMaxSize(),
                contentScale   = ContentScale.FillBounds
            )
        }
    }
}
