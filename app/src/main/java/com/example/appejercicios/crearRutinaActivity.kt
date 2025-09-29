package com.example.appejercicios

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class crearRutinaActivity : AppCompatActivity() {

    var btnCancelarRutina : Button ?= null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_rutina)

        expansionContraccion(
            R.id.headerLunes,
            R.id.contenedorLunes,
            R.id.ivExpandirContraerLunes
        )
        expansionContraccion(
            R.id.headerMartes,
            R.id.contenedorMartes,
            R.id.ivExpandirContraerMartes
        )
        expansionContraccion(
            R.id.headerMiercoles,
            R.id.contenedorMiercoles,
            R.id.ivExpandirContraerMiercoles
        )
        expansionContraccion(
            R.id.headerJueves,
            R.id.contenedorJueves,
            R.id.ivExpandirContraerJueves
        )
        expansionContraccion(
            R.id.headerViernes,
            R.id.contenedorViernes,
            R.id.ivExpandirContraerViernes
        )
        expansionContraccion(
            R.id.headerSabado,
            R.id.contenedorSabado,
            R.id.ivExpandirContraerSabado
        )
        expansionContraccion(
            R.id.headerDomingo,
            R.id.contenedorDomingo,
            R.id.ivExpandirContraerDomingo
        )

        btnCancelarRutina = findViewById(R.id.btnCancelarRutina)
        btnCancelarRutina?.setOnClickListener(){
            cambioActivity()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun cambioActivity(){
        val intent =  Intent(this, principalActivity::class.java)
        startActivity(intent)
    }

    private fun expansionContraccion(headerId: Int, contenedorId: Int, ivId: Int) {
        val header = findViewById<LinearLayout>(headerId)
        val contenedor = findViewById<LinearLayout>(contenedorId)
        val iv = findViewById<ImageView>(ivId)

        header?.setOnClickListener {
            if (contenedor.visibility == View.GONE) {
                contenedor.visibility = View.VISIBLE
                iv.setImageResource(R.drawable.ic_contraer)
            } else {
                contenedor.visibility = View.GONE
                iv.setImageResource(R.drawable.ic_expandir)
            }
        }
    }

}