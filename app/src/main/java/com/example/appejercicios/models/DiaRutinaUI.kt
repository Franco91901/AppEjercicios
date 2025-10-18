package com.example.appejercicios.models

data class DiaRutinaUI(
    val diaSemana: String,
    val ejercicios: MutableList<DiaEjercicioUI>
)