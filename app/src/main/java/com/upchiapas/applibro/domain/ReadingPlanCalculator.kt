package com.upchiapas.applibro.domain

import kotlin.math.ceil //import para redondear los decimales

/**
 Class destinada solamente para los calculos del Calculator
 */
data class ReadingPlanResult(
    val totalHours: Int,
    val totalMinutes: Int,
    val pagesPerDay: Int,
    val daysNeeded: Int,
    val dailyProgress: Float
)

interface ReadingPlanCalculator {
    fun calculate(pages: Int, minutesPerDay: Int, readerType: ReaderType): ReadingPlanResult
}

/**
 * Antes esta lógica vivía dentro de
 * LibroViewModel.recalculate(). Ahora el ViewModel solo recibe estos calculos
 */
class DefaultReadingPlanCalculator : ReadingPlanCalculator {

    override fun calculate(pages: Int, minutesPerDay: Int, readerType: ReaderType): ReadingPlanResult {
        val speed = readerType.speed // páginas por minuto

        // Minutos totales para leer el libro completo
        val totalMinutesRaw = pages / speed

        // Horas y minutos enteros --> convierte el valor en entero
        val totalHours = (totalMinutesRaw / 60).toInt()
        val totalMinutes = (totalMinutesRaw % 60).toInt()

        val pagesPerDay = ceil(minutesPerDay * speed).toInt()

        // Días necesarios se calculan con el tiempo totalMinutesRaw.
        val daysNeeded = ceil(totalMinutesRaw / minutesPerDay).toInt()

        // indica el % del libro que se avanza en un día
        val dailyProgress = (pagesPerDay.toFloat() / pages).coerceIn(0f, 1f)

        return ReadingPlanResult(totalHours, totalMinutes, pagesPerDay, daysNeeded, dailyProgress)
    }
}