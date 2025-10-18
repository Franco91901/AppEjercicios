package com.example.appejercicios.models

data class DiaEjercicioUI(
    val nombreEjercicio: String,
    val repeticiones: Int?,
    val duracionSegundos: Int?,
    val series: Int,
    val descansoSerie: Int
)