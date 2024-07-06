package com.example.waterreminder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WaterIntakeAdapter(private val waterIntakeRecords: List<String>) : RecyclerView.Adapter<WaterIntakeAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_water_intake, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = waterIntakeRecords[position].split(": ")
        holder.tvDate.text = record[0]
        holder.tvAmount.text = record[1]
    }

    override fun getItemCount(): Int {
        return waterIntakeRecords.size
    }
}
