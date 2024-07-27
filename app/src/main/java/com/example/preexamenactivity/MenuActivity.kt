package com.example.preexamenactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    // Declaración de variables para los componentes de la interfaz de usuario
    private lateinit var tvWelcome: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnEditProfile: Button
    private lateinit var btnRecyclerView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Inicialización de los componentes de la interfaz de usuario
        tvWelcome = findViewById(R.id.tvWelcome)
        btnLogout = findViewById(R.id.btnLogout)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnRecyclerView = findViewById(R.id.btnRecyclerView)

        // Obtener los datos del usuario desde el intent
        val userId = intent.getStringExtra("id")
        val userName = intent.getStringExtra("name")
        val userEmail = intent.getStringExtra("email")
        val userPassword = intent.getStringExtra("password")
        val userPhone = intent.getStringExtra("phone")

        // Mostrar un mensaje de bienvenida con el nombre del usuario
        tvWelcome.text = "Bienvenido, $userName"

        // Configuración del botón de logout
        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Configuración del botón para editar el perfil
        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("id", userId)
            intent.putExtra("name", userName)
            intent.putExtra("email", userEmail)
            intent.putExtra("password", userPassword)
            intent.putExtra("phone", userPhone)
            startActivity(intent)
        }

        // Comentado: Configuración del botón para navegar a la actividad con RecyclerView
        // btnRecyclerView.setOnClickListener {
        //     val intent = Intent(this, RecyclerViewActivity::class.java)
        //     startActivity(intent)
        // }
    }
}
