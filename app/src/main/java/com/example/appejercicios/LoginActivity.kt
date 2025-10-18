package com.example.appejercicios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appejercicios.data.dao.UsuarioDAO
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var tilCorreo: TextInputLayout
    private lateinit var tietCorreo: TextInputEditText
    private lateinit var tilContrasenia: TextInputLayout
    private lateinit var tietContrasenia: TextInputEditText
    private lateinit var btnAcceder: Button
    private lateinit var tvRegistro: TextView
    private lateinit var usuarioDAO: UsuarioDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        usuarioDAO = UsuarioDAO(this)

        // Referencias UI
        tietCorreo = findViewById(R.id.tietCorreo)
        tietContrasenia = findViewById(R.id.tietContrasenia)
        tilCorreo = findViewById(R.id.tilCorreo)
        tilContrasenia = findViewById(R.id.tilContrasenia)
        btnAcceder = findViewById(R.id.btnAcceder)
        tvRegistro = findViewById(R.id.tvRegistro)

        btnAcceder.setOnClickListener {
            validarCampos()
        }

        tvRegistro.setOnClickListener {
            cambioRegistroActivity()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clCrearRutina)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarCampos() {
        val correo = tietCorreo.text.toString().trim()
        val contrasenia = tietContrasenia.text.toString().trim()
        var error = false

        if (correo.isEmpty()) {
            tilCorreo.error = "Ingrese un correo"
            error = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            tilCorreo.error = "Correo inválido"
            error = true
        } else {
            tilCorreo.error = null
        }

        if (contrasenia.isEmpty()) {
            tilContrasenia.error = "Ingrese contraseña"
            error = true
        } else {
            tilContrasenia.error = null
        }

        if (error) return

        val usuario = usuarioDAO.login(correo, contrasenia)

        if (usuario != null) {
            Toast.makeText(this, "Bienvenido ${usuario.nombres}", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, PrincipalActivity::class.java))
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cambioRegistroActivity() {
        val intent = Intent(this, RegistroActivity::class.java)
        startActivity(intent)
    }
}
