package com.upchiapas.applibro.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.upchiapas.applibro.domain.ReaderType

// Colores del tema, en un solo lugar para no repetirlos en cada composable
private val GreenPrimary = Color(0xFF4CAF50)
private val GreenDark = Color(0xFF2E7D32)
private val GreenSoft = Color(0xFFE8F5E9)
private val BackgroundApp = Color(0xFFF6FBF6)
private val TextMuted = Color(0xFF6B6B6B)

@Composable
fun LibroScreen(viewModel: LibroViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize().background(BackgroundApp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderBanner()

            Column(
                modifier = Modifier.padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                PagesInputField(
                    pagesInput = uiState.pagesInput,
                    isError = uiState.isError,
                    errorMessage = uiState.errorMessage,
                    onPagesChanged = viewModel::onPagesChanged
                )

                Spacer(modifier = Modifier.height(20.dp))

                TimeSliderSection(
                    timePerDay = uiState.timePerDay,
                    onTimePerDayChanged = viewModel::onTimePerDayChanged
                )

                Spacer(modifier = Modifier.height(20.dp))

                ReaderTypeSelector(
                    selected = uiState.selectedReaderType,
                    onSelected = viewModel::onReaderTypeSelected
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (uiState.pagesInput.isEmpty()) {
                    Text(
                        text = "Escribe las páginas de tu libro",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                } else if (!uiState.isError) {
                    ResultsCard(
                        totalTimeText = uiState.totalTimeText,
                        pagesPerDayText = uiState.pagesPerDayText,
                        daysNeededText = uiState.daysNeededText,
                        summaryText = uiState.summaryText,
                        dailyProgress = uiState.dailyProgress
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                ResetButton(onClick = viewModel::reset)
            }
        }
    }
}

// Encabezado verde con el título de la app
@Composable
private fun HeaderBanner() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(GreenPrimary)
            .padding(vertical = 28.dp, horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Libro al Día",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "CALCULADORA DE RITMO DE LECTURA",
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.85f)
        )
    }
}

@Composable
private fun PagesInputField(
    pagesInput: String,
    isError: Boolean,
    errorMessage: String,
    onPagesChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = pagesInput,
        onValueChange = onPagesChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Páginas del libro") },
        singleLine = true,
        isError = isError,
        supportingText = { if (isError) Text(errorMessage) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = GreenPrimary,
            focusedLabelColor = GreenPrimary,
            cursorColor = GreenPrimary
        )
    )
}

// Slider de tiempo por día
@Composable
private fun TimeSliderSection(
    timePerDay: Int,
    onTimePerDayChanged: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Tiempo disponible por día: $timePerDay min",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(4.dp))
        Slider(
            value = timePerDay.toFloat(),
            onValueChange = { onTimePerDayChanged(it.toInt()) },
            valueRange = 5f..240f,
            steps = 46, // pasos de 5 en 5 minutos
            colors = SliderDefaults.colors(
                thumbColor = GreenDark,
                activeTrackColor = GreenPrimary,
                inactiveTrackColor = GreenSoft
            )
        )
    }
}

// Tarjetas de tipo de lector
@Composable
private fun ReaderTypeSelector(
    selected: ReaderType,
    onSelected: (ReaderType) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "TIPO DE LECTOR", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            ReaderType.values().forEach { type ->
                val isSelected = selected == type
                Card(
                    onClick = { onSelected(type) },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isSelected) GreenSoft else Color.White),
                    border = if (isSelected) BorderStroke(2.dp, GreenPrimary) else BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    modifier = Modifier.weight(1f).padding(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = type.name, style = MaterialTheme.typography.labelLarge)
                        Text(text = "${type.speed} pág/min", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultsCard(
    totalTimeText: String,
    pagesPerDayText: String,
    daysNeededText: String,
    summaryText: String,
    dailyProgress: Float
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Column {
                Text(text = "TIEMPO TOTAL DEL LIBRO", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Text(text = totalTimeText, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = GreenDark)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                MiniStat(label = "Páginas por día", value = pagesPerDayText, modifier = Modifier.weight(1f))
                MiniStat(label = "Días necesarios", value = daysNeededText, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(text = summaryText, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = "Progreso diario estimado (% del libro que avanzas cada día)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { dailyProgress },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = GreenPrimary,
                trackColor = GreenSoft
            )
            Text(text = "${(dailyProgress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        }
    }
}

@Composable
private fun MiniStat(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.background(GreenSoft, shape = RoundedCornerShape(10.dp)).padding(10.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ResetButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
    ) {
        Text("Restablecer valores")
    }
}
//visualizar
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibroScreenPreview() {
    MaterialTheme {
        LibroScreen(viewModel = LibroViewModel())
    }
}