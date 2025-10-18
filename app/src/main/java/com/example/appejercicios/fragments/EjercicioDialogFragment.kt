package com.example.appejercicios.dialogs

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.R
import com.example.appejercicios.adapters.EjercicioAdapter
import com.example.appejercicios.models.Ejercicio

class EjercicioDialogFragment(
    private val ejercicios: List<Ejercicio>,
    private val onEjercicioSeleccionado: (Ejercicio) -> Unit
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_ejercicios, container, false)
        val rvEjercicios = view.findViewById<RecyclerView>(R.id.rvEjercicios)
        val etBuscar = view.findViewById<EditText>(R.id.etBuscar)
        val adapter = EjercicioAdapter(ejercicios) { ejercicio ->
            onEjercicioSeleccionado(ejercicio)
            dismiss()
        }
        rvEjercicios.layoutManager = LinearLayoutManager(context)
        rvEjercicios.adapter = adapter

        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filtrar(s.toString())
            }
        })

        return view
    }
}
