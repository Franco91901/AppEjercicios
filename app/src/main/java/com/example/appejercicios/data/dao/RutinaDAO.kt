package com.example.appejercicios.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.appejercicios.data.AppDatabaseHelper
import com.example.appejercicios.models.Rutina

class RutinaDAO(context: Context) {

    private val dbHelper = AppDatabaseHelper(context)

    fun registrarRutina(rutina: Rutina): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_usuario", rutina.idUsuario)
            put("nombre_rutina", rutina.nombreRutina)
            put("descripcion", rutina.descripcion)
        }

        val id = db.insert("rutina", null, values)
        db.close()
        return id
    }

    fun obtenerRutinasPorUsuario(idUsuario: Int): List<Rutina> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM rutina WHERE id_usuario = ?",
            arrayOf(idUsuario.toString())
        )

        val lista = mutableListOf<Rutina>()
        if (cursor.moveToFirst()) {
            do {
                lista.add(cursorToRutina(cursor))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun obtenerRutinaPorId(idRutina: Int): Rutina? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM rutina WHERE id_rutina = ?",
            arrayOf(idRutina.toString())
        )

        var rutina: Rutina? = null
        if (cursor.moveToFirst()) {
            rutina = cursorToRutina(cursor)
        }

        cursor.close()
        db.close()
        return rutina
    }

    fun actualizarRutina(rutina: Rutina): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("nombre_rutina", rutina.nombreRutina)
            put("descripcion", rutina.descripcion)
        }

        val filasAfectadas = db.update(
            "rutina",
            values,
            "id_rutina = ?",
            arrayOf(rutina.idRutina.toString())
        )

        db.close()
        return filasAfectadas
    }

    fun eliminarRutina(idRutina: Int): Int {
        val db = dbHelper.writableDatabase
        val filas = db.delete(
            "rutina",
            "id_rutina = ?",
            arrayOf(idRutina.toString())
        )
        db.close()
        return filas
    }

    private fun cursorToRutina(cursor: Cursor): Rutina {
        return Rutina(
            idRutina = cursor.getInt(cursor.getColumnIndexOrThrow("id_rutina")),
            idUsuario = cursor.getInt(cursor.getColumnIndexOrThrow("id_usuario")),
            nombreRutina = cursor.getString(cursor.getColumnIndexOrThrow("nombre_rutina")),
            descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
        )
    }
}
