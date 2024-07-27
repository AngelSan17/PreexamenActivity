package com.example.preexamenactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var tvWelcome: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnEditProfile: Button
    private lateinit var btnRecyclerView: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        tvWelcome = findViewById(R.id.tvWelcome)
        btnLogout = findViewById(R.id.btnLogout)
        btnEditProfile = findViewById(R.id.btnEditProfile)
        btnRecyclerView = findViewById(R.id.btnRecyclerView)

        val userId = intent.getStringExtra("id")
        val userName = intent.getStringExtra("name")
        val userEmail = intent.getStringExtra("email")
        val userPassword = intent.getStringExtra("password")
        val userPhone = intent.getStringExtra("phone")

        tvWelcome.text = "Bienvenido, $userName"

        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("id", userId)
            intent.putExtra("name", userName)
            intent.putExtra("email", userEmail)
            intent.putExtra("password", userPassword)
            intent.putExtra("phone", userPhone)
            startActivity(intent)
        }

        //btnRecyclerView.setOnClickListener {
        // Navegar a la actividad con RecyclerView
        //val intent = Intent(this, RecyclerViewActivity::class.java)
        //startActivity(intent)
        //}
    }
}
