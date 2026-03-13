package com.example.tarea3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class MessageLogin(
    val message: String,
    val status: String,
    val user_id: Int,
    val username: String
)

interface ApiLogin {
    @POST("/login")
    suspend fun login(@Body request: User): MessageLogin
}

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
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

        val user = findViewById<EditText>(R.id.user)
        val pass = findViewById<EditText>(R.id.pass)
        val msg = findViewById<TextView>(R.id.mensaje)
        val btn = findViewById<Button>(R.id.btnIniciarSesion)
        val loadingOverlay = findViewById<FrameLayout>(R.id.loadingOverlay)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiLogin::class.java)

        btn.setOnClickListener {
            val usuario = user.text.toString().trim()
            val password = pass.text.toString().trim()

            // Validación de campos
            if (usuario.isEmpty() || password.isEmpty()) {
                msg.text = "Por favor, completa todos los campos"
                msg.setTextColor(resources.getColor(R.color.red, theme))
                return@setOnClickListener
            }

            loadingOverlay.visibility = View.VISIBLE
            val datos = User(usuario, password)

            lifecycleScope.launch {
                try {
                    val res = api.login(datos)

                    // Si el login es exitoso, vamos a la bienvenida
                    val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
                    intent.putExtra("USER_NAME", res.username)
                    startActivity(intent)
                    finish()

                } catch (e: HttpException) {
                    val error = e.response()?.errorBody()?.string()
                    msg.text = "Credenciales inválidas (${e.code()})"
                    msg.setTextColor(resources.getColor(R.color.red, theme))
                } catch (e: Exception) {
                    msg.text = "Error de conexión: ${e.localizedMessage}"
                    msg.setTextColor(resources.getColor(R.color.red, theme))
                } finally {
                    loadingOverlay.visibility = View.GONE
                }
            }
        }
    }
}