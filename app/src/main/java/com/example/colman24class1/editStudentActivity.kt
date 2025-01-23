package com.example.colman24class1

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import java.util.Calendar

class EditStudentActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student) // We can reuse the add_student layout

        val toolbar = findViewById<Toolbar>(R.id.edit_student_toolbar)
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

        val studentId = intent.getStringExtra("student_id")
        val student = studentId?.let { Student.getStudentById(it) }

        // Get references to views
        val nameTextField: EditText = findViewById(R.id.studentNameView)
        val idTextField: EditText = findViewById(R.id.studentIdView)
        val phoneNumberTextField: EditText = findViewById(R.id.studentPhoneView)
        val addressTextField: EditText = findViewById(R.id.studentAddressView)
        val checkedInput: CheckBox = findViewById(R.id.student_checked_input)
        val birthDateView : TextInputEditText= findViewById(R.id.studentBirthDateView)
        val birthTimeView:TextView = findViewById(R.id.studentBirthTimeView)

        // Populate fields with existing student data
        student?.let {
            nameTextField.setText(it.name)
            idTextField.setText(it.id)
            phoneNumberTextField.setText(it.phone)
            addressTextField.setText(it.address)
            birthDateView.setText(it.birthDate)
            birthTimeView.setText(it.birthTime)
            checkedInput.isChecked = it.isChecked
        }

        // Save button click handler
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val updatedStudent = Student(
                idTextField.text.toString(),
                nameTextField.text.toString(),
                phoneNumberTextField.text.toString(),
                addressTextField.text.toString(),
                birthDateView.text.toString(),
                birthTimeView.text.toString(),
                checkedInput.isChecked
            )

            if (studentId != null) {
                Student.updateStudent(studentId, updatedStudent)
                showSaveSuccessDialog(updatedStudent)
            }
        }

        birthDateView.setOnClickListener {
            showDatePicker()
        }

        birthTimeView.setOnClickListener {
            showTimePicker()
        }

         //Delete button click handler
        findViewById<Button>(R.id.delete_button).setOnClickListener {
            studentId?.let { id ->
                Student.deleteStudent(id)
                // Return to list screen
                val intent = Intent(this, StudentListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        // Cancel button click handler
        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
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

    private fun showSaveSuccessDialog(updatedStudent: Student) {
        val dialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Save Successful")
            .setMessage("The student details have been saved successfully.")
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("OK") { _, _ ->
                // Navigate to the StudentDetailsActivity after pressing OK
                val intent = Intent(this, StudentDetailsActivity::class.java)
                intent.putExtra("student_id", updatedStudent.id) // Pass the updated student ID
                startActivity(intent)
                finish() // Close the current activity
            }
            .create()

        dialog.show()
    }
}

