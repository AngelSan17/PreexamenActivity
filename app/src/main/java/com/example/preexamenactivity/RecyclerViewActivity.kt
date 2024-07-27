package com.example.preexamenactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var jobAdapter: JobAdapter
    private lateinit var jobList: MutableList<Job>
    private lateinit var searchView: SearchView
    private lateinit var btnAddJob: Button
    private lateinit var requestQueue: RequestQueue

    companion object {
        const val ADD_JOB_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        // Inicializar la lista y el requestQueue para manejar las solicitudes HTTP
        jobList = mutableListOf()
        requestQueue = Volley.newRequestQueue(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        btnAddJob = findViewById(R.id.btnAddJob)

        // Inicializar el adaptador del RecyclerView
        jobAdapter = JobAdapter(jobList, { job ->
            // Manejador para el clic en un trabajo
            Toast.makeText(this, "Unido a: ${job.name}", Toast.LENGTH_SHORT).show()
        }, { job ->
            // Manejador para eliminar un trabajo
            deleteJob(job)
        })

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = jobAdapter

        // Configurar el botón para agregar un nuevo trabajo
        btnAddJob.setOnClickListener {
            val intent = Intent(this, AddJobActivity::class.java)
            startActivityForResult(intent, ADD_JOB_REQUEST_CODE)
        }

        // Configurar el SearchView para filtrar la lista de trabajos
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                jobAdapter.filter.filter(newText)
                return false
            }
        })

        // Obtener trabajos desde el servidor
        getJobsFromServer()
    }

    private fun getJobsFromServer() {
        val url = "http://192.168.100.14/android1/get_jobs.php"
        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener { response ->
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val jobJson = jsonArray.getJSONObject(i)
                    val job = Job(
                        id = jobJson.getInt("id"),
                        name = jobJson.getString("name"),
                        description = jobJson.getString("description"),
                        city = jobJson.getString("city")
                    )
                    jobList.add(job)
                }
                jobAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            })

        requestQueue.add(stringRequest)
    }

    private fun deleteJob(job: Job) {
        val url = "http://192.168.100.14/android1/delete_job.php"
        val stringRequest = object : StringRequest(Request.Method.POST, url,
            Response.Listener { response ->
                val jsonResponse = JSONObject(response)
                if (jsonResponse.has("message")) {
                    Toast.makeText(this, jsonResponse.getString("message"), Toast.LENGTH_SHORT).show()
                    jobList.remove(job)
                    jobAdapter.notifyDataSetChanged()
                } else if (jsonResponse.has("error")) {
                    Toast.makeText(this, jsonResponse.getString("error"), Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = job.id.toString()
                return params
            }
        }

        requestQueue.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_JOB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Actualizar la lista de trabajos desde el servidor
            jobList.clear()
            getJobsFromServer()
        }
    }
}
