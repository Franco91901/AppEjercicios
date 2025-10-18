package com.example.appejercicios

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appejercicios.models.DiaEjercicio
import com.example.appejercicios.models.DiaRutina
import com.example.appejercicios.models.Ejercicio
import com.example.appejercicios.models.Rutina
import com.google.android.material.button.MaterialButton
import java.util.Calendar

class EjecutarRutinaActivity : AppCompatActivity() {

    private lateinit var tvNombreEjercicio: TextView
    private lateinit var tvInfo: TextView
    private lateinit var tvTemporizador: TextView
    private lateinit var mbAccion: MaterialButton
    private lateinit var mbVolver: MaterialButton

    private var rutina: Rutina? = null
    private var listaDiasRutina: List<DiaRutina> = listOf()
    private var listaDiaEjercicio: List<DiaEjercicio> = listOf()
    private var ejerciciosDelDia: List<DiaEjercicio> = listOf()

    private var ejercicioIndex = 0
    private var serieActual = 1
    private var temporizador: CountDownTimer? = null

    private val listaEjercicios = listOf(
        Ejercicio(101, "Flexiones", "Ejercicio clásico que trabaja pecho, hombros y tríceps.", "AMBAS", "FACIL"),
        Ejercicio(102, "Sentadillas", "Ejercicio base para piernas y glúteos.", "AMBAS", "FACIL"),
        Ejercicio(103, "Plancha", "Isométrico para core, espalda y hombros.", "TIEMPO", "MEDIA"),
        Ejercicio(104, "Burpees", "Burpees completos", "REPETICIONES", "DIFICIL")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejecutar_rutina)

        tvNombreEjercicio = findViewById(R.id.tvNombreEjercicio)
        tvInfo = findViewById(R.id.tvInfo)
        tvTemporizador = findViewById(R.id.tvTimer)
        mbAccion = findViewById(R.id.mbAccion)
        mbVolver = findViewById(R.id.mbVolver)

        mbVolver.setOnClickListener {
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }

        rutina = intent.getSerializableExtra("RUTINA_SELECCIONADA") as? Rutina
        @Suppress("UNCHECKED_CAST")
        listaDiasRutina = intent.getSerializableExtra("LISTA_DIAS_RUTINA") as? ArrayList<DiaRutina> ?: listOf()
        @Suppress("UNCHECKED_CAST")
        listaDiaEjercicio = intent.getSerializableExtra("LISTA_DIA_EJERCICIO") as? ArrayList<DiaEjercicio> ?: listOf()

        if (rutina == null) {
            Toast.makeText(this, "Rutina no recibida", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        prepararEjerciciosDelDia()
        mostrarEjercicioActual()
    }

    private fun prepararEjerciciosDelDia() {
        val diaSemanaActual = when (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> 1
            Calendar.TUESDAY -> 2
            Calendar.WEDNESDAY -> 3
            Calendar.THURSDAY -> 4
            Calendar.FRIDAY -> 5
            Calendar.SATURDAY -> 6
            Calendar.SUNDAY -> 7
            else -> 1
        }

        val diasHoy = listaDiasRutina.filter { it.idRutina == rutina!!.idRutina && it.diaSemana == diaSemanaActual }

        ejerciciosDelDia = diasHoy.flatMap { dia ->
            listaDiaEjercicio.filter { it.idDiaRutina == dia.idDiaRutina }
        }.sortedBy { it.ordenEjercicio }

        if (ejerciciosDelDia.isEmpty()) {
            Toast.makeText(this, "No hay ejercicios programados para hoy", Toast.LENGTH_LONG).show()
        }
    }

    private fun mostrarEjercicioActual() {
        temporizador?.cancel()
        tvTemporizador.text = ""

        if (ejercicioIndex >= ejerciciosDelDia.size) {
            // Rutina finalizada
            tvNombreEjercicio.text = "¡Rutina completada!"
            tvInfo.visibility = View.GONE
            mbAccion.text = "Finalizado"
            mbAccion.isEnabled = false
            mbAccion.visibility = View.GONE
            mbVolver.visibility = View.VISIBLE
            return
        }

        val ejercicio = ejerciciosDelDia[ejercicioIndex]
        // Si idEjercicio == 0, descanso
        val esDescansoEjercicio = ejercicio.idEjercicio == 0

        val nombreEjercicio = if (esDescansoEjercicio) {
            "Descanso"
        } else {
            listaEjercicios.find { it.idEjercicio == ejercicio.idEjercicio }?.nombre ?: "Ejercicio desconocido"
        }

        val tipoMedida = when {
            esDescansoEjercicio -> "TIEMPO"
            ejercicio.repeticiones != null -> "REPETICIONES"
            ejercicio.duracionSegundos != null -> "TIEMPO"
            else -> "REPETICIONES"
        }

        tvNombreEjercicio.text = if (esDescansoEjercicio) "Descanso" else "${nombreEjercicio}"
        tvInfo.visibility = View.VISIBLE
        tvInfo.text = when (tipoMedida) {
            "REPETICIONES" -> "${ejercicio.series} series de ${ejercicio.repeticiones} rep."
            else -> "${ejercicio.series} series de ${ejercicio.duracionSegundos} seg."
        }

        serieActual = 1

        when (tipoMedida) {
            "REPETICIONES" -> prepararRepeticiones(ejercicio)
            "TIEMPO" -> prepararTiempo(ejercicio)
        }
    }

    private fun prepararRepeticiones(ejercicio: DiaEjercicio) {
        mbAccion.isEnabled = true
        mbAccion.text = "Serie terminada (${serieActual}/${ejercicio.series})"

        mbAccion.setOnClickListener {
            if (serieActual < ejercicio.series) {
                serieActual++
                mbAccion.isEnabled = false
                iniciarTemporizador(ejercicio.descansoSerie) {
                    mbAccion.text = "Serie terminada (${serieActual}/${ejercicio.series})"
                    mbAccion.isEnabled = true
                }
            } else {
                siguienteEjercicio()
            }
        }
    }

    private fun prepararTiempo(ejercicio: DiaEjercicio) {
        mbAccion.isEnabled = false
        mbAccion.text = "En progreso..."

        fun ejecutarSerieActual() {
            val durSeg = ejercicio.duracionSegundos ?: 0
            iniciarTemporizador(durSeg) {
                if (serieActual < ejercicio.series) {
                    serieActual++
                    iniciarTemporizador(ejercicio.descansoSerie) {
                        ejecutarSerieActual()
                    }
                } else {
                    siguienteEjercicio()
                }
            }
        }
        ejecutarSerieActual()
    }

    private fun iniciarTemporizador(segundos: Int, onFinish: () -> Unit) {
        temporizador?.cancel()
        tvTemporizador.text = "${segundos+1}s"
        if (segundos <= 0) {
            tvTemporizador.text = "0s"
            onFinish()
            return
        }

        temporizador = object : CountDownTimer((segundos * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvTemporizador.text = "${(millisUntilFinished / 1000)+1}s"
            }

            override fun onFinish() {
                tvTemporizador.text = "0s"
                onFinish()
            }
        }.start()
    }

    private fun siguienteEjercicio() {
        temporizador?.cancel()
        ejercicioIndex++
        mostrarEjercicioActual()
    }

    override fun onDestroy() {
        temporizador?.cancel()
        super.onDestroy()
    }
}
