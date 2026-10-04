package com.upchiapas.applibro.domain

// enum que representa los tres tipos de lector disponibles.
// Se usa un enum porque las velocidades son fijas.
enum class ReaderType(val speed: Double) {
    SLOW(0.25),     // Lector lento: 0.25 páginas por minuto
    AVERAGE(0.5),   // Lector promedio: 0.5 páginas por minuto
    FAST(0.75)      // Lector rápido: 0.75 páginas por minuto
}