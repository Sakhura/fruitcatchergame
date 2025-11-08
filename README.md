# 🍎 Fruit Catcher Game 🍌

Un divertido juego de Android desarrollado con Jetpack Compose donde debes atrapar frutas que caen del cielo mientras evitas las bombas. ¡Supera 5 niveles progresivamente más difíciles y prepara una deliciosa ensalada de frutas!

## 📱 Descripción

Fruit Catcher Game es un juego arcade simple pero adictivo donde:
- Las frutas caen desde la parte superior de la pantalla
- Debes tocar las frutas para atraparlas y ganar puntos
- ¡Cuidado con las bombas! Te restarán puntos
- No dejes caer las frutas o perderás vidas
- Avanza a través de 5 niveles con dificultad creciente

## ✨ Características

### 🎮 Mecánicas de Juego
- **Sistema de niveles**: 5 niveles progresivos (Principiante, Fácil, Intermedio, Difícil, Experto)
- **Múltiples frutas**: 6 tipos diferentes de frutas con diferentes puntuaciones
- **Sistema de bombas**: Aparecen bombas que debes evitar
- **Sistema de vidas**: 3 vidas para completar cada nivel
- **Puntuación dinámica**: Cada fruta otorga diferentes puntos

### 🍓 Tipos de Frutas
| Fruta | Emoji | Puntos |
|-------|-------|--------|
| Manzana | 🍎 | 10 |
| Banana | 🍌 | 15 |
| Naranja | 🍊 | 10 |
| Uvas | 🍇 | 20 |
| Fresa | 🍓 | 25 |
| Sandía | 🍉 | 5 |
| Bomba | 💣 | -30 |

### 📊 Progresión de Niveles
| Nivel | Nombre | Puntos Requeridos | Velocidad | Tasa de Aparición | Probabilidad de Bombas |
|-------|--------|-------------------|-----------|-------------------|------------------------|
| 1 | Principiante | 100 | 3.0 | 1500ms | 0% |
| 2 | Fácil | 250 | 4.0 | 1200ms | 5% |
| 3 | Intermedio | 500 | 5.0 | 1000ms | 10% |
| 4 | Difícil | 800 | 6.0 | 800ms | 15% |
| 5 | Experto | 1200 | 7.0 | 600ms | 20% |

## 🛠️ Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **Framework UI**: Jetpack Compose
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Animaciones**: Compose Animation API
- **Canvas**: Compose Canvas para renderizado de gráficos
- **Coroutines**: Para manejo asíncrono y temporizadores
- **Material Design 3**: Para componentes de UI

## 📋 Requisitos

### Requisitos del Sistema
- Android Studio Ladybug | 2024.2.1 o superior
- JDK 21
- Android SDK 36
- Gradle 8.13

### Requisitos del Dispositivo
- Android 7.0 (API 24) o superior
- Pantalla táctil

## 🚀 Instalación

1. **Clona el repositorio**
   ```bash
   git clone https://github.com/Sakhura/fruitcatchergame
   cd fruitcatchergame
   ```

2. **Abre el proyecto en Android Studio**
    - Abre Android Studio
    - Selecciona "Open an Existing Project"
    - Navega hasta la carpeta del proyecto y selecciónala

3. **Configura el SDK**
    - Asegúrate de tener instalado Android SDK 36
    - File → Project Structure → SDK Location

4. **Sincroniza Gradle**
    - Android Studio debería sincronizar automáticamente
    - Si no, haz clic en "Sync Project with Gradle Files"

5. **Ejecuta la aplicación**
    - Conecta un dispositivo Android o inicia un emulador
    - Haz clic en el botón "Run" (▶️) o presiona `Shift + F10`

## 🎯 Cómo Jugar

1. **Inicio**: Presiona el botón "JUGAR" en el menú principal
2. **Atrapa frutas**: Toca las frutas que caen para atraparlas
3. **Evita bombas**: No toques las bombas (💣) o perderás puntos
4. **Mantén las vidas**: No dejes caer frutas o perderás una vida
5. **Avanza de nivel**: Alcanza la puntuación requerida para pasar al siguiente nivel
6. **Victoria**: Completa los 5 niveles para "preparar tu ensalada de frutas"

## 📁 Estructura del Proyecto

```
fruitcatchergame/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/fruitcatchergame/
│   │   │   │   ├── MainActivity.kt          # Actividad principal y lógica del juego
│   │   │   │   └── ui/theme/                # Configuración de tema
│   │   │   │       ├── Color.kt
│   │   │   │       ├── Theme.kt
│   │   │   │       └── Type.kt
│   │   │   ├── res/                         # Recursos (drawables, valores, etc.)
│   │   │   └── AndroidManifest.xml
│   │   └── test/                            # Tests unitarios
│   └── build.gradle.kts                     # Configuración de Gradle del módulo
├── gradle/                                   # Wrapper de Gradle
├── build.gradle.kts                         # Configuración de Gradle del proyecto
└── settings.gradle.kts                      # Configuración de Gradle
```

## 🎨 Componentes Principales

### Data Classes
- **`Fruit`**: Representa una fruta en el juego
- **`FruitType`**: Enum con los tipos de frutas y sus propiedades
- **`GameLevel`**: Enum con los niveles del juego
- **`GameState`**: Enum para el estado del juego (MENU, PLAYING, GAME_OVER, VICTORY)

### Composables
- **`FruitCatcherGameApp`**: Composable principal que maneja el estado del juego
- **`MenuScreen`**: Pantalla de menú principal
- **`GameScreen`**: Pantalla de juego con lógica de gameplay
- **`GameOverScreen`**: Pantalla de game over
- **`VictoryScreen`**: Pantalla de victoria

## 🐛 Solución de Problemas

### Error: "requires libraries to compile against version 36"
**Solución**: Actualiza `compileSdk` a 36 en `app/build.gradle.kts`:
```kotlin
android {
    compileSdk = 36
}
```

### El juego corre lento en el emulador
**Solución**:
- Usa un dispositivo físico para mejor rendimiento
- Habilita aceleración de hardware en el emulador
- Reduce el número de frutas simultáneas editando `spawnRate`

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Si deseas mejorar el juego:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/NuevaCaracteristica`)
3. Commit tus cambios (`git commit -m 'Agrega nueva característica'`)
4. Push a la rama (`git push origin feature/NuevaCaracteristica`)
5. Abre un Pull Request

## 📝 Ideas de Mejoras Futuras

- [ ] Sistema de puntuación alta (high scores) persistente
- [ ] Power-ups especiales (escudo, tiempo lento, imán)
- [ ] Efectos de sonido y música de fondo
- [ ] Animaciones de partículas al atrapar frutas
- [ ] Modo multijugador local
- [ ] Logros y desafíos diarios
- [ ] Diferentes temas visuales
- [ ] Integración con Google Play Games

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la [Licencia MIT](LICENSE).

## 👨‍💻 Autor

Desarrollado como proyecto educativo para aprender Jetpack Compose y desarrollo de juegos en Android.
Docente Sabina Romero Rodríguez

## 📞 Contacto

¿Tienes preguntas o sugerencias? ¡No dudes en abrir un issue!

---

⭐ Si te gusta este proyecto, ¡dale una estrella en GitHub!

🍎🍌🍊🍇🍓🍉 ¡Que disfrutes jugando! 🍉🍓🍇🍊🍌🍎