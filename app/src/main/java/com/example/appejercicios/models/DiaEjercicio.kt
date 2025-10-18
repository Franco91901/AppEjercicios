package com.example.appejercicios.models

import java.io.Serializable

data class DiaEjercicio(
    val idDiaEjercicio: Int,
    val idDiaRutina: Int,
    val idEjercicio: Int,
    val series: Int,
    val repeticiones: Int?,
    val duracionSegundos: Int?,
    val descansoSerie: Int,
    val ordenEjercicio: Int
) : Serializable