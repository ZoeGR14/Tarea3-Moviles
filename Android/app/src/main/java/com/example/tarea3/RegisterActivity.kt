package com.example.tarea3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

data class User(
    val username: String,
    val password: String
)


interface ApiRegister {
    @POST("/register")
    suspend fun registerUser(@Body request: User): Message
}

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
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

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val user = findViewById<EditText>(R.id.user)
        val pass = findViewById<EditText>(R.id.pass)
        val msg = findViewById<TextView>(R.id.mensaje)
        val btn = findViewById<Button>(R.id.btnRegistrar)
        val loadingOverlay = findViewById<FrameLayout>(R.id.loadingOverlay)

        btnBack.setOnClickListener {
            finish()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiRegister::class.java)

        btn.setOnClickListener {
            val usuario = user.text.toString().trim()
            val password = pass.text.toString().trim()

            if (usuario.isEmpty() || password.isEmpty()) {
                msg.text = "Por favor, completa todos los campos"
                msg.setTextColor(resources.getColor(R.color.red, theme))
                return@setOnClickListener
            }

            val datos = User(usuario, password)

            loadingOverlay.visibility = View.VISIBLE

            lifecycleScope.launch {
                try {
                    val res = api.registerUser(datos)
                    msg.text = "Éxito: ${res.message}"
                    msg.setTextColor(resources.getColor(R.color.azul, theme))
                    // Limpiar campos
                    user.text.clear()
                    pass.text.clear()
                } catch(e: HttpException){
                    val error = e.response()?.errorBody()?.string()
                    try {
                        val errorResponse = Gson().fromJson(error, Message::class.java)
                        msg.text = "Error API: ${errorResponse.message}"
                    } catch (jsonException: Exception) {
                        msg.text = "Error de servidor (${e.code()})"
                    }
                    msg.setTextColor(resources.getColor(R.color.red, theme))
                } catch (e: Exception){
                    msg.text = "Error de conexión: ${e.localizedMessage}"
                    msg.setTextColor(resources.getColor(R.color.red, theme))
                } finally {
                    loadingOverlay.visibility = View.GONE
                }
            }
        }
    }
}