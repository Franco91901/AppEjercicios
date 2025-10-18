package com.example.appejercicios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appejercicios.fragments.EditarPerfilDialogFragment

class PerfilActivity : AppCompatActivity() {

    private lateinit var dlPerfil: DrawerLayout
    private lateinit var ivMenu: ImageButton
    private lateinit var nvMenu: NavigationView
    private lateinit var tvNombresUsuario: TextView
    private lateinit var tvApellidosUsuario: TextView
    private lateinit var tvEdadUsuario: TextView
    private lateinit var tvPesoUsuario: TextView
    private lateinit var tvAlturaUsuario: TextView
    private lateinit var tvGeneroUsuario: TextView
    private lateinit var tvValorIMC: TextView
    private lateinit var tvCategoriaIMC: TextView
    private lateinit var btnEditarPerfil: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        dlPerfil = findViewById(R.id.dlPerfil)
        ivMenu = findViewById(R.id.ivMenu)
        nvMenu = findViewById(R.id.nvMenu)
        tvPesoUsuario = findViewById(R.id.tvPesoUsuario)
        tvNombresUsuario = findViewById(R.id.tvNombresUsuario)
        tvApellidosUsuario = findViewById(R.id.tvApellidosUsuario)
        tvEdadUsuario = findViewById(R.id.tvEdadUsuario)
        tvGeneroUsuario = findViewById(R.id.tvGeneroUsuario)
        tvAlturaUsuario = findViewById(R.id.tvAlturaUsuario)
        tvValorIMC = findViewById(R.id.tvValorIMC)
        tvCategoriaIMC = findViewById(R.id.tvCategoriaIMC)
        btnEditarPerfil = findViewById(R.id.btnEditarPerfil)

        val peso = tvPesoUsuario.text.toString().replace("kg", "").trim().toDouble()
        val altura = tvAlturaUsuario.text.toString().replace("m", "").trim().toDouble()

        calcularIMC(peso, altura)

        ivMenu.setOnClickListener {
            dlPerfil.open()
        }

        nvMenu.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            dlPerfil.closeDrawers()

            when (menuItem.itemId) {
                R.id.itAdmin -> cambioAdminActivity()
                R.id.itMisRutinas -> cambioMRActivity()
                R.id.itBuscar -> cambioBuscarActivity()
                R.id.itPerfil -> cambioPerfilActivity()
                R.id.itLogout -> cambioLoginActivity()
            }
            true
        }

        btnEditarPerfil.setOnClickListener {
            val dialog = EditarPerfilDialogFragment(
                tiNombres = tvNombresUsuario.text.toString().trim(),
                tiApellidos = tvApellidosUsuario.text.toString().trim(),
                tiPeso = tvPesoUsuario.text.toString().replace("kg", "").trim(),
                tiAltura = tvAlturaUsuario.text.toString().replace("m", "").trim(),
                tiEdad = tvEdadUsuario.text.toString().trim(),
                tiGenero = tvGeneroUsuario.text.toString().trim()
            ) { nuevoNombres, nuevoApellidos, nuevoPeso, nuevaAltura, nuevaEdad, nuevoGenero ->

                tvNombresUsuario.text = "$nuevoNombres".trim()
                tvApellidosUsuario.text = "$nuevoApellidos".trim()
                tvEdadUsuario.text = "$nuevaEdad".trim()
                tvPesoUsuario.text = "$nuevoPeso kg".trim()
                tvAlturaUsuario.text = "$nuevaAltura m".trim()
                tvGeneroUsuario.text = "$nuevoGenero".trim()

                val peso = nuevoPeso.toDouble()
                val altura = nuevaAltura.toDouble()
                calcularIMC(peso, altura)
            }
            dialog.show(supportFragmentManager, "EditarPerfilDialog")
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dlPerfil)) { v, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, maxOf(systemBars.bottom, imeInsets.bottom))
            insets
        }

    }

    private fun cambioAdminActivity() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }
    private fun cambioMRActivity() {
        val intent = Intent(this, PrincipalActivity::class.java)
        startActivity(intent)
    }
    private fun cambioBuscarActivity() {
        val intent = Intent(this, BuscarRutinasActivity::class.java)
        startActivity(intent)
    }
    private fun cambioPerfilActivity() {
        val intent = Intent(this, PerfilActivity::class.java)
        startActivity(intent)
    }
    private fun cambioLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun calcularIMC(peso: Double, altura: Double) {
        val imc = peso / (altura * altura)
        tvValorIMC.text = String.format("%.1f", imc)

        val categoria = when {
            imc < 18.5 -> "Estás bajo de peso"
            imc < 25.0 -> "Estás saludable"
            imc < 30.0 -> "Estás con sobrepeso"
            imc < 40.0 -> "Estás obeso"
            else -> "Estás con obesidad de alto riesgo"}

        tvCategoriaIMC.text = categoria
    }
}



