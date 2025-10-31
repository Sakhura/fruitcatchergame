package com.example.fruitcatchergame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fruitcatchergame.ui.theme.FruitcatchergameTheme

//=====================================
// MODELO DE DATOS
//====================================

data class Fruit(
    val id: Int,
    var x: Float,
    var y: Float,
    val type: FruitType,
    val size: Float = 60F

)

enum class FruitType(val emoji: String, val points: Int, val colorHex: Long) {
    APPLE("🍎", 10, 0xFFFF6B6B),
    BANANA("🍌", 20, 0xFFFFE16B),
    ORANGE("🍊", 30, 0xFFFFA56B),
    GRAPE("🍇", 40, 0xFFA56BFF),
    MELON("🍈", 50, 0xFFA),
    PEAR("🍐", 60, 0xFF6BFF),
    STRAWBERRY("🍓", 70, 0xFF6BFF),
    WATERMELON("🍉", 80, 0xFF6BFF),
    BOMB("💣", -30, 0xFF6BFF)

}

enum class GameLevel(
    val number: Int,
    val name: String,
    val requiredScore: Int,
    val fruitSpeed: Float,
    val spawnRate: Long,
    val bombChance: Float
){
    BEGINNER(1, "Principiante", 0, 10F, 1000, 0f),
    EASY(2, "Fácil", 100, 20F, 500, 0.2F),
    MEDIUM(3, "Intermedio", 200, 30F, 250, 0.3F),
    HARD(4, "Dificil", 300, 40F, 125, 0.4F),
    EXPERT(5, "Experto", 400, 50F, 100, 0.21f)

}
enum class GameState{
    MENU, PLAYING, GAME_OVER, VICTORY
}

//=====================================
//ACTIVITY PRINCIPAL
//====================================
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FruitcatchergameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FruitcatchergameTheme {
        Greeting("Android")
    }
}