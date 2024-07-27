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
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPhone: EditText
    private lateinit var btnSave: Button
    private lateinit var requestQueue: RequestQueue

    private val dirIp = "http://192.168.100.14/android1/"
    private val URL_UPDATE = "${dirIp}update.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        initUI()
        requestQueue = Volley.newRequestQueue(this)

        // Obtener los datos del usuario desde el intent
        val userId = intent.getStringExtra("id")
        val userName = intent.getStringExtra("name")
        val userEmail = intent.getStringExtra("email")
        val userPassword = intent.getStringExtra("password")
        val userPhone = intent.getStringExtra("phone")

        // Establecer los valores iniciales de los EditText
        etName.setText(userName)
        etEmail.setText(userEmail)
        etPassword.setText(userPassword)
        etPhone.setText(userPhone)

        btnSave.setOnClickListener {
            if (userId != null) {
                updateUser(URL_UPDATE, userId)
            } else {
                Toast.makeText(this, "User ID is missing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initUI() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etPhone = findViewById(R.id.etPhone)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun updateUser(url: String, id: String) {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.has("message")) {
                    Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                    finish()
                } else if (jsonResponse.has("error")) {
                    Toast.makeText(this, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = id
                if (name.isNotEmpty()) params["name"] = name
                if (email.isNotEmpty()) params["email"] = email
                if (password.isNotEmpty()) params["password"] = password
                if (phone.isNotEmpty()) params["phone"] = phone
                return params
            }
        }

        requestQueue.add(stringRequest)
    }
}
