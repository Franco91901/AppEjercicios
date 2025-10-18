package com.example.appejercicios.models

import java.io.Serializable

data class DiaRutina(
    val idDiaRutina: Int,
    val idRutina: Int,
    val diaSemana: Int // 1=Lunes … 7=Domingo
) : Serializable