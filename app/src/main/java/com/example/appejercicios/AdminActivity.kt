package com.example.appejercicios

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.adapters.EjercicioAdminAdapter
import com.example.appejercicios.models.Categoria
import com.example.appejercicios.models.Ejercicio
import com.example.appejercicios.models.EjercicioCategoria
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView

class AdminActivity : AppCompatActivity() {

    private lateinit var dlAdmin: DrawerLayout
    private lateinit var nvMenu: NavigationView
    private lateinit var ivMenu : ImageView
    private lateinit var etBuscarEjercicioAdmin: EditText
    private lateinit var mbAgregarEjercicio : MaterialButton
    private lateinit var rvEjerciciosAdmin: RecyclerView
    private lateinit var ejercicioAdminAdapter: EjercicioAdminAdapter
    private var listaEjercicios = mutableListOf<Ejercicio>()
    private var listaFiltrada = mutableListOf<Ejercicio>()
    private var listaCategorias = mutableListOf<Categoria>()
    private var listaEjercicioCategoria = mutableListOf<EjercicioCategoria>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)

        dlAdmin = findViewById(R.id.dlAdmin)
        nvMenu = findViewById(R.id.nvMenu)
        ivMenu = findViewById(R.id.ivMenu)
        etBuscarEjercicioAdmin = findViewById(R.id.etBuscarEjercicioAdmin)
        mbAgregarEjercicio = findViewById(R.id.mbAgregarEjercicio)
        rvEjerciciosAdmin = findViewById(R.id.rvEjerciciosAdmin)

        ivMenu.setOnClickListener { dlAdmin.open() }

        nvMenu.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            dlAdmin.closeDrawers()
            when (menuItem.itemId) {
                R.id.itAdmin -> cambioAdminActivity()
                R.id.itMisRutinas -> cambioMRActivity()
                R.id.itBuscar -> cambioBuscarActivity()
                R.id.itPerfil -> cambioPerfilActivity()
                R.id.itLogout -> cambioLoginActivity()
            }
            true
        }

        mbAgregarEjercicio.setOnClickListener {
            mostrarDialogCrear()
        }

        cargarDatos()
        listaFiltrada.addAll(listaEjercicios)

        ejercicioAdminAdapter = EjercicioAdminAdapter(listaFiltrada) { ejercicio ->
            mostrarDialogEditar(ejercicio)
        }

        rvEjerciciosAdmin.layoutManager = LinearLayoutManager(this)
        rvEjerciciosAdmin.adapter = ejercicioAdminAdapter

        configurarBuscador()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dlAdmin)) { v, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, maxOf(systemBars.bottom, imeInsets.bottom))
            insets
        }
    }

    private fun configurarBuscador() {
        etBuscarEjercicioAdmin.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val texto = s.toString().trim().lowercase()
                val filtrado = if (texto.isEmpty()) listaEjercicios else listaEjercicios.filter { it.nombre.lowercase().contains(texto) }
                listaFiltrada.clear()
                listaFiltrada.addAll(filtrado)
                ejercicioAdminAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun mostrarDialogCrear() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_editar_ejercicio_admin, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombreEjercicio)
        val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcionEjercicio)
        val spTipoMedida = dialogView.findViewById<Spinner>(R.id.spTipoMedidaEjercicio)
        val spDificultad = dialogView.findViewById<Spinner>(R.id.spDificultadEjercicio)
        val layoutCategorias = dialogView.findViewById<LinearLayout>(R.id.layoutCategorias)

        val tipos = listOf("REPETICIONES", "TIEMPO", "AMBAS")
        spTipoMedida.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        val dificultades = listOf("FACIL", "MEDIA", "DIFICIL")
        spDificultad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dificultades)

        val categoriasSeleccionadas = mutableSetOf<Int>()
        layoutCategorias.removeAllViews()
        listaCategorias.forEach { categoria ->
            val checkBox = CheckBox(this)
            checkBox.text = categoria.nombre
            checkBox.isChecked = false
            layoutCategorias.addView(checkBox)

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    categoriasSeleccionadas.add(categoria.idCategoria)
                } else {
                    categoriasSeleccionadas.remove(categoria.idCategoria)
                }
            }
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Agregar Ejercicio")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoId = (listaEjercicios.maxOfOrNull { it.idEjercicio } ?: 0) + 1

                val nuevoEjercicio = Ejercicio(
                    idEjercicio = nuevoId,
                    nombre = etNombre.text.toString(),
                    descripcion = etDescripcion.text.toString(),
                    tipoMedida = spTipoMedida.selectedItem.toString(),
                    dificultad = spDificultad.selectedItem.toString()
                )

                listaEjercicios.add(nuevoEjercicio)
                listaFiltrada.add(nuevoEjercicio)

                categoriasSeleccionadas.forEach { idCat ->
                    listaEjercicioCategoria.add(EjercicioCategoria(nuevoId, idCat))
                }

                ejercicioAdminAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Ejercicio agregado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }


    private fun mostrarDialogEditar(ejercicio: Ejercicio) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_editar_ejercicio_admin, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombreEjercicio)
        val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcionEjercicio)
        val spTipoMedida = dialogView.findViewById<Spinner>(R.id.spTipoMedidaEjercicio)
        val spDificultad = dialogView.findViewById<Spinner>(R.id.spDificultadEjercicio)
        val cbPecho = dialogView.findViewById<CheckBox>(R.id.cbPecho)
        val cbBrazos = dialogView.findViewById<CheckBox>(R.id.cbBrazos)
        val cbEspalda = dialogView.findViewById<CheckBox>(R.id.cbEspalda)
        val cbPiernas = dialogView.findViewById<CheckBox>(R.id.cbPiernas)
        val cbAbdominales = dialogView.findViewById<CheckBox>(R.id.cbAbdominales)
        val cbCardio = dialogView.findViewById<CheckBox>(R.id.cbCardio)

        etNombre.setText(ejercicio.nombre)
        etDescripcion.setText(ejercicio.descripcion)

        val tipos = listOf("REPETICIONES", "TIEMPO", "AMBAS")
        spTipoMedida.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        spTipoMedida.setSelection(tipos.indexOf(ejercicio.tipoMedida))

        val dificultades = listOf("FACIL", "MEDIA", "DIFICIL")
        spDificultad.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dificultades)
        spDificultad.setSelection(dificultades.indexOf(ejercicio.dificultad))

        val categoriasSeleccionadas = listaEjercicioCategoria
            .filter { it.idEjercicio == ejercicio.idEjercicio }
            .map { it.idCategoria }
            .toMutableSet()

        cbPecho.isChecked = categoriasSeleccionadas.contains(1)
        cbBrazos.isChecked = categoriasSeleccionadas.contains(2)
        cbEspalda.isChecked = categoriasSeleccionadas.contains(3)
        cbPiernas.isChecked = categoriasSeleccionadas.contains(4)
        cbAbdominales.isChecked = categoriasSeleccionadas.contains(5)
        cbCardio.isChecked = categoriasSeleccionadas.contains(6)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Ejercicio")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->

                ejercicio.nombre = etNombre.text.toString()
                ejercicio.descripcion = etDescripcion.text.toString()
                ejercicio.tipoMedida = spTipoMedida.selectedItem.toString()
                ejercicio.dificultad = spDificultad.selectedItem.toString()

                val nuevasCategorias = mutableSetOf<Int>()
                if(cbPecho.isChecked) nuevasCategorias.add(1)
                if(cbBrazos.isChecked) nuevasCategorias.add(2)
                if(cbEspalda.isChecked) nuevasCategorias.add(3)
                if(cbPiernas.isChecked) nuevasCategorias.add(4)
                if(cbAbdominales.isChecked) nuevasCategorias.add(5)
                if(cbCardio.isChecked) nuevasCategorias.add(6)

                listaEjercicioCategoria.removeAll { it.idEjercicio == ejercicio.idEjercicio }
                nuevasCategorias.forEach { idCat -> listaEjercicioCategoria.add(EjercicioCategoria(ejercicio.idEjercicio, idCat)) }

                ejercicioAdminAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Ejercicio actualizado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun cargarDatos() {
        listaCategorias.addAll(
            listOf(
                Categoria(1, "Pecho"),
                Categoria(2,"Brazos"),
                Categoria(3,"Espalda"),
                Categoria(4,"Piernas"),
                Categoria(5,"Abdominales"),
                Categoria(6,"Cardio")
            )
        )

        listaEjercicios.addAll(
            listOf(
                Ejercicio(1, "Flexiones", "Ejercicio clásico que trabaja pecho, hombros y tríceps.", "AMBAS", "FACIL"),
                Ejercicio(2, "Flexiones declinadas", "Flexiones con pies elevados para mayor intensidad.", "AMBAS", "MEDIA"),
                Ejercicio(3, "Flexiones con palmada", "Variante explosiva de flexiones para potencia y coordinación.", "AMBAS", "DIFICIL"),
                Ejercicio(4, "Curl de bíceps con botella", "Ejercicio de bíceps usando botellas o mancuernas caseras.", "REPETICIONES", "FACIL"),
                Ejercicio(5, "Fondos en silla", "Trabaja tríceps y hombros usando una silla estable.", "REPETICIONES", "Media"),
                Ejercicio(6, "Flexiones diamante", "Flexiones cerradas centradas en tríceps y pecho interno.", "AMBAS", "DIFICIL"),
                Ejercicio(7, "Superman", "Extiende brazos y piernas para fortalecer la espalda baja.", "TIEMPO", "FACIL"),
                Ejercicio(8, "Remo invertido", "Tira del cuerpo hacia una superficie para espalda media.", "REPETICIONES", "MEDIA"),
                Ejercicio(9, "Dominadas", "Ejercicio exigente para espalda y brazos.", "REPETICIONES", "DIFICIL"),
                Ejercicio(10, "Sentadillas", "Ejercicio base para piernas y glúteos.", "AMBAS", "FACIL"),
                Ejercicio(11, "Zancadas", "Fortalece cuádriceps, glúteos y equilibrio.", "AMBAS", "MEDIA"),
                Ejercicio(12, "Sentadillas con salto", "Ejercicio explosivo para fuerza y potencia.", "AMBAS", "DIFICIL"),
                Ejercicio(13, "Abdominales", "Clásico ejercicio para abdomen superior.", "AMBAS", "FACIL"),
                Ejercicio(14, "Plancha", "Isométrico para core, espalda y hombros.", "TIEMPO", "MEDIA"),
                Ejercicio(15, "Elevaciones de piernas", "Fortalece abdomen inferior y flexores de cadera.", "REPETICIONES", "DIFICIL"),
                Ejercicio(16, "Jumping Jacks", "Ejercicio cardiovascular para calentar.", "TIEMPO", "FACIL"),
                Ejercicio(17, "Mountain Climbers", "Cardio que involucra core y piernas.", "TIEMPO", "MEDIA"),
                Ejercicio(18, "Burpees", "Ejercicio de cuerpo completo y alta intensidad.", "AMBAS", "DIFICIL"),
                Ejercicio(19, "Escaladores cruzados", "Versión cruzada de mountain climbers para abdomen y cardio.", "TIEMPO", "MEDIA"),
                Ejercicio(20, "Puente de glúteos", "Fortalece glúteos, espalda baja y piernas.", "REPETICIONES", "FACIL"),
                Ejercicio(21, "Shadow Boxing", "Cardio de boxeo al aire, mejora resistencia y coordinación.", "TIEMPO", "MEDIA")
            )
        )

        listaEjercicioCategoria.addAll(
            listOf(
                // PECHO
                EjercicioCategoria(1, 1),
                EjercicioCategoria(1, 2),
                EjercicioCategoria(2, 1),
                EjercicioCategoria(2, 2),
                EjercicioCategoria(3, 1),
                EjercicioCategoria(3, 2),

                // BRAZOS
                EjercicioCategoria(4, 2),
                EjercicioCategoria(5, 2),
                EjercicioCategoria(6, 2),
                EjercicioCategoria(6, 1),

                // ESPALDA
                EjercicioCategoria(7, 3),
                EjercicioCategoria(8, 3),
                EjercicioCategoria(9, 3),
                EjercicioCategoria(9, 2),

                // PIERNAS
                EjercicioCategoria(10, 4),
                EjercicioCategoria(11, 4),
                EjercicioCategoria(12, 4),
                EjercicioCategoria(20, 4),
                EjercicioCategoria(12, 6),

                // ABDOMINALES
                EjercicioCategoria(13, 5),
                EjercicioCategoria(14, 5),
                EjercicioCategoria(15, 5),
                EjercicioCategoria(19, 5),

                // CARDIO
                EjercicioCategoria(16, 6),
                EjercicioCategoria(17, 6),
                EjercicioCategoria(18, 6),
                EjercicioCategoria(19, 6),
                EjercicioCategoria(21, 6),
                EjercicioCategoria(18, 4)
            )
        )
    }

    private fun cambioAdminActivity() { startActivity(Intent(this, AdminActivity::class.java)) }
    private fun cambioMRActivity() { startActivity(Intent(this, PrincipalActivity::class.java)) }
    private fun cambioBuscarActivity() { startActivity(Intent(this, BuscarRutinasActivity::class.java)) }
    private fun cambioPerfilActivity() { startActivity(Intent(this, PerfilActivity::class.java)) }
    private fun cambioLoginActivity() { startActivity(Intent(this, LoginActivity::class.java)) }
}
