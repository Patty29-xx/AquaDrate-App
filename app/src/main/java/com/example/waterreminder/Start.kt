package com.example.waterreminder


import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Start : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_start)

        val startbtn = findViewById<Button>(R.id.start_btn)
        startbtn.setOnClickListener{
            val Intent = Intent(this, EnterDetailsActivity::class.java)
            startActivity(Intent)
        }

    }
}
