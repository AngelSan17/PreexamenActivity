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

class RecyclerViewActivity : AppCompatActivity() {

    private lateinit var jobAdapter: JobAdapter
    private lateinit var jobList: MutableList<Job>
    private lateinit var searchView: SearchView
    private lateinit var btnAddJob: Button

    companion object {
        const val ADD_JOB_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        jobList = mutableListOf(
            Job(1, "Software Developer", "Desarrollar aplicaciones móviles", "Ciudad de México"),
            Job(2, "Data Analyst", "Analizar datos de ventas", "Guadalajara"),
            Job(3, "Project Manager", "Gestionar proyectos de TI", "Monterrey")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)
        btnAddJob = findViewById(R.id.btnAddJob)

        jobAdapter = JobAdapter(jobList) { job ->
            Toast.makeText(this, "Unido a: ${job.name}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = jobAdapter

        btnAddJob.setOnClickListener {
            val intent = Intent(this, AddJobActivity::class.java)
            startActivityForResult(intent, ADD_JOB_REQUEST_CODE)
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                jobAdapter.filter.filter(newText)
                return false
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_JOB_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val newJob = data?.getSerializableExtra("newJob") as? Job
            if (newJob != null) {
                jobAdapter.addJob(newJob)
            }
        }
    }
}
