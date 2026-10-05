package com.upchiapas.applibro.presentation

import androidx.lifecycle.ViewModel
import com.upchiapas.applibro.domain.DefaultReadingPlanCalculator
import com.upchiapas.applibro.domain.ReadingPlanCalculator
import com.upchiapas.applibro.domain.ReaderType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


//Constantes del libro solo de lectura
private const val MIN_PAGES = 1
private const val MAX_PAGES = 5000
private const val MIN_TIME = 5
private const val MAX_TIME = 240

/**
 * este ViewModel recibe lo de la clase calculator por parámetro
 */
class LibroViewModel(
    private val calculator: ReadingPlanCalculator = DefaultReadingPlanCalculator()) : ViewModel() {

    // Se usa un solo StateFlow con todos los estados
    // Antes había 10 StateFlow sueltos
    private val _uiState = MutableStateFlow(ReadingPlanUiState())
    val uiState: StateFlow<ReadingPlanUiState> = _uiState.asStateFlow()

    fun onPagesChanged(newText: String) {
        val filtered = newText.filter { it.isDigit() }.take(4)
        val pages = filtered.toIntOrNull()
        val invalid = filtered.isNotEmpty() && (pages == null || pages < MIN_PAGES || pages > MAX_PAGES)

        // .update { it.copy(...) } reemplaza el estado por una copia con
        // solo los campos que cambiaron no muta el objeto viejo.
        _uiState.update {
            it.copy(
                pagesInput = filtered,
                isError = invalid,
                errorMessage = if (invalid) "Ingresa un número entre $MIN_PAGES y $MAX_PAGES" else ""
            )
        }
        recalculate()
    }

    fun onTimePerDayChanged(newTime: Int) {
        _uiState.update { it.copy(timePerDay = newTime.coerceIn(MIN_TIME, MAX_TIME)) }
        recalculate()
    }

    fun onReaderTypeSelected(type: ReaderType) {
        _uiState.update { it.copy(selectedReaderType = type) }
        recalculate()
    }

    fun reset() {
        _uiState.value = ReadingPlanUiState()
    }

    /**
     * este ViewModel solo lee el estado actual, le pide el
     * resultado a la clase calculator y actualiza el estado con lo que regresó.
     */
    private fun recalculate() {
        val state = _uiState.value
        val pages = state.pagesInput.toIntOrNull()

        if (pages == null || pages < MIN_PAGES || pages > MAX_PAGES) {
            _uiState.update {
                it.copy(
                    totalTimeText = "",
                    pagesPerDayText = "",
                    daysNeededText = "",
                    summaryText = "",
                    dailyProgress = 0f
                )
            }
            return
        }

        val result = calculator.calculate(
            pages = pages,
            minutesPerDay = state.timePerDay,
            readerType = state.selectedReaderType
        )

        // Actualiza
        _uiState.update {
            it.copy(
                totalTimeText = "${result.totalHours} h ${result.totalMinutes} min",
                pagesPerDayText = "${result.pagesPerDay} pág",
                daysNeededText = "${result.daysNeeded} días",
                summaryText = "Leyendo ${state.timePerDay} min al día terminarás en " +
                        "${result.daysNeeded} días (${result.totalHours} h ${result.totalMinutes} min de lectura en total)",
                dailyProgress = result.dailyProgress
            )
        }
    }
}