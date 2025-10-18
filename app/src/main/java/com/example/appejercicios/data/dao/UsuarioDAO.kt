package com.example.appejercicios.data.dao

import android.content.ContentValues
import android.content.Context
import com.example.appejercicios.data.AppDatabaseHelper
import com.example.appejercicios.models.Usuario

class UsuarioDAO(context: Context) {

    private val dbHelper = AppDatabaseHelper(context)

    fun existeCorreo(correo: String): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT id_usuario FROM usuario WHERE correo = ?",
            arrayOf(correo)
        )
        val existe = cursor.count > 0
        cursor.close()
        db.close()
        return existe
    }

    fun registrarUsuario(usuario: Usuario): Long? {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("correo", usuario.correo)
            put("contrasenia", usuario.contrasenia)
            put("nombres", usuario.nombres)
            put("apellidos", usuario.apellidos)
            put("sexo", usuario.sexo)
            put("edad", usuario.edad)
            put("peso", usuario.peso)
            put("altura", usuario.altura)
            put("rol", usuario.rol) // "BASICO" o "ADMIN"
        }

        val id = db.insert("usuario", null, values)
        db.close()

        return if (id != -1L) {
            id
        } else {
            null
        }
    }

    fun login(correo: String, contrasenia: String): Usuario? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuario WHERE correo = ? AND contrasenia = ?",
            arrayOf(correo, contrasenia)
        )

        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            usuario = cursorToUsuario(cursor)
        }

        cursor.close()
        db.close()
        return usuario
    }

    fun actualizarUsuario(usuario: Usuario): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("correo", usuario.correo)
            put("contrasenia", usuario.contrasenia)
            put("nombres", usuario.nombres)
            put("apellidos", usuario.apellidos)
            put("sexo", usuario.sexo)
            put("edad", usuario.edad)
            put("peso", usuario.peso)
            put("altura", usuario.altura)
        }

        val result = db.update(
            "usuario",
            values,
            "id_usuario = ?",
            arrayOf(usuario.idUsuario.toString())
        )
        db.close()
        return result > 0
    }

    fun obtenerUsuarioPorId(idUsuario: Int): Usuario? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM usuario WHERE id_usuario = ?",
            arrayOf(idUsuario.toString())
        )

        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            usuario = cursorToUsuario(cursor)
        }

        cursor.close()
        db.close()
        return usuario
    }

    private fun cursorToUsuario(cursor: android.database.Cursor): Usuario {
        val idxId = cursor.getColumnIndexOrThrow("id_usuario")
        val idxCorreo = cursor.getColumnIndexOrThrow("correo")
        val idxCon = cursor.getColumnIndexOrThrow("contrasenia")
        val idxNombres = cursor.getColumnIndexOrThrow("nombres")
        val idxApellidos = cursor.getColumnIndexOrThrow("apellidos")
        val idxSexo = cursor.getColumnIndexOrThrow("sexo")
        val idxEdad = cursor.getColumnIndexOrThrow("edad")
        val idxPeso = cursor.getColumnIndexOrThrow("peso")
        val idxAltura = cursor.getColumnIndexOrThrow("altura")
        val idxRol = cursor.getColumnIndexOrThrow("rol")

        val id = cursor.getInt(idxId)
        val correo = cursor.getString(idxCorreo)
        val contrasenia = cursor.getString(idxCon)
        val nombres = cursor.getString(idxNombres)
        val apellidos = cursor.getString(idxApellidos)
        val sexo = if (!cursor.isNull(idxSexo)) cursor.getString(idxSexo) else ""
        val edad = if (!cursor.isNull(idxEdad)) cursor.getInt(idxEdad) else 0
        val peso = if (!cursor.isNull(idxPeso)) cursor.getDouble(idxPeso) else 0.0
        val altura = if (!cursor.isNull(idxAltura)) cursor.getDouble(idxAltura) else 0.0
        val rol = if (!cursor.isNull(idxRol)) cursor.getString(idxRol) else "BASICO"

        return Usuario(
            idUsuario = id,
            correo = correo,
            contrasenia = contrasenia,
            nombres = nombres,
            apellidos = apellidos,
            sexo = sexo,
            edad = edad,
            peso = peso,
            altura = altura,
            rol = rol
        )
    }
}
