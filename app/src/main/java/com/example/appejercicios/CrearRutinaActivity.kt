package com.example.appejercicios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appejercicios.adapters.DiaRutinaAdapter
import com.example.appejercicios.dialogs.EjercicioDialogFragment
import com.example.appejercicios.fragments.CategoriaDialogFragment
import com.example.appejercicios.fragments.DescansoDialogFragment
import com.example.appejercicios.fragments.ParametrosEjercicioDialogFragment
import com.example.appejercicios.models.*
import com.google.android.material.textfield.TextInputEditText

class CrearRutinaActivity : AppCompatActivity() {

    private lateinit var rvDiasRutina: RecyclerView
    private lateinit var btnCancelarRutina: Button
    private lateinit var btnGuardarRutina: Button
    private lateinit var etNombreRutina: TextInputEditText
    private lateinit var etDescripcionRutina: TextInputEditText

    private val listaDiasRutina = mutableListOf<DiaRutinaUI>()

    // LISTAS DE EJEMPLO
    private val listaCategorias = listOf(
        Categoria(1, "Pecho"),
        Categoria(2,"Brazos"),
        Categoria(3,"Espalda"),
        Categoria(4,"Piernas"),
        Categoria(5,"Abdominales"),
        Categoria(6,"Cardio")
    )

    private val listaEjercicios = listOf(
        Ejercicio(1, "Flexiones", "Ejercicio clásico que trabaja pecho, hombros y tríceps.", "AMBAS", "FACIL"),
        Ejercicio(2, "Flexiones declinadas", "Flexiones con pies elevados para mayor intensidad.", "AMBAS", "MEDIA"),
        Ejercicio(3, "Flexiones con palmada", "Variante explosiva de flexiones para potencia y coordinación.", "AMBAS", "DIFICIL"),
        Ejercicio(4, "Curl de bíceps con botella", "Ejercicio de bíceps usando botellas o mancuernas caseras.", "REPETICIONES", "FACIL"),
        Ejercicio(5, "Fondos en silla", "Trabaja tríceps y hombros usando una silla estable.", "REPETICIONES", "MEDIA"),
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

    val listaEjercicioCategoria = listOf(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_rutina)

        rvDiasRutina = findViewById(R.id.rvDiasRutina)
        btnCancelarRutina = findViewById(R.id.btnCancelarRutina)
        btnGuardarRutina = findViewById(R.id.btnGuardarRutina)
        etNombreRutina = findViewById(R.id.etNombreRutina)
        etDescripcionRutina = findViewById(R.id.etDescripcionRutina)

        inicializarDias()
        configurarRecycler()

        btnCancelarRutina.setOnClickListener {
            finish()
        }

        btnGuardarRutina.setOnClickListener {
            val nombre = etNombreRutina.text.toString().trim()
            val descripcion = etDescripcionRutina.text.toString().trim()

            if (nombre.isEmpty()) {
                etNombreRutina.error = "Ingresa un nombre"
                return@setOnClickListener
            }

            val nuevaRutina = Rutina(
                idRutina = listaDiasRutina.hashCode(),
                idUsuario = 1,
                nombreRutina = nombre,
                descripcion = descripcion.ifEmpty { "" }
            )

            val resultIntent = Intent()
            resultIntent.putExtra("rutina", nuevaRutina)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.clCrearRutina)) { v, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                maxOf(systemBars.bottom, imeInsets.bottom)
            )
            insets
        }
    }

    private fun inicializarDias() {
        val diasSemana = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
        diasSemana.forEach { dia ->
            listaDiasRutina.add(
                DiaRutinaUI(
                    diaSemana = dia,
                    ejercicios = mutableListOf()
                )
            )
        }
    }

    private fun configurarRecycler() {
        rvDiasRutina.layoutManager = LinearLayoutManager(this)
        val adapter = DiaRutinaAdapter(
            dias = listaDiasRutina,
            onAgregarEjercicio = { dia ->
                val nombresCategorias = listaCategorias.map { it.nombre }
                CategoriaDialogFragment(nombresCategorias) { categoriaSeleccionada ->

                    val ejerciciosFiltrados = listaEjercicios.filter { ejercicio ->
                        listaEjercicioCategoria.any { it.idEjercicio == ejercicio.idEjercicio && it.idCategoria == listaCategorias.first { cat -> cat.nombre == categoriaSeleccionada }.idCategoria }
                    }

                    EjercicioDialogFragment(ejerciciosFiltrados) { ejercicioSeleccionado ->
                        ParametrosEjercicioDialogFragment(ejercicioSeleccionado) { ejercicioConParametros ->
                            dia.ejercicios.add(ejercicioConParametros)
                            rvDiasRutina.adapter?.notifyItemChanged(listaDiasRutina.indexOf(dia))

                        }.show(supportFragmentManager, "ParametrosEjercicio")

                    }.show(supportFragmentManager, "Ejercicios")

                }.show(supportFragmentManager, "Categorias")
            },
            onAgregarDescanso = { dia ->
                DescansoDialogFragment { descanso ->
                    dia.ejercicios.add(descanso)
                    rvDiasRutina.adapter?.notifyItemChanged(listaDiasRutina.indexOf(dia))
                }.show(supportFragmentManager, "DescansoDialog")
            },
            onEditarEjercicio = { dia, ejercicio ->
                if (ejercicio.nombreEjercicio.equals("Descanso", ignoreCase = true)) {
                    DescansoDialogFragment(ejercicio) { descansoEditado ->
                        val lista = dia.ejercicios
                        val idx = lista.indexOf(ejercicio)
                        if (idx != -1) {
                            lista[idx] = descansoEditado
                            rvDiasRutina.adapter?.notifyItemChanged(listaDiasRutina.indexOf(dia))
                        }
                    }.show(supportFragmentManager, "EditarDescanso")
                } else {
                    ParametrosEjercicioDialogFragment(
                        ejercicioPlantilla = null,
                        ejercicioInicial = ejercicio
                    ) { ejercicioEditado ->
                        val lista = dia.ejercicios
                        val idx = lista.indexOf(ejercicio)
                        if (idx != -1) {
                            lista[idx] = ejercicioEditado
                            rvDiasRutina.adapter?.notifyItemChanged(listaDiasRutina.indexOf(dia))
                        }
                    }.show(supportFragmentManager, "EditarParametros")
                }
            }
        )
        rvDiasRutina.adapter = adapter
    }
}
