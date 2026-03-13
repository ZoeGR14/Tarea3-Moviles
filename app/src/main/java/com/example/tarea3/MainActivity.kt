package com.example.tarea3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class Message(
    val message: String
)

interface ApiService {
    @GET("/")
    suspend fun getMessage(): Message
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left + v.paddingLeft,
                systemBars.top + v.paddingTop,
                systemBars.right + v.paddingRight,
                systemBars.bottom + v.paddingBottom
            )
            insets
        }

        val txt = findViewById<TextView>(R.id.txtAPI)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarMain)
        val layoutBotones = findViewById<LinearLayout>(R.id.layoutBotones)
        val btnRegistrarse = findViewById<Button>(R.id.btnRegistrarse)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnReintentar = findViewById<Button>(R.id.btnReintentar)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        fun conectarApi() {
            txt.text = "Conectando con la API..."
            progressBar.visibility = View.VISIBLE
            layoutBotones.visibility = View.GONE
            btnReintentar.visibility = View.GONE

            lifecycleScope.launch {
                try {
                    val res = apiService.getMessage()
                    txt.text = res.message
                    
                    // Ocultar botones y carga
                    progressBar.visibility = View.GONE
                    layoutBotones.visibility = View.VISIBLE

                } catch (e: Exception) {
                    // Oculta carga y muestra errores + boton
                    progressBar.visibility = View.GONE
                    txt.text = "Error de conexión: ${e.message}"
                    btnReintentar.visibility = View.VISIBLE
                }
            }
        }

        conectarApi()

        btnReintentar.setOnClickListener {
            conectarApi()
        }

        btnRegistrarse.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}