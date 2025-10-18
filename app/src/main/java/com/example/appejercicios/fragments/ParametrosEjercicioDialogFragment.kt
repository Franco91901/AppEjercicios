package com.example.appejercicios.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.appejercicios.R
import com.example.appejercicios.models.DiaEjercicioUI
import com.example.appejercicios.models.Ejercicio
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ParametrosEjercicioDialogFragment(
    private val ejercicioPlantilla: Ejercicio? = null,
    private val ejercicioInicial: DiaEjercicioUI? = null,
    private val onParametrosListos: (DiaEjercicioUI) -> Unit
) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.dialog_parametros_ejercicio, container, false)

        val tietSeries = view.findViewById<TextInputEditText>(R.id.tietSeries)
        val tietDescanso = view.findViewById<TextInputEditText>(R.id.tietDescanso)
        val tilValor = view.findViewById<TextInputLayout>(R.id.tilValor)
        val tietValor = view.findViewById<TextInputEditText>(R.id.tietValor)
        val switchModo = view.findViewById<SwitchMaterial>(R.id.switchModo)
        val btnAgregar = view.findViewById<Button>(R.id.btnAgregar)
        val btnVolver = view.findViewById<Button>(R.id.mbVolver)

        val nombreFinal = ejercicioInicial?.nombreEjercicio ?: ejercicioPlantilla?.nombre ?: ""

        if (nombreFinal.isEmpty()) {
            dismiss()
            return view
        }

        // Modo Edicion
        if (ejercicioInicial != null) {
            tietSeries.setText(ejercicioInicial.series.toString())
            tietDescanso.setText(ejercicioInicial.descansoSerie.toString())

            if (ejercicioInicial.repeticiones != null) {
                switchModo.isChecked = false
                tilValor.hint = "Repeticiones"
                tietValor.setText(ejercicioInicial.repeticiones.toString())
                switchModo.isEnabled = true
            } else {
                switchModo.isChecked = true
                tilValor.hint = "Segundos"
                tietValor.setText(ejercicioInicial.duracionSegundos?.toString() ?: "")
                switchModo.isEnabled = true
            }
        } else {
            // Modo crear
            when (ejercicioPlantilla?.tipoMedida) {
                "REPETICIONES" -> {
                    switchModo.isChecked = false
                    switchModo.isEnabled = false
                    tilValor.hint = "Repeticiones"
                }
                "TIEMPO" -> {
                    switchModo.isChecked = true
                    switchModo.isEnabled = false
                    tilValor.hint = "Segundos"
                }
                else -> {
                    switchModo.isEnabled = true
                    switchModo.isChecked = false
                    tilValor.hint = "Repeticiones"
                }
            }
        }

        // Cambiar según el switch
        switchModo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tilValor.hint = "Segundos"
                tietValor.text?.clear()
            } else {
                tilValor.hint = "Repeticiones"
                tietValor.text?.clear()
            }
        }

        btnAgregar.setOnClickListener {
            val series = tietSeries.text.toString().toIntOrNull() ?: 1
            val descansoSerie = tietDescanso.text.toString().toIntOrNull() ?: 0

            val repeticiones: Int?
            val duracionSegundos: Int?

            if (switchModo.isChecked) {
                // Modo tiempo
                repeticiones = null
                duracionSegundos = tietValor.text.toString().toIntOrNull() ?: 0
            } else {
                // Modo repeticiones
                repeticiones = tietValor.text.toString().toIntOrNull() ?: 0
                duracionSegundos = null
            }

            val ejercicioFinal = DiaEjercicioUI(
                nombreEjercicio = nombreFinal,
                repeticiones = repeticiones,
                duracionSegundos = duracionSegundos,
                series = series,
                descansoSerie = descansoSerie
            )

            onParametrosListos(ejercicioFinal)
            dismiss()
        }

        btnVolver.setOnClickListener { dismiss() }

        return view
    }
}
