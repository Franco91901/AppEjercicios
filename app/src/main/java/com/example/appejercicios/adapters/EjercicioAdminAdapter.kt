package com.example.appejercicios.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.R
import com.example.appejercicios.models.Ejercicio

class EjercicioAdminAdapter(
    private var listaEjercicios : MutableList<Ejercicio>,
    private var onEditarClick: (Ejercicio) -> Unit
) : RecyclerView.Adapter<EjercicioAdminAdapter.EjercicioAdminViewHolder>() {

    inner class EjercicioAdminViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
        var tvNombreEjercicioAdmin : TextView = itemView.findViewById(R.id.tvNombreEjercicioAdmin)
        var btnEditarEjercicioAdmin : ImageButton = itemView.findViewById(R.id.btnEditarEjercicioAdmin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjercicioAdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ejercicio_admin,parent,false)
        return EjercicioAdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: EjercicioAdminViewHolder, position: Int) {
        val ejercicioAdmin = listaEjercicios[position]
        holder.tvNombreEjercicioAdmin.text = ejercicioAdmin.nombre

        holder.btnEditarEjercicioAdmin.setOnClickListener { onEditarClick (ejercicioAdmin) }
    }

    override fun getItemCount(): Int {
        return listaEjercicios.size
    }


}