package com.example.fruitcatchergame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fruitcatchergame.ui.theme.FruitcatchergameTheme
import kotlinx.coroutines.delay
import kotlin.random.Random

// ============================================
// MODELOS DE DATOS
// ============================================

data class Fruit(
    val id: Int,
    var x: Float,
    var y: Float,
    val type: FruitType,
    val size: Float = 60f
)

enum class FruitType(val emoji: String, val points: Int, val colorHex: Long) {
    APPLE("🍎", 10, 0xFFFF6B6B),
    BANANA("🍌", 15, 0xFFFFE66D),
    ORANGE("🍊", 10, 0xFFFF8C42),
    GRAPE("🍇", 20, 0xFF9B59B6),
    STRAWBERRY("🍓", 25, 0xFFFF4757),
    WATERMELON("🍉", 5, 0xFF26DE81),
    BOMB("💣", -30, 0xFF2F3542)
}

enum class GameLevel(
    val number: Int,
    val name: String,
    val requiredScore: Int,
    val fruitSpeed: Float,
    val spawnRate: Long,
    val bombChance: Float
) {
    BEGINNER(1, "Principiante", 100, 3f, 1500L, 0f),
    EASY(2, "Fácil", 250, 4f, 1200L, 0.05f),
    MEDIUM(3, "Intermedio", 500, 5f, 1000L, 0.1f),
    HARD(4, "Difícil", 800, 6f, 800L, 0.15f),
    EXPERT(5, "Experto", 1200, 7f, 600L, 0.2f)
}

enum class GameState {
    MENU, PLAYING, GAME_OVER, VICTORY
}

// ============================================
// ACTIVITY PRINCIPAL
// ============================================

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FruitcatchergameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FruitCatcherGameApp()
                }
            }
        }
    }
}

// ============================================
// COMPOSABLE PRINCIPAL
// ============================================

@Composable
fun FruitCatcherGameApp() {
    var gameState by remember { mutableStateOf(GameState.MENU) }
    var currentLevel by remember { mutableStateOf(GameLevel.BEGINNER) }
    var score by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF87CEEB))
    ) {
        when (gameState) {
            GameState.MENU -> MenuScreen(
                onStartGame = {
                    gameState = GameState.PLAYING
                    currentLevel = GameLevel.BEGINNER
                    score = 0
                    lives = 3
                }
            )
            GameState.PLAYING -> GameScreen(
                currentLevel = currentLevel,
                score = score,
                lives = lives,
                onScoreChange = { score = it },
                onLivesChange = { lives = it },
                onGameOver = { gameState = GameState.GAME_OVER },
                onLevelComplete = { nextLevel ->
                    if (nextLevel != null) {
                        currentLevel = nextLevel
                    } else {
                        gameState = GameState.VICTORY
                    }
                }
            )
            GameState.GAME_OVER -> GameOverScreen(
                score = score,
                level = currentLevel,
                onRestart = {
                    gameState = GameState.PLAYING
                    currentLevel = GameLevel.BEGINNER
                    score = 0
                    lives = 3
                },
                onMenu = { gameState = GameState.MENU }
            )
            GameState.VICTORY -> VictoryScreen(
                score = score,
                onMenu = { gameState = GameState.MENU }
            )
        }
    }
}

// ============================================
// PANTALLA DE MENÚ
// ============================================

@Composable
fun MenuScreen(onStartGame: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🍎 ATRAPA FRUTAS 🍌",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "¡Supera 5 niveles y prepara un postre!",
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onStartGame,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text("JUGAR", fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Instrucciones:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = "• Toca las frutas para atraparlas",
            fontSize = 14.sp,
            color = Color.White
        )
        Text(
            text = "• ¡Evita las bombas! 💣",
            fontSize = 14.sp,
            color = Color.White
        )
        Text(
            text = "• No dejes caer las frutas",
            fontSize = 14.sp,
            color = Color.White
        )
    }
}

// ============================================
// PANTALLA DE JUEGO
// ============================================

@Composable
fun GameScreen(
    currentLevel: GameLevel,
    score: Int,
    lives: Int,
    onScoreChange: (Int) -> Unit,
    onLivesChange: (Int) -> Unit,
    onGameOver: () -> Unit,
    onLevelComplete: (GameLevel?) -> Unit
) {
    var fruits by remember { mutableStateOf(listOf<Fruit>()) }
    var nextFruitId by remember { mutableIntStateOf(0) }
    var screenWidth by remember { mutableFloatStateOf(1080f) }
    var screenHeight by remember { mutableFloatStateOf(1920f) }

    // Generar frutas periódicamente
    LaunchedEffect(currentLevel) {
        while (lives > 0) {
            delay(currentLevel.spawnRate)

            val fruitType = if (Random.nextFloat() < currentLevel.bombChance) {
                FruitType.BOMB
            } else {
                FruitType.values().filter { it != FruitType.BOMB }.random()
            }

            val newFruit = Fruit(
                id = nextFruitId++,
                x = Random.nextFloat() * (screenWidth - 60f),
                y = -60f,
                type = fruitType
            )
            fruits = fruits + newFruit
        }
    }

    // Animar caída de frutas
    LaunchedEffect(currentLevel, fruits.size) {
        while (lives > 0) {
            delay(16L)
            fruits = fruits.map { fruit ->
                fruit.copy(y = fruit.y + currentLevel.fruitSpeed)
            }.filter { it.y < screenHeight }

            val missedFruits = fruits.filter { it.y >= screenHeight - 100f }
            if (missedFruits.isNotEmpty()) {
                val newLives = lives - missedFruits.count { it.type != FruitType.BOMB }
                onLivesChange(newLives.coerceAtLeast(0))
                if (newLives <= 0) {
                    onGameOver()
                }
                fruits = fruits.filter { it.y < screenHeight - 100f }
            }
        }
    }

    // Verificar cambio de nivel
    LaunchedEffect(score) {
        if (score >= currentLevel.requiredScore) {
            delay(500)
            val nextLevel = when (currentLevel) {
                GameLevel.BEGINNER -> GameLevel.EASY
                GameLevel.EASY -> GameLevel.MEDIUM
                GameLevel.MEDIUM -> GameLevel.HARD
                GameLevel.HARD -> GameLevel.EXPERT
                GameLevel.EXPERT -> null
            }
            onLevelComplete(nextLevel)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val tappedFruit = fruits.firstOrNull { fruit ->
                            val dx = offset.x - (fruit.x + fruit.size / 2)
                            val dy = offset.y - (fruit.y + fruit.size / 2)
                            (dx * dx + dy * dy) <= (fruit.size * fruit.size / 4)
                        }

                        tappedFruit?.let { fruit ->
                            onScoreChange(score + fruit.type.points)
                            fruits = fruits.filter { it.id != fruit.id }
                        }
                    }
                }
        ) {
            screenWidth = size.width
            screenHeight = size.height

            fruits.forEach { fruit ->
                drawFruit(fruit)
            }
        }

        // HUD
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Nivel ${currentLevel.number}: ${currentLevel.name}",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "❤️".repeat(lives),
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Puntos: $score / ${currentLevel.requiredScore}",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ============================================
// FUNCIÓN DE DIBUJO
// ============================================

fun DrawScope.drawFruit(fruit: Fruit) {
    val color = Color(fruit.type.colorHex)

    // Sombra
    drawCircle(
        color = Color.Black.copy(alpha = 0.2f),
        radius = fruit.size / 2,
        center = Offset(fruit.x + fruit.size / 2 + 4f, fruit.y + fruit.size / 2 + 4f)
    )

    // Fruta
    drawCircle(
        color = color,
        radius = fruit.size / 2,
        center = Offset(fruit.x + fruit.size / 2, fruit.y + fruit.size / 2)
    )

    // Brillo
    drawCircle(
        color = Color.White.copy(alpha = 0.3f),
        radius = fruit.size / 5,
        center = Offset(fruit.x + fruit.size / 3, fruit.y + fruit.size / 3)
    )
}

// ============================================
// PANTALLA GAME OVER
// ============================================

@Composable
fun GameOverScreen(
    score: Int,
    level: GameLevel,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "😢 GAME OVER 😢",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Llegaste al nivel: ${level.name}",
            fontSize = 24.sp,
            color = Color.White
        )
        Text(
            text = "Puntuación: $score",
            fontSize = 32.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text("REINTENTAR", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onMenu,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            )
        ) {
            Text("MENÚ", fontSize = 20.sp)
        }
    }
}

// ============================================
// PANTALLA DE VICTORIA
// ============================================

@Composable
fun VictoryScreen(score: Int, onMenu: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎉 ¡VICTORIA! 🎉",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFFD700)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "¡Superaste todos los niveles!",
            fontSize = 24.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(30.dp))

        // Preparación del postre
        Text(
            text = "🍰 PREPARANDO TU POSTRE 🍰",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFFF69B4)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Ingredientes recolectados:",
            fontSize = 18.sp,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "🍎 Manzanas", fontSize = 20.sp, color = Color.White)
        Text(text = "🍌 Bananas", fontSize = 20.sp, color = Color.White)
        Text(text = "🍓 Fresas", fontSize = 20.sp, color = Color.White)
        Text(text = "🍇 Uvas", fontSize = 20.sp, color = Color.White)
        Text(text = "🍊 Naranjas", fontSize = 20.sp, color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "= 🍨 ENSALADA DE FRUTAS 🍨",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF00D2FF)
        )

        Spacer(modifier = Modifier.height(30.dp))
        Text(
            text = "Puntuación Final: $score",
            fontSize = 32.sp,
            color = Color(0xFFFFD700),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onMenu,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Text("VOLVER AL MENÚ", fontSize = 20.sp)
        }
    }
}