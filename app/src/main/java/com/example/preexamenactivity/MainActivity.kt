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
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
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
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "LOS DATOS NO ESTÁN COMPLETOS...!!!", Toast.LENGTH_SHORT).show()
                etEmail.requestFocus()
            } else {
                validarUsuario(URL_LOGIN)
            }
        }

        tvRegister.setOnClickListener {
            // Ir a la actividad de registro
            val intent = Intent(this, RegisterActivity2::class.java)
            startActivity(intent)
        }
    }

    private fun initUI() {
        etEmail = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)
    }

    private fun validarUsuario(url: String) {
        val email = etEmail.text.toString().trim()
        val pass = etPassword.text.toString().trim()

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.getString("status") == "success") {
                    val intent = Intent(this@MainActivity, MenuActivity::class.java)
                    intent.putExtra("id", jsonResponse.getString("id"))
                    intent.putExtra("name", jsonResponse.getString("name"))
                    intent.putExtra("email", email)
                    intent.putExtra("password", pass)
                    intent.putExtra("phone", jsonResponse.getString("phone"))
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                params["password"] = pass
                return params
            }
        }

        requestQueue.add(stringRequest)
    }
}
