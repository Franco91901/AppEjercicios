package com.example.appejercicios.models

data class Ejercicio(
    val idEjercicio: Int,
    var nombre: String,
    var descripcion: String?,
    var tipoMedida: String, // REPETICIONES, TIEMPO, AMBAS
    var dificultad: String // FACIL, MEDIA, DIFICIL
)