package com.example.appejercicios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.R
import com.example.appejercicios.models.RutinaUI

class RutinaYTAdapter(
    private val rutinas: List<RutinaUI>,
    private val onItemClick: (RutinaUI) -> Unit
) : RecyclerView.Adapter<RutinaYTAdapter.RutinaViewHolder>() {

    inner class RutinaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgYoutuber: ImageView = itemView.findViewById(R.id.imgYoutuber)
        val tvNombreRutina: TextView = itemView.findViewById(R.id.tvNombreRutina)
        val tvYoutuber: TextView = itemView.findViewById(R.id.tvYoutuber)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rutina_yt, parent, false)
        return RutinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        val rutina = rutinas[position]

        holder.imgYoutuber.setImageResource(rutina.imagenResId)
        holder.tvNombreRutina.text = rutina.nombreRutina
        holder.tvYoutuber.text = "Autor: ${rutina.youtuber}"
        holder.tvDescripcion.text = rutina.descripcion

        // click en la card
        holder.itemView.setOnClickListener {
            onItemClick(rutina)
        }
    }

    override fun getItemCount(): Int = rutinas.size
}
