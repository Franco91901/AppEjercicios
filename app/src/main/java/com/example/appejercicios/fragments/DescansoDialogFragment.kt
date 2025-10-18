package com.example.appejercicios.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.appejercicios.R
import com.example.appejercicios.models.DiaEjercicioUI
import com.google.android.material.textfield.TextInputEditText

class DescansoDialogFragment(
    private val descansoInicial: DiaEjercicioUI? = null,
    private val onDescansoListo: (DiaEjercicioUI) -> Unit
) : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.dialog_descanso, container, false)

        val tietDescanso = view.findViewById<TextInputEditText>(R.id.tietDescanso)
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregarDescanso)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelar)

        tietDescanso.setText(descansoInicial?.duracionSegundos?.toString() ?: "")

        btnAgregar.setOnClickListener {
            val segundos = tietDescanso.text.toString().toIntOrNull() ?: 0

            // "Ejercicio" especial que represente descanso
            val descanso = DiaEjercicioUI(
                nombreEjercicio = "Descanso",
                repeticiones = null,
                duracionSegundos = segundos,
                series = 1,
                descansoSerie = 0
            )
            onDescansoListo(descanso)
            dismiss()
        }

        btnCancelar.setOnClickListener { dismiss() }

        return view
    }
}
