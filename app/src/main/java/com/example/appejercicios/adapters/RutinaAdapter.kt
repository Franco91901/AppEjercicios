package com.example.appejercicios.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.EjecutarRutinaActivity
import com.example.appejercicios.R
import com.example.appejercicios.models.DiaEjercicio
import com.example.appejercicios.models.DiaRutina
import com.example.appejercicios.models.Rutina
import com.google.android.material.button.MaterialButton

class RutinaAdapter(
    private val listaRutinas: List<Rutina>,
    private val listaDiasRutina: List<DiaRutina>,
    private val listaDiaEjercicio: List<DiaEjercicio>
) : RecyclerView.Adapter<RutinaAdapter.RutinaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_rutina, parent, false)
        return RutinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: RutinaViewHolder, position: Int) {
        val rutina = listaRutinas[position]
        holder.tvNombreRutina.text = rutina.nombreRutina
        holder.tvDescripcionRutina.text = rutina.descripcion ?: ""

        holder.btnComenzarRutina.setOnClickListener {
            val intent = Intent(holder.itemView.context, EjecutarRutinaActivity::class.java)
            intent.putExtra("RUTINA_SELECCIONADA", rutina)
            intent.putExtra("LISTA_DIAS_RUTINA", ArrayList(listaDiasRutina))
            intent.putExtra("LISTA_DIA_EJERCICIO", ArrayList(listaDiaEjercicio))
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = listaRutinas.size

    inner class RutinaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreRutina: TextView = view.findViewById(R.id.tvNombreRutina)
        val tvDescripcionRutina: TextView = view.findViewById(R.id.tvDescripcionRutina)
        val btnComenzarRutina: MaterialButton = view.findViewById(R.id.btnComenzarRutina)
    }
}
