package com.example.preexamenactivity

import android.app.Activity
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

// AddJobActivity: Actividad para añadir nuevos trabajos
class AddJobActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText
    private lateinit var etCity: EditText
    private lateinit var btnSave: Button
    private lateinit var requestQueue: RequestQueue

    private val dirIp = "http://192.168.100.14/android1/"
    private val URL_ADD_JOB = "${dirIp}add_job.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_job)

        etName = findViewById(R.id.etName)
        etDescription = findViewById(R.id.etDescription)
        etCity = findViewById(R.id.etCity)
        btnSave = findViewById(R.id.btnSave)
        requestQueue = Volley.newRequestQueue(this)

        // Listener para el botón de guardar trabajo
        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val city = etCity.text.toString().trim()

            if (name.isEmpty() || description.isEmpty() || city.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                addJob(name, description, city)
            }
        }
    }

    // addJob: Envía los datos del nuevo trabajo al servidor para agregarlo
    private fun addJob(name: String, description: String, city: String) {
        val stringRequest = object : StringRequest(Request.Method.POST, URL_ADD_JOB,
            Response.Listener { response ->
                if (response.trim() == "success") {
                    Toast.makeText(this, "Trabajo añadido correctamente", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "Error al añadir trabajo", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name
                params["description"] = description
                params["city"] = city
                return params
            }
        }

        requestQueue.add(stringRequest)
    }
}
