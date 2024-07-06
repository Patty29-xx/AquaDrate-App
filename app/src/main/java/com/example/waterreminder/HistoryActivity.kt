package com.example.waterreminder

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WaterIntakeAdapter
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recyclerView = findViewById(R.id.recyclerView)
        btnBack = findViewById(R.id.dropbtn)

        // Get the water intake records from the Intent
        val waterIntakeRecords = intent.getStringArrayListExtra("waterIntakeRecords") ?: arrayListOf()

        // Initialize the RecyclerView
        adapter = WaterIntakeAdapter(waterIntakeRecords)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnBack.setOnClickListener {
            finish() // Go back to HomePageActivity
        }
    }
}
