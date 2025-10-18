package com.example.appejercicios

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.adapters.RutinaAdapter
import com.example.appejercicios.models.DiaEjercicio
import com.example.appejercicios.models.DiaRutina
import com.example.appejercicios.models.Rutina
import com.google.android.material.navigation.NavigationView

class PrincipalActivity : AppCompatActivity() {

    private var btnAgregarRutina: Button? = null
    private lateinit var rvRutinas: RecyclerView
    private lateinit var rutinaAdapter: RutinaAdapter
    private val listaRutinas = mutableListOf<Rutina>()
    private lateinit var dlPrincipal: DrawerLayout
    private lateinit var nvMenu: NavigationView
    private lateinit var ivMenu: ImageView
    private val listaDiasRutina = mutableListOf<DiaRutina>()
    private val listaDiaEjercicio = mutableListOf<DiaEjercicio>()

    private val rutinaLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val rutina = result.data?.getSerializableExtra("rutina") as? Rutina
            rutina?.let {
                listaRutinas.add(it)
                rutinaAdapter.notifyItemInserted(listaRutinas.size - 1)
                Toast.makeText(this, "Rutina '${it.nombreRutina}' añadida", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_principal)

        dlPrincipal = findViewById(R.id.dlPrincipal)
        nvMenu = findViewById(R.id.nvMenu)
        ivMenu = findViewById(R.id.ivMenu)

        ivMenu.setOnClickListener { dlPrincipal.open() }

        nvMenu.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            dlPrincipal.closeDrawers()

            when (menuItem.itemId) {
                R.id.itAdmin -> cambioAdminActivity()
                R.id.itMisRutinas -> cambioMRActivity()
                R.id.itBuscar -> cambioBuscarActivity()
                R.id.itPerfil -> cambioPerfilActivity()
                R.id.itLogout -> cambioLoginActivity()
            }
            true
        }

        btnAgregarRutina = findViewById(R.id.mbAgregarEjercicio)
        btnAgregarRutina?.setOnClickListener { cambioCrearActivity() }

        rvRutinas = findViewById(R.id.rvRutinas)
        rvRutinas.layoutManager = LinearLayoutManager(this)

        // Rutina de ejemplo
        val rutinaDelDia = Rutina(
            idRutina = 1,
            idUsuario = 1,
            nombreRutina = "Rutina de Ejemplo",
            descripcion = "Rutina mixta con descanso y ejercicios por tiempo"
        )

        listaRutinas.add(rutinaDelDia)

        val diaRutinaCambiable = DiaRutina(
            idDiaRutina = 1,
            idRutina = rutinaDelDia.idRutina,
            diaSemana = 6 // colocar dia
        )

        listaDiasRutina.add(diaRutinaCambiable)

        listaDiaEjercicio.addAll(
            listOf(
                DiaEjercicio(1, 1, 102, 3, 10, null, 5, 1),
                DiaEjercicio(2, 1, 0, 1, null, 10, 0, 2),
                DiaEjercicio(3, 1, 104, 2, 10, null, 5, 3)
            )
        )

        rutinaAdapter = RutinaAdapter(listaRutinas, listaDiasRutina, listaDiaEjercicio)
        rvRutinas.adapter = rutinaAdapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dlPrincipal)) { v, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, maxOf(systemBars.bottom, imeInsets.bottom))
            insets
        }
    }

    private fun cambioCrearActivity() {
        val intent = Intent(this, CrearRutinaActivity::class.java)
        rutinaLauncher.launch(intent)
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
