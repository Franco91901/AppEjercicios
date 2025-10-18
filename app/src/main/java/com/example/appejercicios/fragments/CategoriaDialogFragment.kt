package com.example.appejercicios.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.R
import com.example.appejercicios.adapters.CategoriaAdapter

class CategoriaDialogFragment(
    private val categorias: List<String>,
    private val onCategoriaSeleccionada: (String) -> Unit
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_categorias, container, false)

        val rvCategorias = view.findViewById<RecyclerView>(R.id.rvCategorias)
        rvCategorias.layoutManager = GridLayoutManager(context, 2)
        rvCategorias.adapter = CategoriaAdapter(categorias) { categoria ->
            onCategoriaSeleccionada(categoria)
            dismiss()
        }

        return view
    }
}
