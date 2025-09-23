package com.example.appejercicios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class registroActivity : AppCompatActivity() {

    var btnCancelar : Button ?= null;
    var btnRegistrar : Button ?= null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        btnCancelar = findViewById(R.id.btnCancelar)
        btnCancelar?.setOnClickListener() {
            cambioActivity()
        }

        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnRegistrar?.setOnClickListener() {
            cambioActivity()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun cambioActivity(){
        val intent =  Intent(this, loginActivity::class.java)
        startActivity(intent)
    }

}