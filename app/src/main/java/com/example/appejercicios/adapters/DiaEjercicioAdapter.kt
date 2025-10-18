package com.example.appejercicios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.R
import com.example.appejercicios.models.DiaEjercicioUI

class DiaEjercicioAdapter(
    private var ejercicios: List<DiaEjercicioUI>,
    private var onEditar: (DiaEjercicioUI) -> Unit,
    private var onEliminar: (DiaEjercicioUI) -> Unit
) : RecyclerView.Adapter<DiaEjercicioAdapter.DiaEjercicioViewHolder>() {

    inner class DiaEjercicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreEjercicio: TextView = itemView.findViewById(R.id.tvNombreEjercicio)
        val tvDetalleEjercicio: TextView = itemView.findViewById(R.id.tvDetalleEjercicio)
        val btnEditarEjercicio: ImageButton = itemView.findViewById(R.id.btnEditarEjercicio)
        val btnEliminarEjercicio: ImageButton = itemView.findViewById(R.id.btnEliminarEjercicio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaEjercicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dia_ejercicio, parent, false)
        return DiaEjercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaEjercicioViewHolder, position: Int) {
        val ejercicio = ejercicios[position]

        holder.tvNombreEjercicio.text = ejercicio.nombreEjercicio

        // Crear el detalle dinámicamente según los datos
        val detalle = if (ejercicio.nombreEjercicio == "Descanso") {
            "${ejercicio.duracionSegundos ?: 0} seg de descanso"
        } else {
            buildString {
                ejercicio.repeticiones?.let { append("$it rep x ") }
                ejercicio.duracionSegundos?.let { append("$it seg x ") }
                append("${ejercicio.series} ser / ")
                append("${ejercicio.descansoSerie} seg descanso entre series")
            }
        }
        holder.tvDetalleEjercicio.text = detalle

        holder.btnEditarEjercicio.setOnClickListener { onEditar(ejercicio) }
        holder.btnEliminarEjercicio.setOnClickListener { onEliminar(ejercicio) }
    }

    override fun getItemCount(): Int = ejercicios.size
}
