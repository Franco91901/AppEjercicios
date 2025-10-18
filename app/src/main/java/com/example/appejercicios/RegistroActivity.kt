package com.example.appejercicios

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistroActivity : AppCompatActivity() {

    private lateinit var btnCancelar: Button
    private lateinit var btnRegistrar: Button
    private lateinit var rgIdentificacion: RadioGroup
    private lateinit var rbDNI: RadioButton
    private lateinit var rbCE: RadioButton
    private lateinit var tilIdentificacion: TextInputLayout
    private lateinit var tietIdentificacion: TextInputEditText
    private lateinit var tietPeso: TextInputEditText
    private lateinit var tietAltura: TextInputEditText
    private lateinit var tvIMC: TextView
    private lateinit var tvCategoria: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        btnCancelar = findViewById(R.id.btnCancelar)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        rgIdentificacion = findViewById(R.id.rgIdentificacion)
        rbDNI = findViewById(R.id.rbDNI)
        rbCE = findViewById(R.id.rbCE)
        tilIdentificacion = findViewById(R.id.tilIdentificacion)
        tietIdentificacion = findViewById(R.id.tietIdentificacion)
        tietPeso = findViewById(R.id.tietPeso)
        tietAltura = findViewById(R.id.tietAltura)
        tvIMC = findViewById(R.id.tvIMC)
        tvCategoria = findViewById(R.id.tvCategoria)

        btnCancelar.setOnClickListener {
            cambioActivity()
        }

        btnRegistrar.setOnClickListener {
            if (validarIdentificacion()) {
                Toast.makeText(this, "Registro válido", Toast.LENGTH_SHORT).show()
                cambioActivity()
            } else {
                Toast.makeText(this, "Corrige el campo de Identificación", Toast.LENGTH_SHORT).show()
            }
        }

        tietIdentificacion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validarIdentificacion()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        rgIdentificacion.setOnCheckedChangeListener { _, _ ->
            validarIdentificacion()
        }

        val imcWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { actualizarIMC() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        tietPeso.addTextChangedListener(imcWatcher)
        tietAltura.addTextChangedListener(imcWatcher)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clCrearRutina)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cambioActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun validarIdentificacion(): Boolean {
        val texto = tietIdentificacion.text.toString().trim()
        var longitudEsperada = 0

        when {
            rbDNI.isChecked == true -> longitudEsperada = 8
            rbCE.isChecked == true -> longitudEsperada = 9
        }

        return if (longitudEsperada > 0) {
            tilIdentificacion.counterMaxLength = longitudEsperada

            if (texto.length == longitudEsperada && texto.all { it.isDigit() }) {
                tilIdentificacion.error = null
                true
            } else {
                tilIdentificacion.error = "Debe tener exactamente $longitudEsperada dígitos"
                false
            }
        } else {
            tilIdentificacion.counterMaxLength = 9
            tilIdentificacion.error = "Seleccione un tipo de documento"
            false
        }
    }

    private fun actualizarIMC() {
        val peso = tietPeso.text.toString().toDoubleOrNull()
        val altura = tietAltura.text.toString().toDoubleOrNull()

        if (peso != null && altura != null && altura > 0) {
            val imc = peso / (altura * altura)
            tvIMC.text = "IMC: ${String.format("%.1f", imc)}"

            val categoria = when {
                imc < 18.5 -> "Estás bajo de peso"
                imc < 25.0 -> "Estás saludable"
                imc < 30.0 -> "Estás con sobrepeso"
                imc < 40.0 -> "Estás obeso"
                else -> "Estás con obesidad de alto riesgo"
            }
            tvCategoria.text = "Categoría: $categoria"
        } else {
            tvIMC.text = "IMC: -"
            tvCategoria.text = "Categoría: -"
        }
    }
}
