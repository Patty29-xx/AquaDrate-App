package com.example.waterreminder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AllSetActivity : AppCompatActivity() {

    private lateinit var tvDailyIntakeGoal: TextView
    private lateinit var btnAddFirstDrink: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_set)

        tvDailyIntakeGoal = findViewById(R.id.tvDailyIntakeGoal)
        btnAddFirstDrink = findViewById(R.id.btnAddFirstDrink)

        // Retrieve gender and weight from intent extras
        val gender = intent.getStringExtra("gender") ?: ""
        val weight = intent.getFloatExtra("weight", 0f)
        val dailyWaterIntake = calculateWaterIntake(gender, weight)

        tvDailyIntakeGoal.text = "Daily Goal :  ${dailyWaterIntake.toInt()} ml"

        btnAddFirstDrink.setOnClickListener {
            val intent = Intent(this, HomePageActivity::class.java).apply {
                putExtra("gender", gender)
                putExtra("weight", weight)
                putExtra("dailyWaterIntake", dailyWaterIntake)
            }
            startActivity(intent)
        }
    }

    private fun calculateWaterIntake(gender: String, weight: Float): Float {
        return when (gender) {
            "Male" -> weight * 40 // Convert to ml
            "Female" -> weight * 30 // Convert to ml
            else -> 0f
        }
    }
}
