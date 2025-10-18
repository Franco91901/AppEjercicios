package com.example.appejercicios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.R
import com.example.appejercicios.models.DiaEjercicioUI
import com.example.appejercicios.models.DiaRutinaUI

class DiaRutinaAdapter(
    private var dias: List<DiaRutinaUI>,
    private var onAgregarEjercicio: (DiaRutinaUI) -> Unit,
    private var onAgregarDescanso: (DiaRutinaUI) -> Unit,
    private var onEditarEjercicio: (DiaRutinaUI, DiaEjercicioUI) -> Unit
) : RecyclerView.Adapter<DiaRutinaAdapter.DiaRutinaViewHolder>() {

    inner class DiaRutinaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDia: TextView = itemView.findViewById(R.id.tvDia)
        val rvEjercicios: RecyclerView = itemView.findViewById(R.id.rvEjercicios)
        val btnAgregarEjercicio: Button = itemView.findViewById(R.id.btnAgregarEjercicio)
        val btnAgregarDescanso: Button = itemView.findViewById(R.id.btnAgregarDescanso)
        val headerDia: LinearLayout = itemView.findViewById(R.id.headerDia)
        val contenedorEjercicios: LinearLayout = itemView.findViewById(R.id.contenedorEjercicios)
        val ivExpandirContraer: ImageView = itemView.findViewById(R.id.ivExpandirContraer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaRutinaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dia_rutina, parent, false)
        return DiaRutinaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiaRutinaViewHolder, position: Int) {
        val dia = dias[position]
        holder.tvDia.text = dia.diaSemana

        holder.rvEjercicios.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.VERTICAL, false)

        val ejercicioAdapter = DiaEjercicioAdapter(
            dia.ejercicios,
            onEditar = { ejercicio ->
                onEditarEjercicio(dia, ejercicio)
            },
            onEliminar = { ejercicio ->
                dia.ejercicios.remove(ejercicio)
                notifyItemChanged(position)
            }
        )
        holder.rvEjercicios.adapter = ejercicioAdapter

        // Expandir / contraer contenedor
        holder.headerDia.setOnClickListener {
            if (holder.contenedorEjercicios.visibility == View.GONE) {
                holder.contenedorEjercicios.visibility = View.VISIBLE
                holder.ivExpandirContraer.setImageResource(R.drawable.ic_expandir)
            } else {
                holder.contenedorEjercicios.visibility = View.GONE
                holder.ivExpandirContraer.setImageResource(R.drawable.ic_contraer)
            }
        }

        // Botones de agregar ejercicio / descanso
        holder.btnAgregarEjercicio.setOnClickListener { onAgregarEjercicio(dia) }
        holder.btnAgregarDescanso.setOnClickListener { onAgregarDescanso(dia) }
    }

    override fun getItemCount(): Int = dias.size
}
