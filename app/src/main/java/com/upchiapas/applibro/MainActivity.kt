package com.upchiapas.applibro

// Imports base de Android
import android.os.Bundle
// ComponentActivity es la base para actividades con Compose
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// Import de la pantalla que vamos a mostrar
import com.upchiapas.applibro.presentation.LibroScreen
// Import del tema de la aplicación (ajusta el nombre según tu proyecto)
import com.upchiapas.applibro.ui.theme.ApplibroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // setContent define el contenido usando Compose
        setContent {
            ApplibroTheme(){
                // Mostramos la pantalla principal
                LibroScreen()
            }
        }
    }
}