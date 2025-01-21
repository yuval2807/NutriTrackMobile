package com.example.colman24class1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar

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
        
        // Populate fields with existing student data
        student?.let {
            nameTextField.setText(it.name)
            idTextField.setText(it.id)
            phoneNumberTextField.setText(it.phone)
            addressTextField.setText(it.address)
            checkedInput.isChecked = it.isChecked
        }

        // Save button click handler
        findViewById<Button>(R.id.saveButton).setOnClickListener {
            val updatedStudent = Student(
                idTextField.text.toString(),
                nameTextField.text.toString(),
                phoneNumberTextField.text.toString(),
                addressTextField.text.toString(),
                checkedInput.isChecked
            )

            if (studentId != null) {
                Student.updateStudent(studentId, updatedStudent)
                showSaveSuccessDialog(updatedStudent)
            }
        }

        // Delete button click handler
//        findViewById<Button>(R.id.deleteButton).setOnClickListener {
//            studentId?.let { id ->
//                Student.deleteStudent(id)
//                // Return to list screen
//                val intent = Intent(this, StudentListActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }

        // Cancel button click handler
        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish() // Handle toolbar back button
        return true
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
