package com.upchiapas.applibro.presentation

import com.upchiapas.applibro.domain.ReaderType

// El ViewModel guarda una sola instancia de esto --> cuando algo cambia,
// se reemplaza por una copia con ese campo actualizado (.copy()).
data class ReadingPlanUiState(

    // Entrada String
    val pagesInput: String = "",

    // Minutos disponibles por día. 30 default
    val timePerDay: Int = 30,
    // Tipo de lector elegido --> arranca en promedio.
    val selectedReaderType: ReaderType = ReaderType.AVERAGE,

    // Si hay un error de validación en las páginas
    val isError: Boolean = false,
    val errorMessage: String = "",

    // Resultados ya calculados --> se muestran como texto para pintarse en pantalla
    val totalTimeText: String = "",
    val pagesPerDayText: String = "",
    val daysNeededText: String = "",
    val summaryText: String = "",

    // Barra de porcentaje del libro que se avanza en un día
    val dailyProgress: Float = 0f
)