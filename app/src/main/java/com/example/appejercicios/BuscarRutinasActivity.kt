package com.example.appejercicios

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.adapters.RutinaYTAdapter
import com.example.appejercicios.models.RutinaUI
import com.google.android.material.navigation.NavigationView

class BuscarRutinasActivity : AppCompatActivity() {

    private lateinit var rvRutinasYT : RecyclerView
    private lateinit var dlBuscar : DrawerLayout
    private lateinit var nvMenu : NavigationView
    private lateinit var ivMenu : ImageView

    private val listaRutinasUI= mutableListOf(
        RutinaUI("Rutina Piernas", "GymVirtual", "Rutina intensa para trabajar todo el cuerpo en 30 minutos", R.drawable.ic_perfil),
        RutinaUI("Rutina Pecho", "Athlean X", "Rutina ligera para trabajar todo el cuerpo en 10 minutos", R.drawable.ic_launcher_foreground)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buscar_rutinas)

        dlBuscar = findViewById(R.id.dlBuscar)
        nvMenu = findViewById(R.id.nvMenu)
        ivMenu = findViewById(R.id.ivMenu)

        ivMenu.setOnClickListener {
            dlBuscar.open()
        }

        nvMenu.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            dlBuscar.closeDrawers()

            // Maneja las selecciones
            when (menuItem.itemId) {
                R.id.itAdmin -> cambioAdminActivity()
                R.id.itMisRutinas -> cambioMRActivity()
                R.id.itBuscar -> cambioBuscarActivity()
                R.id.itPerfil -> cambioPerfilActivity()
                R.id.itLogout -> cambioLoginActivity()
            }
            true
        }


        rvRutinasYT = findViewById(R.id.rvRutinasYT)
        rvRutinasYT.layoutManager = LinearLayoutManager(this)
        rvRutinasYT.adapter = RutinaYTAdapter(listaRutinasUI) { rutina ->
            Toast.makeText(this, "Seleccionaste: ${rutina.nombreRutina}", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dlBuscar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
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

}