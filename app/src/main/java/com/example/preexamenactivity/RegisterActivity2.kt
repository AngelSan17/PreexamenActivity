package com.example.preexamenactivity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RegisterActivity2 : AppCompatActivity() {

    // Declaración de variables para los componentes de la interfaz de usuario
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnRegister: Button
    private lateinit var requestQueue: RequestQueue

    // URL del servidor para el registro de usuario
    private val dirIp = "http://192.168.100.14/android1/"
    private val URL_REGISTER = "${dirIp}register.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)

        // Inicialización de la interfaz de usuario
        initUI()
        requestQueue = Volley.newRequestQueue(this)

        // Configuración del botón de registro
        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val phone = etPhone.text.toString().trim()

            // Validación de campos vacíos
            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "LOS DATOS NO ESTÁN COMPLETOS...!!!", Toast.LENGTH_SHORT).show()
            } else {
                // Llamada a la función para registrar el usuario
                registrarUsuario(URL_REGISTER)
            }
        }
    }

    // Función para inicializar los componentes de la interfaz de usuario
    private fun initUI() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPhone = findViewById(R.id.etPhone)
        btnRegister = findViewById(R.id.btnRegister)
    }

    // Función para registrar el usuario en el servidor
    private fun registrarUsuario(url: String) {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        // Solicitud HTTP POST usando Volley
        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                // Mostrar mensaje de respuesta del servidor
                Toast.makeText(this, response, Toast.LENGTH_SHORT).show()
                // Navegar de vuelta a la actividad de login
                finish()
            },
            Response.ErrorListener { error ->
                // Manejar errores de la solicitud
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            // Configuración de los parámetros de la solicitud POST
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name
                params["email"] = email
                params["password"] = password
                params["phone"] = phone
                return params
            }
        }

        // Añadir la solicitud a la cola
        requestQueue.add(stringRequest)
    }
}
