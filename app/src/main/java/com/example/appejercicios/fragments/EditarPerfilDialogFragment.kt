package com.example.appejercicios.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.appejercicios.R

class EditarPerfilDialogFragment(
    private val tiNombres: String,
    private val tiApellidos: String,
    private val tiPeso: String,
    private val tiAltura: String,
    private val tiEdad: String,
    private val tiGenero: String,
    private val btnGuardarPerfil: (String, String, String, String, String, String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_editar_perfil, null)

        val tietPerfilNombres = view.findViewById<EditText>(R.id.tietPerfilNombres)
        val tietPerfilApellidos = view.findViewById<EditText>(R.id.tietPerfilApellidos)
        val tietPerfilPeso = view.findViewById<EditText>(R.id.tietPerfilPeso)
        val tietPerfilAltura = view.findViewById<EditText>(R.id.tietPerfilAltura)
        val tietPerfilEdad = view.findViewById<EditText>(R.id.tietPerfilEdad)
        val rgPerfilGenero = view.findViewById<RadioGroup>(R.id.rgPerfilGenero)
        val btnGuardarPerfil = view.findViewById<Button>(R.id.btnGuardarPerfil)

        tietPerfilNombres.setText(this.tiNombres)
        tietPerfilApellidos.setText(this.tiApellidos)
        tietPerfilPeso.setText(this.tiPeso)
        tietPerfilAltura.setText(this.tiAltura)
        tietPerfilEdad.setText(this.tiEdad)
        when (this.tiGenero) {
            "Masculino" -> rgPerfilGenero.check(R.id.rbMasculino)
            "Femenino" -> rgPerfilGenero.check(R.id.rbFemenino)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        btnGuardarPerfil.setOnClickListener {
            val nuevoNombres = tietPerfilNombres.text.toString()
            val nuevoApellidos = tietPerfilApellidos.text.toString()
            val nuevoPeso = tietPerfilPeso.text.toString()
            val nuevaAltura = tietPerfilAltura.text.toString()
            val nuevaEdad = tietPerfilEdad.text.toString()
            val nuevoGenero = when (rgPerfilGenero.checkedRadioButtonId) {
                R.id.rbMasculino -> "Masculino"
                else -> "Femenino"
            }

            btnGuardarPerfil(nuevoNombres, nuevoApellidos, nuevoPeso, nuevaAltura, nuevaEdad, nuevoGenero)
            dialog.dismiss()
        }

        return dialog
    }
}