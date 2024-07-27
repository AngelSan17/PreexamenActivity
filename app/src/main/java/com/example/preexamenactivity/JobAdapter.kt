package com.example.preexamenactivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobAdapter(
    private val jobList: MutableList<Job>,
    private val onClick: (Job) -> Unit,
    private val onDelete: (Job) -> Unit
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>(), Filterable {

    private var jobListFiltered: MutableList<Job> = jobList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_element, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobListFiltered[position]
        holder.bind(job, onClick, onDelete)
    }

    override fun getItemCount(): Int {
        return jobListFiltered.size
    }

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val jobNameTextView: TextView = itemView.findViewById(R.id.jobNameTextView)
        private val jobCityTextView: TextView = itemView.findViewById(R.id.jobCityTextView)
        private val jobDescriptionTextView: TextView = itemView.findViewById(R.id.jobDescriptionTextView)
        private val btnDeleteJob: Button = itemView.findViewById(R.id.btnDeleteJob)

        fun bind(job: Job, onClick: (Job) -> Unit, onDelete: (Job) -> Unit) {
            jobNameTextView.text = job.name
            jobCityTextView.text = job.city
            jobDescriptionTextView.text = job.description
            itemView.setOnClickListener { onClick(job) }
            btnDeleteJob.setOnClickListener { onDelete(job) }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                jobListFiltered = if (charString.isEmpty()) jobList else {
                    val filteredList = jobList.filter {
                        it.name.contains(charString, true) ||
                                it.description.contains(charString, true) ||
                                it.city.contains(charString, true)
                    }
                    filteredList.toMutableList()
                }
                return FilterResults().apply { values = jobListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                jobListFiltered = if (results?.values == null)
                    mutableListOf()
                else
                    results.values as MutableList<Job>
                notifyDataSetChanged()
            }
        }
    }
}
