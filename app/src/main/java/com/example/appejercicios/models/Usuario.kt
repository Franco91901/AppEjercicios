package com.example.appejercicios.models

data class Usuario(
    val idUsuario: Int,
    val correo: String,
    val contrasenia: String,
    val nombres: String,
    val apellidos: String,
    val sexo: String, // Masculino o Femenino
    val edad: Int?,
    val peso: Double?,
    val altura: Double?,
    val rol: String // ADMIN o BASICO
)