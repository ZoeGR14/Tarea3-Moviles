package com.example.tarea3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_welcome)
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

        val userName = intent.getStringExtra("USER_NAME") ?: "Usuario"

        val welcomeUsername = findViewById<TextView>(R.id.welcomeUsername)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        welcomeUsername.text = "Usuario: ${userName}"

        btnLogout.setOnClickListener {
            // Volver a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // Limpiar el backstack para evitar que vuelvan al Welcome
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}