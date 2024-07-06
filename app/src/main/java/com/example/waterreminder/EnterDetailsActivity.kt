package com.example.waterreminder

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EnterDetailsActivity : AppCompatActivity() {

    private lateinit var rgGender: RadioGroup
    private lateinit var etWeight: EditText
    private lateinit var btnProceed: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_details)

        rgGender = findViewById(R.id.rgGender)
        etWeight = findViewById(R.id.etWeight)
        btnProceed = findViewById(R.id.btnProceed)

        btnProceed.setOnClickListener {
            val selectedGenderId = rgGender.checkedRadioButtonId
            val weight = etWeight.text.toString().toFloatOrNull()

            if (selectedGenderId == -1 || weight == null) {
                Toast.makeText(this, "Please select gender and enter weight", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedGender = findViewById<RadioButton>(selectedGenderId)
            val gender = selectedGender.text.toString()

            val intent = Intent(this, AllSetActivity::class.java).apply {
                putExtra("gender", gender)
                putExtra("weight", weight)
            }
            startActivity(intent)
        }
    }
}
