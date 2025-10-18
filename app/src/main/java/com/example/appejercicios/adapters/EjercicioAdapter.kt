package com.example.appejercicios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.R
import com.example.appejercicios.models.Ejercicio

class EjercicioAdapter(
    private var ejercicios: List<Ejercicio>,
    private var onClick: (Ejercicio) -> Unit
) : RecyclerView.Adapter<EjercicioAdapter.EjercicioViewHolder>() {

    private var ejerciciosFiltrados: MutableList<Ejercicio> = ejercicios.toMutableList()

    inner class EjercicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreEjercicio: TextView = itemView.findViewById(R.id.tvNombreEjercicio)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ejercicio, parent, false)
        return EjercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: EjercicioViewHolder, position: Int) {
        val ejercicio = ejerciciosFiltrados[position]
        holder.tvNombreEjercicio.text = ejercicio.nombre
        holder.itemView.setOnClickListener { onClick(ejercicio) }
    }

    override fun getItemCount(): Int {
      return ejerciciosFiltrados.size
    }

    fun filtrar(texto: String) {
        ejerciciosFiltrados = if (texto.isEmpty()) {
            ejercicios.toMutableList()
        } else {
            ejercicios.filter {
                it.nombre.contains(texto, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}
