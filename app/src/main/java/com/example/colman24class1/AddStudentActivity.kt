package com.example.colman24class1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.activity.enableEdgeToEdge
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.setPadding
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar


class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.add_user_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // Enable the back button
        }

        // Handle back button press using OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Perform any custom logic here if needed
                finish() // Close the activity
            }
        })

        val cancelButton: Button = findViewById<Button>(R.id.cancelButton)
        val createButton: Button = findViewById<Button>(R.id.create_button)
        val nameTextField: TextView = findViewById(R.id.name_input)
        val idTextField: TextView = findViewById(R.id.id_input)
        val phoneNumberTextField: TextView = findViewById(R.id.phone_num_input)
        val addressTextField: TextView = findViewById(R.id.address_input)
        val checkedInput: CheckBox = findViewById(R.id.user_checked_input)
        val birthDateView : TextInputEditText = findViewById(R.id.studentBirthDateView)
        val birthTimeView:TextView = findViewById(R.id.studentBirthTimeView)

        birthDateView.setOnClickListener {
            showDatePicker()
        }

        // Setup time picker
        birthTimeView.setOnClickListener {
            showTimePicker()
        }

        cancelButton.setOnClickListener {
            val intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)
        }

        createButton.setOnClickListener {
            val newStudent: Student =
                Student(idTextField.text.toString(),
                    nameTextField.text.toString(),
                    phoneNumberTextField.text.toString(),
                    addressTextField.text.toString(),
                    birthDateView.text.toString(),
                    birthTimeView.text.toString(),
                    checkedInput.isChecked)

            Student.addStudent(newStudent)
            val intent = Intent(this, StudentListActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Handle toolbar back button
        return true
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val birthDateView = findViewById<TextInputEditText>(R.id.studentBirthDateView)

        DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                birthDateView.setText(selectedDate)
            },
            year,
            month,
            day
        ).show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val birthTimeView = findViewById<TextView>(R.id.studentBirthTimeView)

        TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                birthTimeView.text = selectedTime
            },
            hour,
            minute,
            true // 24-hour format
        ).show()
    }
}