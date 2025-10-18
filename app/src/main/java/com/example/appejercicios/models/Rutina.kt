package com.example.appejercicios.models

import java.io.Serializable

data class Rutina(
    val idRutina: Int,
    val idUsuario: Int,
    val nombreRutina: String,
    val descripcion: String?
) : Serializable