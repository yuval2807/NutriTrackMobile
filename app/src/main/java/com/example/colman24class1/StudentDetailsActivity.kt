package com.example.colman24class1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.colman24class1.R
import com.example.colman24class1.Student

class StudentDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_details)

        // Get student ID from intent
        val studentId = intent.getStringExtra("student_id")
        val student = studentId?.let { Student.getStudentById(it) }

        student?.let {
            // Populate views with student data
            findViewById<TextView>(R.id.studentNameView).text = it.name
            findViewById<TextView>(R.id.studentIdView).text = it.id
            findViewById<TextView>(R.id.studentPhoneView).text = it.phone
            findViewById<TextView>(R.id.studentAddressView).text = it.address
            findViewById<CheckBox>(R.id.student_checked_input).isChecked = it.isChecked
        }

        // Edit button click handler
//        findViewById<Button>(R.id.editButton).setOnClickListener {
//            val intent = Intent(this, EditStudentActivity::class.java)
//            intent.putExtra("student_id", studentId)
//            startActivity(intent)
//        }
//
//        // Back button click handler
//        findViewById<Button>(R.id.backButton).setOnClickListener {
//            finish()
//        }
    }
}
