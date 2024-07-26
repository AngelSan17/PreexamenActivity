package com.example.preexamenactivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var requestQueue: RequestQueue

    private val dirIp = "http://192.168.100.14/android1/"
    private val URL_LOGIN = "${dirIp}login.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
        requestQueue = Volley.newRequestQueue(this)

        btnLogin.setOnClickListener {
            val user = etUsername.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "LOS DATOS NO ESTÁN COMPLETOS...!!!", Toast.LENGTH_SHORT).show()
                etUsername.requestFocus()
            } else {
                validarUsuario(URL_LOGIN)
            }
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun initUI() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
    }

    private fun validarUsuario(url: String) {
        val user = etUsername.text.toString().trim()
        val pass = etPassword.text.toString().trim()

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                if (response.trim() == "success") {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    // Navegar a otra actividad o pantalla
                } else {
                    Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = user
                params["password"] = pass
                return params
            }
        }

        requestQueue.add(stringRequest)
    }
}
