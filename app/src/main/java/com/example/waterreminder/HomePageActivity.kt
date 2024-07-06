package com.example.waterreminder

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class HomePageActivity : AppCompatActivity() {

    private lateinit var etDrinkAmount: EditText
    private lateinit var btnAddDrink: Button
    private lateinit var btnPreset250ml: Button
    private lateinit var btnPreset350ml: Button
    private lateinit var btnPreset500ml: Button
    private lateinit var todayButton: Button
    private lateinit var btnUndo: Button
    private lateinit var btnViewHistory: Button
    private lateinit var tvDate: TextView
    private lateinit var tvRemainingWater: TextView
    private lateinit var progressWater: ProgressBar
    private lateinit var tvWaterRatio: TextView
    private var dailyWaterIntake: Float = 0f
    private var waterConsumed: Float = 0f
    private var goalCompletedDialog: AlertDialog? = null

    // Stack to keep track of water intake entries
    private val waterIntakeStack = Stack<Float>()

    // List to store water intake records
    private val waterIntakeRecords = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        etDrinkAmount = findViewById(R.id.etDrinkAmount)
        btnAddDrink = findViewById(R.id.btnAddDrink)
        btnPreset250ml = findViewById(R.id.btnPreset250ml)
        btnPreset350ml = findViewById(R.id.btnPreset350ml)
        btnPreset500ml = findViewById(R.id.btnPreset500ml)
        todayButton = findViewById(R.id.dropbtn) // Initialize the todayButton
        btnUndo = findViewById(R.id.btnUndo)
        btnViewHistory = findViewById(R.id.btnViewHistory)
        tvDate = findViewById(R.id.tvDate)
        tvRemainingWater = findViewById(R.id.tvRemainingWater)
        progressWater = findViewById(R.id.progressWater)
        tvWaterRatio = findViewById(R.id.tvWaterRatio)

        // Display current date
        val currentDate = getCurrentDate()
        tvDate.text = currentDate

        // Retrieve daily water intake from intent extras
        dailyWaterIntake = intent.getFloatExtra("dailyWaterIntake", 0f)
        tvRemainingWater.text = "Remaining water to drink: ${dailyWaterIntake.toInt()} ml"

        btnAddDrink.setOnClickListener {
            val drinkAmount = etDrinkAmount.text.toString().toFloatOrNull()

            if (drinkAmount == null || drinkAmount <= 0) {
                showErrorMessage("Please enter a valid amount of water")
                return@setOnClickListener
            }

            logDrink(drinkAmount)
        }

        btnPreset250ml.setOnClickListener {
            addPresetAmount(250f)
        }

        btnPreset350ml.setOnClickListener {
            addPresetAmount(350f)
        }

        btnPreset500ml.setOnClickListener {
            addPresetAmount(500f)
        }

        btnUndo.setOnClickListener {
            undoLastDrink()
        }

        btnViewHistory.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            intent.putStringArrayListExtra("waterIntakeRecords", waterIntakeRecords)
            startActivity(intent)
        }
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun logDrink(drinkAmount: Float) {
        waterIntakeStack.push(drinkAmount)
        waterConsumed += drinkAmount
        saveWaterIntakeRecord(drinkAmount)
        updateRemainingWater()
    }

    private fun undoLastDrink() {
        if (waterIntakeStack.isNotEmpty()) {
            val lastDrink = waterIntakeStack.pop()
            waterConsumed -= lastDrink
            removeLastWaterIntakeRecord()  // Remove the last water intake record
            updateRemainingWater()
        } else {
            showErrorMessage("No drinks to undo")
        }
    }

    private fun addPresetAmount(amount: Float) {
        logDrink(amount)
    }

    private fun updateRemainingWater() {
        val remainingWater = dailyWaterIntake - waterConsumed

        // Ensure remainingWater doesn't go below 0
        val displayRemainingWater = if (remainingWater > 0) remainingWater else 0f
        tvRemainingWater.text = "Remaining water to drink: ${displayRemainingWater.toInt()} ml"
        updateProgress()
    }

    private fun updateProgress() {
        val progress = (waterConsumed / dailyWaterIntake * 100).toInt()
        progressWater.progress = progress

        // Update water ratio
        val builder = SpannableStringBuilder()
        builder.append(waterConsumed.toInt().toString())
        builder.setSpan(
            StyleSpan(Typeface.NORMAL),
            0,
            builder.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        builder.setSpan(
            RelativeSizeSpan(5.25f),
            0,
            builder.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Append " ml" with smaller text size
        val mlText = " ml"
        builder.append(mlText)
        builder.setSpan(
            RelativeSizeSpan(2.5f),
            builder.length - mlText.length,
            builder.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the SpannableString to TextView
        tvWaterRatio.text = builder

        // Check if goal is met or exceeded
        if (waterConsumed >= dailyWaterIntake) {
            if (waterConsumed <= dailyWaterIntake * 1.1) { // Check if exceeded by up to 10%
                showGoalCompletedDialog()
            } else {
                showExceededMessage() // Exceeded by more than 10%
            }
        }
    }

    private fun showGoalCompletedDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setView(R.layout.layout_completed_goal)
            .setPositiveButton("OK", null)
        goalCompletedDialog = builder.create()
        goalCompletedDialog?.show()

        // Set the dialog window size
        goalCompletedDialog?.window?.setLayout(360.dpToPx(), 440.dpToPx())
    }

    // Extension function to convert dp to pixels
    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun showExceededMessage() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("You have exceeded your daily water intake goal!")
            .setPositiveButton("OK", null)
            .create()
            .show()
    }

    private fun showErrorMessage(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
            .setPositiveButton("OK", null)
            .create()
            .show()
    }

    private fun saveWaterIntakeRecord(drinkAmount: Float) {
        val currentDate = getCurrentDate()
        val record = "$currentDate: $drinkAmount ml"
        waterIntakeRecords.add(record)
    }

    private fun removeLastWaterIntakeRecord() {
        if (waterIntakeRecords.isNotEmpty()) {
            waterIntakeRecords.removeAt(waterIntakeRecords.size - 1)
        }
    }
}
