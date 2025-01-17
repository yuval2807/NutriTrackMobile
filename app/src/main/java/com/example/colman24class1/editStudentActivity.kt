package com.example.colman24class1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

class EditStudentActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_student) // We can reuse the add_student layout

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
            }
            
            // Return to details screen
            val intent = Intent(this, StudentDetailsActivity::class.java)
            intent.putExtra("student_id", updatedStudent.id)
            startActivity(intent)
            finish()
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
}
